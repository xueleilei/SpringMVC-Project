package com.web.controller;

import com.web.utils.ipUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.model.User;
import com.web.service.UserServiceI;
import com.web.utils.getMD5;
import com.web.utils.ipUtils;

@Controller
@RequestMapping("/userController")
public class UserController {

	private UserServiceI userService;

	@Autowired
	public void setUserService(UserServiceI userService) {
		this.userService = userService;
	}

	//测试显示
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request,Model model) {
		//String user = "我是用来测试显示的";
		//String user = "ccccc";
		
		String username = request.getParameter("s");
		String result = getMD5.MD5(username);
		String ipAddr = ipUtils.getIpAddr(request);
		
		model.addAttribute("user", result);
		model.addAttribute("ipAddr", ipAddr);
		
		
		
		return "showUser";
	}
		
		
	// 直接通过URL访问用户
	@RequestMapping("/{id}/showUser")
	public String showUser(@PathVariable String id, HttpServletRequest request) {
		User u = userService.getUserById(id);
		try {
			// System.out.println(u.getName());
			request.setAttribute("user", u.getName());
		} catch (Exception e) {
			request.setAttribute("user", "未能找到该用户");
		}
		return "showUser";
	}
	

	
	// 根据ID查询用户的名称
	@RequestMapping("/getUser")
	@ResponseBody
	public void getUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		
		User user = userService.getUserById(id);
		
		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;	
		PrintWriter out = response.getWriter();
		
		try {
			System.out.println("----Console 本次查找的用户名是："+user.getName());
            out.write(callback +"({\"success\":true,\"id\":\""+user.getId()+"\",\"name\":\""+user.getName()+"\",\"password\":\""+user.getPassword()+"\"})");   
			//return callback +"({\"success\":true,\"id\":\""+user.getId()+"\",\"name\":\""+user.getName()+"\",\"password\":\""+user.getPassword()+"\"})";
		} catch (Exception e) {
			out.write(callback +"({\"success\":false,\"Code\":\"-1\"})");
		}

        out.flush();  
        out.close(); 
	}
	
	
	/**
	 * 根据用户名查询用户
	 * 精确查询
	 * */
	@RequestMapping("/getName")
	@ResponseBody
	public void getName(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String name = request.getParameter("name");

		User user = userService.getUserByName(name);
		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;
//		response.setContentType("application/x-javascript");
//		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			System.out.println("----Console 本次查找的用户名是："+user.getName());
			out.write(callback +"({\"Code\":\"0000\",\"Name\":\""+user.getName()+"\",\"Password\":\""+user.getPassword()+"\"})"); //用户存在
		} catch (Exception e) {
			out.write(callback +"({\"Code\":\"-1\"})"); //你查找的用户不存在！
		}
		
	}
	
	
	//显示所有用户
	@RequestMapping("/listUser")
	public String listUser(Model model) {
		List<User> list = userService.getAll();
		System.out.println(list); 
		try {
			// System.out.println(u.getName());
			model.addAttribute("list", list);
		} catch (Exception e) {
			model.addAttribute("list", "未能找到该用户");
		}
		return "listUser";
	}



	// 添加一个用户
	@RequestMapping("/insertUser")
	@ResponseBody
	public void insertUser(HttpServletRequest request,HttpServletResponse response, Model model, User user) throws IOException {
		String getIpAddr = ipUtils.getIpAddr(request);
		user.setIpAddr(getIpAddr);;  //获取IP地址并放入user对象里
		
		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;
		PrintWriter out = response.getWriter();
		
		try {
			User user2 = userService.getUserByName(request.getParameter("name"));
			
			if (user2.getName() != null) {
				out.write(callback + "({\"Code\":1001,\"msg\":\"用户已经存在，添加失败！\"})");
			}
		} catch (Exception e) {
			try {
				userService.insertUser(user);
				//model.addAttribute("user", "您添加的用户是：" + name + "，密码是：" + password);
				System.out.println("----Console 本次添加的用户ID是："+user.getId()+"，用户名是："+user.getName()+"，状态：成功");
				out.write(callback + "({\"Code\":0000,\"msg\":\"添加成功！\",\"id\":\""+user.getId()+"\",\"name\":\""+user.getName()+"\",\"password\":\""+user.getPassword()+"\",\"ipAddr\":\""+getIpAddr+"\"})");
			} catch (Exception e2) {
				//model.addAttribute("user", "添加失败");
				out.write(callback + "({\"Code\":-1,\"msg\":\"系统错误，添加失败！\"})");
			}
			
		}
		
		
		
		
	}
	
	// 更新一个用户
	@RequestMapping("/updateUser")
	@ResponseBody
	public void updateUser(HttpServletRequest request,HttpServletResponse response,User user) throws IOException {
		String getIpAddr = ipUtils.getIpAddr(request);
        user.setIpAddr(getIpAddr);;  //获取IP地址并放入user对象里

		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;
		PrintWriter out = response.getWriter();
		try {
			userService.updateUser(user);
			
			System.out.println("----Console 本次用户更新成功，您更改的用户是：" + user.getName() + "，密码是：" + user.getPassword()+ "，IP是：" + user.getIpAddr());
			out.write(callback + "({\"Code\":\"0000\",\"name\":\""+user.getName()+"\",\"password\":\""+user.getPassword()+"\",\"ipAddr\":\""+user.getIpAddr()+"\"})");
		
		} catch (Exception e) {
			System.out.println("更新失败");
			System.out.println(e.getMessage());
			
			out.write(callback + "({\"Code\":\"-1\"})");
		}
	}
	
	

	// 删除一个用户
	@RequestMapping("/deleteUser")
	@ResponseBody
	public void deleteUser(HttpServletResponse response,HttpServletRequest request,User user) throws IOException {
		//String id = request.getParameter("id");
		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;
		PrintWriter out = response.getWriter();
		try {
			userService.deleteUser(user);
			System.out.println("----Console 删除成功，删除的用户ID是："+user.getId()+",用户名是："+user.getName());
			out.write(callback + "({\"Code\":\"0000\"})");
		} catch (Exception e) {
			System.out.println("删除失败");
			out.write(callback + "({\"Code\":\"-1\"})");
		}

	}
	
	
	
	
	
	
	
	
	

}
