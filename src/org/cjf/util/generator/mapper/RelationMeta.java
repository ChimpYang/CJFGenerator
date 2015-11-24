package org.cjf.util.generator.mapper;

import java.io.Serializable;

public class RelationMeta implements Serializable {
	private static final long serialVersionUID = -4496029752953527341L;
	
	private String relationName;
	private String relationFiledName;
	private String relationTableName;
	
	public String getRelationFiledName() {
		return relationFiledName;
	}
	public void setRelationFiledName(String relationFiledName) {
		this.relationFiledName = relationFiledName;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getRelationTableName() {
		return relationTableName;
	}
	public void setRelationTableName(String relationTableName) {
		this.relationTableName = relationTableName;
	}
	
}
