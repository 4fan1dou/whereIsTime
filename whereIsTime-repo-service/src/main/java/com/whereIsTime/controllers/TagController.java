package com.whereIsTime.controllers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/tag")
public class TagController {
	@Autowired
	ClassificationService cs;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ModelAndView all(@RequestBody Id f) {
		Long uid = f.getId();
		Map map=new HashMap();
		List<Classification> lc = cs.getAllByUser(uid);
		Classification tmp;
		
		if (lc == null) {
			map.put("result", "fail");
	        return new ModelAndView(new MappingJackson2JsonView(),map);
		} else {
			for (int i = 0; i < lc.size(); i++) {
				tmp = lc.get(i);
				tmp.setTasks(new ArrayList<Task>());
				tmp.setUser(null);
				lc.set(i, tmp);
			}
			map.put("result", "success");
		}
        map.put("tags", lc);
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ModelAndView newTag(@RequestBody TagForm tf) {
		Classification c = cs.addOne(tf.getName(), tf.getUid());
		Map map=new HashMap();
		if (c == null) {
			map.put("result", "fail");
			return new ModelAndView(new MappingJackson2JsonView(),map); 
		} else {
			map.put("result", "success");
		}
		c.setUser(null);
        map.put("newTag", c);
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestBody Id f) {
		Long cid = f.getId();
		cs.deleteOne(cid);
		Map map=new HashMap();  
        map.put("result", "success");
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	
	@RequestMapping(value = "/time/interval", method = RequestMethod.GET)
	public ModelAndView getTimeByInterval(@RequestBody TagQIntervalForm f) {
		Double t = cs.getTagInterval(f.getCid(), f.getFrom(), f.getTo());
		Map map=new HashMap();
        map.put("result", "success");
        map.put("time", t);
        return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	@RequestMapping(value = "/time/day", method = RequestMethod.GET)
	public ModelAndView getTimeByDay(@RequestBody TagQdayForm f) {
		Double t = cs.getTagTime(f.getCid(), f.getDay());
		Map map=new HashMap();  
        map.put("result", "success");
        map.put("time", t);
        return new ModelAndView(new MappingJackson2JsonView(),map);
	}
}
