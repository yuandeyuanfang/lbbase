package com.lb.base.workproject.shidaiyintong; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class CreateBanben {
public final String BanbenNamePath = "D:\\版本列表.txt";

public static final String path = "D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.0珠海农发改动"; //上次抽取珠海农发版本号21057-21099;每次都要更新
public static final String pathdeletenew = "D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.0\\AC\\3.0_AC\\rcs";//上次同步产品组版本号20745-21086;每次都要更新
public static final String pathdeleteold = "D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.01\\AC\\3.0_AC\\rcs";//可直接覆盖

public void pathdelete(){
	List<String> pathList = new ArrayList<String>();
	listFile(path,pathList);
	System.out.println("珠海农发改动文件数量："+pathList.size());
//	for (String string : pathList) {
//		System.out.println(string.replace("D:\\林博\\工作记录-时代银通\\版本管理\\01Sources\\trunk\\gits_zq", ""));
//	}
	pathdeletenew(pathList);
	pathdeleteold(pathList);
}

public void pathdeletenew(List<String> pathList805){
	List<String> pathList = new ArrayList<String>();
	listFile(pathdeletenew,pathList);
	System.out.println("本次RCS修改文件数量"+pathList.size());
	for (String string : pathList) {
		boolean isdelete = true;
		String a = string.replace("D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.0\\AC\\3.0_AC\\rcs", "");
		for (String string805 : pathList805) {
			if(a.equals(string805.replace("D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.0珠海农发改动\\app\\ADBOC", ""))){
				isdelete = false;
			}
		}
		if(isdelete){
			File file = new File(string);
			file.delete();
			System.out.println("珠海农商没有改动，删除"+string);
		}
	}
}

public void pathdeleteold(List<String> pathList805){
	List<String> pathList = new ArrayList<String>();
	listFile(pathdeleteold,pathList);
	System.out.println("本次RCS修改文件数量"+pathList.size());
	for (String string : pathList) {
		String a = string.replace("D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.01\\AC\\3.0_AC\\rcs", "");
		for (String string805 : pathList805) {
			if(a.equals(string805.replace("D:\\林博\\工作记录-时代银通\\版本管理\\RCS2.0珠海农发改动\\app\\ADBOC", ""))){
				File file = new File(string);
				file.delete();
				System.out.println("珠海农商有改动，删除"+string);
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


