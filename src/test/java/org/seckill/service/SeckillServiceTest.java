package org.seckill.service;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.Page;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/28 10:32
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillServiceTest.class);

    @Autowired
    private SeckillService seckillService;


    @Test
    public void getSeckillById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillService.getSeckillById(seckillId);
        LOGGER.info("seckill: {}", seckill);
        assertNotNull(seckill);
    }

    @Test
    public void getSeckillList() throws Exception {
        Page page = new Page();
        page.setCurrentPage(5);
        page.setPageNumber(2);
        List<Seckill> seckillList = seckillService.getSeckillList(page);
        LOGGER.debug("list size = {}, {}", seckillList.size(), seckillList);
        LOGGER.debug("page: {}", page);
    }

    @Test
    public void exportSeckill() throws Exception {
        Exposer exposer = seckillService.exportSeckillUrl(1111);
        assertFalse(exposer.isExposed());
        LOGGER.debug("exposer: ", exposer);
        assertEquals(0, exposer.getNow());

        Exposer exposer1 = seckillService.exportSeckillUrl(1000);
        assertFalse(exposer1.isExposed());
        assertNotNull(exposer.getNow());

        Exposer exposer2 = seckillService.exportSeckillUrl(1003);
        assertTrue(exposer2.isExposed());
        assertNotNull(exposer2.getMd5());
    }

    @Test
    public void executeSeckill() throws Exception {
        long userPhone = 11111111111L;
        Exposer exposer = seckillService.exportSeckillUrl(1003);
        LOGGER.info("md5: {}" + exposer.getMd5());

        try {
            SeckillExecution execution = seckillService.executeSeckill(1000, userPhone, null);
            fail();
        } catch (SeckillException e) {
            e.printStackTrace();
        }

       /* try {
            LOGGER.info("md5: {}",exposer.getMd5());
            SeckillExecution execution = seckillService.executeSeckill(1000, userPhone, exposer.getMd5());
            fail();
        } catch (SeckillCloseException e) {
            e.printStackTrace();
        }*/

        try {
            SeckillExecution execution = seckillService.executeSeckill(1003, userPhone, exposer.getMd5());
            SeckillExecution execution1 = seckillService.executeSeckill(1003, userPhone, exposer.getMd5());
            fail();
        } catch (RepeatKillException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void executeSeckillByProcedure() {
        long userPhone = 11111111111L;
        Exposer exposer = seckillService.exportSeckillUrl(1003);
        LOGGER.info("md5: {}" + exposer.getMd5());

        SeckillExecution execution = seckillService.executeSeckillByProcedure(1000, userPhone, null);
        assertEquals(SeckillStateEnum.REPEAT_KILL, SeckillStateEnum.stateOf(execution.getState()));

        execution = seckillService.executeSeckillByProcedure(1003, userPhone, exposer.getMd5());
        assertEquals(SeckillStateEnum.SUCCESS, SeckillStateEnum.stateOf(execution.getState()));
        execution = seckillService.executeSeckillByProcedure(1003, userPhone, exposer.getMd5());
        assertEquals(SeckillStateEnum.REPEAT_KILL, SeckillStateEnum.stateOf(execution.getState()));
    }

}