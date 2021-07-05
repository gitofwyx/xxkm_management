package com.xxkm.management.office.materials.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.device.entity.DeviceClass;
import com.xxkm.management.device.service.DeviceClassService;
import com.xxkm.management.office.materials.entity.Materials;
import com.xxkm.management.office.materials.service.MaterialsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class MaterialsController extends BaseController {

    private static Logger log = Logger.getLogger(MaterialsController.class);

    @Autowired
    private MaterialsService materialsService;
    @Autowired
    private DeviceClassService deviceClassService;




}
