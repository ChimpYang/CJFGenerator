package org.cjf.util.generator.action;

import java.io.Serializable;

import org.cjf.util.generator.mapper.CustomUpdateMeta;

public class CustomModifyMeta implements Serializable {
	private static final long serialVersionUID = -6082978889021409858L;
	
	private String customStatementId;
	private String propertyFunctionName;
	private String propertyType;
	
	public String getPropertyFunctionName() {
		return propertyFunctionName;
	}
	public void setPropertyFunctionName(String propertyFunctionName) {
		this.propertyFunctionName = propertyFunctionName;
	}
	public String getCustomStatementId() {
		return customStatementId;
	}
	public void setCustomStatementId(String customStatementId) {
		this.customStatementId = customStatementId;
	}
	
	public String toString() {
		return String.format("%s - %s - %s", customStatementId, propertyFunctionName, propertyType);
	}
	
	public static CustomModifyMeta cloneFromCustomUpdateMeta(CustomUpdateMeta source) {
		CustomModifyMeta clone = new CustomModifyMeta();
		clone.setCustomStatementId("update" + source.getUpdateName());
		clone.setPropertyFunctionName(getFName(source.getPropertyName()));
		
		return clone;
	}
	
	public static String getFName(String s) {
		if(null == s) {
			return null;
		}
		if(0 == s.length()) {
			return s;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(s.charAt(0)));
		sb.append(s.substring(1));
		
		return sb.toString();
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
}
