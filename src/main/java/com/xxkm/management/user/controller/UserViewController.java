package com.xxkm.management.user.controller;

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
@RequestMapping("user_view")
public class UserViewController extends BaseController {

    private static Logger log = Logger.getLogger(UserViewController.class);

    /*@RequestMapping("/index")
    public ModelAndView  index() {
        Map<String, Object> result = new HashMap<>();
        return new ModelAndView("/index", "result", result);
    }*/

    @RequestMapping("/user_details")
    public ModelAndView  user_details() {
        Map<String, Object> result = new HashMap<>();
        return new ModelAndView("/detail/user_details", "result", result);
    }

}
