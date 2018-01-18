package com.lepus.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据smart cvs列举的文件列表复制文件
 * @author whenguycan
 * @date 2018年1月18日 下午12:18:49
 */
public class FileUtils3 {
	
	private static String CURRENT_SERVER_CODE = "--";
	private static String CURRENT_LINKER = "--";
	private static final String SERVER_PREFIX = "server-";
	
	private static final String SOURCE_LIST_FILE_PATH = "C:\\Users\\Administrator\\Desktop\\change.txt";
	private static final String TARGET_BASE_FOLDER = "C:\\Users\\Administrator\\Desktop\\changes";
	
	private static final String SERVER_LOC = "D:\\Tomcat";

	public static void main(String[] args){
		go();
	}
	
	public static void go(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(SOURCE_LIST_FILE_PATH)));
			String line = null;
			while((line = reader.readLine()) != null){
				copy(line, TARGET_BASE_FOLDER);
			}
			reader.close();
			System.out.println("copy complete .");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String, String> getServerMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("--", "--");
		map.put("educationoa", "apache-tomcat-7.0.59");
		map.put("jcsj", "apache-tomcat-7.0.59");
		map.put("security", "apache-tomcat-7.0.59");
		map.put("evaluate", "apache-tomcat-7.0.59.2");
		map.put("exam", "apache-tomcat-7.0.59.2");
		map.put("performance", "apache-tomcat-7.0.59.2");
		map.put("schooloa", "apache-tomcat-7.0.59.3");
		map.put("student", "apache-tomcat-7.0.59.3");
		return map;
	}
	
	private static String transferFromSourcePathToClassPath(String sourcePath){
		String[] arr = sourcePath.split("\\\\");
		String classPath = SERVER_LOC
							+ "\\"
							+ getServerMap().get(CURRENT_SERVER_CODE) 
							+ "\\webapps\\" 
							+ CURRENT_SERVER_CODE
							+ "\\WEB-INF\\classes";
		String linker = "";
		boolean open = false;
		for(int i=0, len=arr.length; i<len; i++){
			String part = arr[i];
			if(open){
				classPath += "\\" + part;
				linker += "\\" + part;
			}
			if(!open && part.indexOf(CURRENT_SERVER_CODE) != -1){
				open = true;
				i++;
			}
		}
		CURRENT_LINKER = linker;
		return classPath.replace(".java", ".class");
	}
	
	private static void copy(String sourcePath, String targetFolder){
		if(isServerConfig(sourcePath)){
			changeServer(sourcePath);
		}
		if(needCopy(sourcePath)){
			String classPath = transferFromSourcePathToClassPath(sourcePath);
			File classFile = new File(classPath);
			if(classFile.exists()){
				copy(classFile, targetFolder);
			}
		}
	}
	
	private static void copy(File file, String targetFolder){
		String filename = file.getName();
		String purename = filename.replace(".class", "");
		String linker = CURRENT_LINKER.replace(purename + ".java", "");
		String targetFoldername = targetFolder + "\\" + CURRENT_SERVER_CODE + "\\WEB-INF\\classes" + linker;
		String targetFilename = targetFoldername + filename;
		File targetFolder_ = new File(targetFoldername);
		if(!targetFolder_.exists()){
			targetFolder_.mkdirs();
		}
		copyFile(file, targetFilename);
		File[] siblings = file.getParentFile().listFiles();
		for(File sib : siblings){
			if(sib.exists() && sib.isFile()){
				if(sib.getName().indexOf(purename) == 0 && sib.getName().indexOf("$") != -1){
					String targetSibname = targetFoldername + sib.getName();
					copyFile(sib, targetSibname);
				}
			}
		}
	}
	
	private static void copyFile(File file, String targetFilename){
		System.out.println("copy file : " + file.getPath());
		try {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fci = fis.getChannel();
			FileOutputStream fos = new FileOutputStream(targetFilename);
			FileChannel fco = fos.getChannel();
			fci.transferTo(0, fci.size(), fco);
			fco.close();
			fci.close();
			fos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean isServerConfig(String sourcePath){
		return sourcePath.indexOf(SERVER_PREFIX) != -1;
	}
	
	private static void changeServer(String sourcePath){
		CURRENT_SERVER_CODE = sourcePath.replace(SERVER_PREFIX, "");
	}
	
	private static boolean needCopy(String sourcePath){
		String[] arr = {".java"};
		boolean need = false;
		for(String suffix : arr){
			if(sourcePath.indexOf(suffix) != -1){
				need = true;
				break;
			}
		}
		return need;
	}
	
}
