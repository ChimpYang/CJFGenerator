package org.cjf.util.generator.mapper;

import java.io.Serializable;
import java.util.List;

public class LookupFieldMeta implements Serializable {
	private static final long serialVersionUID = 2240789253032063415L;
	
	private String lookupTable;
	private String prefixName;
	private List<String> lookupFields;
	private String joinType;
	private String mainField;
	private String joinField;
	
	public String getMainField() {
		return mainField;
	}

	public void setMainField(String mainField) {
		this.mainField = mainField;
	}

	public String getJoinField() {
		return joinField;
	}

	public void setJoinField(String joinField) {
		this.joinField = joinField;
	}

	public LookupFieldMeta() {
		joinType = "left join";
	}
	
	public String getLookupTable() {
		return lookupTable;
	}
	public void setLookupTable(String lookupTable) {
		this.lookupTable = lookupTable;
	}
	public List<String> getLookupFields() {
		return lookupFields;
	}
	public void setLookupFields(List<String> lookupFields) {
		this.lookupFields = lookupFields;
	}
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
}
