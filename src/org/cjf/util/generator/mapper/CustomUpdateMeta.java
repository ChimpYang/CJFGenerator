package org.cjf.util.generator.mapper;

import java.io.Serializable;

public class CustomUpdateMeta implements Serializable {
	private static final long serialVersionUID = -6335989691373955485L;
	
	private String updateName;
	private String fieldName;
	private String propertyName;
	
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
