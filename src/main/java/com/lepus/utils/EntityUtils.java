package com.lepus.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author whenguycan
 */
public class EntityUtils {

	public static String getTableName(Class<?> clazz){
		String tn = clazz.getName().toLowerCase(Locale.getDefault());
		int lidx = tn.lastIndexOf(".");
		return tn.substring(lidx+1);
	}
	
	public static String[] getAllColumnNames(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();
		String[] fn_arr = new String[fields.length];
		for(int i=0,len=fields.length;i<len;i++){
			fn_arr[i] = fields[i].getName();
		}
		return fn_arr;
	}

	//将修改后的类中非空的字段填充到模版
	public static <T> void loadFields(T model,T modified) throws Exception {
		Field[] fields = model.getClass().getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			Object med = field.get(modified);
			if(med != null){
				field.set(model, med);
			}
			field.setAccessible(false);
		}
	}
	
	/**
	 * 这个版本只做映射，下个版本解决类型不兼容问题
	 * @version 0.1
	 */
	public static <T> T map2Entity(Class<T> clazz,Map<String,Object> data) throws Exception{
		if(clazz == null || data == null){
			return null;
		}
		Map<String,Field> fm = new HashMap<String, Field>();
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			fm.put(f.getName(),f);
		}
		T t = (T)clazz.newInstance();
		for(Entry<String,Object> e : data.entrySet()){
			Field f = fm.get(e.getKey());
			f.setAccessible(true);
			f.set(t,e.getValue());
			f.setAccessible(false);
		}
		return t;
	}
}
