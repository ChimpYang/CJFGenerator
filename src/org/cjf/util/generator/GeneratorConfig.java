package org.cjf.util.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cjf.util.generator.action.ActionMetaConfig;
import org.cjf.util.generator.mapper.TableFieldMetaConfig;

public class GeneratorConfig {
	private static String schemaPattern = ""; 
	
	private static Map<String, TableFieldMetaConfig> allConfigMap = new HashMap<String, TableFieldMetaConfig>();
	private static Map<String, String> tableToEntityMap = new HashMap<String, String>();
	private static Map<String, ActionMetaConfig> actionConfigMap = new HashMap<String, ActionMetaConfig>();
	
	private static String entityTemplatePath;
	private static String entityTemplateFileName;
	private static String entityOutputPath;
	private static String packageName;
	
	private static String mapperTemplatePath;
	private static String mapperTemplateFileName;
	private static String mapperOutputPath;
	private static String nameSpace;
	
	private static String actionTemplateFilePath;
	private static String actionOutputPath;
	
	private static Map<String, String> versionMap = new HashMap<String, String>();
	
	public static void addVersionMap(Map<String, String> map) {
		versionMap.clear();
		
		versionMap.putAll(map);
	}
	
	public static String getVersion(String templateType) {
		String version = "1.0";
		
		if(versionMap.containsKey(templateType)) {
			version = versionMap.get(templateType);
			if(null == version || "".equals(version)) {
				version = "1.0";
			}
		}
		
		return version;
	}
	
	public static boolean containsTable(String tableName) {
		return allConfigMap.containsKey(tableName);
	}
	
	public static TableFieldMetaConfig getTableFieldMetaConfig(String tableName) {
		return allConfigMap.get(tableName);
	}
	
	public static boolean containsTableToEntity(String tableName) {
		return tableToEntityMap.containsKey(tableName);
	}
	
	public static String getEntityNameFromTableName(String tableName) {
		return tableToEntityMap.get(tableName);
	}
	
	public static boolean canNotInsertExist(String tableName, String fieldName) {
		if(!containsTable(tableName)) {
			return false;
		}
		
		TableFieldMetaConfig tfc = allConfigMap.get(tableName);
		List<String> list = tfc.getListNotInsert();
		if(null != list && list.contains(fieldName)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean canNotUpdateExist(String tableName, String fieldName) {
		if(!containsTable(tableName)) {
			return false;
		}
		
		TableFieldMetaConfig tfc = allConfigMap.get(tableName);
		List<String> list = tfc.getListNotUpdate();
		if(null != list && list.contains(fieldName)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean canNotQueryExist(String tableName, String fieldName) {
		if(!containsTable(tableName)) {
			return false;
		}
		
		TableFieldMetaConfig tfc = allConfigMap.get(tableName);
		List<String> list = tfc.getListNotQuery();
		if(null != list && list.contains(fieldName)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean getLike(String tableName, String fieldName) {
		if(!containsTable(tableName)) {
			return false;
		}
		
		TableFieldMetaConfig tfc = allConfigMap.get(tableName);
		List<String> list = tfc.getListQueryLikeFiled();
		
		if(null != list && list.contains(fieldName)) {
			return true;
		}
		
		return false;
	}
	
	public static void addTableFieldConfig(Map<String, TableFieldMetaConfig> map) {
		allConfigMap.clear();
		
		allConfigMap.putAll(map);
	}
	
	public static void addTableToEntityConfg(Map<String, String> map) {
		tableToEntityMap.clear();
		
		tableToEntityMap.putAll(map);
	}
	
	public static void addActionConfig(Map<String, ActionMetaConfig> map) {
		actionConfigMap.clear();
		
		System.out.println(map.size());
		actionConfigMap.putAll(map);
	}
	
	public static Map<String, ActionMetaConfig> getActionConfigMap() {
		return actionConfigMap;
	}
	
	public static void setConfig(String p1,  String p2, String p3, String p4, 
			String p5, String p6, String p7, String p8, 
			String p9, String p10
			) {
		entityTemplatePath = p1;
		entityTemplateFileName = p2;
		entityOutputPath = p3;
		packageName = p4;
		
		mapperTemplatePath = p5;
		mapperTemplateFileName = p6;
		mapperOutputPath = p7;
		nameSpace = p8;
		
		actionTemplateFilePath = p9;
		actionOutputPath = p10;
	}
	
	public static Map<String, TableFieldMetaConfig> getAllConfigMap() {
		return allConfigMap;
	}
	
	public static String getNameSpace() {
		return nameSpace;
	}

	public static void setNameSpace(String nameSpace) {
		GeneratorConfig.nameSpace = nameSpace;
	}

	public static void print() {
		System.out.println(String.format("%s = %s", "mapperTemplatePath", mapperTemplatePath));
		System.out.println(String.format("%s = %s", "mapperTemplateFileName", mapperTemplateFileName));
		System.out.println(String.format("%s = %s", "mapperOutputPath", mapperOutputPath));
		System.out.println(String.format("%s = %s", "nameSpace", nameSpace));
		
		System.out.println(String.format("%s = %s", "entityTemplatePath", entityTemplatePath));
		System.out.println(String.format("%s = %s", "entityTemplateFileName", entityTemplateFileName));
		System.out.println(String.format("%s = %s", "entityOutputPath", entityOutputPath));
		System.out.println(String.format("%s = %s", "packageName", packageName));
		
		for(Map.Entry<String, TableFieldMetaConfig> entry : allConfigMap.entrySet()) {
			System.out.println(entry.getKey());
			printTableFieldConfig(entry.getValue());
			System.out.println("------");
		}
	}
	
	private static void printTableFieldConfig(TableFieldMetaConfig tfc) {
		System.out.println(String.format("\t%s = %s", "getEntityName", tfc.getEntityName()));
		System.out.println(String.format("\t%s = %s", "getBizFieldName", tfc.getBizFieldName()));
		System.out.println(String.format("\t%s = %s", "getBizPropertyName", tfc.getBizPropertyName()));
		
		printList("insert", tfc.getListNotInsert());
		printList("update", tfc.getListNotUpdate());
		printList("query", tfc.getListNotQuery());
	}
	
	private static void printList(String type, List<String> list) {
		System.out.println("*** " + type +" ***");
		for(String item : list) {
			System.out.println(item);
		}
	}

	public static String getPackageName() {
		return packageName;
	}

	public static void setPackageName(String packageName) {
		GeneratorConfig.packageName = packageName;
	}

	public static String getMapperOutputPath() {
		return mapperOutputPath;
	}

	public static void setMapperOutputPath(String mapperOutputPath) {
		GeneratorConfig.mapperOutputPath = mapperOutputPath;
	}

	public static String getEntityOutputPath() {
		return entityOutputPath;
	}

	public static void setEntityOutputPath(String entityOutputPath) {
		GeneratorConfig.entityOutputPath = entityOutputPath;
	}

	public static String getEntityTemplateFileName() {
		return entityTemplateFileName;
	}

	public static void setEntityTemplateFileName(String entityTemplateFileName) {
		GeneratorConfig.entityTemplateFileName = entityTemplateFileName;
	}

	public static String getMapperTemplateFileName() {
		return mapperTemplateFileName;
	}

	public static void setMapperTemplateFileName(String mapperTemplateFileName) {
		GeneratorConfig.mapperTemplateFileName = mapperTemplateFileName;
	}

	public static String getActionOutputPath() {
		return actionOutputPath;
	}

	public static void setActionOutputPath(String actionOutputPath) {
		GeneratorConfig.actionOutputPath = actionOutputPath;
	}

	public static String getActionTemplateFilePath() {
		return actionTemplateFilePath;
	}

	public static void setActionTemplateFilePath(String actionTemplateFilePath) {
		GeneratorConfig.actionTemplateFilePath = actionTemplateFilePath;
	}

	public static String getMapperTemplatePath() {
		return mapperTemplatePath;
	}

	public static void setMapperTemplatePath(String mapperTemplatePath) {
		GeneratorConfig.mapperTemplatePath = mapperTemplatePath;
	}

	public static String getEntityTemplatePath() {
		return entityTemplatePath;
	}

	public static void setEntityTemplatePath(String entityTemplatePath) {
		GeneratorConfig.entityTemplatePath = entityTemplatePath;
	}

	public static String getSchemaPattern() {
		return schemaPattern;
	}

	public static void setSchemaPattern(String schemaPattern) {
		GeneratorConfig.schemaPattern = schemaPattern;
	}
}
