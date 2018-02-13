package org.seckill.web;

import java.util.Date;
import java.util.List;
import org.seckill.dto.Exposer;
import org.seckill.dto.Page;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/28 14:28
 * @Description:
 */
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "4") int pageNumber,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
        LOGGER.debug("start get seckillList");
        Page page = new Page();
        page.setPageNumber(pageNumber);
        page.setCurrentPage(currentPage);
        List<Seckill> seckillList = seckillService.getSeckillList(page);
        model.addAttribute("list", seckillList);
        model.addAttribute("page", page);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        LOGGER.debug("seckill: {}", seckill);
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 返回json类型
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result = null;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long phone) {
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result = null;
        try {
            SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            result = new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            result = new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNTER_ERROR);
            result = new SeckillResult<SeckillExecution>(true, execution);
        }
        return result;
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager(Model model,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "4") int pageNumber,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
        Page page = new Page();
        page.setPageNumber(pageNumber);
        page.setCurrentPage(currentPage);
        List<Seckill> seckillList = seckillService.getSeckillList(page);
        model.addAttribute("list", seckillList);
        model.addAttribute("page", page);
        return "manager";
    }

    @ResponseBody
    @RequestMapping(value = "/single", method = RequestMethod.POST)
    public Integer insertOne(@RequestBody Seckill seckill) {
        int res = seckillService.insertOne(seckill);
        return res;
    }

    /**
     * consumes很重要,否则无法识别。 ajax调用需要返回json，不能返回view
     */
    @ResponseBody
    @RequestMapping(value = "/single/{seckillId}", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public Integer updateOne(@PathVariable("seckillId") long seckillId, @RequestBody Seckill seckill) {
        LOGGER.debug("start update {}: {}", seckillId, seckill);
        int res = seckillService.update(seckillId, seckill);
        LOGGER.debug("end update {}: {}", seckillId, seckill);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/single/{seckillId}", method = RequestMethod.DELETE)
    public Integer deleteOne(@PathVariable("seckillId") long seckillId) {
        int res = seckillService.deleteOne(seckillId);
        return res;
    }
}
