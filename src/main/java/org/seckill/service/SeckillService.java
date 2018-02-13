package org.seckill.service;

import java.util.List;
import org.seckill.dto.Exposer;
import org.seckill.dto.Page;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/27 19:48
 * @Description:
 */
public interface SeckillService {

    Seckill getSeckillById(long seckillId);

    List<Seckill> getSeckillList(Page page);

    /**
     * 秒杀开始返回秒杀接口地址<br> 否则输出系统时间和秒杀开始结束时间
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws RepeatKillException, SeckillCloseException, SeckillException;

    /**
     * 使用存储过程执行秒杀
     */
    SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5);

    /**
     * 增加商品对象
     */
    int insertOne(Seckill seckill);

    /**
     * 删除商品对象
     */
    int deleteOne(long seckillId);

    /**
     * 更新商品对象
     */
    int update(long seckillId, Seckill seckill);

}
