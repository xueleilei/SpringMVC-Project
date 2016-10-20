package com.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/file")
public class UploadController { 
	
	
	
	@RequestMapping("/uploading"	)
	public String upload2(HttpServletRequest request,HttpServletResponse response,Model model) throws IllegalStateException, IOException {
		//创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断 request 是否有文件上传,即多部分请求
		if(multipartResolver.isMultipart(request)){
			//转换成多部分request  
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			//取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while(iter.hasNext()){
				//记录上传过程起始时的时间，用来计算上传时间
				int pre = (int) System.currentTimeMillis();
				//取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if(file != null){
					//取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					//如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if(myFileName.trim() !=""){
						try {
							System.out.println(myFileName);
							//重命名上传后的文件名
							String fileName = new Date().getTime() + file.getOriginalFilename();
							//从项目根目录开始
							String pathLocal = request.getSession().getServletContext().getRealPath("/");
							//定义上传路径
							String path = pathLocal+"/uploadfile/" + fileName;
							File localFile = new File(path);
							file.transferTo(localFile);
							
							String upUrl = "/uploadfile/" + fileName;
							model.addAttribute("upUrl", upUrl);
							model.addAttribute("upTimes", ((int) System.currentTimeMillis()-pre));
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("上传出错");
							model.addAttribute("upUrl", "上传出错");
						}
					}
				}
				//记录上传该文件后的时间
				int finaltime = (int) System.currentTimeMillis();
				System.out.println("时间："+(finaltime - pre)+"毫秒");
			}
			
		}
		
		
		return "/success";
	}
	
	@RequestMapping("/toUpload"	) 
	public String toUpload() {
		
		return "/upload";
	}
	
}
