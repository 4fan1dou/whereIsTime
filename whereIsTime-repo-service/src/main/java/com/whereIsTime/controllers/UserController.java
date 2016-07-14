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
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signup(@RequestBody UserForm uf) {
		User somebody = us.signUp(uf.getName(), uf.getPw());
		Map map=new HashMap();
		if (somebody == null) {
			map.put("result", "fail"); 
	        return new ModelAndView(new MappingJackson2JsonView(),map); 
		} else {
			map.put("result", "success"); 
		}
		map.put("user", somebody);
        return new ModelAndView(new MappingJackson2JsonView(),map); 
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestBody UserForm uf) {
		User somebody = us.signIn(uf.getName(), uf.getPw());
		Map map=new HashMap();
		if (somebody == null) {
			map.put("result", "fail"); 
			return new ModelAndView(new MappingJackson2JsonView(),map);
		} else {
			map.put("result", "success"); 
		}
		map.put("user", somebody);
		return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestBody Long uid) {
		Map map=new HashMap();
		us.deleteUser(uid);
		map.put("result", "success"); 
		return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	@RequestMapping(value = "/changepw", method = RequestMethod.POST)
	public ModelAndView changepw(@RequestBody changepwForm f) {
		Map map=new HashMap();
		User u = us.getUserOnly(f.getUid());
		if (u == null) {
			map.put("result", "fail:no such user");
			return new ModelAndView(new MappingJackson2JsonView(),map);
		}
		if (!u.getPw().equals(f.getOldpw())) {
			map.put("result", "fail:wrong password");
			return new ModelAndView(new MappingJackson2JsonView(),map);
		}
		u.setPw(f.getNewpw());
		u = us.updateUser(u);
		map.put("result", "success");
		return new ModelAndView(new MappingJackson2JsonView(),map);
	}
}
