package org.cjf.util.generator.mapper;

import java.util.List;

public class TableFieldMetaConfig {
	
	private List<String> listNotInsert;
	private List<String> listNotUpdate;
	private List<String> listNotQuery;
	private List<CustomUpdateMeta> customUpdateList;
	private List<RelationMeta> relationList;
	private List<String> listQueryLikeFiled;
	private List<LookupFieldMeta> listLookup;
	
	private String entityName = "";
	private String bizFieldName = "";
	private String bizPropertyName = "";
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getBizFieldName() {
		return bizFieldName;
	}
	public void setBizFieldName(String bizFieldName) {
		this.bizFieldName = bizFieldName;
	}
	public String getBizPropertyName() {
		return bizPropertyName;
	}
	public void setBizPropertyName(String bizPropertyName) {
		this.bizPropertyName = bizPropertyName;
	}
	
	public List<String> getListNotInsert() {
		return listNotInsert;
	}
	public void setListNotInsert(List<String> listNotInsert) {
		this.listNotInsert = listNotInsert;
	}
	public List<String> getListNotUpdate() {
		return listNotUpdate;
	}
	public void setListNotUpdate(List<String> listNotUpdate) {
		this.listNotUpdate = listNotUpdate;
	}
	public List<String> getListNotQuery() {
		return listNotQuery;
	}
	public void setListNotQuery(List<String> listNotQuery) {
		this.listNotQuery = listNotQuery;
	}
	public List<RelationMeta> getRelationList() {
		return relationList;
	}
	public void setRelationList(List<RelationMeta> relationList) {
		this.relationList = relationList;
	}
	public List<CustomUpdateMeta> getCustomUpdateList() {
		return customUpdateList;
	}
	public void setCustomUpdateList(List<CustomUpdateMeta> customUpdateList) {
		this.customUpdateList = customUpdateList;
	}
	public List<String> getListQueryLikeFiled() {
		return listQueryLikeFiled;
	}
	public void setListQueryLikeFiled(List<String> listQueryLikeFiled) {
		this.listQueryLikeFiled = listQueryLikeFiled;
	}
	public List<LookupFieldMeta> getListLookup() {
		return listLookup;
	}
	public void setListLookup(List<LookupFieldMeta> listLookup) {
		this.listLookup = listLookup;
	}

}
