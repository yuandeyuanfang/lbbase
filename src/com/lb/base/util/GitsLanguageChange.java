package com.lb.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 根据数据库表名生成增删改查各层相关文件
 * @author lb
 *
 */
public class GitsLanguageChange {
	
	
	public static void main(String[] args) {
		GitsLanguageChange gitsLanguageChange = new GitsLanguageChange();
		//sds码表中英文
//		String sdsLanguageFileName = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/GITS_SDS系统备选代码表 --- 翻译 .xlsx";
//		String sdsCodeFileName = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/4_codifier.sql";
//		gitsLanguageChange.sdsCodifiers(sdsLanguageFileName,sdsCodeFileName);
		
//		String messageFileName = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/错误提示整理 --- 翻译（部分）.xlsx";
//		String messageFilePath = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/webapp";
//		//message文件中英文
//		gitsLanguageChange.messageFile(messageFileName,messageFilePath);
		
		//lang文件中英文
		String langFileName = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/多语言整理(GITS) --- 翻译.xlsx";
		String langFilePath = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/webapp";
		gitsLanguageChange.langFile(langFileName,langFilePath);
		
//		String tableColumnFileName = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/GITS表结构.xlsx";
//		String tableColumnFilePath = "C:/Users/lb-pc/Desktop/RCS&GITS翻译/RCS&GITS翻译/GITS/";
//		gitsLanguageChange.tableColumn(tableColumnFileName,tableColumnFilePath);
	}
	
	private void tableColumn(String fileName1,String fileName2){

		List<String> chongfukeyList = new ArrayList<String>();//解析出重复的key对应不同的值，需考虑是否使用
		Map<String,String> ruleMap = new HashMap<String, String>();//解析翻译信息存放到此map中
		int sheetNum = 5;//一共解析多少个sheet页
		FileInputStream reader;
		File file = new File(fileName1);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if(isImpxlsx){
			Workbook workbookx =null;
			try {
				workbookx =  new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			int sameCount = 0;
			for (int sheeti = 0; sheeti < sheetNum; sheeti++) {
				Sheet sheet = workbookx.getSheetAt(sheeti);   
				int rcount=sheet.getPhysicalNumberOfRows();
				int jiexiCount = 0;
				String columnName = "";
				if(rcount>0){
					for (int i = 3; i < rcount; i++) {
						Row row=sheet.getRow(i);
						String error_id = caseCellType(row.getCell(1)).trim();
						String error_id_language = caseCellType(row.getCell(2)).trim();
						if(columnName.equals("")){
							if(error_id_language.trim().length()>0){
								columnName =error_id_language+"("+error_id+")";
							}else{
								columnName = error_id;
							}
						}else{
							if(error_id_language.trim().length()>0){
								columnName =columnName +  "	" +error_id_language.replaceAll("	","")+"("+error_id+")";
							}else{
								columnName = columnName +  "	" +error_id;
							}
						}
					}
					System.out.println(columnName);
				}
			}
		}
	

	}
	
	/**
	 * message文件中英文
	* @author lb-pc
	* @throws
	 */
	private void messageFile(String fileName1,String fileName2){
		List<String> chongfukeyList = new ArrayList<String>();//解析出重复的key对应不同的值，需考虑是否使用
		Map<String,String> ruleMap = new HashMap<String, String>();//解析翻译信息存放到此map中
		int sheetNum = 20;//一共解析多少个sheet页
		FileInputStream reader;
		File file = new File(fileName1);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if(isImpxlsx){
			Workbook workbookx =null;
			try {
				workbookx =  new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			int sameCount = 0;
			for (int sheeti = 0; sheeti < sheetNum; sheeti++) {
				Sheet sheet = workbookx.getSheetAt(sheeti);   
				int rcount=sheet.getPhysicalNumberOfRows();
				int jiexiCount = 0;
				if(rcount>0){
					for (int i = 1; i < rcount; i++) {
						Row row=sheet.getRow(i);
						String error_id = caseCellType(row.getCell(1)).trim();
						String error_id_language = caseCellType(row.getCell(3)).trim();
						if(error_id.indexOf(".")>0 && error_id_language.length()>0){
							if(!ruleMap.containsKey(error_id)){
								jiexiCount++;
								ruleMap.put(error_id, error_id_language);
//								System.out.println(error_id+"  ----------  "+error_id_language);
							}else{
								if(!error_id_language.equals(ruleMap.get(error_id))){
									if(!chongfukeyList.contains(error_id)){
										chongfukeyList.add(error_id);
										sameCount++;
									}
//									System.out.println("重复的key: "+error_id+"  ----------  "+error_id_language+"------" + ruleMap.get(error_id));
								}
							}
						}
					}
					System.out.println("第"+sheeti+"个sheet页解析出"+jiexiCount+"个错误码");
				}
			}
			System.out.println("重复的key: "+sameCount);
			System.out.println("一共解析出key: "+ruleMap.size());
		}
		
		File file2 = new File(fileName2);
//		List<File> fileListReturn = new ArrayList<File>();
		File[] fileList = file2.listFiles();
		if(fileList!=null && fileList.length>0){
			for (int i = 0; i < fileList.length; i++) {
				File fileMessage = new File(fileName2+"/"+fileList[i].getName()+"/locale/message-en.js");
				int tihuanCount = 0;
				if(fileMessage.exists()){
					try {
						StringBuffer resultstr = new StringBuffer();
						reader = new FileInputStream(fileMessage);
						InputStreamReader isr = new InputStreamReader(reader);
						BufferedReader br = new BufferedReader(isr);
						String temp = "";
						while ((temp = br.readLine()) != null) {
							if(temp.length()>0 && temp.indexOf("'")>=0  && temp.indexOf(":")>=0){
								String tempKey = temp.split(":")[0].replaceAll("'", "").trim();
								String tempValue = temp.split(":")[1].trim();
								tempValue = tempValue.substring(1);
								if(tempValue.indexOf("'")>0){
									tempValue = tempValue.substring(0,tempValue.indexOf("'"));
//									System.out.println(tempKey);
//									System.out.println(ruleMap.get(tempKey));
									if(ruleMap.get(tempKey)!=null && !tempValue.equals(ruleMap.get(tempKey))){
										tihuanCount++;
										System.out.println(tempKey+"需要修改: "+tempValue+"-->"+ruleMap.get(tempKey));
										temp=temp.replace(tempValue, ruleMap.get(tempKey));
									}
								}
							}
							resultstr = resultstr.append(temp);
							resultstr = resultstr.append(System.getProperty("line.separator"));
						}
						
						System.out.println(fileMessage.getAbsolutePath()+"需要修改数："+tihuanCount);
						br.close();
						FileOutputStream fos = new FileOutputStream(fileMessage);
						fos.write(resultstr.toString().getBytes());
						fos.flush();
						fos.close();
						System.out.println(fileMessage.getAbsolutePath()+"已修改");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		
//		try {
//			FileOutputStream fos = new FileOutputStream(fileName2);
//			StringBuffer sb = new StringBuffer("");
//			fos.write(sb.toString().getBytes());
//			fos.flush();
//			fos.close();
//		} catch (Exception e) {
//		}
	}
	
	/**
	 * message文件中英文
	* @author lb-pc
	* @throws
	 */
	private void langFile(String fileName1,String fileName2){
		List<String> chongfukeyList = new ArrayList<String>();//解析出重复的key对应不同的值，需考虑是否使用
		Map<String,String> ruleMap = new HashMap<String, String>();//解析翻译信息存放到此map中
		int sheetNum = 7;//一共解析多少个sheet页
		FileInputStream reader;
		File file = new File(fileName1);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if(isImpxlsx){
			Workbook workbookx =null;
			try {
				workbookx =  new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			int sameCount = 0;
			for (int sheeti = 0; sheeti < sheetNum; sheeti++) {
				Sheet sheet = workbookx.getSheetAt(sheeti);   
				int rcount=sheet.getPhysicalNumberOfRows()+760;
				int jiexiCount = 0;
				if(rcount>0){
					for (int i = 760; i < rcount; i++) {
						Row row=sheet.getRow(i);
						if(row==null){
							continue;
						}
						String error_id = caseCellType(row.getCell(1)).trim();
						String error_id_language = caseCellType(row.getCell(3)).trim();
						if(error_id.indexOf(".")>0 && error_id_language.length()>0){
							if(!ruleMap.containsKey(error_id)){
								jiexiCount++;
								ruleMap.put(error_id, error_id_language);
//								System.out.println(error_id+"  ----------  "+error_id_language);
							}else{
								if(!error_id_language.equals(ruleMap.get(error_id))){
									if(!chongfukeyList.contains(error_id)){
										chongfukeyList.add(error_id);
										sameCount++;
									}
//									System.out.println("重复的key: "+error_id+"  ----------  "+error_id_language+"------" + ruleMap.get(error_id));
								}
							}
						}
					}
					System.out.println("第"+sheeti+"个sheet页解析出"+jiexiCount+"个langid");
				}
			}
			System.out.println("重复的key: "+sameCount);
			System.out.println("一共解析出key: "+ruleMap.size());
		}
		
		File file2 = new File(fileName2);
//		List<File> fileListReturn = new ArrayList<File>();
		File[] fileList = file2.listFiles();
		if(fileList!=null && fileList.length>0){
			for (int i = 0; i < fileList.length; i++) {
				File fileMessage = new File(fileName2+"/"+fileList[i].getName()+"/locale/lang-en.js");
				int tihuanCount = 0;
				if(fileMessage.exists()){
					try {
						StringBuffer resultstr = new StringBuffer();
						reader = new FileInputStream(fileMessage);
						InputStreamReader isr = new InputStreamReader(reader);
						BufferedReader br = new BufferedReader(isr);
						String temp = "";
						
						String level1 = "";
						String level2 = "";
						String level3 = "";
						String level4 = "";
						int level = 0;
						while ((temp = br.readLine()) != null) {
							if(temp.trim().length()>0){
								if(temp.indexOf("{")>=0  && temp.indexOf("}")<0  && temp.indexOf(":")>0){
									level++;
									if(level==1){
										level1 = temp.split(":")[0].trim();
									}else if(level==2){
										level2 = temp.split(":")[0].trim();
									}else if(level==3){
										level3 = temp.split(":")[0].trim();
									}else if(level==4){
										level4 = temp.split(":")[0].trim();
										System.out.println(level4);
									}
								}
								if(temp.indexOf("}")>=0  && temp.indexOf("{")<0){
									level--;
								}
								if(temp.indexOf("'")>=0  && temp.indexOf(":")>=0){//最后一层，进行解析
									String tempKey = temp.split(":")[0].replaceAll("'", "").trim();
									if(level==1){
										tempKey = level1+"."+tempKey;
									}else if(level==2){
										tempKey = level1+"."+level2+"."+tempKey;
									}else if(level==3){
										tempKey = level1+"."+level2+"."+level3+"."+tempKey;
									}
									String tempValue = temp.split(":")[1].trim();
									tempValue = tempValue.substring(1);
									if(tempValue.indexOf("'")>0){
										tempValue = tempValue.substring(0,tempValue.indexOf("'"));
//										System.out.println(tempKey);
//										System.out.println(ruleMap.get(tempKey));
										if(ruleMap.get(tempKey)!=null && !tempValue.equals(ruleMap.get(tempKey))){
											if(!chongfukeyList.contains(tempKey)){
												tihuanCount++;
												System.out.println(tempKey+"需要修改: "+tempValue+"-->"+ruleMap.get(tempKey));
												temp=temp.replace("'"+tempValue+"'", "'"+ruleMap.get(tempKey)+"'");
											}else{
//												System.out.println(tempKey+"重复，不做修改");
											}
										}
									}
								}
							}
							resultstr = resultstr.append(temp);
							resultstr = resultstr.append(System.getProperty("line.separator"));
						}
						
						System.out.println(fileMessage.getAbsolutePath()+"需要修改数："+tihuanCount);
						br.close();
						FileOutputStream fos = new FileOutputStream(fileMessage);
						fos.write(resultstr.toString().getBytes());
						fos.flush();
						fos.close();
						System.out.println(fileMessage.getAbsolutePath()+"已修改");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * sds码表中英文
	* @param fileName1
	* @param fileName2 void
	* @author lb-pc
	* @throws
	 */
	private void sdsCodifiers(String fileName1,String fileName2){
		FileInputStream reader;
		List<String> sdsCodlist =new ArrayList<String>(); 
		try {
			reader = new FileInputStream(fileName2);
			InputStreamReader isr = new InputStreamReader(reader);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				sdsCodlist.add(temp);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		StringBuffer resultSql = new StringBuffer("");
		File file = new File(fileName1);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if(isImpxlsx){
			Workbook workbookx =null;
			try {
				workbookx =  new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Sheet sheet = workbookx.getSheetAt(0);   
			int rcount=sheet.getPhysicalNumberOfRows();
			int allCount = rcount-1;
			int sameCount = 0;
			int notsameCount = 0;
			int notsamePipeiCount = 0;
			if(rcount>0){
				for (int i = 1; i < rcount; i++) {
					Row row=sheet.getRow(i);
					String codifiers_id = caseCellType(row.getCell(3)).trim();
					String codifiers_code = caseCellType(row.getCell(4)).trim();
					String oldName = caseCellType(row.getCell(5)).trim();
					String newName = caseCellType(row.getCell(6)).trim();
					if(oldName.trim().equals(newName.trim())){
						sameCount++;
					}else{
						if(newName.length()>0 && newName.indexOf("'")<=0){
							if(newName.indexOf(" ")>=0){
								newName = newName.replace(" ", "");
							}
							notsameCount++;
							for (int j = 0; j < sdsCodlist.size(); j++) {
								if(sdsCodlist.get(j).indexOf("("+codifiers_id)>0 && sdsCodlist.get(j).indexOf("'"+codifiers_code+"',")>0 && sdsCodlist.get(j).indexOf("'"+oldName+"',")>0){
									notsamePipeiCount++;
									resultSql.append("UPDATE SDS_CODIFIERS SET codifiers_name='"+newName.trim()+"' WHERE CODIFIERS_ID="+codifiers_id+" and codifiers_code ='"+codifiers_code +"';");
									resultSql.append(System.getProperty("line.separator"));
									continue;
								}
							}
//							System.out.println(oldName+" -------- "+newName);
						}
					}
				}
				System.out.println("总数："+allCount +" 相同数："+sameCount +" 不同数："+notsameCount +" 不同数匹配上："+notsamePipeiCount);
			}
		}
		File filesql = new File("D:/sdsCodifiers.sql");
		try {
			FileOutputStream fos = new FileOutputStream(filesql);
			fos.write(resultSql.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String caseCellType(Cell cell){
		DateFormat daf = new SimpleDateFormat("yyyy-MM-dd");
		String value="";
		try {
			if(cell==null){
				return value;
			}
			switch (cell.getCellType()) {  
			 
	         case Cell.CELL_TYPE_FORMULA:  
	        	 value = cell.getCellFormula();  
	             break;  
	         case Cell.CELL_TYPE_NUMERIC:  
	             if(HSSFDateUtil.isCellDateFormatted(cell)){  
	            	 value = daf.format(cell.getDateCellValue());  
	             }else{  
	            	 BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());  
	            	 value = String.valueOf(bigDecimal);  
	             }  
	             break;  
	         case Cell.CELL_TYPE_STRING:  
	        	 value = cell.getRichStringCellValue().getString();  
	             break;  
	         case Cell.CELL_TYPE_BOOLEAN:  
	        	 value = String.valueOf(cell.getBooleanCellValue());  
	             break;  
	         default: 
	        	 value = cell.getStringCellValue();  
	        	 break;
	         }  
		} catch (Exception e) {
			return value;
//			e.printStackTrace();
		}
		 return value;
	}
	
	
	
}
