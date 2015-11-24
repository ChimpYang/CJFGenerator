package org.cjf.util.generator.action;

import java.io.Serializable;
import java.util.List;

public class ActionMetaConfig implements Serializable {
	private static final long serialVersionUID = -6279393781947770731L;
	
	private String tableName;
	private String entityName;
	private String actionPackage;
	private String bizPackage;
	private String bizInterface;
	private boolean hasLastModifyDate;
	private List<CustomModifyMeta> listCustomModify;
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getActionPackage() {
		return actionPackage;
	}
	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}
	public String getBizPackage() {
		return bizPackage;
	}
	public void setBizPackage(String bizPackage) {
		this.bizPackage = bizPackage;
	}
	public boolean isHasLastModifyDate() {
		return hasLastModifyDate;
	}
	public void setHasLastModifyDate(boolean hasLastModifyDate) {
		this.hasLastModifyDate = hasLastModifyDate;
	}
	public List<CustomModifyMeta> getListCustomModify() {
		return listCustomModify;
	}
	public void setListCustomModify(List<CustomModifyMeta> listCustomModify) {
		this.listCustomModify = listCustomModify;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getBizInterface() {
		return bizInterface;
	}
	public void setBizInterface(String bizInterface) {
		this.bizInterface = bizInterface;
	}
	
}
