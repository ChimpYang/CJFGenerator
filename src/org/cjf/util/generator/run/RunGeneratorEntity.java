package org.cjf.util.generator.run;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.cjf.util.generator.GeneratorConfig;
import org.cjf.util.generator.TableFieldMetaUtil;
import org.cjf.util.generator.entity.PropertyMeta;
import org.cjf.util.generator.mapper.LookupFieldMeta;
import org.cjf.util.generator.mapper.TableFieldMetaConfig;
import org.cjf.util.generator.test.TestVelocity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunGeneratorEntity {
	public static void main(String[] args) throws SQLException {
		RunGeneratorEntity generator = new RunGeneratorEntity();
		System.out.println("********* 处理开始 ********");
		generator.process();
		System.out.println("********* 处理完成 ********");
		
	}

	Map<String, ArrayList<PropertyMeta>> tabelFieldMetaMap = null;
	
	@SuppressWarnings("resource")
	private void process() throws SQLException {
		ApplicationContext context = new ClassPathXmlApplicationContext("resources/template/generatorApp.xml");
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		
		TableFieldMetaUtil tfmu = new TableFieldMetaUtil();
		tabelFieldMetaMap = tfmu.databaseMeta2Map(dataSource);
		processAll(dataSource);		
	}
	
	private void processAll(DataSource dataSource) throws SQLException {
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
					System.out.println(String.format("--- 忽略 %s", tableName));
					continue;
				}
				
				System.out.println(String.format("*** 处理 %s", tableName));
				processOneTable(dataSource, metaData, catalog, rs.getString("TABLE_NAME"));
			}	
		} finally {
			connection.close();
		}
	}

	private void processOneTable(DataSource dataSource, DatabaseMetaData metaData, String catalog, String table) throws SQLException {
		ResultSet rs = metaData.getColumns(null, GeneratorConfig.getSchemaPattern(), table, null);
		String columnName = "";
        String typeName = "";
        
        boolean hasDateField = false;
        List<PropertyMeta> list = new ArrayList<PropertyMeta>();
        PropertyMeta item = null;
        while(rs.next()){  
            columnName = rs.getString("COLUMN_NAME");
            typeName = rs.getString("TYPE_NAME");
            
            item = new PropertyMeta();
            item.setPropertyType(TableFieldMetaUtil.getType(typeName));
            item.setPropertyFunctionName(TableFieldMetaUtil.getFName(columnName));
            item.setPropertyName(columnName);
            if ("Date".equals(TableFieldMetaUtil.getType(typeName))) {
            	hasDateField = true;
            }
            list.add(item);
        } 
        
        processEntity(table, list, hasDateField);		
	}

	private void processEntity(String table, List<PropertyMeta> list, boolean hasDateField) {
		String fileDir = TestVelocity.class.getResource(GeneratorConfig.getEntityTemplatePath()).getPath();
		
		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
		
		VelocityEngine ve = new VelocityEngine();
		ve.init(properties);
        Template t = ve.getTemplate( GeneratorConfig.getEntityTemplateFileName() );
        
        TableFieldMetaConfig tfc = GeneratorConfig.getTableFieldMetaConfig(table);
        boolean hasLookupField = true;
        if(null == tfc || null == tfc.getListLookup() || 0 == tfc.getListLookup().size()) {
        	hasLookupField = false;
        }
        
        String entityName = GeneratorConfig.getEntityNameFromTableName(table);
        VelocityContext context = new VelocityContext();  
        context.put("PACKAGENAME", GeneratorConfig.getPackageName());
        context.put("ENTITYNAME", entityName);
        context.put("hasDateField", hasDateField);
        context.put("propertyList", list);
        context.put("hasLookupField", hasLookupField);
        if(hasLookupField) {
        	context.put("lookupList", transFiled2Entity(table, tfc.getListLookup()));
        }
        context.put("EntityVersion", GeneratorConfig.getVersion("EntityVersion"));
        
        StringWriter writer = new StringWriter();  
        t.merge( context, writer );  
        
        SaveEntityFile(writer.toString(), GeneratorConfig.getEntityOutputPath(), entityName);
	}

	private List<PropertyMeta> transFiled2Entity(String table, List<LookupFieldMeta> listLookup) {
		List<PropertyMeta> newList = new ArrayList<PropertyMeta>();
		if(null == listLookup) {
			return newList;
		}
		
		String temp = "";
		PropertyMeta item = null;
		for(LookupFieldMeta lfm : listLookup) {
			System.out.println(table + ", " + lfm.getLookupTable());
			for(String fieldName : lfm.getLookupFields()) {
				item = new PropertyMeta();
				item.setPropertyName(fieldName);
				
				temp = getTypeFromMap(lfm.getLookupTable(), fieldName);
				item.setPropertyType(temp);
				item.setPropertyFunctionName(TableFieldMetaUtil.getFName(fieldName));
				
				newList.add(item);
			}
		}//for
		
		return newList;
	}

	private String getTypeFromMap(String lookupTable, String fieldName) {
		String type = "String";
		if(!tabelFieldMetaMap.containsKey(lookupTable)) {
			return type;
		}		
		
		ArrayList<PropertyMeta> list = tabelFieldMetaMap.get(lookupTable);
		for(PropertyMeta item : list) {
			if(fieldName.equals(item.getPropertyName())) {
				type = item.getPropertyType();
				break;
			}
		}
		
		return type;
	}

	private void SaveEntityFile(String content, String outputPath, String entityName) {
		FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String path = String.format("%s/%s.java", outputPath, entityName);
        File filePath = null;
        	
        try {
            File file = new File(path);
            filePath = file.getParentFile();
            
            if( filePath != null && !filePath.exists()){ 
            	filePath.mkdirs(); 
            } 
            
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(content.getBytes("UTF-8"));
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            	fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }		
	}
}
