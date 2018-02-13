package org.seckill.dao.cache;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/30 22:48
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RedisDao redisDao;

    @Test
    public void getSeckill() throws Exception {
        long seckillId = 1000;
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            System.out.println("get from mysql");
            seckill = seckillDao.queryById(seckillId);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                assertEquals("OK", result);
                System.out.println("get from redis");
                seckill = redisDao.getSeckill(seckillId);
                System.out.println(seckill);
                assertEquals(seckillId, seckill.getSeckillId());
            }
        } else {
            System.out.println("get from redis");
        }
    }

}