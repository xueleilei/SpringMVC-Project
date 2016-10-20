package com.web.dao;



import java.util.List;

import com.web.model.User;

public interface UserMapper {
	
	//这里以接口形式定义了数据库操作方法,我们只需
	//在Mybatis映射文件中对其进行映射就可以直接使用
	//mybatis映射文件文件名必须与接口类相同，否则无法映射成功
	
	boolean login(String name,String password);

	User selectByPrimaryKey(String id);
	
	User selectByName(String name);
	
	List<User> getAll();
	
	int insertUser(User user);
	
	int updateUser(User user);
	
	int deleteByPrimaryKey(User user);
	
}