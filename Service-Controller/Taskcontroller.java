package com.whereIsTime.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.whereIsTime.entities.*;
import com.whereIsTime.services.*;

@Controller
@RequestMapping("/user/data/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /*
    json字符串格式：{"Name":xxx,"list":xxx,"description":xxx,"BeginTime":xxx,"EndTime":xxx,"status":xxx}
    list对应List<Long>类型，存入对应cid；
    time这里设置为string类型，status用0和1代表未完成和完成
    成功返回taskid格式：{"result":"success","tid":xxx}……
    */
    @RequestMapping("/addTask")
    @ResponseBody
    public void addTask(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb = new JSONObject();
        String name=(String)jb.fromObject(str).get("Name");
        List<Long> all = new ArrayList<Long>();
        JSONArray ja = (JSONArray)jb.fromObject(str).getJSONArray("list");
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            all.add((jo.get("Cids"));
        }
        Task list = taskService.addTask(name, all);
        JSONObject jsons=new JSONObject();
        if (list == null) {
            jsons.put("result", "fail");
        } else {
            jsons.put("result", "success");
            String desc=(String)jb.fromObject(str).get("description");
            list.setDescription(desc);
            String beginTime0 = (String)jb.fromObject(str).get("BeginTime");
            String endTime0 = (String)jb.fromObject(str).get("EndTime");
            java.text.SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            java.text.SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date beginTime = formatter1.parse(beginTime0);
            Date endTime = formatter2.parse(endTime0);
            list.setBeginTime(beginTime);
            list.setEndTime(endTime);
            int status0 = (int)jb.fromObject(str).get("status");
            Status status = Status.LIVE;
            if (status0 == 0) status = Status.LIVE;
            if (status0 == 1) status = Status.COMPLETED;
            list.setStatus(status);
            Long tid = list.getId();
            jsons.put("tid", tid);
        }
        response.getWriter().print(jsons.toString());
    }

    /*
    json字符串格式：{"Name":xxx,"Tid":xxx,"status":xxx}
    获取taskid，创建子任务，返回子任务id
    status用0和1代表未完成和完成
    成功返回iid格式：{"result":"success","iid":xxx}……
    */
    @RequestMapping("/addItem")
    @ResponseBody
    public void addItem(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //解码
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb = new JSONObject();
        String name=(String)jb.fromObject(str).get("Name");
        Long tid = (Long)jb.fromObject(str).get("Tid");
        Task list = taskService.addItem(tid, name);
        JSONObject jsons=new JSONObject();
        if (list == null) {
            jsons.put("result", "fail");
        } else {
            jsons.put("result", "success");
            int status0 = (int)jb.fromObject(str).get("status");
            Status status = Status.LIVE;
            if (status0 == 0) status = Status.LIVE;
            if (status0 == 1) status = Status.COMPLETED;
            list.setStatus(status);
            Long iid = list.getId();
            jsons.put("iid", iid);
        }
        response.getWriter().print(jsons.toString());
    }

    /*
    获取taskid 删除任务
    */
    @RequestMapping("/deleteTask")
    @ResponseBody
    public void deleteTask(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //解码
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb=new JSONObject(); 
        Long tid=(Long)jb.fromObject(str).get("Tid");
        taskService.deleteTask(tid);
    }

    /*
    获取Itemid 删除子任务
    */
    @RequestMapping("/deleteItem")
    @ResponseBody
    public void deleteItem(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //解码
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb=new JSONObject();
        Long itemId=(Long)jb.fromObject(str).get("ItemId");
        taskService.deleteItem(itemId);
    }

}