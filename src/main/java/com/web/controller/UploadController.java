package com.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.web.model.User;



@Controller
@RequestMapping("/file")
public class UploadController { 
	
	@RequestMapping("/toUpload"	) 
	public String toUpload(Model model)  {
		
		String path="/Users/mac/resin-pro-4.0.49/webapps/maven/uploadfile/";
		
		File file=new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		

		File[] tempList = file.listFiles();
		System.out.println("该目录下对象个数："+tempList.length);
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				System.out.println("文件："+tempList[i]);
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹："+tempList[i]);
			}
		}
		
		
		
	
		System.out.println(tempList); 
		try {
			// System.out.println(u.getName());
			model.addAttribute("file_item", tempList);
		} catch (Exception e) {
			model.addAttribute("file_item", "没有文件");
		}

		return "/upload";   //选择模板名称upload.html
	}
	


    // 根据ID查询用户的名称
	@RequestMapping("/delete")
	@ResponseBody
	public void getUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String filename = request.getParameter("filename");
		String fileurl = "/Users/mac/resin-pro-4.0.49/webapps/maven/uploadfile/"+filename;
		System.out.println("文件名是："+filename);
		System.out.println("文件路径是："+fileurl);
		
		//获取跨域JSONP callback参数，并返回页面
		String callback = request.getParameter("callback");
		callback = callback == null ? "" : callback;
		PrintWriter out = response.getWriter();
	    try {
	    	//执行删除并返回结果
		    boolean result = deleteFile(fileurl);
		    System.out.println(result);
			System.out.println("----Console 删除成功，删除文件路径是："+fileurl);
			out.write(callback + "({\"Code\":\"0000\"})");
		} catch (Exception e) {
			System.out.println("删除失败");
			out.write(callback + "({\"Code\":\"-1\"})");
		}
	    
	}
    /** 
     *  根据路径删除指定的目录或文件，无论存在与否 
     *@param sPath  要删除的目录或文件 
     *@return 删除成功返回 true，否则返回 false。 
     */  
    public boolean DeleteFolder(String sPath) {  
        boolean flag = false;  
        File file = new File(sPath);  
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;  
        } else {  
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);  
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);  
            }  
        }  
    }  
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public boolean deleteFile(String sPath) {  
    	boolean flag = false;  
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    }  
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public boolean deleteDirectory(String sPath) {  
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {  
            sPath = sPath + File.separator;  
        }  
        File dirFile = new File(sPath);  
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
	
	
	//上传
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
	

	
	public static void main(String[] args) {
		String path="/Users/mac/resin-pro-4.0.49/webapps/maven/uploadfile/";
		
		File file=new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		

		File[] tempList = file.listFiles();
		System.out.println("该目录下对象个数："+tempList.length);
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				System.out.println("文件："+tempList[i]);
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹："+tempList[i]);
			}
		}
	}

}
