package org.cjf.util.generator.entity;

import java.util.List;

public class EntityPropertyMetaConfig {
	private String entityName = "";
	private List<PropertyMeta> listPropertyField;
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public List<PropertyMeta> getListPropertyField() {
		return listPropertyField;
	}
	public void setListPropertyField(List<PropertyMeta> listPropertyField) {
		this.listPropertyField = listPropertyField;
	}
	
}