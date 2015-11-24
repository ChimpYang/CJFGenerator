package org.cjf.util.generator.entity;

import java.io.Serializable;

public class PropertyMeta implements Serializable{
	private static final long serialVersionUID = -2459353291262290066L;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyFunctionName() {
		return propertyFunctionName;
	}
	public void setPropertyFunctionName(String propertyFunctionName) {
		this.propertyFunctionName = propertyFunctionName;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	private String propertyName;
	private String propertyFunctionName;
	private String propertyType;
}
