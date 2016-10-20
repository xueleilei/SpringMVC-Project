package com.web.service;

import java.util.List;

import com.web.model.User;


//定义一个用户的接口
public interface UserServiceI {
	
	//根据用户密码进行验证
	public boolean login(String name,String password);
		
	//根据ID获取一个用户的姓名
	public User getUserById(String id);
	
	//根据NAME获取一个用户的姓名
	public User getUserByName(String name);
	
	//获取所有用户列表
	public List<User> getAll();
	
	//添加一个用户
	public int insertUser(User user);
	
	//更新一个用户
	public int updateUser(User user);
	
	//删除一个用户
	public int deleteUser(User user);
	
    

}
