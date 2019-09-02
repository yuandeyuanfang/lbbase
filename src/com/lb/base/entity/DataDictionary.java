package com.lb.base.entity;

import java.io.Serializable;
import java.util.Date;



public class DataDictionary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String dataTypesCode;		
	private String dataTypesName;
	private String dataTypeCode;	
	private String dataTypeName;
	private Integer isUse;		
	private String memo;
	private Date createTime;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDataTypesCode() {
		return dataTypesCode;
	}
	public void setDataTypesCode(String dataTypesCode) {
		this.dataTypesCode = dataTypesCode;
	}
	public String getDataTypesName() {
		return dataTypesName;
	}
	public void setDataTypesName(String dataTypesName) {
		this.dataTypesName = dataTypesName;
	}
	public String getDataTypeCode() {
		return dataTypeCode;
	}
	public void setDataTypeCode(String dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
