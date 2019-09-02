package com.lb.base.workproject.shidaiyintong.gits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 新开行参数检查与脚本生成类
* @author lb-pc
* @date 2018-12-29 上午10:08:13  
* @Copyright: 2018 www.erayt.com Inc. All rights reserved. 
* 注意：本内容仅限于杭州时代银通软件股份有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class NewBranch {

	private final Logger logger = Logger.getLogger(this.getClass());
	//最好是生产库，需与生成库对比
	private static String driverClassName="oracle.jdbc.driver.OracleDriver";//数据库驱动名
	private static String url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";//数据库地址
	private static String username="GITSSC20190111";//用户名
	private static String password="GITSSC20190111";//密码
	
	private static String usernameEcas="GITSSC20190111ECAS";//用户名
	private static String passwordEcas="GITSSC20190111ECAS";//密码
	
	//目前只支持xlsx格式的文档，如果是xls的请复制一份到xlsx
	private static String paramFileName = "D:/林博/工作记录-时代银通/中国银行集团内部资金交易系统(GITS)/GITS相关文件/新开行参数/P901-GITS新开行参数-前中台（布宜诺斯艾利斯分行）-终版.xlsx";//新开行参数文档路径
	private static String userFileName = "C:/Users/lb-pc/Desktop/GITS相关文件/新开行参数/P901-GITS系统柜员信息（布宜诺斯艾利斯分行）.xlsx";//新开行柜员文档路径
	private static String newBranchName="布宜诺斯艾利斯";//新开行名称
	private static int newBranchCode=311;//新开行行号
	private static String newBranchCcy="ARS";//新开行货币
	private static int sdsIdStart = 1010101;//观察生产数据 从1002357到1050002之间的id是空的，因此新开行的id可以从1010001开始，建议每家新开行占用100个id--罗安达 1010001-1010100--布宜诺斯艾利斯 1010101-1010200
	private static String sqlFilePath="C:/Users/lb-pc/Desktop/";//生成新开行脚本文件路径
	
	private static int userSheetIndex = 0;//新开行柜员文档需要解析的sheet页索引
	private static int userColumnIndex = 1;//新开行柜员文档开始解析的行索引
	
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs =null;
	
	
	private Map<String,String> yunyingjigou = new HashMap<String, String>();//运营机构
	private int yunyingjigouSheetIndex = 1;//需要解析的sheet页索引
	private int yunyingjigouColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> yewujigouList = new ArrayList<Map<String,String>>();//业务机构
	private int yewujigouSheetIndex = 2;//需要解析的sheet页索引
	private int yewujigouColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> bujijiagouList = new ArrayList<Map<String,String>>();//簿记架构
	private int bujijiagouSheetIndex = 3;//需要解析的sheet页索引
	private int bujijiagouColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> jiejiariList = new ArrayList<Map<String,String>>();//节假日
	private int jiejiariSheetIndex = 4;//需要解析的sheet页索引
	private int jiejiariColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> huobiList = new ArrayList<Map<String,String>>();//货币
	private int huobiSheetIndex = 5;//需要解析的sheet页索引
	private int huobiColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> huobiduiList = new ArrayList<Map<String,String>>();//货币对
	private int huobiduiSheetIndex = 6;//需要解析的sheet页索引
	private int huobiduiColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> zhibiaoList = new ArrayList<Map<String,String>>();//货币指标
	private int zhibiaoSheetIndex = 7;//需要解析的sheet页索引
	private int zhibiaoColumnIndex = 3;//开始解析的行索引
	private List<Map<String,String>> jiaoyiduishouList = new ArrayList<Map<String,String>>();//交易对手
	private int jiaoyiduishouSheetIndex = 8;//需要解析的sheet页索引
	private int jiaoyiduishouColumnIndex = 3;//开始解析的行索引
	
	private int SDS_ENTITIESId;//实体id
	private int SDS_SiteId;//运营机构id
	private int SDS_CURRENCIESId;//货币id
	private String SDS_CURRENCIESName;//货币Name
	private int SDS_HOLIDAYSId;//节假日id
	private List<String> jydszIdList = new ArrayList<String>();//交易对手组id
	private List<String> jgjydszIdList = new ArrayList<String>();//机构交易对手组id
	private List<String> jydsIdList = new ArrayList<String>();//交易对手id
	private List<String> jgjydsIdList = new ArrayList<String>();//机构交易对手id
	
	
	public static void main(String[] args) {
		NewBranch newBranch = new NewBranch();
		//从新开行文档解析出各种数据
		newBranch.jiexiByNewBranchDocument();
		//解析新开行文档生成新开行ecas脚本
		newBranch.createUserSqlByNewBranchDocument();
		//解析新开行文档生成新开行参数脚本
		newBranch.createParamSqlByNewBranchDocument();
	}
	
	/**
	 * 从新开行文档解析出各种数据
	* @author lb-pc
	* @throws
	 */
	private void jiexiByNewBranchDocument() {
		File file = new File(paramFileName);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if (isImpxlsx) {
			Workbook workbookx = null;
			try {
				workbookx = new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//运营机构
			Sheet sheet = workbookx.getSheetAt(yunyingjigouSheetIndex);
			int rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = yunyingjigouColumnIndex; i == yunyingjigouColumnIndex; i++) {
					Row row = sheet.getRow(i);
					String siteCode = caseCellType(row.getCell(0)).trim();
					if(newBranchCode==Integer.parseInt(siteCode)){
						yunyingjigou.put("columnA", siteCode);
						yunyingjigou.put("columnB", caseCellType(row.getCell(1)).trim());
						yunyingjigou.put("columnC", caseCellType(row.getCell(2)).trim());
						yunyingjigou.put("columnD", caseCellType(row.getCell(3)).trim());
						logger.error("时区偏移量需再做检查"+caseCellType(row.getCell(3)).trim());
					}else{
						logger.error("无法解析运营机构sheet页，请检查");
					}
				}
			}
			
			//业务机构
			sheet = workbookx.getSheetAt(yewujigouSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = yewujigouColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> yewujigou = new HashMap<String, String>();
						yewujigou.put("columnA", columnA);
						yewujigou.put("columnB", caseCellType(row.getCell(1)).trim());
						yewujigou.put("columnC", caseCellType(row.getCell(2)).trim());
						yewujigou.put("columnD", caseCellType(row.getCell(3)).trim());
						yewujigou.put("columnE", caseCellType(row.getCell(4)).trim());
						yewujigou.put("columnF", caseCellType(row.getCell(5)).trim());
						yewujigou.put("columnG", caseCellType(row.getCell(6)).trim());
						if(Integer.parseInt(caseCellType(row.getCell(6)).trim()) != newBranchCode){
							continue;
						}
						yewujigouList.add(yewujigou);
					}else{
						break;
					}
				}
			}
			
			//簿记架构
			sheet = workbookx.getSheetAt(bujijiagouSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = bujijiagouColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> bujijiagou = new HashMap<String, String>();
						bujijiagou.put("columnA", columnA);
						bujijiagou.put("columnB", caseCellType(row.getCell(1)).trim());
						bujijiagou.put("columnC", caseCellType(row.getCell(2)).trim());
						bujijiagou.put("columnD", caseCellType(row.getCell(3)).trim());
						bujijiagou.put("columnE", caseCellType(row.getCell(4)).trim());
						bujijiagou.put("columnF", caseCellType(row.getCell(5)).trim());
						bujijiagou.put("columnG", caseCellType(row.getCell(6)).trim());
						bujijiagou.put("columnH", caseCellType(row.getCell(6)).trim());
						bujijiagouList.add(bujijiagou);
					}else{
						break;
					}
				}
			}
			
			//节假日
			sheet = workbookx.getSheetAt(jiejiariSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = jiejiariColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> jiejiari = new HashMap<String, String>();
						jiejiari.put("columnA", columnA);
						jiejiari.put("columnB", caseCellType(row.getCell(1)).trim());
						jiejiari.put("columnC", caseCellType(row.getCell(2)).trim());
						jiejiari.put("columnD", caseCellType(row.getCell(3)).trim());
						jiejiariList.add(jiejiari);
					}else{
						break;
					}
				}
			}
			
			//货币
			sheet = workbookx.getSheetAt(huobiSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = huobiColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> huobi = new HashMap<String, String>();
						huobi.put("columnA", columnA);
						huobi.put("columnB", caseCellType(row.getCell(1)).trim());
						huobi.put("columnC", caseCellType(row.getCell(2)).trim());
						huobi.put("columnD", caseCellType(row.getCell(3)).trim());
						huobi.put("columnE", caseCellType(row.getCell(4)).trim());
						huobiList.add(huobi);
					}else{
						break;
					}
				}
			}
			
			//货币对
			sheet = workbookx.getSheetAt(huobiduiSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = huobiduiColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> huobidui = new HashMap<String, String>();
						huobidui.put("columnA", columnA);
						huobidui.put("columnB", caseCellType(row.getCell(1)).trim());
						huobidui.put("columnC", caseCellType(row.getCell(2)).trim());
						huobidui.put("columnD", caseCellType(row.getCell(3)).trim());
						huobidui.put("columnE", caseCellType(row.getCell(4)).trim());
						huobidui.put("columnF", caseCellType(row.getCell(5)).trim());
						huobidui.put("columnG", caseCellType(row.getCell(6)).trim());
						huobiduiList.add(huobidui);
					}else{
						break;
					}
				}
			}
			
			//货币指标
			sheet = workbookx.getSheetAt(zhibiaoSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = zhibiaoColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> zhibiao = new HashMap<String, String>();
						zhibiao.put("columnA", columnA);
						zhibiao.put("columnB", caseCellType(row.getCell(1)).trim());
						zhibiao.put("columnC", caseCellType(row.getCell(2)).trim());
						zhibiaoList.add(zhibiao);
					}else{
						break;
					}
				}
			}
			
			//交易对手
			sheet = workbookx.getSheetAt(jiaoyiduishouSheetIndex);
			rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = jiaoyiduishouColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					if(row==null){
						break;
					}
					String columnA = caseCellType(row.getCell(0)).trim();
					if(columnA.length()>0){
						Map<String,String> jiaoyiduishou = new HashMap<String, String>();
						jiaoyiduishou.put("columnA", columnA);
						jiaoyiduishou.put("columnB", caseCellType(row.getCell(1)).trim());
						jiaoyiduishou.put("columnC", caseCellType(row.getCell(2)).trim());
						jiaoyiduishou.put("columnD", caseCellType(row.getCell(3)).trim());
						jiaoyiduishou.put("columnE", caseCellType(row.getCell(4)).trim());
						jiaoyiduishou.put("columnF", caseCellType(row.getCell(5)).trim());
						jiaoyiduishou.put("columnG", caseCellType(row.getCell(6)).trim());
						jiaoyiduishou.put("columnH", caseCellType(row.getCell(6)).trim());
						jiaoyiduishouList.add(jiaoyiduishou);
					}else{
						break;
					}
				}
			}
			
		}
	}

	/**
	 * 解析新开行参数文档生成新开行参数脚本
	* @author lb-pc
	* @throws
	 */
	private void createParamSqlByNewBranchDocument() {
		logger.info("[START]解析新开行参数文档生成新开行参数脚本");
		StringBuffer sbSqlParam = new StringBuffer("");
		sbSqlParam.append("--新开行参数脚本");
		sbSqlParam.append(System.getProperty("line.separator"));
		//组装禁用约束sql
		disableConstraint(sbSqlParam);
		//组装用户机构关联表sql
		baseUserbranch(sbSqlParam);
		//组装运营机构关联实体表、影像配置、下发IMA头寸数据控制表sql
		baseDft(sbSqlParam);
		//组装外汇指标sql
		baseIndex(sbSqlParam);
		//组装实体/运营机构/节假日/货币/账户/账簿sql
		sdsEntitiesBooks(sbSqlParam);
		//组装业务机构/货币对sql
		sdsBRANCHESPAIRS(sbSqlParam);
		//组装交易对手sql
		sdsCpty(sbSqlParam);
		//组装交易对手组sql
		sdsCptyGrp(sbSqlParam);
		//组装启用约束sql
		enableConstraint(sbSqlParam);
		sbSqlParam.append("COMMIT;");
		sbSqlParam.append(System.getProperty("line.separator"));
		//生成脚本文件
		try {
			File filesql = new File(sqlFilePath+newBranchName+"新开行参数脚本.sql");
			if(!filesql.exists()){
				filesql.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(filesql);
			fos.write(sbSqlParam.toString().getBytes());
			logger.info(newBranchName+"新开行参数脚本.sql生成成功，文件路径为："+sqlFilePath);
		} catch (Exception e) {
		}
		logger.info("[END]解析新开行参数文档生成新开行参数脚本");
	}


	/**
	 * 组装交易对手组sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void sdsCptyGrp(StringBuffer sbSqlParam) {
		String cptyDelete = "";
		StringBuffer sbcpty = new StringBuffer("");
		sbSqlParam.append("--交易对手组");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String,String> jiaoyiduishou : yewujigouList) {
			if(String.valueOf(newBranchCode).equals(jiaoyiduishou.get("columnA"))){
				cptyDelete = cptyDelete + "'"+jiaoyiduishou.get("columnA") + "',";
				sbcpty.append("INSERT INTO SDS_COMMONGRP (GRP_ID, GRP_CODE, GRP_NAME, GRPLOCALNAME, GLOBALID, GRPTYPE, AUDITTIMESTAMP, COMMENTS)VALUES("+sdsIdStart+",'"+jiaoyiduishou.get("columnA")+"','"+jiaoyiduishou.get("columnB")+"','"+jiaoyiduishou.get("columnC")+"','SDS"+sdsIdStart+"','CP',SYSDATE,NULL);");
				jgjydszIdList.add(sdsIdStart+"");
				sdsIdStart++;
				sbcpty.append(System.getProperty("line.separator"));
			}
		}
		for (Map<String,String> jiaoyiduishou : jiaoyiduishouList) {
			String sql = "select CPTY_ID,CPTY_CODE from SDS_CPTY where CPTY_CODE =?";
			String CPTY_ID = "";
			String CPTYNAME = "";
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, username, password);
				ps = con.prepareStatement(sql);
				ps.setString(1, jiaoyiduishou.get("columnA"));
				rs = ps.executeQuery();
				while(rs.next()){
					CPTY_ID = rs.getString(1);//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
					CPTYNAME = rs.getString(2);
				}
				if(!"".equals(CPTY_ID)){
					logger.info("交易对手已经存在："+jiaoyiduishou.get("columnA"));
					continue;
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
			cptyDelete = cptyDelete + "'"+jiaoyiduishou.get("columnA") + "',";
			sbcpty.append("INSERT INTO SDS_COMMONGRP (GRP_ID, GRP_CODE, GRP_NAME, GRPLOCALNAME, GLOBALID, GRPTYPE, AUDITTIMESTAMP, COMMENTS)VALUES("+sdsIdStart+",'"+jiaoyiduishou.get("columnA")+"','"+jiaoyiduishou.get("columnB")+"','"+jiaoyiduishou.get("columnC")+"','SDS"+sdsIdStart+"','CP',SYSDATE,NULL);");
			jiaoyiduishou.put(""+sdsIdStart, jiaoyiduishou.get("columnA"));
			jydszIdList.add(sdsIdStart+"");
			sdsIdStart++;
			sbcpty.append(System.getProperty("line.separator"));
		}
		String jydszIds = "";
		for (String id : jgjydszIdList) {
			jydszIds = jydszIds + id +",";
		}
		for (String id : jydszIdList) {
			jydszIds = jydszIds + id +",";
		}
		sbSqlParam.append("DELETE FROM SDS_COMMONGRPELT WHERE GRP_ID IN ("+jydszIds.substring(0, jydszIds.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM SDS_COMMONGRPELT WHERE ID IN ('"+SDS_SiteId+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM SDS_COMMONGRP WHERE GRP_CODE IN ("+cptyDelete.substring(0, cptyDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sbcpty);
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--关联交易对手与交易对手组");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (String jgjydszId : jgjydszIdList) {
			for (String jgjydsId : jgjydsIdList) {
				sbSqlParam.append("INSERT INTO SDS_COMMONGRPELT (GRP_ID, ID, AUDITTIMESTAMP)VALUES("+jgjydszId+","+jgjydsId+",SYSDATE);");
				sbSqlParam.append(System.getProperty("line.separator"));
			}
		}
		for (String jydszId : jydszIdList) {
			for (Map<String,String> jiaoyiduishou : jiaoyiduishouList) {
				if(jiaoyiduishou.get(jydszId)!=null && jiaoyiduishou.get(jiaoyiduishou.get(jydszId))!=null){
					sbSqlParam.append("INSERT INTO SDS_COMMONGRPELT (GRP_ID, ID, AUDITTIMESTAMP)VALUES("+jydszId+","+jiaoyiduishou.get(jiaoyiduishou.get(jydszId))+",SYSDATE);");
					sbSqlParam.append(System.getProperty("line.separator"));
					break;
				}
			}
		}
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--交易对手组关联分行");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (String jgjydszId : jgjydszIdList) {
			sbSqlParam.append("INSERT INTO SDS_COMMONGRPELT(ID,GRP_ID,AUDITTIMESTAMP)VALUES("+SDS_SiteId+","+jgjydszId+",SYSDATE);");
			sbSqlParam.append(System.getProperty("line.separator"));
		}
		for (String jydszId : jydszIdList) {
			sbSqlParam.append("INSERT INTO SDS_COMMONGRPELT(ID,GRP_ID,AUDITTIMESTAMP)VALUES("+SDS_SiteId+","+jydszId+",SYSDATE);");
			sbSqlParam.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append("INSERT INTO SDS_COMMONGRPELT(ID,GRP_ID,AUDITTIMESTAMP)VALUES("+SDS_SiteId+",1050200,SYSDATE);");//fince交易对手组
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
		
		
	}

	/**
	 * 组装交易对手sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void sdsCpty(StringBuffer sbSqlParam) {
		String cptyDelete = "";
		StringBuffer sbcpty = new StringBuffer("");
		sbSqlParam.append("--交易对手--如果已存在，则不要新增");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String,String> jiaoyiduishou : yewujigouList) {
			String sql = "select CPTY_ID,CPTY_CODE from SDS_CPTY where CPTY_CODE =?";
			String CPTY_ID = "";
			String CPTYNAME = "";
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, username, password);
				ps = con.prepareStatement(sql);
				ps.setString(1, jiaoyiduishou.get("columnA"));
				rs = ps.executeQuery();
				while(rs.next()){
					CPTY_ID = rs.getString(1);//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
					CPTYNAME = rs.getString(2);
				}
				if(!"".equals(CPTY_ID)){
					logger.info("交易对手已经存在："+jiaoyiduishou.get("columnA"));
					continue;
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
			cptyDelete = cptyDelete + "'"+jiaoyiduishou.get("columnA") + "',";
			String coreCode = "SDS"+sdsIdStart;
			String cptyClass = "1050006";
			sbcpty.append("INSERT INTO SDS_CPTY(CPTY_ID,CPTY_CODE,CPTY_NAME,CPTYLOCALNAME,ISRESIDENT,PHONENUMBER,FAXNUMBER,CONTACT,ADDRESS,GLOBALID,LOCKED,AUDITTIMESTAMP,CPTY_ID_PARENT,CPTYCLASSES_ID,IsIssuer,Countries_Id,Comments,IsCpty,IsGuarantee,ISCUSTODIAN,SWIFTCODE)VALUES("+sdsIdStart+",'"+jiaoyiduishou.get("columnA")+"','"+jiaoyiduishou.get("columnB")+"','"+jiaoyiduishou.get("columnC")+"','Y',NULL,NULL,NULL,NULL,'"+coreCode+"','N',SYSDATE,NULL,"+cptyClass+",'Y',NULL,NULL,'Y','Y','Y',NULL);");
			jiaoyiduishou.put(jiaoyiduishou.get("columnA"), ""+sdsIdStart);
			jgjydsIdList.add(sdsIdStart+"");
			sdsIdStart++;
			sbcpty.append(System.getProperty("line.separator"));
		}
		for (Map<String,String> jiaoyiduishou : jiaoyiduishouList) {
			String sql = "select CPTY_ID,CPTY_CODE from SDS_CPTY where CPTY_CODE =?";
			String CPTY_ID = "";
			String CPTYNAME = "";
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, username, password);
				ps = con.prepareStatement(sql);
				ps.setString(1, jiaoyiduishou.get("columnA"));
				rs = ps.executeQuery();
				while(rs.next()){
					CPTY_ID = rs.getString(1);//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
					CPTYNAME = rs.getString(2);
				}
				if(!"".equals(CPTY_ID)){
					logger.info("交易对手已经存在："+jiaoyiduishou.get("columnA"));
					continue;
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
			cptyDelete = cptyDelete + "'"+jiaoyiduishou.get("columnA") + "',";
			String coreCode = "SDS"+sdsIdStart;
			if(jiaoyiduishou.get("columnE").trim().length() > 0){
				coreCode = jiaoyiduishou.get("columnE");
			}
			String cptyClass = "";
			if("301-银行".equals(jiaoyiduishou.get("columnD"))){
				cptyClass = "1050006";
			}else if("603-虚拟交易对手".equals(jiaoyiduishou.get("columnD"))){
				cptyClass = "1050016";
			}else{
				logger.error("无法解析交易对手分类："+jiaoyiduishou.get("columnD"));
			}
			sbcpty.append("INSERT INTO SDS_CPTY(CPTY_ID,CPTY_CODE,CPTY_NAME,CPTYLOCALNAME,ISRESIDENT,PHONENUMBER,FAXNUMBER,CONTACT,ADDRESS,GLOBALID,LOCKED,AUDITTIMESTAMP,CPTY_ID_PARENT,CPTYCLASSES_ID,IsIssuer,Countries_Id,Comments,IsCpty,IsGuarantee,ISCUSTODIAN,SWIFTCODE)VALUES("+sdsIdStart+",'"+jiaoyiduishou.get("columnA")+"','"+jiaoyiduishou.get("columnB")+"','"+jiaoyiduishou.get("columnC")+"','Y',NULL,NULL,NULL,NULL,'"+coreCode+"','N',SYSDATE,NULL,"+cptyClass+",'Y',NULL,NULL,'Y','Y','Y',NULL);");
			jiaoyiduishou.put(jiaoyiduishou.get("columnA"), ""+sdsIdStart);
			jydsIdList.add(sdsIdStart+"");
			sdsIdStart++;
			sbcpty.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append("DELETE FROM SDS_CPTY WHERE CPTY_CODE IN ("+cptyDelete.substring(0, cptyDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sbcpty);
		sbSqlParam.append(System.getProperty("line.separator"));
	}

	/**
	 * 组装业务机构/货币对sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void sdsBRANCHESPAIRS(StringBuffer sbSqlParam) {
		Map<String,String> yewujigouid = new HashMap<String, String>();
		yewujigouid.put("00001", "1000006");
		yewujigouid.put(""+newBranchCode, ""+SDS_SiteId);
		String branchesDelete = "";
		StringBuffer sbbranches = new StringBuffer("");
		sbSqlParam.append("--业务机构--参考新开行参数-业务机构sheet页");
		sbSqlParam.append(System.getProperty("line.separator"));
		yewujigouid.put("00001", ""+1000006);
		for (Map<String,String> yewujigou : yewujigouList) {
			branchesDelete = branchesDelete + "'"+yewujigou.get("columnA") + "',";
			sbbranches.append("INSERT INTO SDS_BRANCHES(BRANCHES_ID,BRANCHES_CODE,BRANCHES_NAME,BRANCHESLOCALNAME,BRANCHESTYPE,BRANCHESLEVEL,GLOBALID,LOCKED,AUDITTIMESTAMP,BRANCHES_ID_PARENT,SITES_ID,COMMENTS,SETTLEBRANCHESCODE,Core_Bank_Id,Account_Bank_Id,Belong_Branch,BEGIN_BRANCHES_ID,END_BRANCHES_ID,SettlementCurrency,LocalCurrency,brancharea)VALUES("+sdsIdStart+",'"+yewujigou.get("columnA")+"','"+yewujigou.get("columnB").replaceAll("  ", " ")+"','"+yewujigou.get("columnC")+"','"+yewujigou.get("columnD").replaceAll("Ｉ", "I").replaceAll("Ｄ", "D")+"',"+yewujigou.get("columnE")+",'SDS"+sdsIdStart+"','N',SYSDATE,"+yewujigouid.get(yewujigou.get("columnF"))+","+SDS_SiteId+",NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);");
			yewujigouid.put(yewujigou.get("columnA"), ""+sdsIdStart);
			sdsIdStart++;
			sbbranches.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append("DELETE FROM SDS_BRANCHES WHERE BRANCHES_CODE IN ("+branchesDelete.substring(0, branchesDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sbbranches);
		sbSqlParam.append(System.getProperty("line.separator"));
		
		String pairsDelete = "";
		StringBuffer sbpairs = new StringBuffer("");
		sbSqlParam.append("--货币对");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String,String> huobidui : huobiduiList) {
			pairsDelete = pairsDelete + "'"+huobidui.get("columnB").replaceAll("/", "") + "',";
			String sql = "select CURRENCIES_ID,CURRENCIESLOCALNAME from SDS_CURRENCIES where CURRENCIES_CODE=?";
			String CURRENCIES_ID = "";
			String CURRENCIESLOCALNAME = "";
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, username, password);
				ps = con.prepareStatement(sql);
				ps.setString(1, huobidui.get("columnB").replaceAll("/", "").replaceAll(newBranchCcy, ""));
				rs = ps.executeQuery();
				while(rs.next()){
					CURRENCIES_ID = rs.getString(1);//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
					CURRENCIESLOCALNAME = rs.getString(2);
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
			if(huobidui.get("columnB").indexOf(newBranchCcy)>0){//新开行货币在后
				sbpairs.append("INSERT INTO SDS_PAIRS(PAIRS_ID,PAIRS_CODE,PAIRS_NAME,PAIRSLOCALNAME,GLOBALID,QUOTATIONMODE,QUOTATIONUNIT,POINTSDIGIT,SPOTNODECIMAL,FWDNODECIMAL,SWAPNODECIMAL,VOLNODECIMAL,PREMIUMNODECIMAL,SPOTLAG,AUDITTIMESTAMP,CURRENCIES_ID_1,CURRENCIES_ID_2,holidays_Id,SwapRateNoDecimal,Comments,percentNoDecimal,pipNoDecimal,holidayIds)VALUES("+sdsIdStart+",'"+huobidui.get("columnB").replaceAll("/", "")+"','"+huobidui.get("columnB")+"','"+CURRENCIESLOCALNAME+"兑"+SDS_CURRENCIESName+"','SDS"+sdsIdStart+"','D','0',"+huobidui.get("columnE")+",4,6,2,4,2,2,SYSDATE,"+CURRENCIES_ID+","+SDS_CURRENCIESId+",NULL,2,NULL,0,0,NULL);");
			}else{
				sbpairs.append("INSERT INTO SDS_PAIRS(PAIRS_ID,PAIRS_CODE,PAIRS_NAME,PAIRSLOCALNAME,GLOBALID,QUOTATIONMODE,QUOTATIONUNIT,POINTSDIGIT,SPOTNODECIMAL,FWDNODECIMAL,SWAPNODECIMAL,VOLNODECIMAL,PREMIUMNODECIMAL,SPOTLAG,AUDITTIMESTAMP,CURRENCIES_ID_1,CURRENCIES_ID_2,holidays_Id,SwapRateNoDecimal,Comments,percentNoDecimal,pipNoDecimal,holidayIds)VALUES("+sdsIdStart+",'"+huobidui.get("columnB").replaceAll("/", "")+"','"+huobidui.get("columnB")+"','"+SDS_CURRENCIESName+"兑"+CURRENCIESLOCALNAME+"','SDS"+sdsIdStart+"','D','0',"+huobidui.get("columnE")+",4,6,2,4,2,2,SYSDATE,"+SDS_CURRENCIESId+","+CURRENCIES_ID+",NULL,2,NULL,0,0,NULL);");
			}
			sdsIdStart++;
			sbpairs.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append("DELETE FROM SDS_PAIRS WHERE PAIRS_CODE IN ("+pairsDelete.substring(0, pairsDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sbpairs);
		sbSqlParam.append(System.getProperty("line.separator"));
	}

	/**
	 * 组装实体/运营机构/簿记架构sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void sdsEntitiesBooks(StringBuffer sbSqlParam) {
		sbSqlParam.append("--实体--参考新开行参数运营机构sheet页");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM SDS_ENTITIES WHERE ENTITIES_CODE IN ('"+newBranchCode+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("INSERT INTO SDS_ENTITIES(ENTITIES_ID,ENTITIES_CODE,ENTITIES_NAME,ENTITIESLOCALNAME,GLOBALID,COMMENTS,AUDITTIMESTAMP,ENTITY_CURRENCIES_ID,LOCALCURRENCIES_ID,SETTLECURRENCIES_ID,EntityType)VALUES("+sdsIdStart+",'"+newBranchCode+"','"+yunyingjigou.get("columnB")+"','"+yunyingjigou.get("columnC")+"','SDS"+sdsIdStart+"',NULL,SYSDATE,1000314,NULL,1000314,'0');");
		SDS_ENTITIESId = sdsIdStart;
		sdsIdStart++;
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--运营机构--参考新开行参数运营机构sheet页");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM SDS_SITES WHERE SITES_CODE IN ('"+newBranchCode+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("INSERT INTO SDS_SITES(SITES_ID,SITES_CODE,SITES_NAME,SITESLOCALNAME,GLOBALID,TIMEOFFSET,AUDITTIMESTAMP,Comments)VALUES("+sdsIdStart+",'"+newBranchCode+"','"+yunyingjigou.get("columnB")+"','"+yunyingjigou.get("columnC")+"','SDS"+sdsIdStart+"','"+yunyingjigou.get("columnD")+"',SYSDATE,NULL);");
		SDS_SiteId = sdsIdStart;
		sdsIdStart++;
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
		
		for (Map<String,String> huobi  : huobiList) {
			if(newBranchCcy.equals(huobi.get("columnB"))){
				sbSqlParam.append("--节假日--参考新开行参数-货币，注意HOLIDAYS_ID与SDS_HOLIDAYS表id一致");
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlParam.append("DELETE FROM SDS_HOLIDAYS WHERE HOLIDAYS_CODE IN ('"+newBranchCcy+"');");
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlParam.append("INSERT INTO SDS_HOLIDAYS(HOLIDAYS_ID,HOLIDAYS_CODE,HOLIDAYS_NAME,HOLIDAYSLOCALNAME,GLOBALID,AUDITTIMESTAMP,Comments)VALUES("+sdsIdStart+",'"+newBranchCcy+"','"+newBranchCcy+"','"+huobi.get("columnC")+"节假日','SDS"+sdsIdStart+"',SYSDATE,NULL);");
				SDS_HOLIDAYSId = sdsIdStart;
				sdsIdStart++;
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlParam.append(System.getProperty("line.separator"));
				
				sbSqlParam.append("--货币--参考新开行参数-货币，注意HOLIDAYS_ID与SDS_HOLIDAYS表id一致");
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlParam.append("DELETE FROM SDS_CURRENCIES WHERE CURRENCIES_CODE IN ('"+newBranchCcy+"');");
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlParam.append("INSERT INTO SDS_CURRENCIES(CURRENCIES_ID,CURRENCIES_CODE,CURRENCIES_NAME,CURRENCIESLOCALNAME,GLOBALID,ISOCODE,HOLIDAYS_ID,ROUNDINGTYPE,NODECIMAL,INTERESTNODECIMAL,AMOUNTUNIT,BASIS,AUDITTIMESTAMP,CurrencyType,Comments)VALUES("+sdsIdStart+",'"+newBranchCcy+"','"+newBranchCcy+"','"+huobi.get("columnC")+"','SDS"+sdsIdStart+"','"+newBranchCcy+"',"+SDS_HOLIDAYSId+",'R',2,4,'Y','A',SYSDATE,NULL,NULL);");
				SDS_CURRENCIESName = huobi.get("columnC");
				SDS_CURRENCIESId = sdsIdStart;
				sdsIdStart++;
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlParam.append(System.getProperty("line.separator"));
			}
		}
		
		sbSqlParam.append("--账簿--参考新开行参数-簿记架构");
		sbSqlParam.append(System.getProperty("line.separator"));
		String sdBookDelete = "";
		String sdFolderDelete = "";
		StringBuffer sdBook = new StringBuffer("");
		StringBuffer sdFolder = new StringBuffer("");
		int idBook = 0;
		String BookName = "";
		for (Map<String,String> bujijiagou : bujijiagouList) {
			if("Book".equals(bujijiagou.get("columnD"))){
				sdBookDelete = sdBookDelete + "'"+bujijiagou.get("columnA")+"',";
				idBook = sdsIdStart;
				BookName = bujijiagou.get("columnA");
				sdBook.append("INSERT INTO SDS_BOOKS(BOOKS_ID,BOOKS_CODE,BOOKS_NAME,BOOKSLOCALNAME,GLOBALID,COMMENTS,AUDITTIMESTAMP,ENTITIES_ID,books_Id_Parent)VALUES("+sdsIdStart+",'"+bujijiagou.get("columnA")+"','"+bujijiagou.get("columnA")+"','','"+sdsIdStart+"',NULL,SYSDATE,"+SDS_ENTITIESId+",NULL);");
				sdsIdStart++;
				sdBook.append(System.getProperty("line.separator"));
			}
			if("Folder".equals(bujijiagou.get("columnD")) && BookName.equals(bujijiagou.get("columnE"))){
				sdFolderDelete = sdFolderDelete + "'"+bujijiagou.get("columnA")+"',";
				String folderStatus = "I";
				if("交易账户".equals(bujijiagou.get("columnF"))){
					folderStatus = "T";
				}
				sdFolder.append("INSERT INTO SDS_FOLDERS(FOLDERS_ID,FOLDERS_CODE,FOLDERS_NAME,FOLDERSLOCALNAME,FOLDERSSTATUS,GLOBALID,CURRENCIES_ID,COMMENTS,AUDITTIMESTAMP,BOOKS_ID,AccountingSection,LOCALCURRENCIES_ID,SETTLECURRENCIES_ID,HomeType)VALUES("+sdsIdStart+",'"+bujijiagou.get("columnA")+"','"+bujijiagou.get("columnA")+"','"+bujijiagou.get("columnA")+"','"+folderStatus+"','SDS"+sdsIdStart+"','"+SDS_CURRENCIESId+"',NULL,SYSDATE,"+idBook+",NULL,NULL,1000314,NULL);");
				sdsIdStart++;
				sdFolder.append(System.getProperty("line.separator"));
			}
		}
		sbSqlParam.append("DELETE FROM SDS_BOOKS WHERE BOOKS_CODE IN ("+sdBookDelete.substring(0, sdBookDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sdBook);
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--账户--参考新开行参数-簿记架构,注意CURRENCIES_ID与SDS_CURRENCIES表id一致");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM SDS_FOLDERS WHERE FOLDERS_CODE IN ("+sdFolderDelete.substring(0, sdFolderDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sdFolder);
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--自定义节假日--参考新开行参数-节假日");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM SDS_VARIABLEHOLIDAYS WHERE HOLIDAYS_ID IN ('"+SDS_HOLIDAYSId+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String,String> jiejiari : jiejiariList) {
			sbSqlParam.append("INSERT INTO SDS_VARIABLEHOLIDAYS(HOLIDAYS_ID, ADJUSTTYPE, HOLIDAYDATE, HOLIDAYDESCRIPTION, COMMENTS, AUDITTIMESTAMP)VALUES("+SDS_HOLIDAYSId+", 'A', "+Long.parseLong(jiejiari.get("columnC").replaceAll("-", ""))+", '"+jiejiari.get("columnD").replaceAll("'", "''")+"', '批量导入', SYSDATE);");
			sbSqlParam.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append(System.getProperty("line.separator"));
	}

	/**
	 * 组装外汇指标sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void baseIndex(StringBuffer sbSqlParam) {
		sbSqlParam.append("--外汇指标,对应货币对sheet页-需调整REFER_ID, PAIRS_ID");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM BASE_FOREXINDEX WHERE REFER_ID IN (");
		for (Map<String, String> zhibiao : huobiduiList) {
			if(huobiduiList.indexOf(zhibiao)==huobiduiList.size()-1){
				sbSqlParam.append("'"+zhibiao.get("columnB").replaceAll("/", "")+"_"+newBranchCode+"'");
			}else{
				sbSqlParam.append("'"+zhibiao.get("columnB").replaceAll("/", "")+"_"+newBranchCode+"',");
			}
		}
		sbSqlParam.append(");");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String, String> zhibiao : huobiduiList) {
			sbSqlParam.append("INSERT INTO BASE_FOREXINDEX(REFER_ID, PAIRS_ID, MARKETS_ID, SOURCE_ID, UPDATEFREQ, MEMO)Values ('"+zhibiao.get("columnB").replaceAll("/", "")+"_"+newBranchCode+"', '"+zhibiao.get("columnB").replaceAll("/", "")+"', NULL, '3', NULL, NULL);");
			sbSqlParam.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--数据集与外汇指标的关系,对应货币对sheet页-需调整REFER_ID, PAIRS_ID");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM BASE_MKTGRIDPAIRSREF WHERE REFER_ID IN (");
		for (Map<String, String> zhibiao : huobiduiList) {
			if(huobiduiList.indexOf(zhibiao)==huobiduiList.size()-1){
				sbSqlParam.append("'"+zhibiao.get("columnB").replaceAll("/", "")+"_"+newBranchCode+"'");
			}else{
				sbSqlParam.append("'"+zhibiao.get("columnB").replaceAll("/", "")+"_"+newBranchCode+"',");
			}
		}
		sbSqlParam.append(");");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String, String> zhibiao : huobiduiList) {
			sbSqlParam.append("INSERT INTO BASE_MKTGRIDPAIRSREF(MKTGRID_ID, SOURCE_ID, REFER_ID, PAIRS_ID)VALUES('SYS_DATASET', NULL, '"+zhibiao.get("columnB").replaceAll("/", "")+"_"+newBranchCode+"', '"+zhibiao.get("columnB").replaceAll("/", "")+"');");
			sbSqlParam.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append(System.getProperty("line.separator"));
		
		
		String cptyDelete = "";
		StringBuffer sbcpty = new StringBuffer("");
		sbSqlParam.append("--货币指标,需调整INDEX_ID, CURRENCIES_ID, HOLIDAYS_ID");
		sbSqlParam.append(System.getProperty("line.separator"));
		for (Map<String, String> zhibiao : zhibiaoList) {
			String sql = "select count(1) from BASE_MONEYINDEX where INDEX_ID =?";
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, username, password);
				ps = con.prepareStatement(sql);
				ps.setString(1, zhibiao.get("columnB"));
				rs = ps.executeQuery();
				long num = 0;
				while(rs.next()){
					num = rs.getLong(1);
				}
				if(num > 0){
					logger.info("货币指标已经存在："+zhibiao.get("columnB"));
					continue;
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
			cptyDelete = cptyDelete + "'"+zhibiao.get("columnB") + "',";
			sbcpty.append("INSERT INTO BASE_MONEYINDEX(INDEX_ID, CURRENCIES_ID, MARKETS_ID, RESETFREQ, OBSERFREQ, OBSERTIME, SPOTOFFSET, BUSINESSDAYCONV, HOLIDAYS_ID, UPFRONTORAREAS, SOURCE_ID, UPDATEFREQ, MEMO)");
			sbcpty.append(System.getProperty("line.separator"));
			sbcpty.append("VALUES('"+zhibiao.get("columnB")+"', '"+newBranchCcy+"', NULL, NULL, '1D', 0, 0, 0, '"+newBranchCcy+"', 0, '3', NULL, 'GITS投产批量导入');");
			sbcpty.append(System.getProperty("line.separator"));
		}
		sbSqlParam.append("DELETE FROM BASE_MONEYINDEX WHERE INDEX_ID IN ("+cptyDelete.substring(0, cptyDelete.length()-1)+");");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(sbcpty);
		sbSqlParam.append(System.getProperty("line.separator"));
		
	}

	/**
	 * 组装运营机构关联实体表、影像配置、下发IMA头寸数据控制表sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void baseDft(StringBuffer sbSqlParam) {
		sbSqlParam.append("--运营机构关联实体,需将SITES_CODE,ENTITIES_CODE替换为新的行号");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM BASE_ENTITIESSITES WHERE SITES_CODE IN ('"+newBranchCode+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("INSERT INTO BASE_ENTITIESSITES(SITES_CODE, ENTITIES_CODE, ISVAL_FLAG, IS_DAILY, UPDATETIME, COMMENTS)VALUES ('"+newBranchCode+"', '"+newBranchCode+"', 'Y', 'N', TO_CHAR(SYSDATE,'YYYYMMDD'), NULL);");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--影像配置,需将MASTERBANKID替换为新的行号");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM BASE_BANKSTATE WHERE MASTERBANKID IN ('"+newBranchCode+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("INSERT INTO BASE_BANKSTATE (MASTERBANKID, STATE, FTPSERVERIP, FTPSERVERPORT, SYSTEMNAME, UPDATETIME) VALUES ('"+newBranchCode+"', 1, '21.125.38.105', '18118', 'GI', SYSDATE);");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
		
		sbSqlParam.append("--下发IMA头寸数据控制表,需将BANK_ID替换为新的行号，LOCAL_CCY替换为新开行货币");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("DELETE FROM DFT_CONTROL WHERE BANK_ID IN ('"+newBranchCode+"');");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("INSERT INTO DFT_CONTROL (BANK_ID, LOCAL_CCY, COMMENTS) VALUES('"+newBranchCode+"', '"+newBranchCcy+"', NULL);");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
	}

	/**
	 * 组装用户机构关联表sql
	* @param sbSqlParam void
	* @author lb-pc
	* @throws
	 */
	private void baseUserbranch(StringBuffer sbSqlParam) {
		sbSqlParam.append("--用户机构关联 需调整USER_ID, BRANCH_ID, USER_NAME");
		sbSqlParam.append(System.getProperty("line.separator"));
		File file = new File(userFileName);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if (isImpxlsx) {
			Workbook workbookx = null;
			try {
				workbookx = new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Sheet sheet = workbookx.getSheetAt(userSheetIndex);
			int rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				sbSqlParam.append("delete from BASE_USERBRANCH where USER_ID in(");
				StringBuffer sbSqlTemp = new StringBuffer("");
				for (int i = userColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					String userId = caseCellType(row.getCell(0)).trim();
					String userName = caseCellType(row.getCell(1)).trim();
					if(userId.length()>0){
						sbSqlParam.append("'"+userId+"',");
						sbSqlTemp.append("INSERT INTO BASE_USERBRANCH (USER_ID, BRANCH_ID, USER_NAME) VALUES ('"+userId+"', '"+newBranchCode+"', '"+userName+"');");
						sbSqlTemp.append(System.getProperty("line.separator"));
					}
				}
				sbSqlParam.append("'admin"+newBranchCode+"','qtadmin"+newBranchCode+"');");
				sbSqlParam.append(System.getProperty("line.separator"));
				sbSqlTemp.append("INSERT INTO BASE_USERBRANCH (USER_ID, BRANCH_ID, USER_NAME) VALUES ('admin"+newBranchCode+"', '"+newBranchCode+"', '"+newBranchCode+"管理员');");
				sbSqlTemp.append(System.getProperty("line.separator"));
				sbSqlTemp.append("INSERT INTO BASE_USERBRANCH (USER_ID, BRANCH_ID, USER_NAME) VALUES ('qtadmin"+newBranchCode+"', '"+newBranchCode+"', '"+newBranchCode+"前台管理员');");
				sbSqlTemp.append(System.getProperty("line.separator"));
				sbSqlParam.append(sbSqlTemp);
			}
		}
		sbSqlParam.append(System.getProperty("line.separator"));
	}

	/**
	* 新开行参数脚本禁用约束
	* @author lb-pc
	* @throws
	 */
	public void disableConstraint(StringBuffer sbSqlParam){
		sbSqlParam.append("--禁用约束");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_ALLTABLESID DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_ENTITIES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BOOKS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYCLASSES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTY DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_HOLIDAYS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CURRENCIES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPBOOKS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPENTITIES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPFOLDERS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTCLASSES DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITESCPTYGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRP DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRPELT DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_VARIABLEHOLIDAYS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_WEEKHOLIDAYS DISABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BOOKS DISABLE CONSTRAINT SDS_BOOKS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHES DISABLE CONSTRAINT SDS_BRANCHES_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHES DISABLE CONSTRAINT SDS_BRANCHES_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRPELT DISABLE CONSTRAINT SDS_BRANCHESGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRPELT DISABLE CONSTRAINT SDS_BRANCHESGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERS DISABLE CONSTRAINT SDS_CODIFIERS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRPELT DISABLE CONSTRAINT SDS_CODIFIERSGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRPELT DISABLE CONSTRAINT SDS_CODIFIERSGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTY DISABLE CONSTRAINT SDS_CPTY_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTY DISABLE CONSTRAINT SDS_CPTY_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRPELT DISABLE CONSTRAINT SDS_CPTYGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRPELT DISABLE CONSTRAINT SDS_CPTYGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CURRENCIES DISABLE CONSTRAINT SDS_CURRENCIES_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERS DISABLE CONSTRAINT SDS_FOLDERS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERS DISABLE CONSTRAINT SDS_FOLDERS_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPBOOKS DISABLE CONSTRAINT SDS_FOLDERSGRPBOOKS_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPENTITIES DISABLE CONSTRAINT SDS_FOLDERSGRPENTITIES_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPFOLDERS DISABLE CONSTRAINT SDS_FOLDERSGRPFOLDERS_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENT DISABLE CONSTRAINT SDS_INSTRUMENT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRPELT DISABLE CONSTRAINT SDS_INSTRUMENTGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRPELT DISABLE CONSTRAINT SDS_INSTRUMENTGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRPELT DISABLE CONSTRAINT SDS_INSTRUMENTSALESGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRPELT DISABLE CONSTRAINT SDS_INSTRUMENTSALESGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRS DISABLE CONSTRAINT SDS_PAIRS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRS DISABLE CONSTRAINT SDS_PAIRS_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRPELT DISABLE CONSTRAINT SDS_PAIRSGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRPELT DISABLE CONSTRAINT SDS_PAIRSGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITESCPTYGRPELT DISABLE CONSTRAINT SDS_SITESCPTYGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITESCPTYGRPELT DISABLE CONSTRAINT SDS_SITESCPTYGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRPELT DISABLE CONSTRAINT SDS_TENORSGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRPELT DISABLE CONSTRAINT SDS_TENORSGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_VARIABLEHOLIDAYS DISABLE CONSTRAINT SDS_VARIABLEHOLIDAYS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_WEEKHOLIDAYS DISABLE CONSTRAINT SDS_WEEKHOLIDAYS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_IBORATEINDEX DISABLE CONSTRAINT SDS_IBORATEINDEX_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append(System.getProperty("line.separator"));
	}
	
	/**
	* 新开行参数脚本启用约束
	* @author lb-pc
	* @throws
	 */
	public void enableConstraint(StringBuffer sbSqlParam){
		sbSqlParam.append("--启用约束");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BOOKS ENABLE CONSTRAINT SDS_BOOKS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHES ENABLE CONSTRAINT SDS_BRANCHES_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHES ENABLE CONSTRAINT SDS_BRANCHES_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRPELT ENABLE CONSTRAINT SDS_BRANCHESGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRPELT ENABLE CONSTRAINT SDS_BRANCHESGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERS ENABLE CONSTRAINT SDS_CODIFIERS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRPELT ENABLE CONSTRAINT SDS_CODIFIERSGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRPELT ENABLE CONSTRAINT SDS_CODIFIERSGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTY ENABLE CONSTRAINT SDS_CPTY_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTY ENABLE CONSTRAINT SDS_CPTY_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRPELT ENABLE CONSTRAINT SDS_CPTYGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERS ENABLE CONSTRAINT SDS_FOLDERS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPBOOKS ENABLE CONSTRAINT SDS_FOLDERSGRPBOOKS_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPENTITIES ENABLE CONSTRAINT SDS_FOLDERSGRPENTITIES_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPFOLDERS ENABLE CONSTRAINT SDS_FOLDERSGRPFOLDERS_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENT ENABLE CONSTRAINT SDS_INSTRUMENT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRPELT ENABLE CONSTRAINT SDS_INSTRUMENTGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRPELT ENABLE CONSTRAINT SDS_INSTRUMENTGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRPELT ENABLE CONSTRAINT SDS_INSTRUMENTSALESGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRPELT ENABLE CONSTRAINT SDS_INSTRUMENTSALESGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRS ENABLE CONSTRAINT SDS_PAIRS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRPELT ENABLE CONSTRAINT SDS_PAIRSGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRPELT ENABLE CONSTRAINT SDS_PAIRSGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITESCPTYGRPELT ENABLE CONSTRAINT SDS_SITESCPTYGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRPELT ENABLE CONSTRAINT SDS_TENORSGRPELT_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRPELT ENABLE CONSTRAINT SDS_TENORSGRPELT_FK2;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_WEEKHOLIDAYS ENABLE CONSTRAINT SDS_WEEKHOLIDAYS_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_IBORATEINDEX ENABLE CONSTRAINT SDS_IBORATEINDEX_FK1;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_ALLTABLESID ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_ENTITIES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BOOKS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_BRANCHESGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CODIFIERSGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYCLASSES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTY ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CPTYGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_HOLIDAYS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_CURRENCIES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPBOOKS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPENTITIES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_FOLDERSGRPFOLDERS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTCLASSES ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_INSTRUMENTSALESGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_PAIRSGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_SITESCPTYGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRP ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_TENORSGRPELT ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_VARIABLEHOLIDAYS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
		sbSqlParam.append("ALTER TABLE SDS_WEEKHOLIDAYS ENABLE ALL TRIGGERS;");
		sbSqlParam.append(System.getProperty("line.separator"));
	}
	
	/**
	 * 解析新开行柜员文档生成新开行ecas脚本
	* @author lb-pc
	* @throws
	 */
	private void createUserSqlByNewBranchDocument() {
		logger.info("[START]解析新开行柜员文档生成新开行ecas脚本");
		File file = new File(userFileName);
		Workbook workbook = new HSSFWorkbook();
		boolean isImpxlsx = false;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			isImpxlsx=true;
		}
		if (isImpxlsx) {
			Workbook workbookx = null;
			try {
				workbookx = new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			StringBuffer sbSqlUserId = new StringBuffer("");
			StringBuffer sbSqlUserRole = new StringBuffer("");
			sbSqlUserId.append("--新开行ECAS参数脚本");
			sbSqlUserId.append(System.getProperty("line.separator"));
			sbSqlUserId.append("--用户表,需调整LOGONID、NAME、BANKID,需检查用户之前是否已经存在");
			sbSqlUserId.append(System.getProperty("line.separator"));
			sbSqlUserRole.append("--用户角色关联表,需调整LOGONID, APPLID, ROLEID");
			sbSqlUserRole.append(System.getProperty("line.separator"));
			Sheet sheet = workbookx.getSheetAt(userSheetIndex);
			int rcount = sheet.getPhysicalNumberOfRows();
			if (rcount > 0) {
				for (int i = userColumnIndex; i < rcount; i++) {
					Row row = sheet.getRow(i);
					String userId = caseCellType(row.getCell(0)).trim();
					String userName = caseCellType(row.getCell(1)).trim();
					String userRole = caseCellType(row.getCell(2)).trim();
					if(userId.length()>0){
						logger.info("第"+(i+1)+"行解析结果：[用户ID]"+userId+" [用户姓名]"+userName+" [用户角色]"+userRole);
						//检查该用户之前是否已经存在
						String sql = "select count(1) from ECAS_USER where LOGONID = ?";
						try {
							Class.forName(driverClassName);
							con = DriverManager.getConnection(url, usernameEcas, passwordEcas);
							ps = con.prepareStatement(sql);
							ps.setString(1, userId);//动态写入表名
							rs = ps.executeQuery();
							long userNum = 0;
							while(rs.next()){
								userNum = rs.getLong(1);//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
							}
							if(userNum > 0){
								logger.error("用户已存在："+userId);
								continue;
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
						//生成该用户的用户表sql
						sbSqlUserId.append("insert into ecas_user (LOGONID, NAME, PASSWORD, TYPE, ENABLED, CERTIFICATETYPE, CERTIFICATEID, VALIDDATE, LASTDATE, TRYTIMES, BANKID, DEPTID, LVL, FIALTIME, TELEPHONE, FAILEDDATE, ISREGISTER, REGISTERTYPE, EVENTID, EVENTID2, LASTURL) values ('"+userId+"', '"+userName+"', 'Sgzeca7nFYVC0BP8DJ9az8c1xhI=', 1, 0, 1, '1', sysdate, sysdate, 0, '"+newBranchCode+"', '2', 'O', 0, null, sysdate, 0, 0, 0, 0, null);");
						sbSqlUserId.append(System.getProperty("line.separator"));
						//生成该用户的用户权限表sql
						if("GITS前中台-分行管理角色".equals(userRole)){
							sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('"+userId+"', 9999, 1001, 0, 0);");
							sbSqlUserRole.append(System.getProperty("line.separator"));
							sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('"+userId+"', 9, 301, 0, 0);");
							sbSqlUserRole.append(System.getProperty("line.separator"));
						}else if("GITS前中台-交易员角色".equals(userRole)){
							sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('"+userId+"', 9999, 1002, 0, 0);");
							sbSqlUserRole.append(System.getProperty("line.separator"));
						}else if("GITS前中台-中台角色".equals(userRole)){
							sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('"+userId+"', 9999, 1003, 0, 0);");
							sbSqlUserRole.append(System.getProperty("line.separator"));
						}else{
							logger.error("无法解析角色"+userRole);
						}
					}
				}
				//生成分行的admin和qtadmin用户
				sbSqlUserId.append("insert into ecas_user (LOGONID, NAME, PASSWORD, TYPE, ENABLED, CERTIFICATETYPE, CERTIFICATEID, VALIDDATE, LASTDATE, TRYTIMES, BANKID, DEPTID, LVL, FIALTIME, TELEPHONE, FAILEDDATE, ISREGISTER, REGISTERTYPE, EVENTID, EVENTID2, LASTURL) values ('admin"+newBranchCode+"', '"+newBranchCode+"管理员', 'Sgzeca7nFYVC0BP8DJ9az8c1xhI=', 1, 0, 1, '1', sysdate, sysdate, 0, '"+newBranchCode+"', '2', 'O', 0, null, sysdate, 0, 0, 0, 0, null);");
				sbSqlUserId.append(System.getProperty("line.separator"));
				sbSqlUserId.append("insert into ecas_user (LOGONID, NAME, PASSWORD, TYPE, ENABLED, CERTIFICATETYPE, CERTIFICATEID, VALIDDATE, LASTDATE, TRYTIMES, BANKID, DEPTID, LVL, FIALTIME, TELEPHONE, FAILEDDATE, ISREGISTER, REGISTERTYPE, EVENTID, EVENTID2, LASTURL) values ('qtadmin"+newBranchCode+"', '"+newBranchCode+"前台管理员', 'Sgzeca7nFYVC0BP8DJ9az8c1xhI=', 1, 0, 1, '1', sysdate, sysdate, 0, '"+newBranchCode+"', '2', 'O', 0, null, sysdate, 0, 0, 0, 0, null);");
				sbSqlUserId.append(System.getProperty("line.separator"));
				sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('admin"+newBranchCode+"', 9, 301, 0, 0);");
				sbSqlUserRole.append(System.getProperty("line.separator"));
				sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('admin"+newBranchCode+"', 9999, 1001, 0, 0);");
				sbSqlUserRole.append(System.getProperty("line.separator"));
				sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('admin"+newBranchCode+"', 9992, 999201, 0, 0);");
				sbSqlUserRole.append(System.getProperty("line.separator"));
				sbSqlUserRole.append("insert into ecas_userrole (LOGONID, APPLID, ROLEID, REGISTERTYPE, EVENTID) values ('qtadmin"+newBranchCode+"', 9999, 1001, 0, 0);");
				sbSqlUserRole.append(System.getProperty("line.separator"));
				
			}
			//生成脚本文件
			try {
				StringBuffer sdFile = new StringBuffer("");
				sdFile.append(sbSqlUserId);
				sdFile.append(System.getProperty("line.separator"));
				sdFile.append(sbSqlUserRole);
				sdFile.append("COMMIT;");
				sdFile.append(System.getProperty("line.separator"));
				File filesql = new File(sqlFilePath+newBranchName+"新开行ecas脚本.sql");
				if(!filesql.exists()){
					filesql.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(filesql);
				fos.write(sdFile.toString().getBytes());
				logger.info(newBranchName+"新开行ecas脚本.sql生成成功，文件路径为："+sqlFilePath);
			} catch (Exception e) {
			}
		}
		logger.info("[END]解析新开行柜员文档生成新开行ecas脚本");
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
