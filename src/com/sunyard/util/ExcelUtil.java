package com.sunyard.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.ant.util.FileUtils;
import org.aspectj.util.FileUtil;

import com.opensymphony.xwork2.ActionContext;

public class ExcelUtil {

	   /**   
     * Excel导入方法
     * @param response   
     * @param fileName
     * @throws IOException 
     * 获得数据方法：
     * 	int dataSize = data.size();
		for (int i = 0; i < dataSize; i++) {
			ArrayList<String> em = data.get(i);
			System.out.println(em.get(0));
			System.out.println(em.get(1));
			System.out.println(em.get(2));
		}
     * @throws InvalidFormatException 
     */ 
    public static ArrayList<ArrayList<String>> excelImport(String realpath) throws IOException, InvalidFormatException
    {
		/** 读取Excel并返回 */
		ArrayList<ArrayList<String>> data = readExcel(realpath);
		return data;
	}
    
    
	/**
	 * 数据导出到excel
	 * @param exportList 要导出数据列表
	 * @param columnStr  要导出数据库字段
	 * @param titleStr   要导出对应字段中文名称
	 * @param dictMap    需要翻译的字段对应的数据字典主键，英文字段为key,数组字典主键为value
	 * @param response
	 * @param fileName   文件名称
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	@SuppressWarnings("rawtypes")
	public static void excelExport(List exportList,String columnStr,String titleStr ,Map<String, String> dictMap,
			HttpServletResponse response,String fileName) throws IOException, RowsExceededException, WriteException{
		response.reset();
	    response.setContentType("application/vnd.ms-excel");
	    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	    Common.setFileDownloadHeader(request, response, fileName + ".xls");
	    
	    WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
	    WritableSheet ws = wwb.createSheet(fileName, 0);
	    
	    //设置每列宽度
	    for(int col=0;col<StringUtil.toArr(columnStr).length;col++){
	    	ws.setColumnView(col, 25);
	    }
	    
	    int row = -1;
	    List list = makeList(exportList, columnStr ,titleStr,dictMap);
	    //单元格填充数据
	    for (Iterator it = list.iterator(); it.hasNext(); ) {
	    	List tmpList = (List) it.next();
	    	row++;
	    	int col = 0;
	    	for (Iterator it1 = tmpList.iterator(); it1.hasNext(); ) {
	    		String tmp = String.valueOf(it1.next());
	    		if(("null").equals(tmp) || tmp == null) tmp = "";
	    		Label labelC = new Label(col++, row, tmp);
	    		ws.addCell(labelC);
	    	}
	    }
	    wwb.write();
	    wwb.close();
	}
       
	/**
	 * 把数据保存为ArrayList结构存放
	 * @param exportList 要导出数据列表
	 * @param columnStr  要导出数据库字段
	 * @param titleStr   要导出对应字段中文名称
	 * @param dictMap    需要翻译的字段对应的数据字典主键，英文字段为key,数组字典主键为value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List makeList(List exportList,String columnStr,String titleStr ,Map<String, String> dictMap){
 		ArrayList<List<Object>> list = new ArrayList<List<Object>>();//待导出数据列表
			try {
				List<String> columnList = StringUtil.toList(columnStr);
				String[] titleArr = StringUtil.toArr(titleStr);
				List<Object> titleList = new ArrayList<Object>();
				for(String title : titleArr){
					titleList.add(title);
				}
				list.add(titleList);
				
				for (Iterator ii = exportList.iterator(); ii.hasNext();) {
					List<Object> item = new ArrayList<Object>();
					Map<String,Object> m = new LinkedHashMap<String,Object>();
					
					Object e = (Object) ii.next();
					//数据类型判断，最终转换为map
					if(e instanceof java.util.Map<?, ?>){
						m = (Map<String, Object>) e;
					}else{
						m = ParamUtil.transBean2Map(e);
					}
					//遍历map,使导出数据顺序与titleList一一对应
					for(String column : columnList){
						if(dictMap != null && !dictMap.isEmpty() && dictMap.containsKey(column)){
							item.add(DDUtil.getContent(dictMap.get(column), (String)m.get(column.toUpperCase())));//根据数据字典翻译字段
						}else{
							if(m.get(column.toLowerCase()) == null)
								item.add(m.get(column.toUpperCase()));
							else
								item.add(m.get(column.toLowerCase()));
						}
					}
					list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
    /**   
     * 读取Excel,直接用workbookFactory,无须判断03或07版本
     * @param response   
     * @param excelPath
     */ 
	public static ArrayList<ArrayList<String>> readExcel(String excelPath)
			throws InvalidFormatException, FileNotFoundException, IOException {
		ArrayList<ArrayList<String>> aal = new ArrayList<ArrayList<String>>();
		org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(excelPath)));
		Sheet sheet = workbook.getSheetAt(0);
		int startRowNum = sheet.getFirstRowNum();
		int endRowNum = sheet.getLastRowNum();
		for (int rowNum = startRowNum + 1; rowNum <= endRowNum; rowNum++) {
			ArrayList<String> al = new ArrayList<String>();
			Row row = sheet.getRow(rowNum);
			if (row == null)
				continue;
			int startCellNum = row.getFirstCellNum();
			int endCellNum = row.getLastCellNum();
			for (int cellNum = startCellNum; cellNum <= endCellNum; cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell == null)
				{
					al.add("");
					continue;
				}
				int type = cell.getCellType();
		        String cellValue = "";  
		        DecimalFormat df = new DecimalFormat("#");  
				switch (type) {
				case Cell.CELL_TYPE_NUMERIC:// 数值、日期类型
					cellValue = df.format(cell.getNumericCellValue()).toString();  
					break;
				case Cell.CELL_TYPE_BLANK:// 空白单元格
					cellValue = "";
					break;
				case Cell.CELL_TYPE_STRING:// 字符类型
					cellValue = cell.getRichStringCellValue().getString().trim();  
					break;
				case Cell.CELL_TYPE_BOOLEAN:// 布尔类型
					cellValue = String.valueOf(cell.getBooleanCellValue()).trim();  
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					System.err.println("非法字符");// 非法字符;
					break;
		        case XSSFCell.CELL_TYPE_FORMULA:  
		            cellValue = cell.getCellFormula(); 
				default:
					cellValue = "";
					break;
				}
				al.add(cellValue);
			}
			aal.add(al);
		}
		File file = new File(excelPath);
		FileUtils.delete(file);
		return aal;
	}
	
    /**   
     * 获取文件后缀名
     * @param response   
     * @param fileName
     */ 
    private static String SufFix(String fileName)
    {
    	int index = fileName.lastIndexOf(".");
    	int length = fileName.length();
    	String suffix = fileName.substring(index,length);
    	return suffix;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
