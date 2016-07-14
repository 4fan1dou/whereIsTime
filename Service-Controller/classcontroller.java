package com.whereIsTime.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList; 
import java.util.List;
import java.util.Date;
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
@RequestMapping("/user/data")
public class ClassController {
    @Autowired
    private ClassificationService classificationService;

    /*
    json字符串格式：{list:[{"Cid":xxx,"Day":xxx},……]}
    cid对应Long类型，day对应string类型
    显示该天的统计情况
    返回格式：{"time":xxx}……
    */
    @RequestMapping("/classification/listall1")
    @ResponseBody
    public void listall1(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONArray all = new JSONArray();
        JSONArray ja = (JSONArray)jb.fromObject(str).getJSONArray("list");
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            Long cid = (Long)jo.get("Cid");
            String day = (String)jo.get("Day");
            JSONObject js = new JSONObject();
            js.put("time", classificationService.getTagTime(cid, day));
            all.add(js);
        }
        response.getWriter().print(all.toString());
    }

    /*
    json字符串格式：{list:[{"Cid":xxx,"From":xxx,"To":xxx},……]}
    cid对应Long类型，from和to对应Date类型
    显示一段时间的统计情况
    返回格式：{"time":xxx}……
    */
    @RequestMapping("/classification/listall2")
    @ResponseBody
    public void listall2(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONArray all = new JSONArray();
        JSONArray ja = (JSONArray)jb.fromObject(str).getJSONArray("list");
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            Long cid = (Long)jo.get("Cid");
            Date from = (Date)jo.get("From");
            Date to = (Date)jo.get("To");
            JSONObject js = new JSONObject();
            js.put("time", classificationService.getTagInterval(cid, from, to));
            all.add(js);
        }
        response.getWriter().print(all.toString());
    }
}