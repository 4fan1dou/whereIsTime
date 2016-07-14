package com.whereIsTime.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList; 
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
@RequestMapping("/user/data")
public class UserController {
    @Autowired
    private UserService userService;

    //接收前台传过来的字符串格式的json对象，在后台进行解析
    /*
    json字符串格式：{"userName":xxx,"password":xxx}
    登陆成功返回uid
    */
    @RequestMapping("/signin")
    @ResponseBody
    public void signin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //解码
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb = new JSONObject(); 
        String name=(String)jb.fromObject(str).get("userName");
        String pw=(String)jb.fromObject(str).get("password");
        User user = userService.signIn(name, pw);
        JSONObject jsons=new JSONObject();
        if (user == null) {
            jsons.put("result", "fail");
        } else {
            jsons.put("result", "success");
            Long uid = user.getId();
            jsons.put("uid", uid);
        }
        response.getWriter().print(jsons.toString());
    }

    /*
    json字符串格式：{"userName":xxx,"password":xxx}
    注册成功返回uid
    与上面的区别在于userService的调用函数不同
    */
    @RequestMapping("/login")
    @ResponseBody
    public void login(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //解码
        String str = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
        JSONObject jb=new JSONObject();
        String name=(String)jb.fromObject(str).get("userName");
        String pw=(String)jb.fromObject(str).get("password");
        User user = userService.signUp(name, pw);
        JSONObject jsons=new JSONObject();
        if (user == null) {
            jsons.put("result", "fail");
        } else {
            jsons.put("result", "success");
            Long uid = user.getId();
            jsons.put("uid", uid);
        }
        response.getWriter().print(jsons.toString());
    }
   
}