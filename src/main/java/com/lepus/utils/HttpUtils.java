package com.lepus.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author whenguycan
 */
public class HttpUtils {
	
	public static String post(String url){
		return post(url, null, null);
	}

	public static String post(String url, Map<String,Object> headers, Map<String,Object> params){
		return connect(url, headers, params, XRequestMethod.POST, null);
	}
	
	public static String get(String url, Map<String,Object> headers, Map<String,Object> params){
		return connect(url, headers, params, XRequestMethod.GET, null);
	}
	
	private static String connect(String url, Map<String,Object> headers, Map<String,Object> params, XRequestMethod requestMethod, XReadCharset readCharest) {
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			conn.setRequestMethod(requestMethod.name());
			conn.setConnectTimeout(16000);
			conn.setReadTimeout(16000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			StringBuffer p = new StringBuffer();
			if(headers != null){
				for(Entry<String,Object> e : headers.entrySet()){
					if(e.getKey() != null && e.getValue() != null){
						conn.addRequestProperty(e.getKey(),e.getValue().toString());
					}
				}
			}
			if(params != null){
				for(Entry<String,Object> e : params.entrySet()){
					if(e.getKey() != null && e.getValue() != null){
						p.append("&").append(e.getKey()).append("=").append(e.getValue());
					}
				}
			}
			conn.getOutputStream().write(p.toString().getBytes());
			conn.getOutputStream().flush();
			int responseCode = conn.getResponseCode();
			Map<String, List<String>> rh = conn.getHeaderFields();
			for(String key : rh.keySet()){
				System.out.print(key + ":");
				for(String val : rh.get(key)){
					System.out.println(val);
				}
			}
			if(responseCode == 200){
				readCharest = readCharest==null?XReadCharset.UTF8:readCharest;
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), readCharest.cs));
				String line = "";
				StringBuffer content = new StringBuffer();
				while((line=reader.readLine())!=null){
					content.append(line);
				}
				conn.disconnect();
				return content.toString();
			}
			return new JsonResponse(responseCode, "connect error").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse(-1, "exception").toString();
		}
	}
	
	public static class JsonResponse{
		protected Integer code;
		protected String message;
		public JsonResponse(int code, String message){
			this.code = code;
			this.message = message;
		}
		public String toString(){
			return "{\"code\":\"" + this.code + "\",\"message\":\"" + this.message + "\"}";
		}
	}
	
	public static enum XRequestMethod{
		POST,GET;
	}
	
	public static enum XReadCharset{
		UTF8("UTF-8"),
		GBK("GBK"),
		ISO88591("ISO-8859-1");
		private String cs;
		private XReadCharset(String cs){
			this.cs = cs;
		}
		public String cs(){
			return this.cs;
		}
	}
	
}
