package com.lepus.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author whenguycan
 * @date 2016年12月29日 上午10:00:07
 */
public class DateUtils {
	
	public static String Date2String(Date date, DateStyle dateStyle){
		DateFormat df = new SimpleDateFormat(dateStyle.pattern);
		return df.format(date);
	}
	
	public static Date String2Date(String source, DateStyle dateStyle){
		DateFormat df = new SimpleDateFormat(dateStyle.pattern);
		try {
			return df.parse(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对于不支持的操作将直接返回原值
	 * @param date
	 * @param type=plus,minus支持的field=week,day,hour,minute,second
	 * 		type=set支持的field=year,month,day,hour,minute,second
	 * @param value
	 * @return
	 */
	public static Date calculate(Date date, OperationField field, OperationType type, int amount){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch(type){
			case PLUS:
				switch(field){
					case YEAR:c.add(Calendar.YEAR, amount); break;
					case MONTH:c.add(Calendar.MONTH, amount); break;
					case WEEK:c.add(Calendar.DAY_OF_YEAR, amount * 7); break;
					case DAY:c.add(Calendar.DAY_OF_YEAR, amount); break;
					case HOUR:c.add(Calendar.HOUR_OF_DAY, amount); break;
					case MINUTE:c.add(Calendar.MINUTE, amount); break;
					case SECOND:c.add(Calendar.SECOND, amount); break;
					default: return null;
				}
				break;
			case MINUS:
				switch(field){
					case YEAR:c.add(Calendar.YEAR, 0 -amount); break;
					case MONTH:c.add(Calendar.MONTH, 0 -amount); break;
					case WEEK:c.add(Calendar.DAY_OF_YEAR, 0 -amount * 7); break;
					case DAY:c.add(Calendar.DAY_OF_YEAR, 0 -amount); break;
					case HOUR:c.add(Calendar.HOUR_OF_DAY, 0 -amount); break;
					case MINUTE:c.add(Calendar.MINUTE, 0 -amount); break;
					case SECOND:c.add(Calendar.SECOND, 0 -amount); break;
					default: return null;
				}
				break;
			case SET:
				switch(field){
					case YEAR: c.set(Calendar.YEAR, amount); break;
					case MONTH: c.set(Calendar.MONTH, amount); break;
					case DAY: c.set(Calendar.DAY_OF_MONTH, amount); break;
					case HOUR: c.set(Calendar.HOUR_OF_DAY, amount); break;
					case MINUTE: c.set(Calendar.MINUTE, amount); break;
					case SECOND: c.set(Calendar.SECOND, amount); break;
					default: return null;
				}
				break;
			default:
				return null;
		}
		return new Date(c.getTimeInMillis());
	}
	
}

enum OperationField{
	YEAR,
	MONTH,
	WEEK,
	DAY,
	HOUR,
	MINUTE,
	SECOND;
}

enum OperationType{
	PLUS,
	MINUS,
	SET;
}

enum DateStyle{
	YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
	YYYY_MM_DD("yyyy-MM-dd");
	protected String pattern;
	private DateStyle(String pattern){
		this.pattern = pattern;
	}
}