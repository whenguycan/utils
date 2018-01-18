package com.lepus.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Excel2007解析工具，针对大量数据，少量数据请使用原有WorkbookFactory方式
 * 请使用poi3.10.1以上版本
 * @author whenguycan
 * @date 2016年12月25日 上午11:28:58
 */
public class ExcelUtils {
	
	public static void main(String[] args){
		ExcelUtils.parse("C:\\Users\\Administrator\\Desktop\\test.xlsx");
	}
	
	public static void parse(String filename){
		new ExcelEventParser(filename, new SimpleSheetContentsHandler()).parse();
	}
	
}

class ExcelEventParser{
	
	private String filename;
	private SheetContentsHandler handler;
	
	public ExcelEventParser(String filename, SheetContentsHandler handler){
		this.filename = filename;
		this.handler = handler;
	}
	
	public void parse(){
		OPCPackage pkg = null;
		InputStream sheetInputStream = null;
		try {
			pkg = OPCPackage.open(filename, PackageAccess.READ);
			XSSFReader xssfReader = new XSSFReader(pkg);
			StylesTable styles = xssfReader.getStylesTable();
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
			sheetInputStream = xssfReader.getSheetsData().next();
			processSheet(styles, strings, sheetInputStream);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}finally{
			if(sheetInputStream != null){
				try {
					sheetInputStream.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			if(pkg != null){
				try {
					pkg.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	
	private void processSheet(StylesTable styles,ReadOnlySharedStringsTable strings,InputStream sheetInputStream) throws SAXException,ParserConfigurationException,IOException{
		XMLReader sheetParser = SAXHelper.newXMLReader();
		if(handler != null)
			sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, handler, false));
		else
			sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, new SimpleSheetContentsHandler(), false));
		sheetParser.parse(new InputSource(sheetInputStream));
	}
	
}

class SimpleSheetContentsHandler implements SheetContentsHandler{

	protected List<String> row = new ArrayList<String>();
	
	public void startRow(int rowNum) {
		
	}

	public void endRow() {
		
	}

	public void cell(String cellReference, String formattedValue) {
		String lineNum = cellReference.replaceAll("/W", "");
		System.out.println(lineNum);
	}

	public void headerFooter(String text, boolean isHeader, String tagName) {
		
	}
	
}