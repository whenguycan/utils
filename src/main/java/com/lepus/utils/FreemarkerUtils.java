package com.lepus.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * TODO 未完成
 * freemarker
 * @author whenguycan
 * @date 2017年6月2日 下午3:08:14
 */
public class FreemarkerUtils {
	
	private static final String folder = "c:\\fm";
	private static final String dbName = "anime";
	private static final String[] tableNames = {"t_e_anime", "t_e_user"};
	private static final String packageName = "org.rings.framework.scmp.csms.xxx";
	
	
	public static void main(String[] args){
		make(folder, dbName, tableNames);
	}
	
	/**
	 * freemarker, make sure template.len == products.len
	 */
	public static void make(String folder, String dbName, String[] tableNames){
		try {
			List<EntityMeta> metas = getTableMetas(dbName, tableNames);
			for(EntityMeta meta : metas){
				String entityName = getEntityName(meta.name);
				String tableName = getTableName(meta.name);
				//update config
				Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
				config.setDirectoryForTemplateLoading(new File(folder));
				config.setDefaultEncoding("UTF-8");
				config.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
				//bulid template
				Template template = config.getTemplate("Entity.java");
				//fill params
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("packageName", packageName);
				params.put("tableName", tableName);
				params.put("entityName", entityName);
				//make file
				Writer writer = new OutputStreamWriter(new FileOutputStream(folder + File.separator + entityName + ".java"), "UTF-8");
				template.process(params, writer);
			}
			System.out.println("over");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getTableName(String origin){
		return origin.toUpperCase();
	}
	
	private static String getEntityName(String origin){
		String source = origin.toLowerCase();
		while(source.indexOf("_") != -1){
			int pin = source.indexOf("_");
			if(pin == source.length() - 1){
				source = source.replace("_", "");
			}else{
				source = source.substring(0, pin) + source.substring(pin + 1, pin + 2).toUpperCase() + source.substring(pin + 2);
			}
		}
		//TODO 首字母大写
		return source;
	}
	
	private static List<EntityMeta> getTableMetas(String dbName, String[] tableNames){
		List<EntityMeta> list = new ArrayList<EntityMeta>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+ dbName +"?user=wang&password=12345");
			DatabaseMetaData dbMeta = conn.getMetaData();
			for(int i=0,iLen=tableNames.length;i<iLen;i++){
				String tableName = tableNames[i];
				ResultSet rs = dbMeta.getColumns(conn.getCatalog(), dbName, tableName, null);
				while (rs.next()) {
					ResultSetMetaData rsMeta = rs.getMetaData();
					int jLen = rsMeta.getColumnCount();
					for(int j=1;j-1<jLen;j++){
						list.add(new EntityMeta(tableName, rs.getObject(4), rs.getObject(6), rs.getObject(12)));
					}
				}
				rs.close();
			}
			conn.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

class EntityMeta{
	public String name;
	public List<String> columns = new ArrayList<String>();
	public EntityMeta(String tableName, Object... metas){
		if(metas == null)
			return;
		this.name = tableName;
		Object[] arr = metas;
		if(arr.length > 0)
			columns.add(String.valueOf(arr[0]));
		if(arr.length > 1)
			columns.add(String.valueOf(arr[1]));
		if(arr.length > 2)
			columns.add(String.valueOf(arr[2]));
	}
}
