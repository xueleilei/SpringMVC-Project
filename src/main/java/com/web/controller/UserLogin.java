package com.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.model.User;
import com.web.service.UserServiceI;
import com.web.utils.getMD5;

//@Controller注解用于标示本类为web层控制组件
@Controller
//@RequestMapping("/user")用于标定访问时对url位置
@RequestMapping("/user")
public class UserLogin {

	private UserServiceI userService;
	//自动注入业务层的userService类
	@Autowired
	public void setUserService(UserServiceI userService) {
		this.userService = userService;
	}

	// 登陆验证
	//login业务的访问位置为/user/login
	@RequestMapping("/login")
	@ResponseBody
	public String login(User user, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;
		response.setContentType("application/x-javascript");
		response.setCharacterEncoding("UTF-8");

		//MD5
		String md5Password = getMD5.MD5(user.getPassword());
		System.out.println(md5Password);
	
		
		//调用login方法来验证是否是注册用户
		boolean loginType = userService.login(user.getName(), md5Password);

		if (loginType) {
			// 如果验证通过,则将用户信息传到前台
			
			//Cookie userCookie = new Cookie("username",user.getName());
			//userCookie.setMaxAge(1000);
			//response.addCookie(userCookie);
			
			//在Session里保存信息  
	        session.setAttribute("username", user.getName()); 
	        System.out.println("---------------"+session.getAttribute("username"));
			// request.setAttribute("user",user);
			return callback + "({\"Code\":\"0000\",\"type\":\"1\"})";
		} else {
			// 若不对,则将错误信息显示到错误页面
			request.setAttribute("message", "用户名密码错误");

			return callback + "({\"Code\":\"-1\",\"type\":\"-1\"})";
		}

	}
	
	
	
	/** 
     * 退出系统 
     * @param session 
     *          Session 
     * @return 
     * @throws Exception 
     */  
    @RequestMapping(value="/loginOut")  
    public String logout(HttpSession session) throws Exception{  
        //清除Session  
        session.invalidate();  
        return "redirect:../index.html";  
    }  

}
