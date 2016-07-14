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
    private MTomatoService mTomatoService;

    /*
    json字符串格式：{"Tid":xxx,"Feedback":xxx,"BeginTime":xxx,"EndTime":xxx,list:[{"Item":xxx},……]}
    "delayed"和"breaked"可能有
    Feedback 用0,1,2代表好，正常，坏
    list存的是已完成的子任务
    */
    @RequestMapping("/mTomato/complete")
    @ResponseBody
    public void complete(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //解码
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb = new JSONObject();
        Long tid = (Long)jb.fromObject(str).get("Tid");
        int feedback0 = (int)jb.fromObject(str).get("Feedback");
        Mtomato.feedback feedback = feedback.normal;
        if (feedback0 == 0) feedback = feedback.great;
        if (feedback0 == 1) feedback = feedback.normal;
        if (feedback0 == 2) feedback = feedback.bad;
        boolean delayed = (boolean)jb.fromObject(str).get("delayed");
        boolean breaked = (boolean)jb.fromObject(str).get("breaked");
        String beginTime0 = (String)jb.fromObject(str).get("BeginTime");
        String endTime0 = (String)jb.fromObject(str).get("EndTime");
        Integer nt=(int)jb.fromObject(str).get("Nt");
        java.text.SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        java.text.SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = formatter1.parse(beginTime0);
        Date endTime = formatter2.parse(endTime0);
        List<Long> all = new ArrayList<Long>();
        JSONArray ja = (JSONArray)jb.fromObject(str).getJSONArray("list");
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            all.add((jo.get("Item"));
        }
        MTomato list = mTomato.completeMTomato(tid, feedback, delayed, breaked, beginTime, endTime, nt, all);
    }

}