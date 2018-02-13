package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.Page;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/27 20:19
 * @Description:
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    /**
     * 混淆md5
     */
    private String slat = "sdf#$#SFKFF_S)SDSS43434*@";

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public List<Seckill> getSeckillList(Page page) {
        int totalNumber = seckillDao.getTotalNumber();
        page.setTotalNumber(totalNumber);
        page.count();

        LOGGER.debug("before queryAll, page: " + page);
        return seckillDao.queryAll(page.getDbOffset(), page.getDbLimit());
    }

    public Exposer exportSeckillUrl(long seckillId) {
        // 未使用redis优化
        /* Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }*/

        //使用redis缓存优化，在超时的基础上维护一致性。
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            LOGGER.debug("get seckill from mysql");
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date currentTime = new Date();
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        if (currentTime.getTime() < startTime.getTime() || currentTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, currentTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, seckillId, md5);
    }

    /**
     * 根据id获得md5值
     */
    private String getMD5(long seckillId) {
        String base = seckillId + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 执行秒杀
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws RepeatKillException, SeckillCloseException, SeckillException {
        //验证md5值
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("数据被篡改");

        }
        Date currentTime = new Date();

        try {
/*            int updateCount = seckillDao.reduceNumber(seckillId, currentTime);
            if (updateCount <= 0) {
                //没有更新到，没库存或者时间已过,秒杀结束
                throw new SeckillCloseException("秒杀结束");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("重复秒杀");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }*/

            //首先记录购买行为，zain更新库存，可以减少update行级锁的持有时间。
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("重复秒杀");
            } else {
                int updateCount = seckillDao.reduceNumber(seckillId, currentTime);
                if (updateCount <= 0) {
                    //没有更新到，没库存或者时间已过,秒杀结束
                    throw new SeckillCloseException("秒杀结束");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new SeckillException("内部错误:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        //验证md5值
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
        }

        Date currentTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", currentTime);
        map.put("result", null);

        try {
            //执行存储过程时，result被赋值
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNTER_ERROR);
        }
    }

    @Override
    public int insertOne(Seckill seckill) {
        return seckillDao.insertOne(seckill);
    }

    @Override
    public int deleteOne(long seckillId) {
        return seckillDao.deleteOne(seckillId);
    }

    @Override
    public int update(long seckillId, Seckill seckill) {
        return seckillDao.update(seckillId, seckill);
    }
}
