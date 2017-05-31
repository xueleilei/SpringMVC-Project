package com.web.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.dao.UserMapper;
import com.web.model.User;
import com.web.utils.getMD5;
import com.web.utils.ipUtils;

//@Service("UserService") 注解用于标示此类为业务层组件,在使用时会被注解的类会自动由
//spring进行注入,无需我们创建实例
//实现用户的接口类
@Service("userService")
public class UserServiceImpl implements UserServiceI {

	private UserMapper userMapper;

	public UserMapper getUserMapper() {
		return userMapper;
	}

	//自动注入userMapper dao 用于访问数据库
	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	//根据用户密码进行验证 [实现方法]  从前台页面获取name与password
	public boolean login(String name,String password){
		//System.out.println("输入的账号:" + name + "输入的密码:" + password);
        //对输入账号进行查询,取出数据库中保存对信息
        User user = userMapper.selectByName(name);
        if (user != null) {
            //System.out.println("查询出来的账号:" + user.getUsername() + "密码:" + user.getPassword());
            //System.out.println("---------");
            if (user.getName().equals(name) && user.getPassword().equals(password))
                return true;

        }
        return false;
		
		
	}
	
	//根据ID获取一个用户的姓名 [实现方法]
	public User getUserById(String id) {
		return userMapper.selectByPrimaryKey(id);
	}
	
	//根据ID获取一个用户的姓名 [实现方法]
	public User getUserByName(String name) {
		return userMapper.selectByName(name);
	}
	
	//获取所有用户列表 [实现方法]
    public List<User> getAll() {
        return userMapper.getAll();
    }
    
    //添加一个用户 [实现方法]
	public int insertUser(User user) {
		
		String md5Password = getMD5.MD5(user.getPassword());
		user.setPassword(md5Password);
		
		return userMapper.insertUser(user);
	}
	
	//更新一个用户 [实现方法]
	public int updateUser(User user) {
		String md5Password = getMD5.MD5(user.getPassword());
		user.setPassword(md5Password);
	
		System.out.println("---------------"+user.getIpAddr());
		
		return userMapper.updateUser(user);
	}
	
	//删除一个用户 [实现方法]
	public int deleteUser(User user) {
		System.out.println("删除用户：id="+user.getId());
		return userMapper.deleteByPrimaryKey(user);
	}
	
	

	
}
