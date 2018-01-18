package com.lepus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件比对工具
 * @author wangchenyu@cit
 * @date   2017-11-2
 */
public class FileUtils {

	private static String PROJECT_NAME = "teacherblog";
	private static String TEMPLATE_NAME = "2017_12_08_11_52_22";
	
	private static String FILE_SEPARATOR = "\\";
	private static String DESKTOP = "C:\\Users\\Administrator\\Desktop\\geermu_diff\\" + PROJECT_NAME;
	private static String TEMP = DESKTOP + FILE_SEPARATOR + "comparator";
	private static String SUFFIX = ".fc";
	private static String[] COPY_IGNORE = {".jar", ".xls", ".zip", ".png", ".jpg"};
	
	/**
	 * 测试
	 */
	public static void main(String[] args){
		String root = "D:\\Tomcat\\apache-tomcat-7.0.59.2\\webapps\\" + PROJECT_NAME;
		String curr = curr();
		String target = DESKTOP + FILE_SEPARATOR + PROJECT_NAME + "_" + curr;
		match(root, TEMPLATE_NAME, target, curr);
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private static String curr(){
		return sdf.format(new Date());
	}
	
	public static void match(String root, String template, String target, String curr){
		System.out.println("loading template...");
		FileComparator fm = deserialize(TEMP, template);
		if(fm == null){
			System.out.println("template not found, create curr as template...");
			fm = new FileComparator(getPaths(root));
			serialize(TEMP, fm, curr);
		}else{
			System.out.println("loading curr...");
			Map<String, String> origin = getPaths(root);
			System.out.println("serlalize curr...");
			String serializePath = serialize(TEMP, new FileComparator(origin), curr);
			System.out.println("starting compare files...");
			List<String> paths = new ArrayList<String>();
			Map<String, String> map = fm.getPaths();
			for(String key : origin.keySet()){
				if(!map.containsKey(key) || !origin.get(key).equals(map.get(key))){
					if(!ifIgnore(key))
						paths.add(key);
				}
			}
			System.out.println("operate diff files...");
			if(paths.size() == 0){
				System.err.println("no diff files...");
				File f = new File(serializePath);
				if(f.exists())
					f.delete();
			}
			for(String path : paths){
				operate(root, path, target);
			}
		}
		System.out.println("mission complete.");
	}
	
	private static boolean ifIgnore(String key){
		boolean ignore = false;
		if(COPY_IGNORE != null && COPY_IGNORE.length != 0){
			for(String s : COPY_IGNORE){
				if(key != null && key.indexOf(s) != -1){
					ignore = true;
					break;
				}
			}
		}
		return ignore;
	}
	
	private static void operate(String root, String path, String target){
		String abs = root + path;
		String absTarget = target + path;
		File folder = new File(absTarget.substring(0, absTarget.lastIndexOf(FILE_SEPARATOR)));
		if(!folder.exists())
			folder.mkdirs();
		copyFile(abs, absTarget);
	}
	
	private static void copyFile(String source, String target){
		System.out.println("copying file... " + source);
		try {
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			FileChannel fci = fis.getChannel();
			FileChannel fco = fos.getChannel();
			fco.write(fci.map(FileChannel.MapMode.READ_ONLY, 0, fci.size()));
			fco.close();
			fci.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class FileComparator implements Serializable{
		private static final long serialVersionUID = 182715362928555363L;
		private Map<String, String> paths = new HashMap<String, String>();
		public FileComparator(Map<String, String> paths){
			this.paths.putAll(paths);
		}
		public Map<String, String> getPaths() {
			return paths;
		}
		public void setPaths(Map<String, String> paths) {
			this.paths = paths;
		}
	}
	
	private static String serialize(String temp, FileComparator fm, String curr){
		try {
			File folder = new File(temp);
			if(!folder.exists())
				folder.mkdirs();
			String filepath = temp + FILE_SEPARATOR + curr + SUFFIX;
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath));
			oos.writeObject(fm);
			oos.flush();
			oos.close();
			return filepath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static FileComparator deserialize(String temp, String tag){
		if(tag == null || "".equals(tag))
			return null;
		File file = new File(temp + FILE_SEPARATOR + tag + SUFFIX);
		if(!file.exists())
			return null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object o = ois.readObject();
			if(o != null && o instanceof FileComparator)
				return (FileComparator)o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<String, String> getPaths(String pathRoot){
		Map<String, String> map = new HashMap<String, String>();
		File root = new File(pathRoot);
		fill(root, pathRoot, map);
		return map;
	}
	private static void fill(File file, String filter, Map<String, String> pathMap){
		if(file.isDirectory()) {
			for(File f : file.listFiles()){
				fill(f, filter, pathMap);
			}
		} else
//			pathMap.put(cut(file.getAbsolutePath(), filter), Encrypt.md5(file));
			pathMap.put(cut(file.getAbsolutePath(), filter), Encrypt.md5(file, "\t", "\r\n", "\n", " "));
	}
	private static String cut(String path, String filter){
		return path.replace(filter, "");
	}
	
}
