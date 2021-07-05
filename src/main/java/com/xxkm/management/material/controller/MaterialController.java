package com.xxkm.management.material.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.device.entity.DeviceClass;
import com.xxkm.management.device.service.DeviceClassService;
import com.xxkm.management.material.entity.Material;
import com.xxkm.management.material.service.MaterialService;
import org.apache.log4j.Logger;
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
public class MaterialController extends BaseController {

    private static Logger log = Logger.getLogger(MaterialController.class);

    @Autowired
    private MaterialService materialService;
    @Autowired
    private DeviceClassService deviceClassService;


    @ResponseBody
    @RequestMapping("/listMaterial")
    public Map<String, Object> listMaterial(@RequestParam(value = "pageIndex") String pageIndex,
                                            @RequestParam(value = "limit") String limit,
                                            @RequestParam(value = "dev_name") String dev_name,
                                            @RequestParam(value = "dev_type") String dev_type,
                                            @RequestParam(value = "startDate") String startDate) {
        Map<String, Object> result = new HashMap<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//因为pageindex 从0开始
            int pageSize = Integer.parseInt(limit);

            List<Material> listDevice = materialService.listMaterial(pageNumber, pageSize);
            if (listDevice == null) {
                log.error("获取分页出错");
                result.put("success", false);
                return result;
            } else {
                result.put("rows", listDevice);
                result.put("results", materialService.countMaterial());
            }
        } catch (Exception e) {
            log.error(e);
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/addMaterial")
    public Map<String, Object> addMaterial(Material material, DeviceClass deviceClass,
                                            @RequestParam(value = "dev_class_id") String dev_class_id) {
        Map<String, Object> result = new HashMap<>();
        String Date = DateUtil.getFullTime();
        String deviceId = UUIdUtil.getUUID();
        String mat_ident = "";
        Boolean resultBl = true;
        try {
            if ("".equals(material.getMat_name()) ||"".equals(material.getMat_type())) {
                result.put("hasError", true);
                result.put("error", "配件、耗材名称或种类获取失败!");
                return result;
            }
            if (("".equals(dev_class_id) || dev_class_id == null)) {  //生成编号
                mat_ident=IdentUtil.makeEntNo(Date,deviceClass.getClass_ident(),deviceClass.getMat_max());
            } else  {
                mat_ident=IdentUtil.makeEntNo(Date,deviceClass.getClass_ident(),deviceClass.getMat_max());
            }
            //判断种类id
            if (!"".equals(material.getDev_class_id()) && material.getDev_class_id() != null) {
                deviceClass.setId(material.getDev_class_id());
                deviceClass.setUpdateUserId("admin");
                deviceClass.setUpdateDate(Date);
                resultBl = deviceClassService.updateMat_maxMax(deviceClass);
                if (!(resultBl)) {
                    result.put("hasError", true);
                    result.put("error", "添加出错");
                    return result;
                }
            }
            else if ("".equals(material.getDev_class_id()) || material.getDev_class_id() == null) {
                deviceClass.setClass_tab(material.getGenre_tags());
                deviceClass.setEnt_class(material.getMat_name());
                String materialClassId = deviceClassService.updateEntityClass(deviceClass, Date);
                if (materialClassId != null && !"".equals(materialClassId)) {
                    material.setDev_class_id(materialClassId);
                }
            }
            material.setId(deviceId);
            material.setMat_ident(mat_ident);
            material.setCreateDate(Date);
            material.setCreateUserId("admin");
            material.setUpdateDate(Date);
            material.setUpdateUserId("admin");
            material.setDeleteFlag("0");

            boolean Result = materialService.addMaterial(material);
            if (!(Result)) {
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                result.put("success", true);
            }
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "添加出错");
            log.error(e);
        }
        return result;
        //return "system/index";
    }

    @ResponseBody
    @RequestMapping(value = "/getMaterialName",method = RequestMethod.GET)
    public Map<String, Object> getMaterialName(@RequestParam(value = "tab") String tab) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listResult = new ArrayList<>();
        //List<Map<String, Object>> dev_count = new ArrayList<>();
        try {
            listResult = deviceClassService.listMaterialOfTab(tab);
            //dev_count = deviceClassService.getCountClassById("1fa2614d-4a55-1234-a79a-5546319b9123");
            if (listResult == null) {
                log.error("获取出错");
                return null;
            }
            result.put("value", listResult);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/getMaterialSelect",method = RequestMethod.POST)
    public Map<String, Object> getMaterialSelect(@RequestParam(value = "tab") String tab) {
        int id = 0;
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listClass = new ArrayList<>();
        List<Map<String, Object>> listMaterial = new ArrayList<>();
        try {
            listClass = deviceClassService.listDeviceOfTab(tab);
            listMaterial = materialService.getMaterialSelect(tab);
            if (listClass == null || listMaterial == null) {
                log.error("获取出错");
                return null;
            }
            result.put("Class_data", listClass);
            result.put("Entity_data", listMaterial);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }
}
