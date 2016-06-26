package com.whereIsTime.controllers;

import java.util.HashMap;
import java.util.Map;
import com.whereIsTime.entities.*;
import com.whereIsTime.Services.*;
import com.whereIsTime.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService us;
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView sayHello() {
		User me = us.signUp("xiaoming", "12345");
		Map map=new HashMap();  
        map.put("result", "success");
        map.put("user", me);
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	public ModelAndView signup(@RequestBody UserForm uf) {
		User somebody = us.signUp(uf.getName(), uf.getPw());
		Map map=new HashMap();  
        map.put("result", "success");
        map.put("user", somebody);
        return new ModelAndView(new MappingJackson2JsonView(),map); 
	}
}
