package com.lepus.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

//import com.google.gson.ExclusionStrategy;
//import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author whenguycan
 */
public class GsonUtils {
	
	private static Gson gson = new GsonBuilder().serializeNulls().create();

	public static Object getContentFromJsonString(String json, String key){
		Map<?,?> map = gson.fromJson(json, HashMap.class);
		for(Object o : map.keySet()){
			if(key.equals(o)){
				return map.get(o);
			}
		}
		return null;
	}
	
	public static void main(String[] args){
		go();
	}
	
	public static void go(){
		List<ResponseData> list = new ArrayList<GsonUtils.ResponseData>();
		ResponseData data = new ResponseData();
		data.name = "jim";
		list.add(data);
		JsonReponse resp = new JsonReponse();
		resp.code = 1;
//		resp.message = "";
		resp.data = list;
		String json = gson.toJson(resp);
		JsonReponse map = gson.fromJson(json, JsonReponse.class);
		System.out.println(gson.toJson(map));
		
		//解决循原引用
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setAllowNonStringKeys(true);
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object arg0, String arg1, Object arg2) {
				return arg2 instanceof List;
			}
		});
		JSONObject sfJson = JSONObject.fromObject(map, jsonConfig);
		System.out.println(sfJson);
//		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
//			public boolean shouldSkipField(FieldAttributes arg0) {
//				return arg0.getName().indexOf("List") != -1;
//			}
//			public boolean shouldSkipClass(Class<?> arg0) {
//				return false;
//			}
//		}).create();
		
		
	}
	
	public static class JsonReponse{
		Object data;
		Integer code;
		String message;
	}
	
	public static class ResponseData{
		String name; 
	}
	
}
