package com.lb.base.util; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CreateBanben {
public static final String Ver = "FMMS_20180119";
public static final String BanbenRoot = "D:\\"+Ver+"\\";
public static final String BanbenVer = "D:\\"+Ver+"\\FMMSAPP\\";
public final String BanbenNamePath = "D:\\版本列表.txt";
public static final String serverPath29 = "D:\\dbos4";
public static final String serverPath31 = "D:\\dbos4_surface";
public final String serverPath29Sc = "/home/fmmsapp/workspace/dbos4_war.ear/dbos4.war";
public final String serverPath31Sc = "/home/fmmsuser/workspace/dbos4_surface_war.ear/dbos4_surface.war";

public static final String path = "D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\01Sources";
public static final String pathdeletenew = "D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\RCS_Finance1";
public static final String pathdeleteold = "D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\RCS_Finance2";//可直接覆盖

public void pathdelete(){
	List<String> pathList = new ArrayList<String>();
	listFile(path,pathList);
	System.out.println(pathList.size());
//	for (String string : pathList) {
//		System.out.println(string.replace("D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\01Sources\\trunk\\gits_zq", ""));
//	}
	pathdeletenew(pathList);
	pathdeleteold(pathList);
}

public void pathdeletenew(List<String> pathList805){
	List<String> pathList = new ArrayList<String>();
	listFile(pathdeletenew,pathList);
	System.out.println(pathList.size());
	for (String string : pathList) {
		boolean isdelete = true;
		String a = string.replace("D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\RCS_Finance1\\02.00\\RCS2_System", "");
		for (String string805 : pathList805) {
			if(a.equals(string805.replace("D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\01Sources\\trunk\\gits_zq", ""))){
				isdelete = false;
			}
		}
		if(isdelete){
			File file = new File(string);
			file.delete();
			System.out.println("805没有改动，删除"+string);
		}
	}
}

public void pathdeleteold(List<String> pathList805){
	List<String> pathList = new ArrayList<String>();
	listFile(pathdeleteold,pathList);
	System.out.println(pathList.size());
	for (String string : pathList) {
		String a = string.replace("D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\RCS_Finance2\\02.00\\RCS2_System", "");
		for (String string805 : pathList805) {
			if(a.equals(string805.replace("D:\\林博\\工作记录-时代银通\\中国银行集团内部资金交易系统(GITS)\\版本管理\\01Sources\\trunk\\gits_zq", ""))){
				File file = new File(string);
				file.delete();
				System.out.println("805有改动，删除"+string);
				break;
			}
		}
	}
}

public void listFile(String path,List<String> pathList){
	File dir = new File(path);
	File[] files = dir.listFiles();
	for (int i = 0; i < files.length; i++) {
		if(files[i].isDirectory()){
			listFile(files[i].getAbsolutePath(),pathList);
		}else{
			String fullName = files[i].getAbsolutePath();
			pathList.add(fullName);
		}
	}
}

public static void main(String[] args) { 
	CreateBanben createMenu = new CreateBanben();
	createMenu.pathdelete(); 
}

public void createBanben29(){
	try{
		File file = new File(BanbenNamePath);
		StringBuffer sbsqlbackup = new StringBuffer("");
		StringBuffer sbsqlupdate = new StringBuffer("");
		StringBuffer sbsqlrecover = new StringBuffer("");
		sbsqlbackup.append("#backup dbos");
		sbsqlbackup.append(System.getProperty("line.separator"));
		sbsqlbackup.append("tar -cvf ./dbos4_bak.tar -C /home/fmmsapp/workspace/dbos4_war.ear/ .");
		sbsqlbackup.append(System.getProperty("line.separator"));
		sbsqlbackup.append(System.getProperty("line.separator"));
		sbsqlbackup.append("echo 0");
		sbsqlbackup.append(System.getProperty("line.separator"));
		
		sbsqlrecover.append("rm -rf /home/fmmsapp/workspace/dbos4_war.ear/*");
		sbsqlrecover.append(System.getProperty("line.separator"));
		sbsqlrecover.append("tar -xvf ./dbos4_bak.tar -C /home/fmmsapp/workspace/dbos4_war.ear/");
		sbsqlrecover.append(System.getProperty("line.separator"));
		sbsqlrecover.append(System.getProperty("line.separator"));
		sbsqlrecover.append("echo 0");
		sbsqlrecover.append(System.getProperty("line.separator"));
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
		BufferedReader br = new BufferedReader(isr);
		String filename=null;
		while ((filename=br.readLine())!=null) {
			if(filename.indexOf(serverPath29)>=0){
				String filenameDbos = filename.replace(serverPath29, "dbos4");
				filenameDbos = filenameDbos.replaceAll("\\\\", "/");
				filenameDbos = filenameDbos.replaceAll("\\$", "\\\\"+"\\$");
				filename = filename.replace(serverPath29, serverPath29Sc);
				filename = filename.replaceAll("\\\\", "/");
				filename = filename.replaceAll("\\$", "\\\\"+"\\$");
				System.out.println(filename);
				
				sbsqlupdate.append("cp ./"+filenameDbos+" "+filename);
				sbsqlupdate.append(System.getProperty("line.separator"));
				
			}
		}
	sbsqlupdate.append(System.getProperty("line.separator"));
	sbsqlupdate.append("echo 0");
	
	File f = new File(BanbenVer);
	if(!f.exists()){
		f.mkdir();
	}
//	File filesql = new File(BanbenVer+"backup.sh");
//	if(!filesql.exists()){
//		filesql.createNewFile();
//	}
//	FileOutputStream fos = new FileOutputStream(filesql);
//	fos.write(sbsqlbackup.toString().getBytes());
	
	File filesql2 = new File(BanbenVer+"update.sh");
	if(!filesql2.exists()){
		filesql2.createNewFile();
	}
	FileOutputStream fos2 = new FileOutputStream(filesql2);
	fos2.write(sbsqlupdate.toString().getBytes());
	
//	File filesql3 = new File(BanbenVer+"recover.sh");
//	if(!filesql3.exists()){
//		filesql3.createNewFile();
//	}
//	FileOutputStream fos3 = new FileOutputStream(filesql3);
//	fos3.write(sbsqlrecover.toString().getBytes());
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void createBanben31(){
	try{
		File file = new File(BanbenNamePath);
		StringBuffer sbsqlbackup = new StringBuffer("");
		StringBuffer sbsqlupdate = new StringBuffer("");
		StringBuffer sbsqlrecover = new StringBuffer("");
		sbsqlbackup.append("#backup dbos");
		sbsqlbackup.append(System.getProperty("line.separator"));
		sbsqlbackup.append("tar -cvf ./surface_bak.tar -C /home/fmmsuser/workspace/dbos4_surface_war.ear/ .");
		sbsqlbackup.append(System.getProperty("line.separator"));
		sbsqlbackup.append(System.getProperty("line.separator"));
		sbsqlbackup.append("echo 0");
		sbsqlbackup.append(System.getProperty("line.separator"));
		
		sbsqlrecover.append("rm -rf /home/fmmsuser/workspace/dbos4_surface_war.ear/*");
		sbsqlrecover.append(System.getProperty("line.separator"));
		sbsqlrecover.append("tar -xvf ./surface_bak.tar -C /home/fmmsuser/workspace/dbos4_surface_war.ear/");
		sbsqlrecover.append(System.getProperty("line.separator"));
		sbsqlrecover.append(System.getProperty("line.separator"));
		sbsqlrecover.append("echo 0");
		sbsqlrecover.append(System.getProperty("line.separator"));
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
		BufferedReader br = new BufferedReader(isr);
		String filename=null;
		boolean is31 = false;
		while ((filename=br.readLine())!=null) {
			if(filename.indexOf(serverPath31)>=0){
				is31 = true;
				String filenameDbos = filename.replace(serverPath31, "dbos4_surface");
				filenameDbos = filenameDbos.replaceAll("\\\\", "/");
				filenameDbos = filenameDbos.replaceAll("\\$", "\\\\"+"\\$");
				filename = filename.replace(serverPath31, serverPath31Sc);
				filename = filename.replaceAll("\\\\", "/");
				filename = filename.replaceAll("\\$", "\\\\"+"\\$");
				System.out.println(filename);
				
				sbsqlupdate.append("cp ./"+filenameDbos+" "+filename);
				sbsqlupdate.append(System.getProperty("line.separator"));
				
			}
		}
	sbsqlupdate.append(System.getProperty("line.separator"));
	sbsqlupdate.append("echo 0");
	
	if(is31){
//		File filesql = new File(BanbenRoot+"FMMSUSER\\"+"backup.sh");
//		FileOutputStream fos = new FileOutputStream(filesql);
//		fos.write(sbsqlbackup.toString().getBytes());
		
		File filesql2 = new File(BanbenRoot+"FMMSUSER\\"+"update.sh");
		FileOutputStream fos2 = new FileOutputStream(filesql2);
		fos2.write(sbsqlupdate.toString().getBytes());
		
//		File filesql3 = new File(BanbenRoot+"FMMSUSER\\"+"recover.sh");
//		FileOutputStream fos3 = new FileOutputStream(filesql3);
//		fos3.write(sbsqlrecover.toString().getBytes());
	}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void cheakBanben(){
	
}

public void listFile(String path,StringBuffer sb){
	File dir = new File(path);
	File[] files = dir.listFiles();
	for (int i = 0; i < files.length; i++) {
		String fileName = files[i].getName();
		if(files[i].isDirectory()){
			listFile(files[i].getAbsolutePath(),sb);
		}else{
			String fullName = files[i].getAbsolutePath();
			sb.append(fullName);
			sb.append(System.getProperty("line.separator"));
//			copyFile(fullName,BanbenVer+files[i].getName());
//			System.out.println(fullName);
		}
	}
}

public void writeBanbenliebiao(StringBuffer sb){
	try {
		File filesql = new File(BanbenNamePath);
		FileOutputStream fos = new FileOutputStream(filesql);
		fos.write(sb.toString().getBytes());
		fos.flush();
		fos.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
}

public void listFile31(String path,StringBuffer sb){
	File dir = new File(path);
	File[] files = dir.listFiles();
	for (int i = 0; i < files.length; i++) {
		String fileName = files[i].getName();
		if(files[i].isDirectory()){
			listFile(files[i].getAbsolutePath(),sb);
		}else{
			String fullName = files[i].getAbsolutePath();
			sb.append(fullName);
			sb.append(System.getProperty("line.separator"));
//			copyFile(fullName,BanbenVer+files[i].getName());
//			System.out.println(fullName);
		}
	}
}

public void writeBanbenliebiao31(StringBuffer sb){
	try {
		File filesql = new File(BanbenNamePath);
		FileOutputStream fos = new FileOutputStream(filesql);
		fos.write(sb.toString().getBytes());
		fos.flush();
		fos.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
}

public void copyFile(String oldPath,String newPath){
	try {
		int bytesum=0;
		int byteread = 0;
		File oldFile = new File(oldPath);
		if(oldFile.exists()){
			InputStream is = new FileInputStream(oldFile);
			FileOutputStream fo = new FileOutputStream(newPath);
			byte[] buffer = new byte[1444];
			int length;
			while ((byteread = is.read(buffer))!=-1) {
				bytesum+=byteread;
				fo.write(buffer, 0, byteread);
			}
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}

}


