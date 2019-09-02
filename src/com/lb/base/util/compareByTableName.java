package com.lb.base.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.lb.base.system.JdbcUtil;

/**
 * 根据数据库表名生成增删改查各层相关文件
 * @author lb
 *
 */
public class compareByTableName {
	
	private static String tableName="DCS_SPOTDEALS";//表名
	private static String tableName2="TP_SPOTDEALS";//表名2
	
	
	private static String entityName="ml_maturityfile";//实体类名
	private static String packageName="com.erayt.sub.trade";//包名
	private static String filePath="D:/";//生成文件路径名
	private static String XMLType="Ibatis";//Mybatis或者Ibatis或者Hibernate
	private static int idIndex=0;//表的主键id位于第几列（从0开始）
	private static String packageNameEntity=packageName+".domain";//包名
	private static String packageNameService=packageName+".iface";//包名
	private static String packageNameServiceImpl=packageName+".iface.impl";//包名
	private static String packageNameDao=packageName+".dao";//包名
	private static String packageNameDaoImpl=packageName+".dao.impl";//包名
	private static String tableSeq=tableName+"_SEQ";//数据库序列名
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs =null;
	
	public static void main(String[] args) {
			SimpleDateFormat sf = (SimpleDateFormat)DateFormat.getInstance();
			sf.applyPattern("HHmmssSSS");
			String time = sf.format(Calendar.getInstance().getTime());
			
			System.out.println(String.format("%06d",Integer.parseInt(time)));
		Map<String,String> tablemap = new HashMap<String, String>();
		
		tablemap.put("CDS_USERFOLDER", "BASE_FOLDERAUTH");
		
		tablemap.put("ECAS_WORKSPACELAYOUT", "BASE_WORKSPACELAYOUT");
		tablemap.put("ECAS_WORKSPACECONF", "BASE_WORKSPACECONF");
		tablemap.put("ECAS_WORKSPACE", "BASE_WORKSPACE");
		tablemap.put("ECAS_USERROLE", "ECAS_USERROLE");
		tablemap.put("ECAS_USERLOGON", "ECAS_USERLOGON");
		tablemap.put("ECAS_USER", "ECAS_USER");
		tablemap.put("ECAS_ROLE", "ECAS_ROLE");
		tablemap.put("ECAS_PWDINFO", "ECAS_PWDINFO");
		tablemap.put("ECAS_PASSWORD", "ECAS_PASSWORD");
		tablemap.put("ECAS_PARAM", "ECAS_PARAM");
		tablemap.put("ECAS_MULTILINGUAL", "ECAS_MULTILINGUAL");
		tablemap.put("ECAS_MENU", "ECAS_MENU");
		tablemap.put("ECAS_LOGGER", "ECAS_LOGGER");
		tablemap.put("ECAS_FUNC", "ECAS_FUNC");
		tablemap.put("ECAS_EVENT", "ECAS_EVENT");
		tablemap.put("ECAS_DESKTOP", "ECAS_DESKTOP");
		tablemap.put("ECAS_DEPARTMENT", "ECAS_DEPARTMENT");
		tablemap.put("ECAS_BINDACCOUNT", "ECAS_BINDACCOUNT");
		tablemap.put("ECAS_BASBANK", "ECAS_BASBANK");
		tablemap.put("ECAS_APPL", "ECAS_APPL");
		tablemap.put("ECAS_ACTIONPOWER", "ECAS_ACTIONPOWER");
		
		tablemap.put("DCS_SPOTDEALS", "TP_SPOTDEALS");
		tablemap.put("DCS_FORWARDDEALS", "TP_FORWARDDEALS");
		tablemap.put("DCS_FXSWAPDEALS", "TP_FXSWAPDEALS");
		tablemap.put("DCS_IAMDEALS", "TP_IAMDEALS");
		tablemap.put("DCS_LOANDEPOSITDEALS", "TP_IRSDEALS");
		tablemap.put("DCS_CASHFLOWDEALS", "TP_IAMDEALS");
		tablemap.put("DCS_SWAPDEALS", "TP_IRSDEALS");
		tablemap.put("DCS_RESETLOG", "TP_IRSRESET");
		tablemap.put("DCS_SWAPLEGS", "TP_IRSLEGS");
		tablemap.put("RCS_FIELD_TRADEINFO", "TP_DEALS_EXT");
		tablemap.put("DCS_SWAPLEGS", "TP_IRSLEGS");
		tablemap.put("DCS_CASHFLOW", "TP_FXCASHFLOW");
//		tablemap.put("DCS_CASHFLOW", "TP_IAMCASHFLOW");
//		tablemap.put("DCS_CASHFLOW", "TP_IRSCASHFLOW");
		tablemap.put("TP_CHECKEVENT", "TP_CHECKEVENT");
		tablemap.put("TP_CHECKEVENT_DATA", "TP_CHECKEVENT_DATA");
		tablemap.put("TP_DEALREVERSAL", "TP_DEALREVERSAL");
		tablemap.put("TP_FTPDATA", "TP_FTPDATA");
		tablemap.put("TP_FUNDSPRICING", "TP_FUNDSPRICING");
		
		
		
		
		
		
		tablemap.put("MDS_CURVEPOINTS", "MKT_YIELDCURVEPOINT");
		tablemap.put("MDS_CURVES", "MKT_YIELDCURVE");
		tablemap.put("MDS_DATASETFEEDS", "BASE_MKTGRIDPAIRSREF");
		tablemap.put("MDS_DATASETS", "BASE_MKTGRID");
		tablemap.put("MDS_FXSPOTRATE", "MKT_SPOTQUOTE");
		tablemap.put("MDS_FXSWAPRATE", "MKT_SWAPPOINT");

		tablemap.put("SDS_ALLTABLESID","SDS_ALLTABLESID");
		tablemap.put("SDS_AUDITTRAIL","SDS_AUDITTRAIL");
		tablemap.put("SDS_AUDITTRAIL_MESSAGEI18N","SDS_AUDITTRAIL_MESSAGEI18N");
		tablemap.put("SDS_BONDS","SDS_BONDS");
		tablemap.put("SDS_BONDSCASHFLOW","SDS_BONDSCASHFLOW");
		tablemap.put("SDS_BONDSCODEINCLUDEFIELD","SDS_BONDSCODEINCLUDEFIELD");
		tablemap.put("SDS_BONDSEXERCISERECORD","SDS_BONDSEXERCISERECORD");
		tablemap.put("SDS_BONDSINCOME","SDS_BONDSINCOME");
		tablemap.put("SDS_BONDSRESETLOG","SDS_BONDSRESETLOG");
		tablemap.put("SDS_BONDSTYPOLOGY","SDS_BONDSTYPOLOGY");
		tablemap.put("SDS_BOOKS","SDS_BOOKS");
		tablemap.put("SDS_BRANCHES","SDS_BRANCHES");
		tablemap.put("SDS_BRANCHESGRP","SDS_BRANCHESGRP");
		tablemap.put("SDS_BRANCHESGRPELT","SDS_BRANCHESGRPELT");
		tablemap.put("SDS_CODIFIERS","SDS_CODIFIERS");
		tablemap.put("SDS_CODIFIERSGRP","SDS_CODIFIERSGRP");
		tablemap.put("SDS_CODIFIERSGRPELT","SDS_CODIFIERSGRPELT");
		tablemap.put("SDS_COMMODITY","SDS_COMMODITY");
		tablemap.put("SDS_COMMODITYSUBJECT","SDS_COMMODITYSUBJECT");
		tablemap.put("SDS_COMMONGRP","SDS_COMMONGRP");
		tablemap.put("SDS_COMMONGRPELT","SDS_COMMONGRPELT");
		tablemap.put("SDS_CONTACTINFO","SDS_CONTACTINFO");
		tablemap.put("SDS_COUNTRIES","SDS_COUNTRIES");
		tablemap.put("SDS_CPTY","SDS_CPTY");
		tablemap.put("SDS_CPTYCLASSES","SDS_CPTYCLASSES");
		tablemap.put("SDS_CPTYGRP","SDS_CPTYGRP");
		tablemap.put("SDS_CURRENCIES","SDS_CURRENCIES");
		tablemap.put("SDS_ENTITIES","SDS_ENTITIES");
		tablemap.put("SDS_FOLDERS","SDS_FOLDERS");
		tablemap.put("SDS_FOLDERSGRP","SDS_FOLDERSGRP");
		tablemap.put("SDS_FOLDERSGRPBOOKS","SDS_FOLDERSGRPBOOKS");
		tablemap.put("SDS_FOLDERSGRPENTITIES","SDS_FOLDERSGRPENTITIES");
		tablemap.put("SDS_FOLDERSGRPFOLDERS","SDS_FOLDERSGRPFOLDERS");
		tablemap.put("SDS_HOLIDAYS","SDS_HOLIDAYS");
		tablemap.put("SDS_IBORATEINDEX","SDS_IBORATEINDEX");
		tablemap.put("SDS_INSTRUMENT","SDS_INSTRUMENT");
		tablemap.put("SDS_INSTRUMENTCLASSES","SDS_INSTRUMENTCLASSES");
		tablemap.put("SDS_INSTRUMENTSALESGRP","SDS_INSTRUMENTSALESGRP");
		tablemap.put("SDS_INSTRUMENTSALESGRPELT","SDS_INSTRUMENTSALESGRPELT");
		tablemap.put("SDS_MULTLANGERRORINFO","SDS_MULTLANGERRORINFO");
		tablemap.put("SDS_PAIRS","SDS_PAIRS");
		tablemap.put("SDS_RATINGAGENCIES","SDS_RATINGAGENCIES");
		tablemap.put("SDS_RATINGS","SDS_RATINGS");
		tablemap.put("SDS_REISSUEINFO","SDS_REISSUEINFO");
		tablemap.put("SDS_SITES","SDS_SITES");
		tablemap.put("SDS_TARGETRATINGS","SDS_TARGETRATINGS");
		tablemap.put("SDS_TENORS","SDS_TENORS");
		tablemap.put("SDS_VARIABLEHOLIDAYS","SDS_VARIABLEHOLIDAYS");
		tablemap.put("SDS_WEEKHOLIDAYS","SDS_WEEKHOLIDAYS");
		tablemap.put("SOLAR_TASK","SOLAR_TASK");
		tablemap.put("SOLAR_TASK_BUNDLE","SOLAR_TASK_BUNDLE");
		tablemap.put("SOLAR_TASK_INSTANCE","SOLAR_TASK_INSTANCE");
		tablemap.put("SOLAR_TASK_JOB","SOLAR_TASK_JOB");
		tablemap.put("SOLAR_TASK_JOB_HISTORY","SOLAR_TASK_JOB_HISTORY");
		tablemap.put("SOLAR_TASK_JOB_INSTANCE","SOLAR_TASK_JOB_INSTANCE");
		tablemap.put("SOLAR_TASK_TRIGGER","SOLAR_TASK_TRIGGER");
		
		tablemap.put("POS_CCYFOLDER","POS_CCYFOLDER");
		tablemap.put("POS_CCYFOLDER_BAK","POS_CCYFOLDER_BAK");
		tablemap.put("POS_CNYREPOFOLDER","POS_CNYREPOFOLDER");
		tablemap.put("POS_CNYREPOFOLDER_BAK","POS_CNYREPOFOLDER_BAK");
		tablemap.put("POS_CRSFOLDER","POS_CRSFOLDER");
		tablemap.put("POS_CRSFOLDER_BAK","POS_CRSFOLDER_BAK");
		tablemap.put("POS_FXSWAPFOLDER","POS_FXSWAPFOLDER");
		tablemap.put("POS_FXSWAPFOLDER_BAK","POS_FXSWAPFOLDER_BAK");
		tablemap.put("POS_FXSWAPFOLDER_LOCK","POS_FXSWAPFOLDER_LOCK");
		tablemap.put("POS_IAMFOLDER","POS_IAMFOLDER");
		tablemap.put("POS_IAMFOLDER_BAK","POS_IAMFOLDER_BAK");
		tablemap.put("POS_INTERESTRATESWAPFOLDER","POS_INTERESTRATESWAPFOLDER");
		tablemap.put("POS_INTERESTRATESWAPFOLDER_BAK","POS_INTERESTRATESWAPFOLDER_BAK");
		tablemap.put("POS_IRSFOLDER","POS_IRSFOLDER");
		tablemap.put("POS_IRSFOLDER_BAK","POS_IRSFOLDER_BAK");
		tablemap.put("POS_OPTIONFOLDER","POS_OPTIONFOLDER");
		tablemap.put("POS_OPTIONFOLDER_BAK","POS_OPTIONFOLDER_BAK");
		tablemap.put("POS_REPOFOLDER","POS_REPOFOLDER");
		tablemap.put("POS_REPOFOLDER_BAK","POS_REPOFOLDER_BAK");
		tablemap.put("POS_SPOTFOLDER","POS_SPOTFOLDER");
		tablemap.put("POS_SPOTFOLDERCUR","POS_SPOTFOLDERCUR");
		tablemap.put("POS_SPOTFOLDERCUR_BAK","POS_SPOTFOLDERCUR_BAK");
		tablemap.put("POS_SPOTFOLDER_BAK","POS_SPOTFOLDER_BAK");
		tablemap.put("REVAL_BALANCE","REVAL_BALANCE");
		tablemap.put("REVAL_CNYREPOFOLDER","REVAL_CNYREPOFOLDER");
		tablemap.put("REVAL_CRSTRADE","REVAL_CRSTRADE");
		tablemap.put("REVAL_DAILYLOG","REVAL_DAILYLOG");
		tablemap.put("REVAL_FORWARDTRADE","REVAL_FORWARDTRADE");
		tablemap.put("REVAL_FXOPTIONTRADE","REVAL_FXOPTIONTRADE");
		tablemap.put("REVAL_FXSWAPFOLDER","REVAL_FXSWAPFOLDER");
		tablemap.put("REVAL_FXSWAPFOLDER_HIS","REVAL_FXSWAPFOLDER_HIS");
		tablemap.put("REVAL_FXSWAPTRADE","REVAL_FXSWAPTRADE");
		tablemap.put("REVAL_IAMFOLDER","REVAL_IAMFOLDER");
		tablemap.put("REVAL_IRSTRADE","REVAL_IRSTRADE");
		tablemap.put("REVAL_OPTIONFOLDER","REVAL_OPTIONFOLDER");
		tablemap.put("REVAL_OPTIONFOLDER_HIS","REVAL_OPTIONFOLDER_HIS");
		tablemap.put("REVAL_PRODUCTFTP","REVAL_PRODUCTFTP");
		tablemap.put("REVAL_PRODUCTTOTAL","REVAL_PRODUCTTOTAL");
		tablemap.put("REVAL_REPOFOLDER","REVAL_REPOFOLDER");
		tablemap.put("REVAL_RISKEVENT","REVAL_RISKEVENT");
		tablemap.put("REVAL_SPOTFOLDER","REVAL_SPOTFOLDER");
		tablemap.put("REVAL_SPOTFOLDER_HIS","REVAL_SPOTFOLDER_HIS");
		tablemap.put("REVAL_SPOTQUOTE","REVAL_SPOTQUOTE");
		tablemap.put("REVAL_SPOTTRADE","REVAL_SPOTTRADE");
		tablemap.put("REVAL_SWAPPOINT","REVAL_SWAPPOINT");
		tablemap.put("REVAL_VOLATILITY","REVAL_VOLATILITY");
		tablemap.put("REVAL_VOLUME","REVAL_VOLUME");
		tablemap.put("REVAL_YIELDCURVEPOINT","REVAL_YIELDCURVEPOINT");
		
		compareByTableName createFileByTableName = new compareByTableName();
//		createFileByTableName.createExcel(tablemap);
//		createFileByTableName.createExcelRCS();
		createFileByTableName.createExcelGITS();
	}
	
	
	private void createExcelGITS() {


		JdbcUtil JdbcUtil = new JdbcUtil();
		con = JdbcUtil.getConnection(con);
		List<TableColumn> list2 = new ArrayList<TableColumn>();
		//where t.TABLE_NAME in ('DCS_SPOTDEALS','DCS_FORWARDDEALS')
		String sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				knowledge.setColumnid(rs.getString(8));
				knowledge.setPrecision(rs.getString(9));
				knowledge.setScale(rs.getString(10));
				list2.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JdbcUtil jdbcUtil = new JdbcUtil();
		con = jdbcUtil.getConnection(con);
//		List<TableColumn> list = new ArrayList<TableColumn>();
		//where t.TABLE_NAME in ('DCS_SPOTDEALS','DCS_FORWARDDEALS')
		 sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				knowledge.setColumnid(rs.getString(8));
				knowledge.setPrecision(rs.getString(9));
				knowledge.setScale(rs.getString(10));
//				list.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
		String tableName="";
		int rowindex = 0;
		HSSFSheet sheet = null;
		 // 生成一个样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    // 设置这些样式
//	    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    // 生成一个字体
	    HSSFFont font = workbook.createFont();
	//    font.setColor(HSSFColor.VIOLET.index);
	    font.setFontHeightInPoints((short) 12);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    // 把字体应用到当前的样式
	    style.setFont(font);
	    // 生成并设置另一个样式
	    HSSFCellStyle style2 = workbook.createCellStyle();
//	    style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//	    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    
	    HSSFCellStyle style3 = workbook.createCellStyle();
	    style3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	    style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	    style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    
	    String[] headers = {"序号","字段名","字段名称","GITS字段名","迁移规则","数据类型","LENGTH","PRECISION","SCALE","是否为空","是否主键","是否必输","是否码值","码值取值信息","备注"};
	    String[] headers1 = {"序号","表名称","GITS表名","是否迁移（迁移/保留/删除）","迁移规则"};
	    List<String> collist = new ArrayList<String>();
	    boolean isis = true;
	    int aa =1;
	    for (TableColumn tableColumn : list2) {
	    	if(!tableColumn.getTableName().equals(tableName)){
				collist=new ArrayList<String>();
				tableName = tableColumn.getTableName();
				 // 生成一个表格
				if(isis){
					sheet = workbook.createSheet("迁移总表");
				     rowindex = 0;
				     //表名、表名称
					    HSSFRow row = sheet.createRow(rowindex);
					   
					    //产生表格标题行
					     row = sheet.createRow(rowindex);
					    rowindex++;
					   
					    for (short i = 0; i < headers1.length; i++) {
					    	HSSFCell  cell = row.createCell(i);
					       cell.setCellStyle(style);
					       HSSFRichTextString  text = new HSSFRichTextString(headers1[i]);
					       cell.setCellValue(text);
					    }
					    
					 // 设置表格默认列宽度为15个字节
					    sheet.setDefaultColumnWidth((short) 10);
					    sheet.setColumnWidth(0, (short) 2000);
					    sheet.setColumnWidth(1, (short) 7000);
					    sheet.setColumnWidth(2, (short) 7000);
					    sheet.setColumnWidth(3, (short) 8000);
					    sheet.setColumnWidth(4, (short) 20000);
					    isis = false;
				}
			     
				    // 生成另一个字体
				    HSSFFont font2 = workbook.createFont();
				    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				    // 把字体应用到当前的样式
				    style2.setFont(font2);
				   
				    // 声明一个画图的顶级管理器
				    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				    // 定义注释的大小和位置,详见文档
				    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
				    // 设置注释内容
				    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
				    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				    comment.setAuthor("leno");
				
				  HSSFRow  row = sheet.createRow(rowindex);
				        for (int i = 0; i < headers1.length; i++) {
				        	HSSFCell cell = row.createCell(i);
				            cell.setCellStyle(style2);
				            HSSFRichTextString  text = new HSSFRichTextString(getElementRCS(tableColumn,i,aa));
				            cell.setCellValue(text);
				            if(i == 2){
				            	cell.setCellFormula("HYPERLINK(\"#"+tableColumn.getTableName()+"!C1\",\""+tableColumn.getTableName()+"\")"); //HYPERLINK("#明细!A1","homepage")
				            	HSSFCellStyle linkStyle = workbook.createCellStyle();
				            	HSSFFont cellFont= workbook.createFont();
				            	cellFont.setUnderline((byte) 1);
				            	cellFont.setColor(HSSFColor.BLUE.index);
				            	linkStyle.setFont(cellFont);
				            	linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				            	linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				            	linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				            	linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				            	cell.setCellStyle(linkStyle);
				            }
				            }
				        rowindex++;
				        aa++;
			}
	    }
	    tableName="";
	    aa =1;
	    for (TableColumn tableColumn : list2) {
			if(1==1){
				// || tableColumn.getTableName().startsWith("ENGINE_") || tableColumn.getTableName().startsWith("SOLAR_") || tableColumn.getTableName().startsWith("TP_") 
				int index2 = 100;
				if(!tableColumn.getTableName().equals(tableName)){aa++;
					collist=new ArrayList<String>();
					 // 生成一个表格
				     sheet = workbook.createSheet(tableColumn.getTableName());
				     rowindex = 0;
				     tableName = tableColumn.getTableName();
				     //表名、表名称
					    HSSFRow row = sheet.createRow(rowindex);
					    rowindex++;
					       HSSFCell cell = row.createCell(0);
					       cell.setCellStyle(style);
					       HSSFRichTextString text = new HSSFRichTextString("表名");
					       cell.setCellValue(text);
					       cell = row.createCell(1);
					       cell.setCellStyle(style);
					        text = new HSSFRichTextString(tableColumn.getTableName());
					       cell.setCellValue(text);
					       
					       cell = row.createCell(2);
					       HSSFCellStyle linkStyle = workbook.createCellStyle();
			            	HSSFFont cellFont= workbook.createFont();
			            	cellFont.setUnderline((byte) 1);
			            	cellFont.setColor(HSSFColor.BLUE.index);
			            	linkStyle.setFont(cellFont);
			            	linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			            	linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			            	linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			            	linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			            	cell.setCellStyle(linkStyle);
					        text = new HSSFRichTextString("返回迁移总表");
					        cell.setCellFormula("HYPERLINK(\"#迁移总表!C"+aa+"\",\"返回迁移总表\")"); //HYPERLINK("#明细!A1","homepage")
			            	
					       
					        row = sheet.createRow(rowindex);
						    rowindex++;
						        cell = row.createCell(0);
						       cell.setCellStyle(style);
						        text = new HSSFRichTextString("表名称");
						       cell.setCellValue(text);
						       cell = row.createCell(1);
						       cell.setCellStyle(style);
						        text = new HSSFRichTextString(tableColumn.getTableComments());
						       cell.setCellValue(text);
						       
					    
					    //产生表格标题行
					     row = sheet.createRow(rowindex);
					    rowindex++;
					   
					    for (short i = 0; i < headers.length; i++) {
					        cell = row.createCell(i);
					       cell.setCellStyle(style);
					        text = new HSSFRichTextString(headers[i]);
					       cell.setCellValue(text);
					    }
				}
			    // 设置表格默认列宽度为15个字节
			    sheet.setDefaultColumnWidth((short) 10);
			    sheet.setColumnWidth(0, (short) 2000);
			    sheet.setColumnWidth(1, (short) 5000);
			    sheet.setColumnWidth(2, (short) 5000);
			    sheet.setColumnWidth(3, (short) 5000);
			    sheet.setColumnWidth(4, (short) 10000);
			    // 生成另一个字体
			    HSSFFont font2 = workbook.createFont();
			    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			    // 把字体应用到当前的样式
			    style2.setFont(font2);
			   
			    // 声明一个画图的顶级管理器
			    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			    // 定义注释的大小和位置,详见文档
			    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
			    // 设置注释内容
			    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
			    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
			    comment.setAuthor("leno");
			
			  
			    HSSFRow   row = sheet.createRow(rowindex);
			        for (int i = 0; i < headers.length; i++) {
			        	HSSFCell cell = row.createCell(i);
			            cell.setCellStyle(style2);
			            HSSFRichTextString  text = new HSSFRichTextString(getElement(tableColumn,i));
			            cell.setCellValue(text);
					}
			        rowindex++;
			}
		   
		}
		File file = null;
	   	file = new File("D:\\GITS.xls");
	   	if(file.exists()){
	   		if( file.delete() ) {
	   			file = new File("D:\\GITS.xls");
	   		}
	   	}
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	FileOutputStream fos = null;  
	    	BufferedOutputStream bos = null;
		    try {
		    	workbook.write(out);
		    	fos = new FileOutputStream(file);  
		    	bos = new BufferedOutputStream(fos);  
		    	bos.write(out.toByteArray());  
			} catch (IOException e) {
			} finally {
				try {
					if( bos != null ) {
						bos.close();
					}
					if( fos != null ) {
						fos.close();
					}
				} catch (IOException e) {
				}
			}
	
	}


	private void createExcelRCS() {

		JdbcUtil JdbcUtil = new JdbcUtil();
		con = JdbcUtil.getConnection(con);
		List<TableColumn> list2 = new ArrayList<TableColumn>();
		//where t.TABLE_NAME in ('DCS_SPOTDEALS','DCS_FORWARDDEALS')
		String sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				knowledge.setColumnid(rs.getString(8));
				knowledge.setPrecision(rs.getString(9));
				knowledge.setScale(rs.getString(10));
				list2.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JdbcUtil jdbcUtil = new JdbcUtil();
		con = jdbcUtil.getConnection(con);
		List<TableColumn> list = new ArrayList<TableColumn>();
		//where t.TABLE_NAME in ('DCS_SPOTDEALS','DCS_FORWARDDEALS')
		 sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				knowledge.setColumnid(rs.getString(8));
				knowledge.setPrecision(rs.getString(9));
				knowledge.setScale(rs.getString(10));
				list.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
		String tableName="";
		int rowindex = 0;
		HSSFSheet sheet = null;
		 // 生成一个样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    // 设置这些样式
//	    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    // 生成一个字体
	    HSSFFont font = workbook.createFont();
	//    font.setColor(HSSFColor.VIOLET.index);
	    font.setFontHeightInPoints((short) 12);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    // 把字体应用到当前的样式
	    style.setFont(font);
	    // 生成并设置另一个样式
	    HSSFCellStyle style2 = workbook.createCellStyle();
//	    style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//	    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    
	    HSSFCellStyle style3 = workbook.createCellStyle();
	    style3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	    style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	    style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    
	    String[] headers = {"序号","字段名","字段名称","GITS字段名","迁移规则","数据类型","LENGTH","PRECISION","SCALE","是否为空","是否主键","是否必输","是否码值","码值取值信息","备注"};
	    String[] headers1 = {"序号","表名称","RCS表名","对应GITS表名","迁移规则"};
	    List<String> collist = new ArrayList<String>();
	    boolean isis = true;
	    int aa =1;
	    for (TableColumn tableColumn : list) {
	    	if(!tableColumn.getTableName().equals(tableName)){
				collist=new ArrayList<String>();
				tableName = tableColumn.getTableName();
				 // 生成一个表格
				if(isis){
					sheet = workbook.createSheet("迁移总表");
				     rowindex = 0;
				     //表名、表名称
					    HSSFRow row = sheet.createRow(rowindex);
					   
					    //产生表格标题行
					     row = sheet.createRow(rowindex);
					    rowindex++;
					   
					    for (short i = 0; i < headers1.length; i++) {
					    	HSSFCell  cell = row.createCell(i);
					       cell.setCellStyle(style);
					       HSSFRichTextString  text = new HSSFRichTextString(headers1[i]);
					       cell.setCellValue(text);
					    }
					    
					 // 设置表格默认列宽度为15个字节
					    sheet.setDefaultColumnWidth((short) 10);
					    sheet.setColumnWidth(0, (short) 2000);
					    sheet.setColumnWidth(1, (short) 7000);
					    sheet.setColumnWidth(2, (short) 7000);
					    sheet.setColumnWidth(3, (short) 7000);
					    sheet.setColumnWidth(4, (short) 20000);
					    isis = false;
				}
			     
				    // 生成另一个字体
				    HSSFFont font2 = workbook.createFont();
				    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				    // 把字体应用到当前的样式
				    style2.setFont(font2);
				   
				    // 声明一个画图的顶级管理器
				    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				    // 定义注释的大小和位置,详见文档
				    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
				    // 设置注释内容
				    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
				    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				    comment.setAuthor("leno");
				
				  HSSFRow  row = sheet.createRow(rowindex);
				        for (int i = 0; i < headers1.length; i++) {
				        	HSSFCell cell = row.createCell(i);
				            cell.setCellStyle(style2);
				            HSSFRichTextString  text = new HSSFRichTextString(getElementRCS(tableColumn,i,aa));
				            cell.setCellValue(text);
				            if(i == 2){
				            	cell.setCellFormula("HYPERLINK(\"#"+tableColumn.getTableName()+"!C1\",\""+tableColumn.getTableName()+"\")"); //HYPERLINK("#明细!A1","homepage")
				            	HSSFCellStyle linkStyle = workbook.createCellStyle();
				            	HSSFFont cellFont= workbook.createFont();
				            	cellFont.setUnderline((byte) 1);
				            	cellFont.setColor(HSSFColor.BLUE.index);
				            	linkStyle.setFont(cellFont);
				            	linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				            	linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				            	linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				            	linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				            	cell.setCellStyle(linkStyle);
				            }
				            }
				        rowindex++;
				        aa++;
			}
	    }
	    tableName="";
	    aa =1;
	    for (TableColumn tableColumn : list) {
			if(1==1){
				// || tableColumn.getTableName().startsWith("ENGINE_") || tableColumn.getTableName().startsWith("SOLAR_") || tableColumn.getTableName().startsWith("TP_") 
				int index2 = 100;
				if(!tableColumn.getTableName().equals(tableName)){aa++;
					collist=new ArrayList<String>();
					 // 生成一个表格
				     sheet = workbook.createSheet(tableColumn.getTableName());
				     rowindex = 0;
				     tableName = tableColumn.getTableName();
				     //表名、表名称
					    HSSFRow row = sheet.createRow(rowindex);
					    rowindex++;
					       HSSFCell cell = row.createCell(0);
					       cell.setCellStyle(style);
					       HSSFRichTextString text = new HSSFRichTextString("表名");
					       cell.setCellValue(text);
					       cell = row.createCell(1);
					       cell.setCellStyle(style);
					        text = new HSSFRichTextString(tableColumn.getTableName());
					       cell.setCellValue(text);
					       
					       cell = row.createCell(2);
					       HSSFCellStyle linkStyle = workbook.createCellStyle();
			            	HSSFFont cellFont= workbook.createFont();
			            	cellFont.setUnderline((byte) 1);
			            	cellFont.setColor(HSSFColor.BLUE.index);
			            	linkStyle.setFont(cellFont);
			            	linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			            	linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			            	linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			            	linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			            	cell.setCellStyle(linkStyle);
					        text = new HSSFRichTextString("返回迁移总表");
					        cell.setCellFormula("HYPERLINK(\"#迁移总表!C"+aa+"\",\"返回迁移总表\")"); //HYPERLINK("#明细!A1","homepage")
			            	
					       
					        row = sheet.createRow(rowindex);
						    rowindex++;
						        cell = row.createCell(0);
						       cell.setCellStyle(style);
						        text = new HSSFRichTextString("表名称");
						       cell.setCellValue(text);
						       cell = row.createCell(1);
						       cell.setCellStyle(style);
						        text = new HSSFRichTextString(tableColumn.getTableComments());
						       cell.setCellValue(text);
						       
					    
					    //产生表格标题行
					     row = sheet.createRow(rowindex);
					    rowindex++;
					   
					    for (short i = 0; i < headers.length; i++) {
					        cell = row.createCell(i);
					       cell.setCellStyle(style);
					        text = new HSSFRichTextString(headers[i]);
					       cell.setCellValue(text);
					    }
				}
			    // 设置表格默认列宽度为15个字节
			    sheet.setDefaultColumnWidth((short) 10);
			    sheet.setColumnWidth(0, (short) 2000);
			    sheet.setColumnWidth(1, (short) 5000);
			    sheet.setColumnWidth(2, (short) 5000);
			    sheet.setColumnWidth(3, (short) 5000);
			    sheet.setColumnWidth(4, (short) 10000);
			    // 生成另一个字体
			    HSSFFont font2 = workbook.createFont();
			    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			    // 把字体应用到当前的样式
			    style2.setFont(font2);
			   
			    // 声明一个画图的顶级管理器
			    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			    // 定义注释的大小和位置,详见文档
			    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
			    // 设置注释内容
			    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
			    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
			    comment.setAuthor("leno");
			
			  
			    HSSFRow   row = sheet.createRow(rowindex);
			        for (int i = 0; i < headers.length; i++) {
			        	HSSFCell cell = row.createCell(i);
			            cell.setCellStyle(style2);
			            HSSFRichTextString  text = new HSSFRichTextString(getElement(tableColumn,i));
			            cell.setCellValue(text);
					}
			        rowindex++;
			}
		   
		}
		File file = null;
	   	file = new File("D:\\RCS.xls");
	   	if(file.exists()){
	   		if( file.delete() ) {
	   			file = new File("D:\\RCS.xls");
	   		}
	   	}
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	FileOutputStream fos = null;  
	    	BufferedOutputStream bos = null;
		    try {
		    	workbook.write(out);
		    	fos = new FileOutputStream(file);  
		    	bos = new BufferedOutputStream(fos);  
		    	bos.write(out.toByteArray());  
			} catch (IOException e) {
			} finally {
				try {
					if( bos != null ) {
						bos.close();
					}
					if( fos != null ) {
						fos.close();
					}
				} catch (IOException e) {
				}
			}
	}


	private String getElementRCS(TableColumn tableColumn, int index, int aa) {
		try {
			if (index == 0) {
				return aa+"";
			}
			if (index == 1) {
				return tableColumn.getTableComments();
			}
			if (index == 2) {
				return tableColumn.getTableName();
			}
			if (index == 3) {
				return "";
			}
			if (index == 4) {
				return "";
			}
		} catch (Exception e) {
		}
		return "";
	}


	/**
	 * 创建相关文件
	 */
	public void createFile(){
		JdbcUtil jdbcUtil = new JdbcUtil();
		con = jdbcUtil.getConnection(con);
		List<TableColumn> list = new ArrayList<TableColumn>();
		String sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name where t.TABLE_NAME = ? order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				list.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {}
		
		JdbcUtil JdbcUtil = new JdbcUtil();
		con = JdbcUtil.getConnection(con);
		List<TableColumn> list2 = new ArrayList<TableColumn>();
		String sql2 = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, b.comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name where t.TABLE_NAME = ? order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql2);
			ps.setString(1, tableName2.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				list2.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		for (TableColumn tableColumn : list) {
			System.out.println(tableColumn.getColumnName());
		}
		for (TableColumn tableColumn : list2) {
			System.out.println(tableColumn.getColumnName());
		}
		
	}
	
	
	
	/**
	 * 创建实体类文件
	 * @param list
	 */
	private void createEntity(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+".java");
			try {
			StringBuffer sb = new StringBuffer();
			sb.append("package "+packageNameEntity+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.io.Serializable;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public class "+entityName+" implements Serializable{");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("private static final long serialVersionUID = 1L;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			for (TableColumn tableColumn : list) {
				//声明变量
				sb.append("private "+getType(tableColumn.getDataType(),false)+" "+tableColumn.getColumnName().toLowerCase()+";//"+tableColumn.getColumnComments());
				if(list.indexOf(tableColumn)==idIndex){
					sb.append("（主键）");
				}
				sb.append(System.getProperty("line.separator"));
			}
			for (TableColumn tableColumn : list) {
				//get方法
				sb.append(System.getProperty("line.separator"));
				sb.append("public "+getType(tableColumn.getDataType(),false)+" get"+getName(tableColumn.getColumnName())+"() {");
				sb.append(System.getProperty("line.separator"));
				sb.append("    return "+tableColumn.getColumnName().toLowerCase()+";");
				sb.append(System.getProperty("line.separator"));
				sb.append("}");
				sb.append(System.getProperty("line.separator"));
				//set方法
				sb.append(System.getProperty("line.separator"));
				sb.append("public void set"+getName(tableColumn.getColumnName())+"("+getType(tableColumn.getDataType(),false)+" "+tableColumn.getColumnName().toLowerCase()+") {");
				sb.append(System.getProperty("line.separator"));
				sb.append("    this."+tableColumn.getColumnName().toLowerCase()+" = "+tableColumn.getColumnName().toLowerCase()+";");
				sb.append(System.getProperty("line.separator"));
				sb.append("}");
				sb.append(System.getProperty("line.separator"));
			}
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建service文件
	 * @param list
	 */
	private void createService(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"Service.java");
			try {
			StringBuffer sb = new StringBuffer();
			//包名
			sb.append("package "+packageNameService+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//引入包
			sb.append("import java.util.List;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public interface "+entityName+"Service {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("public int add ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("public int remove (Long id);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("public int update ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("public List<"+entityName+"> query ();");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建serviceImpl文件
	 * @param list
	 */
	private void createServiceImpl(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"ServiceImpl.java");
			try {
			StringBuffer sb = new StringBuffer();
			//包名
			sb.append("package "+packageNameServiceImpl+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//引入包
			sb.append("import java.util.List;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameService+"."+entityName+"Service;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameDao+"."+entityName+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public class "+entityName+"ServiceImpl implements "+entityName+"Service{");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("private "+entityName+"Dao "+entityName.toLowerCase()+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public void set"+entityName+"Dao ("+entityName+"Dao "+entityName.toLowerCase()+"Dao) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("    this."+entityName.toLowerCase()+"Dao = "+entityName.toLowerCase()+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("public int add ("+entityName+" "+entityName.toLowerCase()+") {");
			sb.append(System.getProperty("line.separator"));
			sb.append("    return "+entityName.toLowerCase()+"Dao.insert("+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("public int remove (Long id) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("    return "+entityName.toLowerCase()+"Dao.delete(id);");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("public int update ("+entityName+" "+entityName.toLowerCase()+") {");
			sb.append(System.getProperty("line.separator"));
			sb.append("    return "+entityName.toLowerCase()+"Dao.update("+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("public List<"+entityName+"> query () {");
			sb.append(System.getProperty("line.separator"));
			sb.append("    return "+entityName.toLowerCase()+"Dao.query();");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建Dao文件
	 * @param list
	 */
	private void createDao(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"Dao.java");
			try {
			StringBuffer sb = new StringBuffer();
			//包名
			sb.append("package "+packageNameDao+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//引入包
			sb.append("import java.util.List;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public interface "+entityName+"Dao {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("public int insert ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("public int delete (Long id);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("public int update ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("public List<"+entityName+"> query ();");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建Mybatis或者Ibatis或者Hibernate的XML文件
	 * @param list
	 */
	private void createXml(List<TableColumn> list) {
		if("Mybatis".equals(XMLType)){
			if(list!=null && list.size()>0){
				File file = new File(filePath+entityName+"Dao.xml");
				try {
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("<mapper namespace=\""+packageNameDao+"."+entityName+"Dao\">");
				sb.append(System.getProperty("line.separator"));
				//增
				sb.append("<insert id=\"insert\" parameterType=\"map\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("<![CDATA[");
				sb.append(System.getProperty("line.separator"));
				sb.append("insert into "+tableName+" (");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName()+") values ");
					}else{
						sb.append(tableColumn.getColumnName()+",");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("(");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						if(list.indexOf(tableColumn)==idIndex){
							sb.append(tableSeq+".nextval)");
						}else{
							sb.append("#{"+tableColumn.getColumnName().toLowerCase()+"})");
						}
					}else{
						if(list.indexOf(tableColumn)==idIndex){
							sb.append(tableSeq+".nextval,");
						}else{
							sb.append("#{"+tableColumn.getColumnName().toLowerCase()+"},");
						}
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("]]>	");
				sb.append(System.getProperty("line.separator"));
				sb.append("</insert>");
				sb.append(System.getProperty("line.separator"));
				//删
				sb.append("<delete id=\"delete\" parameterType=\"long\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("<![CDATA[");
				sb.append(System.getProperty("line.separator"));
				sb.append(" delete from "+tableName+" where id =#{id}");
				sb.append(System.getProperty("line.separator"));
				sb.append("]]>	");
				sb.append(System.getProperty("line.separator"));
				sb.append("</delete>");
				sb.append(System.getProperty("line.separator"));
				//改
				sb.append("<update id=\"update\" parameterType=\"map\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("<![CDATA[");
				sb.append(System.getProperty("line.separator"));
				sb.append("update "+tableName+" set ");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName()+" = #{"+tableColumn.getColumnName()+"}");
					}else{
						sb.append(tableColumn.getColumnName()+" = #{"+tableColumn.getColumnName()+"},");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append(" where id =#{id} ]]>	");
				sb.append(System.getProperty("line.separator"));
				sb.append("</update>");
				sb.append(System.getProperty("line.separator"));
				//查
				sb.append("<select id=\"search\" parameterType=\"map\" resultType=\""+entityName+"\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("<![CDATA[");
				sb.append(System.getProperty("line.separator"));
				sb.append("select ");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName());
					}else{
						sb.append(tableColumn.getColumnName()+",");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append(" from "+tableName );
				sb.append(System.getProperty("line.separator"));
				sb.append("]]>	");
				sb.append(System.getProperty("line.separator"));
				sb.append("</select>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</mapper>");
				sb.append(System.getProperty("line.separator"));
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if("Ibatis".equals(XMLType)){
			if(list!=null && list.size()>0){
				File file = new File(filePath+entityName+"Dao.xml");
				try {
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("<sqlMap namespace=\""+packageNameDao+"."+entityName+"Dao\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("<typeAlias alias="+entityName+" type="+packageNameEntity+"."+entityName+" />");
				sb.append(System.getProperty("line.separator"));
				//增
				sb.append(System.getProperty("line.separator"));
				sb.append("<insert id=\"insert\" parameterClass=\""+entityName+"\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("    insert into "+tableName+" (");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName()+") values ");
					}else{
						sb.append(tableColumn.getColumnName()+",");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("    (");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						if(list.indexOf(tableColumn)==idIndex){
							sb.append(tableSeq+".nextval)");
						}else{
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+"#)");
						}
					}else{
						if(list.indexOf(tableColumn)==idIndex){
							sb.append(tableSeq+".nextval,");
						}else{
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+"#,");
						}
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("</insert>");
				sb.append(System.getProperty("line.separator"));
				//删
				sb.append(System.getProperty("line.separator"));
				sb.append("<delete id=\"delete\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("    delete from "+tableName);
				sb.append(System.getProperty("line.separator"));
				sb.append("  <dynamic prepend=\"where\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <isNotNull>");
				sb.append(System.getProperty("line.separator"));
				sb.append("      ID IN");
				sb.append(System.getProperty("line.separator"));
				sb.append("      <iterate conjunction=\",\" open=\"(\" close=\")\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("      #[]#");
				sb.append(System.getProperty("line.separator"));
				sb.append("      </iterate>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </isNotNull>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <isNull>");
				sb.append(System.getProperty("line.separator"));
				sb.append("      1 = 0");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </isNull>");
				sb.append(System.getProperty("line.separator"));
				sb.append("  </dynamic>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</delete>");
				sb.append(System.getProperty("line.separator"));
				//改
				sb.append(System.getProperty("line.separator"));
				sb.append("<update id=\"update\" parameterClass=\"map\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("  update "+tableName+" set ");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName()+"#");
					}else{
						sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName()+"#,");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append(" where id =#id#	");
				sb.append(System.getProperty("line.separator"));
				sb.append("</update>");
				sb.append(System.getProperty("line.separator"));
				//查
				sb.append(System.getProperty("line.separator"));
				sb.append("<select id=\"search\" parameterClass=\"map\" resultType=\""+entityName+"\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("  select ");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName());
					}else{
						sb.append(tableColumn.getColumnName()+",");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append(" from "+tableName );
				sb.append(System.getProperty("line.separator"));
				sb.append("</select>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</sqlMap>");
				sb.append(System.getProperty("line.separator"));
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if("Hibernate".equals(XMLType)){
			if(list!=null && list.size()>0){
				File file = new File(filePath+entityName+".hbm.xml");
				try {
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\"?>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD//EN\" \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("<hibernate-mapping package=\""+packageNameEntity+"\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("  <class name=\""+entityName+"\" table=\""+tableName+"\">");
				sb.append(System.getProperty("line.separator"));
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==idIndex){
						sb.append("    <id name=\""+tableColumn.getColumnName().toLowerCase()+"\" type=\""+getType(tableColumn.getDataType(),true)+"\" column=\""+tableColumn.getColumnName()+"\">");
						sb.append(System.getProperty("line.separator"));
						sb.append("      <generator class=\"native\"><param name=\"sequence\">"+tableSeq+"</param></generator>");
						sb.append(System.getProperty("line.separator"));
						sb.append("    </id>");
						sb.append(System.getProperty("line.separator"));
					}else{
						if("N".equals(tableColumn.getNullable())){
							sb.append("    <property name=\""+tableColumn.getColumnName().toLowerCase()+"\" type=\""+getType(tableColumn.getDataType(),true)+"\"><column name=\""+tableColumn.getColumnName()+"\" not-null=\"true\"  /></property>");
						}else{
							sb.append("    <property name=\""+tableColumn.getColumnName().toLowerCase()+"\" type=\""+getType(tableColumn.getDataType(),true)+"\"><column name=\""+tableColumn.getColumnName()+"\"  /></property>");
						}
						sb.append(System.getProperty("line.separator"));
					}
				}
				sb.append("  </class>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</hibernate-mapping>");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取数据库类型对应的java类型
	 * @param type,isPacName(返回值是否带包名)
	 * @return
	 */
	public String getType(String type,boolean isPacName){
		if(type!=null){
			if("VARCHAR2".equals(type)){
				if(isPacName){
					return "java.lang.String";
				}else{
					return "String";
				}
			}else if("NUMBER".equals(type)){
				if(isPacName){
					return "java.lang.Long";
				}else{
					return "long";
				}
			}else if("DATE".equals(type)){
				return "java.util.Date";
			}else if("BLOB".equals(type)){
				if(isPacName){
					return "byte[]";
				}else{
					return "byte[]";
				}
			}else if("CLOB".equals(type)){
				if(isPacName){
					return "java.lang.String";
				}else{
					return "String";
				}
			}else if("TIMESTAMP".equals(type)){
				return "java.util.Date";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 获取表和字段对应的实体变量名（首字母大写）
	 * @param name
	 */
	public String getName(String name){
		if(name!=null && name.length()>0){
			if(name.length()==1){
				return name.toUpperCase();
			}else{
				return name.substring(0, 1).toUpperCase()+name.substring(1).toLowerCase();
			}
		}else{
			return "";
		}
	}
	
	
	
	public void createExcel(Map<String,String> tablemap){
		JdbcUtil JdbcUtil = new JdbcUtil();
		con = JdbcUtil.getConnection(con);
		List<TableColumn> list2 = new ArrayList<TableColumn>();
		//where t.TABLE_NAME in ('DCS_SPOTDEALS','DCS_FORWARDDEALS')
		String sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				knowledge.setColumnid(rs.getString(8));
				knowledge.setPrecision(rs.getString(9));
				knowledge.setScale(rs.getString(10));
				list2.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JdbcUtil jdbcUtil = new JdbcUtil();
		con = jdbcUtil.getConnection(con);
		List<TableColumn> list = new ArrayList<TableColumn>();
		//where t.TABLE_NAME in ('DCS_SPOTDEALS','DCS_FORWARDDEALS')
		 sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, tableName.toUpperCase());//动态写入表名
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setTableComments(rs.getString(2));
				knowledge.setColumnName(rs.getString(3));
				knowledge.setColumnComments(rs.getString(4));
				knowledge.setDataType(rs.getString(5));
				knowledge.setDataLength(rs.getString(6));
				knowledge.setNullable(rs.getString(7));
				knowledge.setColumnid(rs.getString(8));
				knowledge.setPrecision(rs.getString(9));
				knowledge.setScale(rs.getString(10));
				list.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
		String tableName="";
		int rowindex = 0;
		HSSFSheet sheet = null;
		 // 生成一个样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    // 设置这些样式
//	    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    // 生成一个字体
	    HSSFFont font = workbook.createFont();
	//    font.setColor(HSSFColor.VIOLET.index);
	    font.setFontHeightInPoints((short) 12);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    // 把字体应用到当前的样式
	    style.setFont(font);
	    // 生成并设置另一个样式
	    HSSFCellStyle style2 = workbook.createCellStyle();
//	    style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//	    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    
	    HSSFCellStyle style3 = workbook.createCellStyle();
	    style3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	    style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	    style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    
	    String[] headers = {"序号","字段名","字段名称","GITS字段名","迁移规则","数据类型","LENGTH","PRECISION","SCALE","是否为空","是否主键","是否必输","是否码值","码值取值信息","备注"};
	    List<String> collist = new ArrayList<String>();
	    for (TableColumn tableColumn : list) {
			if(tableColumn.getTableName().startsWith("CDS_") || tableColumn.getTableName().startsWith("ST_CPTY")){
//				tableColumn.getTableName().startsWith("DCS_") || tableColumn.getTableName().startsWith("RCS_") || 
				// || tableColumn.getTableName().startsWith("ENGINE_") || tableColumn.getTableName().startsWith("SOLAR_") || tableColumn.getTableName().startsWith("TP_") 
				int index2 = 100;
				if(!tableColumn.getTableName().equals(tableName)){
					for (TableColumn tableColumn2 : list2) {
			        	if(tablemap.get(tableName) !=null && tablemap.get(tableName).equals(tableColumn2.getTableName())){

				            	HSSFRow   row2 = sheet.createRow(index2);
				            	index2++;
								for (int i = 0; i < headers.length; i++) {
									HSSFCell cell = row2.createCell(i);
									if(collist.contains(tableColumn2.getColumnName())){
										 cell.setCellStyle(style3);
									}else{
										 cell.setCellStyle(style2);
									}
						            HSSFRichTextString   text = new HSSFRichTextString(getElement(tableColumn2,i));
						            cell.setCellValue(text);
								}
			        	}
			        	
			        }
					collist=new ArrayList<String>();
					 // 生成一个表格
				     sheet = workbook.createSheet(tableColumn.getTableName());
				     rowindex = 0;
				     tableName = tableColumn.getTableName();
				     //表名、表名称
					    HSSFRow row = sheet.createRow(rowindex);
					    rowindex++;
					       HSSFCell cell = row.createCell(0);
					       cell.setCellStyle(style);
					       HSSFRichTextString text = new HSSFRichTextString("表名");
					       cell.setCellValue(text);
					       cell = row.createCell(1);
					       cell.setCellStyle(style);
					        text = new HSSFRichTextString(tableColumn.getTableName());
					       cell.setCellValue(text);
					       
					        row = sheet.createRow(rowindex);
						    rowindex++;
						        cell = row.createCell(0);
						       cell.setCellStyle(style);
						        text = new HSSFRichTextString("表名称");
						       cell.setCellValue(text);
						       cell = row.createCell(1);
						       cell.setCellStyle(style);
						        text = new HSSFRichTextString(tableColumn.getTableComments());
						       cell.setCellValue(text);
					    
					    //产生表格标题行
					     row = sheet.createRow(rowindex);
					    rowindex++;
					   
					    for (short i = 0; i < headers.length; i++) {
					        cell = row.createCell(i);
					       cell.setCellStyle(style);
					        text = new HSSFRichTextString(headers[i]);
					       cell.setCellValue(text);
					    }
				}
			    // 设置表格默认列宽度为15个字节
			    sheet.setDefaultColumnWidth((short) 10);
			    sheet.setColumnWidth(0, (short) 2000);
			    sheet.setColumnWidth(1, (short) 5000);
			    sheet.setColumnWidth(2, (short) 5000);
			    sheet.setColumnWidth(3, (short) 5000);
			    sheet.setColumnWidth(4, (short) 10000);
			    // 生成另一个字体
			    HSSFFont font2 = workbook.createFont();
			    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			    // 把字体应用到当前的样式
			    style2.setFont(font2);
			   
			    // 声明一个画图的顶级管理器
			    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			    // 定义注释的大小和位置,详见文档
			    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
			    // 设置注释内容
			    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
			    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
			    comment.setAuthor("leno");
			
			  
			    HSSFRow   row = sheet.createRow(rowindex);
			        for (int i = 0; i < headers.length; i++) {
			        	HSSFCell cell = row.createCell(i);
			            cell.setCellStyle(style2);
			            HSSFRichTextString  text = new HSSFRichTextString(getElement(tableColumn,i));
			            cell.setCellValue(text);
					}
			        for (TableColumn tableColumn2 : list2) {
						if(tablemap.get(tableColumn.getTableName()) !=null && tablemap.get(tableColumn.getTableName()).equals(tableColumn2.getTableName())){
							if(tableColumn.getColumnName().equals(tableColumn2.getColumnName())){
								HSSFCell cell = row.createCell(3);
					            cell.setCellStyle(style2);
					            String texts = "";
					            if(tableColumn.getDataType().equals(tableColumn2.getDataType()) && tableColumn.getDataLength().equals(tableColumn2.getDataLength()) && (tableColumn.getPrecision()==null?"0":tableColumn.getPrecision()).equals((tableColumn2.getPrecision()==null?"0":tableColumn2.getPrecision()))  && (tableColumn.getScale()==null?"0":tableColumn.getScale()).equals(tableColumn2.getScale()==null?"0":tableColumn2.getScale())){
					            	texts = tableColumn.getColumnName();
					            }else{
					            	texts = tableColumn.getColumnName() +"-"+tableColumn2.getDataType()+"-"+tableColumn2.getDataLength()+"-"+tableColumn2.getPrecision()+"-"+tableColumn2.getScale();
					            }
					            HSSFRichTextString  text = new HSSFRichTextString(texts);
					            cell.setCellValue(text);
					            collist.add(tableColumn.getColumnName());
							}
						}
					}
			        rowindex++;
			}
		   
		}
		File file = null;
	   	file = new File("D:\\RCS.xls");
	   	if(file.exists()){
	   		if( file.delete() ) {
	   			file = new File("D:\\RCS.xls");
	   		}
	   	}
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	FileOutputStream fos = null;  
	    	BufferedOutputStream bos = null;
		    try {
		    	workbook.write(out);
		    	fos = new FileOutputStream(file);  
		    	bos = new BufferedOutputStream(fos);  
		    	bos.write(out.toByteArray());  
			} catch (IOException e) {
			} finally {
				try {
					if( bos != null ) {
						bos.close();
					}
					if( fos != null ) {
						fos.close();
					}
				} catch (IOException e) {
				}
			}
	}
	
	/**
	 * 	导出excel时动态为列赋值
	 */
	public String getElement(TableColumn tableColumn, int index) {
		try {
			if (index == 0) {
				return tableColumn.getColumnid();
			}
			if (index == 1) {
				return tableColumn.getColumnName();
			}
			if (index == 2) {
				return tableColumn.getColumnComments();
			}
			if (index == 5) {
				return tableColumn.getDataType();
			}
			if (index == 6) {
				return tableColumn.getDataLength();
			}
			if (index == 7) {
				return tableColumn.getPrecision();
			}
			if (index == 8) {
				return tableColumn.getScale();
			}
			if (index == 9) {
				return tableColumn.getNullable();
			}

		} catch (Exception e) {
		}
		return "";
	}
	
}
