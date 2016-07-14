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
@RequestMapping("/task")
public class TaskController {
	@Autowired
	TaskService ts;
	
	@Autowired
	ClassificationService  cs;
	
	@RequestMapping(value = "/id", method = RequestMethod.GET)
	public ModelAndView findOne(@RequestBody Id f) {
		Task t = ts.getTaskWithTags(f.getId());
		Map map=new HashMap();
		if (t == null) {
			map.put("result", "fail");
		} else {
			map.put("result", "success");
			Taskjson tj = new Taskjson(t);
			map.put("task", tj);
		}
		return new ModelAndView(new MappingJackson2JsonView(),map); 
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ModelAndView allTasks(@RequestBody Id f) {
		Long uid = f.getId();
		Map map=new HashMap();
		List<Task> lt = ts.getTasksByUser(uid);
		List<Taskjson> tjs = new ArrayList<Taskjson>();
		Task tmp;
		if (lt == null) {
			map.put("result", "fail");
		} else {
			for (int i = 0; i < lt.size(); i++) {
				tmp = lt.get(i);
				tjs.add(new Taskjson(tmp));
			}
			map.put("result", "success");
		}
        map.put("tasks", tjs);
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	@RequestMapping(value = "/allByTag", method = RequestMethod.GET)
	public ModelAndView allTasksGivenTag(@RequestBody Id f) {
		Long cid = f.getId();
		Map map=new HashMap();
		List<Task> lt = cs.getTasks(cid);
		List<Taskjson> tjs = new ArrayList<Taskjson>();
		Task tmp;
		if (lt == null) {
			map.put("result", "fail");
	        return new ModelAndView(new MappingJackson2JsonView(),map);
		} else {
			for (int i = 0; i < lt.size(); i++) {
				tmp = lt.get(i);
				tjs.add(new Taskjson(tmp));
			}
			map.put("result", "success");
		}
        map.put("tasks", tjs);
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	
	@RequestMapping(value = "/allByStatus", method = RequestMethod.GET)
	public ModelAndView allTasksGivenStatus(@RequestBody TaskStatusForm f) {
		Long uid = f.getId();
		Task.Status tss = Task.Status.LIVE;
		if (f.getStatus() == 0) {
			tss = Task.Status.COMPLETED;
		}
		Map map=new HashMap();
		List<Task> lt = ts.getTasksByStatus(tss, uid);
		List<Taskjson> tjs = new ArrayList<Taskjson>();
		Task tmp;
		if (lt == null) {
			map.put("result", "fail");
		} else {
			for (int i = 0; i < lt.size(); i++) {
				tmp = lt.get(i);
				tjs.add(new Taskjson(tmp));
			}
			map.put("result", "success");
		}
        map.put("tasks", tjs);
        return new ModelAndView(new MappingJackson2JsonView(),map);  
	}
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateTask(@RequestBody TaskUpdateForm f) {
		Map map=new HashMap();
		Long tid = f.getId();
		List<String> tags = f.getTags();
		String newName = f.getName();
		Task t = ts.getTaskOnly(tid);
		if (t == null) {
			 map.put("result", "fail:no such task");
			 return new ModelAndView(new MappingJackson2JsonView(),map); 
		}
		User u = t.getUser();
		t.setClassifications(new ArrayList<Classification>());
		t.setName(newName);
		Classification tmp;
		for (int i = 0; i < tags.size(); i++) {
			tmp = cs.getByName(tags.get(i), u.getId());
			if (tmp != null) {
				t.addClassification(tmp);
			} else {
				tmp = cs.addOne(tags.get(i), u.getId());
				t.addClassification(tmp);
			}
		}
		t = ts.updateTask(t);
		Taskjson tj = new Taskjson(t);
		map.put("result", "success");
		map.put("task", tj);
		return new ModelAndView(new MappingJackson2JsonView(),map); 
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ModelAndView newTask(@RequestBody TaskForm tf) {
		Map map=new HashMap();
		System.out.println(tf.getName());
		System.out.println(tf.getCids());
		Task t = ts.addTask(tf.getName(), tf.getCids());
		if (t == null) {
			map.put("result", "fail");
		} else {
			Taskjson tj = new Taskjson(t);
			map.put("result", "success");
			map.put("task", tj);
		}
		return new ModelAndView(new MappingJackson2JsonView(),map);	
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestBody Id f) {
		Long tid = f.getId();
		ts.deleteTask(tid);
		Map map=new HashMap();  
        map.put("result", "success");
        return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	@RequestMapping(value = "/item/add", method = RequestMethod.POST)
	public ModelAndView addItem(@RequestBody TaskItemForm f) {
		Map map=new HashMap();
		TaskItem ti = ts.addItem(f.getTid(), f.getName());
		if (ti == null) {
			 map.put("result", "fail");
			 return new ModelAndView(new MappingJackson2JsonView(),map);
		} else {
			 map.put("result", "success");
		}
		ti.setTask(null);
		map.put("item", ti);
		return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	@RequestMapping(value = "/item/delete", method = RequestMethod.GET)
	public ModelAndView deleteItem(@RequestBody Id f) {
		Map map=new HashMap();
		ts.deleteItem(f.getId());
		map.put("result", "success");
		return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	@RequestMapping(value = "/item/all", method = RequestMethod.GET)
	public ModelAndView allitems(@RequestBody Id f) {
		Map map=new HashMap();
		Task t = ts.getTask(f.getId());
		if (t == null) {
			 map.put("result", "fail");
			 return new ModelAndView(new MappingJackson2JsonView(),map);
		} else {
			 map.put("result", "success");
		}
		List<TaskItem> tis = new ArrayList<TaskItem>();
		TaskItem tmp;
		for (int i = 0; i < t.getTaskItems().size(); i++) {
			tmp = t.getTaskItems().get(i);
			tmp.setTask(null);
			tis.add(tmp);
		}
		map.put("items",tis);
		return new ModelAndView(new MappingJackson2JsonView(),map);
	}
	
	
}
