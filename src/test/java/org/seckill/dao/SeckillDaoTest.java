package org.seckill.dao;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/26 22:12
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

    /**
     * 自动装配
     */
    @Resource
    private SeckillDao seckillDao;
    private Calendar calendar = Calendar.getInstance();

    @Test
    public void queryById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillDao.queryById(seckillId);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckillList = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckillList) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        long seckillId = 1004L;
        int numBefore = seckillDao.queryById(seckillId).getNumber();
        int res = 0;

        calendar.set(2018, 0, 5, 1, 1, 1);
        Date afterDate = calendar.getTime();
        calendar.clear();
        res = seckillDao.reduceNumber(seckillId, afterDate);
        Assert.assertEquals(0, res);

        calendar.set(2017, 0, 1, 1, 1, 1);
        Date beforeDate = calendar.getTime();
        calendar.clear();
        res = seckillDao.reduceNumber(seckillId, beforeDate);
        Assert.assertEquals(0, res);

        calendar.set(2018, 0, 1, 1, 1, 1);
        Date duringDate = calendar.getTime();
        calendar.clear();
        res = seckillDao.reduceNumber(seckillId, duringDate);
        Assert.assertEquals(1, res);

        int numAfter = seckillDao.queryById(seckillId).getNumber();
        Assert.assertEquals(numBefore - 1, numAfter);
    }

    @Test
    public void testupdateTime() {
        long seckillId = 1003L;
        calendar.set(2018, 0, 1, 1, 1, 1);
        Date duringDate = calendar.getTime();
        calendar.clear();
        int res = seckillDao.reduceNumber(seckillId, duringDate);
        Seckill seckill = seckillDao.queryById(seckillId);
        Assert.assertEquals(1, res);
        System.out.println(seckill.toString());
    }

    @Test
    public void testDate() {

        calendar.set(2018, 0, 1, 1, 1, 1);
        Date date = calendar.getTime();
        calendar.clear();
        System.out.println(
                date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " " + date.getHours() + ":" + date
                        .getMinutes() + ":" + date.getSeconds());
    }

    @Test
    public void testInsert() {
        Seckill seckill = new Seckill();
        seckill.setName("100元秒杀mate10");
        seckill.setNumber(100);
        seckill.setStartTime(getDate(2019, 0, 1, 1, 1, 1));
        seckill.setEndTime(getDate(2019, 1, 1, 1, 1, 1));

        int res = seckillDao.insertOne(seckill);
        assertEquals(1, res);
    }

    @Test
    public void testUpdate() {
        Seckill seckill = new Seckill();
        long seckillId = 1000L;
        seckill.setSeckillId(seckillId);
        seckill.setName("200元秒杀mate10");
        seckill.setNumber(200);
        seckill.setStartTime(getDate(2029, 0, 1, 1, 1, 1));
        seckill.setEndTime(getDate(2029, 1, 1, 1, 1, 1));
//        assertEquals(Seckill.class, seckillDao.queryById(seckillId).getClass());
        int res = seckillDao.update(seckillId, seckill);
        assertEquals(1, res);
    }

    @Test
    public void testDelete() {
        long seckillId = 1004;
        seckillDao.deleteOne(seckillId);
        assertNull(seckillDao.queryById(seckillId));
    }

    private Date getDate(int year, int month, int date, int hourOfDay, int minute,
            int second) {
        calendar.set(year, month, date, hourOfDay, minute, second);
        Date time = calendar.getTime();
        calendar.clear();
        return time;
    }

}