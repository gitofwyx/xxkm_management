package com.xxkm.management.system.bar_code.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.system.bar_code.entity.Bar_code;
import com.xxkm.management.system.bar_code.service.DeviceService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Controller
@RequestMapping("")
public class Bar_codeController extends BaseController {

    private static Logger log = Logger.getLogger(Bar_codeController.class);

    @Autowired
    private DeviceService deviceService;
    
    @ResponseBody
    @RequestMapping("/listDevice")
    public Map<String, Object> listDevice(@RequestParam(value = "pageIndex") String pageIndex,
                                          @RequestParam(value = "limit") String limit,
                                          @RequestParam(value = "dev_name") String dev_name,
                                          @RequestParam(value = "dev_type") String dev_type,
                                          @RequestParam(value = "startDate") String startDate) {
        Map<String, Object> result = new HashMap<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//因为pageindex 从0开始
            int pageSize = Integer.parseInt(limit);

            List<Bar_code> listDevice = deviceService.listDevice(pageNumber, pageSize);
            if (listDevice == null) {
                log.error("获取分页出错");
                result.put("success", false);
                return result;
            } else {
                result.put("rows", listDevice);
                result.put("results", deviceService.countDevice());
            }
        } catch (Exception e) {
            log.error(e);
            result.put("success", false);
        }
        return result;
    }






}
