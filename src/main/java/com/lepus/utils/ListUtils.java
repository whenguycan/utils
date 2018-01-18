package com.lepus.utils;

import java.util.List;

/**
 * 
 * @author whenguycan
 */
public class ListUtils {

	public static boolean isBlank(List<?> list){
		return list==null||list.size()==0?true:false;
	}
	
}
