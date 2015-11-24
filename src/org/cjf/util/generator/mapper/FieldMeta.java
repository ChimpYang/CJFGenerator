package org.cjf.util.generator.mapper;

import java.io.Serializable;

public class FieldMeta implements Serializable {
	private static final long serialVersionUID = -5290947487435576639L;
	
	public static final String PT_INT_EQUAL = "IntEqual";
	public static final String PT_OBJ_EQUAL = "ObjEqual";
	public static final String PT_STR_EQUAL = "StringEqual";
	public static final String PT_STR_LIKE = "StringLike";
	//TODO;add date type
	
	private String conditionType;
	private String fieldName;
	private String propertyName;
	private boolean canInsert;
	private boolean canUpdate;
	private boolean canQuery;
	
	public FieldMeta() {
		canInsert = true;
		canUpdate = true;
		canQuery = true;
	}
	
	public String getConditionType() {
		return conditionType;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
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

	public boolean isCanInsert() {
		return canInsert;
	}

	public void setCanInsert(boolean canInsert) {
		this.canInsert = canInsert;
	}

	public boolean isCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	public boolean isCanQuery() {
		return canQuery;
	}

	public void setCanQuery(boolean canQuery) {
		this.canQuery = canQuery;
	}

}
