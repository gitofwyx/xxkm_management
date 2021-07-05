package com.xxkm.management.device.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.device.entity.Device;
import com.xxkm.management.device.entity.DeviceClass;
import com.xxkm.management.device.service.DeviceClassService;
import com.xxkm.management.device.service.DeviceService;
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
public class DeviceController extends BaseController {

    private static Logger log = Logger.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceClassService deviceClassService;


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

            List<Device> listDevice = deviceService.listDevice(pageNumber, pageSize);
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

    @ResponseBody
    @RequestMapping("/addDevice")
    public Map<String, Object> addDevice(Device device, DeviceClass deviceClass,
                                         @RequestParam(value = "dev_class_id") String dev_class_id) {
        Map<String, Object> result = new HashMap<>();
        String Date = DateUtil.getFullTime();
        String deviceId = UUIdUtil.getUUID();
        String deviceClassId = "";
        String dev_ident = "";
        try {
            if ("".equals(device.getDev_name()) ||"".equals(device.getDev_type())) {
                result.put("hasError", true);
                result.put("error", "设备名称或种类获取失败!");
                return result;
            }
            if (("".equals(dev_class_id) || dev_class_id == null)) {  //生成编号
                dev_ident=IdentUtil.makeEntNo(Date,deviceClass.getClass_ident(),deviceClass.getDev_max());
            } else  {
                dev_ident=IdentUtil.makeEntNo(Date,deviceClass.getClass_ident(),deviceClass.getDev_max());
            }
            if (!"".equals(device.getDev_class_id()) && device.getDev_class_id() != null) {
                deviceClass.setId(device.getDev_class_id());
                deviceClass.setUpdateUserId("admin");
                deviceClass.setUpdateDate(Date);
                Boolean resultBl = deviceClassService.updateDev_maxMax(deviceClass);
                if (!(resultBl)) {
                    result.put("hasError", true);
                    result.put("error", "设备种类数量更新出错!");
                    return result;
                }
            }  else if ("".equals(device.getDev_class_id()) || device.getDev_class_id() == null) {
                deviceClass.setClass_tab("1");
                deviceClass.setEnt_class(device.getDev_name());
                deviceClassId = deviceClassService.updateEntityClass(deviceClass, Date);
                if (deviceClassId != null && !"".equals(deviceClassId)) {
                    device.setDev_class_id(deviceClassId);
                }
            }
            device.setId(deviceId);
            device.setDev_ident(dev_ident);
            device.setGenre_tags("1");//类型标识，设备始终为1
            device.setDev_flag("1");
            device.setCreateDate(Date);
            device.setCreateUserId("admin");
            device.setUpdateDate(Date);
            device.setUpdateUserId("admin");
            device.setDeleteFlag("0");
            boolean Result = deviceService.addDevice(device);
            if (!(Result)) {
                result.put("hasError", true);
                result.put("error", "设备添加出错!");
                return result;
            } else {
                result.put("success", true);
            }
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "设备更新出错！"+e.getCause().getLocalizedMessage());
            log.error(e);
            return result;
        }
        return result;
        //return "system/index";
    }

    @ResponseBody
    @RequestMapping(value = "/getDeviceName",method = RequestMethod.GET)
    public Map<String, Object> getDeviceName(@RequestParam(value = "tab") String tab) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listResult = new ArrayList<>();
        //List<Map<String, Object>> dev_count = new ArrayList<>();
        String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
        String name = (String) SecurityUtils.getSubject().getSession().getAttribute("userName");
        try {
            listResult = deviceClassService.listDeviceOfTab(tab);
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
    @RequestMapping(value = "/getDeviceSelect",method = RequestMethod.POST)
    public Map<String, Object> getDeviceSelect(@RequestParam(value = "tab") String tab) {
        int id = 0;
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listClass = new ArrayList<>();
        List<Map<String, Object>> listDevice = new ArrayList<>();
        try {
            listClass = deviceClassService.listDeviceOfTab(tab);
            listDevice = deviceService.getDeviceSelect();
            if (listClass == null || listDevice == null) {
                log.error("获取出错");
                return null;
            }
            result.put("Class_data", listClass);
            result.put("Entity_data", listDevice);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }

}
