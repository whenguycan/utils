package com.lepus.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author whenguycan
 * @date 2018年1月30日 下午3:58:58
 */
public class JsonElement implements Serializable{
	private static final long serialVersionUID = 3576370599186573296L;
    
    public static JsonElement getData(JSONObject jsonObject){
    	try {
			return getElement(jsonObject, "data");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static JsonElement getElement(JSONObject jsonObject, String key){
    	JsonElement element = new JsonElement();
    	try {
			JSONObject obj = jsonObject.getJSONObject(key);
			Iterator<String> iter = obj.keys();
			while(iter.hasNext()){
				String k = iter.next();
				Object v = obj.get(k);
				if(v instanceof JSONObject){
					element.put(k, getElement(obj, k));
				}else if(v instanceof JSONArray){
					element.put(k, getElementList(obj, k));
				}else{
					element.put(k, v);
				}
			}
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static List<JsonElement> getElementList(JSONObject jsonObject, String key){
    	List<JsonElement> list = new ArrayList<JsonElement>();
    	try {
			JSONArray jsonArr = jsonObject.getJSONArray(key);
			if(jsonArr != null){
				for(int i=0; i<jsonArr.length(); i++){
					JsonElement e = new JsonElement();
					JSONObject obj = jsonArr.getJSONObject(i);
					Iterator<String> iter = obj.keys();
					while(iter.hasNext()){
						String k = iter.next();
						Object v = obj.get(k);
						if(v instanceof JSONObject){
							e.put(k, getElement(obj, k));
						}else if(v instanceof JSONArray){
							e.put(k, getElementList(obj, k));
						}else{
							e.put(k, v);
						}
					}
					list.add(e);
				}
			}
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	private Map<String, Object> vMap = new HashMap<String, Object>();
	private Map<String, JsonElement> eMap = new HashMap<String, JsonElement>();
	private Map<String, List<JsonElement>> lMap = new HashMap<String, List<JsonElement>>();
	
	public void put(String key, Object value){
		if(value != null){
			if(value instanceof JsonElement){
				eMap.put(key, (JsonElement)value);
			}else if(value instanceof Collection){
				lMap.put(key, new ArrayList<JsonElement>((Collection)value));
			}else{
				vMap.put(key, value);
			}
		}
	}
	
	public Object getValue(String key){
		return vMap.get(key);
	}
	
	public JsonElement getElement(String key){
		return eMap.get(key);
	}
	
	public List<JsonElement> getElementList(String key){
		return lMap.get(key);
	}
	
	public boolean containsKey(String key){
		return isValue(key) || isElement(key) || isList(key);
	}
	
	public boolean isValue(String key){
		return vMap.containsKey(key);
	}
	
	public boolean isElement(String key){
		return eMap.containsKey(key);
	}
	
	public boolean isList(String key){
		return lMap.containsKey(key);
	}

}
