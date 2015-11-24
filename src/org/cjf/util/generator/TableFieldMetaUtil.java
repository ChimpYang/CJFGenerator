package org.cjf.util.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.cjf.util.generator.entity.PropertyMeta;

public class TableFieldMetaUtil {
	
	private Map<String, ArrayList<PropertyMeta>> tabelFieldMetaMap = new HashMap<String, ArrayList<PropertyMeta>>();
	public Map<String, ArrayList<PropertyMeta>> databaseMeta2Map(DataSource dataSource) throws SQLException {
		Connection connection = null;
		
		try {
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			String catalog = dataSource.getConnection().getCatalog();
			ResultSet rs = metaData.getTables(catalog, GeneratorConfig.getSchemaPattern(), null, new String[]{"TABLE"});
			
			String tableName = "";
			while(rs.next()) {
				tableName = rs.getString("TABLE_NAME");
				
				if(!GeneratorConfig.containsTableToEntity(tableName)) {
					continue;
				}
				
				oneTableMeta2Map(dataSource, metaData, rs.getString("TABLE_NAME"));
			}
		} finally {
			connection.close();
		}
		
		return tabelFieldMetaMap;
	}
	
	private void oneTableMeta2Map(DataSource dataSource, DatabaseMetaData metaData, String table) throws SQLException {
		ResultSet rs = metaData.getColumns(null, GeneratorConfig.getSchemaPattern(), table, null);
		String columnName = "";
        String typeName = "";
        
        ArrayList<PropertyMeta> list = new ArrayList<PropertyMeta>();
        PropertyMeta item = null;
        while(rs.next()){  
            columnName = rs.getString("COLUMN_NAME");
            typeName = rs.getString("TYPE_NAME");
            
            item = new PropertyMeta();
            item.setPropertyType(getType(typeName));
            item.setPropertyFunctionName(getFName(columnName));
            item.setPropertyName(columnName);
            
            list.add(item);
        } 
        
        tabelFieldMetaMap.put(table, list);
	}
	
	public static String getType(String typeName) {
		String type = typeName.toUpperCase();
		String result = "";
		if(type.indexOf("INT") != -1) {
			result = "int";
		} else if(type.indexOf("CHAR") != -1 || type.indexOf("TEXT") != -1) {
			result = "String";
		} else if(type.indexOf("DOUBLE") != -1) {
			result = "double";
		} else if(type.indexOf("DATE") != -1 || type.indexOf("TIME") != -1) {
			result = "Date";
		} else {
			System.out.println(typeName);
			System.out.println("--- un expect field type.");
		}
		
		return result;
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
}
