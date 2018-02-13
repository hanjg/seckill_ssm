package org.seckill.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/26 22:13
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long seckillId = 1000L;
        long userPhone = 11111111111L;
        int res = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.assertEquals(1, res);
        res = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.assertEquals(0, res);
        long seckillId1 = 1001L;
        res = successKilledDao.insertSuccessKilled(seckillId1, userPhone);
        Assert.assertEquals(1, res);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long seckillId = 1000L;
        long userPhone = 11111111111L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
        Assert.assertEquals(seckillId, successKilled.getSeckillId());
        System.out.println(successKilled);
        Seckill seckill = successKilled.getSeckill();
        Assert.assertEquals(seckillId, seckill.getSeckillId());
        System.out.println(seckill);
    }

}