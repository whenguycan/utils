package com.lepus.utils;

/**
 * 根据包名和实体名自动生成dao,daoimpl,service,serviceimpl
 * 
 * @author whenguycan
 * @date 2018年12月14日 下午8:24:52
 */
public class DaoServiceUtils {

	public static void main(String[] args) {
		String path = "D:\\MyEclipse\\Workspaces\\gsdataanalysis\\src";
		String pkg = "com.dc.module.data";
		String pkg_dao = pkg + ".dao";
		String pkg_dao_impl = pkg_dao + ".impl";
		String pkg_service = pkg + ".service";
		String pkg_service_impl = pkg_service + ".impl";
		String path_base = path + "\\" + pkg.replace(".", "\\");
		String path_dao = path_base + "\\dao";
		String path_dao_impl = path_dao + "\\impl";
		String path_service = path_base + "\\service";
		String path_service_impl = path_service + "\\impl";
		
	}

}
