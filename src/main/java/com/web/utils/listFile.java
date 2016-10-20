package com.web.utils;

import java.io.File;


public class listFile {
	
	
	public static void main(String[] args) {
		String localPath = System.getProperty("user.dir")+"/uploadfile/";
		File file = new File(localPath+File.separator);
		list(file);
	}

	
	
	public static void list(File file) {
		if (file.isDirectory())// 判断file是否是目录
		{
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					list(lists[i]);// 是目录就递归进入目录内再进行判断
				}
			}
		}
		System.out.println(file);// file不是目录，就输出它的路径名，这是递归的出口
	}
}
