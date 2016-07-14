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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
@Controller
@RequestMapping("/tomato")
public class TomatoController {
	@Autowired
	MTomatoService mts;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ModelAndView allTomatosByTask(@RequestBody Id f) {
		Map map=new HashMap();
		List<Mtomato> tl = mts.getByTask(f.getId());
		if (tl == null) {
			map.put("result", "fail");
			return new ModelAndView(new MappingJackson2JsonView(),map); 
		}
		map.put("result", "success");
		List<Mtomato> ret = new ArrayList<Mtomato>();
		Mtomato tmp;
		for (int i = 0; i < tl.size(); i++) {
			tmp = tl.get(i);
			tmp.setTask(null);
			ret.add(tmp);
		}
		map.put("tomatos",ret);
		return new ModelAndView(new MappingJackson2JsonView(),map); 
	}
	
	@RequestMapping(value = "/day", method = RequestMethod.GET)
	public ModelAndView byday(@RequestBody tomatoDayForm f) {
		Map map=new HashMap();
		List<Mtomato> tl = mts.getByTaskAndDay(f.getId(), f.getDay());
		if (tl == null) {
			map.put("result", "fail");
			return new ModelAndView(new MappingJackson2JsonView(),map); 
		}
		map.put("result", "success");
		List<Mtomato> ret = new ArrayList<Mtomato>();
		Mtomato tmp;
		for (int i = 0; i < tl.size(); i++) {
			tmp = tl.get(i);
			tmp.setTask(null);
			ret.add(tmp);
		}
		map.put("tomatos",ret);
		return new ModelAndView(new MappingJackson2JsonView(),map); 
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ModelAndView newTomato(@RequestBody TomatoForm f) {
		Map map=new HashMap();
		Mtomato t = mts.completeMTomato(f.getTid(), null, false, false, f.getBeginTime(), f.getEndTime(), f.getNt(), f.getCompletedItems());
		if (t == null) {
			map.put("result", "fail");
			return new ModelAndView(new MappingJackson2JsonView(),map);
		} else {
			map.put("result", "success");
			t.setTask(null);
			map.put("tomato", t);
			return new ModelAndView(new MappingJackson2JsonView(),map);
		}
	}
}
