package com.lepus.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由smartcvs导出的列表拷贝文件
 * @author whenguycan
 * @date 2018年3月1日 下午3:49:37
 */
public class CVSCopier {
	
	public static final String TEMPLATE_FILEPATH = "C:\\Users\\Administrator\\Desktop\\changes.txt";
	public static final String TARGET_FOLDER = "C:\\Users\\Administrator\\Desktop\\changes";
	public static final String WORKSPACES_PREF = "D:\\MyEclipse\\Workspaces\\";
	public static final String HOLDER_WEBINF = "WEB-INF";
	public static final String HOLDER_CLASSES = "classes";
	public static final String HOLDER_SUFFIX_JAVA = ".java";
	public static final String HOLDER_SUFFIX_CLASS = ".class";
	public static final String HOLDER_DOLLAR = "$";
	
	public static Map<String, String> getDeployMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("jcsj", "D:\\Tomcat\\apache-tomcat-7.0.59\\webapps\\jcsj\\");
		map.put("exam", "D:\\Tomcat\\apache-tomcat-7.0.59.2\\webapps\\exam\\");
		return map;
	}

	public static void main(String[] args){
		doCopy(TEMPLATE_FILEPATH, TARGET_FOLDER);
	}
	
	public static void doCopy(String templateFilepath, String targetFolder){
		try {
			Files.lines(new File(templateFilepath).toPath())
				.filter(line -> line.startsWith(WORKSPACES_PREF))
				.forEach(line -> {
					String holder = line.replace(WORKSPACES_PREF, "");
					String[] idxArr = holder.split("\\\\");
					transferPref(idxArr);
					copy(idxArr, targetFolder);
				});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void transferPref(String[] idxArr){
		if(idxArr[0].contains(" "))
			idxArr[0] = idxArr[0].substring(0, idxArr[0].indexOf(" "));
	}
	
	public static void copy(String[] idxArr, String targetFolder){
		if(idxArr[idxArr.length - 1].contains(HOLDER_SUFFIX_JAVA))
			copyClass(idxArr, targetFolder);
		else
			copyStatic(idxArr, targetFolder);
	}
	
	public static void copyClass(String[] idxArr, String targetFolder){
		String pref = getDeployMap().get(idxArr[0]);
		String suff = makeSuff(idxArr, 2);
		String source = (pref + HOLDER_WEBINF + "\\" + HOLDER_CLASSES + suff).replace(HOLDER_SUFFIX_JAVA, HOLDER_SUFFIX_CLASS);
		listWithAnonymous(source).stream().forEach(f -> {
			String suffPartial = suff;
			if(f.getName().contains(HOLDER_DOLLAR)){
				String reg = f.getName().substring(0, f.getName().indexOf(HOLDER_DOLLAR)) + HOLDER_SUFFIX_JAVA;
				suffPartial = suff.replace(reg, f.getName());
			}
			String target = (targetFolder + "\\" + idxArr[0] + "\\" + HOLDER_WEBINF + "\\" + HOLDER_CLASSES + suffPartial).replace(HOLDER_SUFFIX_JAVA, HOLDER_SUFFIX_CLASS);
			copyFile(f.getAbsolutePath(), target);
		});
	}
	
	public static void copyStatic(String[] idxArr, String targetFolder){
		String pref = getDeployMap().get(idxArr[0]);
		String suff = makeSuff(idxArr, 2);
		String source = (pref + suff).replace("\\\\", "\\");
		String target = targetFolder + "\\" + idxArr[0] + suff;
		copyFile(source, target);
	}
	
	public static List<File> listWithAnonymous(String source){
		List<File> list = new ArrayList<File>();
		File file = new File(source);
		list.add(file);
		String pureName = file.getName().replace(HOLDER_SUFFIX_CLASS, "").replace(HOLDER_SUFFIX_JAVA, "");
		String pureName$ = pureName + HOLDER_DOLLAR;
		Arrays.asList(file.getParentFile().listFiles())
			.stream()
			.filter(f -> f.getName().contains(pureName$))
			.forEach(f -> list.add(f));
		return list;
	}
	
	public static String makeSuff(String[] idxArr, int idxStart){
		String suff = "";
		for(int i=idxStart; i<idxArr.length; i++){
			suff += "\\" + idxArr[i];
		}
		return suff;
	}
	
	public static void copyFile(String source, String target){
		File folder = new File(target.substring(0, target.lastIndexOf("\\")));
		if(!folder.exists())
			folder.mkdirs();
		try {
			Files.copy(new File(source).toPath(), new FileOutputStream(new File(target)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failed to copy : " + target);
		}
	}
	
}
