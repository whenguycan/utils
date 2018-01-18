package com.lepus.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * 文档工具，替换文档中所有中文
 * @author wangchenyu@cit
 * @date   2017-9-15
 */
public class FileUtils2 {
	
	//文档需要修改成utf8编码（目测u4e00和u9fa5是utf8的编码）
	//清空文档中的所有中文字符
	public static void format(String source, String target){
		try {
			String regex = "[\u4e00-\u9fa5]";
			Pattern pat = Pattern.compile(regex);
			InputStream is = new FileInputStream(new File(source));
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder txt = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				txt.append(pat.matcher(line).replaceAll("").replace("author", ""));
				txt.append("\r\n");
			}
			reader.close();
			OutputStream os = new FileOutputStream(new File(target));
			os.write(txt.toString().getBytes());
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		String source = "C:\\Users\\Administrator\\Desktop\\x.txt";
		String target = "C:\\Users\\Administrator\\Desktop\\y.txt";
		format(source, target);
	}
	
}
