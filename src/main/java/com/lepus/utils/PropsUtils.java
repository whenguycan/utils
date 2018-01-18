package com.lepus.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * 资源文件读取工具
 * @author wangchenyu
 * @date   2017-9-14
 */
public class PropsUtils {

	public static Properties props = new Properties();
	
	public static Properties init(String path){
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + path);
			props.load(is);
		} catch (Exception e) {
			System.out.println("资源文件[" + path + "]加载失败！");
			e.printStackTrace();
		}
		return props;
	}
	
	public static Properties reload(String path){
		props.clear();
		return init(path);
	}
	
}
