package com.xxkm.management.office.controller;

import com.xxkm.core.file.BaseController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Controller
@RequestMapping("/office_menu")
public class officeMenuController extends BaseController {

    private static Logger log = Logger.getLogger(officeMenuController.class);

    /*@RequestMapping("/index")
    public ModelAndView  index() {
        Map<String, Object> result = new HashMap<>();
        return new ModelAndView("/index", "result", result);
    }*/

    @RequestMapping("/depository_tab")
    public ModelAndView  depository_tab() {
        Map<String, Object> result = new HashMap<>();
        return new ModelAndView("/offices/search/depository_dialog", "result", result);
    }

    @RequestMapping("/devices_tab")
    public ModelAndView  material_tab() {
        Map<String, Object> result = new HashMap<>();
        return new ModelAndView("/offices/search/devices_dialog", "result", result);
    }

}
