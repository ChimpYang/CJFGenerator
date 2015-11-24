package org.cjf.util.generator.run;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.cjf.util.generator.GeneratorConfig;
import org.cjf.util.generator.mapper.FieldMeta;
import org.cjf.util.generator.mapper.TableFieldMetaConfig;
import org.cjf.util.generator.test.TestVelocity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunGeneratorMapper {
	
	public static void main(String[] args) throws SQLException {
		RunGeneratorMapper generator = new RunGeneratorMapper();
		System.out.println("********* 处理开始 ********");
		generator.process();
		System.out.println("********* 处理完成 ********");
		
	}
	
	public RunGeneratorMapper() {
		
	}
	
	@SuppressWarnings("resource")
	private void process() throws SQLException {
		ApplicationContext context = new ClassPathXmlApplicationContext("resources/template/generatorApp.xml");
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		
		processAll(dataSource);
	}

	List<FieldMeta> fieldMetaList = null;
	
	private void processAll(DataSource dataSource) throws SQLException {
		DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
		String catalog = dataSource.getConnection().getCatalog();
		ResultSet rs = metaData.getTables(catalog, GeneratorConfig.getSchemaPattern(), null, new String[]{"TABLE"});
		
		String tableName = "";
		while(rs.next()) {
			tableName = rs.getString("TABLE_NAME");
			
			if(!GeneratorConfig.containsTable(tableName)) {
				System.out.println(String.format("--- 忽略 %s", tableName));
				continue;
			}
			
			System.out.println(String.format("*** 处理 %s", tableName));
			processOneTable(dataSource, metaData, catalog, rs.getString("TABLE_NAME"));
		}
	}

	private void processOneTable(DataSource dataSource, DatabaseMetaData metaData, String catalog, String table) throws SQLException {
		ResultSet rs = metaData.getColumns(null, GeneratorConfig.getSchemaPattern(), table, null);
		String columnName = "";
        String typeName = "";
        
        fieldMetaList = new ArrayList<FieldMeta>();
        FieldMeta item = null;
        while(rs.next()){  
            columnName = rs.getString("COLUMN_NAME");
            typeName = rs.getString("TYPE_NAME");
            
            System.out.println(columnName + ", " + typeName + ", " + GeneratorConfig.canNotUpdateExist(table, columnName) + ", " + getConditinType(table, columnName, typeName));
            
            item = new FieldMeta();
            item.setConditionType(getConditinType(table, columnName, typeName));
            item.setFieldName(columnName);
            item.setPropertyName(columnName);
            item.setCanInsert(!GeneratorConfig.canNotInsertExist(table, columnName));
            item.setCanUpdate(!GeneratorConfig.canNotUpdateExist(table, columnName));
            item.setCanQuery(!GeneratorConfig.canNotQueryExist(table, columnName));
            fieldMetaList.add(item);
        } 
        
        processMapper(table, fieldMetaList);
	}
	
	private void processMapper(String table, List<FieldMeta> fieldMetaList2) {
		TableFieldMetaConfig tfc = GeneratorConfig.getTableFieldMetaConfig(table);
		
		String fileDir = TestVelocity.class.getResource(GeneratorConfig.getMapperTemplatePath()).getPath();
		
		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8"); //important
		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
		
		VelocityEngine ve = new VelocityEngine();
		ve.init(properties);
        Template t = ve.getTemplate( GeneratorConfig.getMapperTemplateFileName() );
        
        boolean hasLookupField = true;
        if(null == tfc || null == tfc.getListLookup() || 0 == tfc.getListLookup().size()) {
        	hasLookupField = false;
        }
        
        VelocityContext context = new VelocityContext();  
        context.put("NAMESPACE", GeneratorConfig.getNameSpace());
        context.put("ENTITYNAME", tfc.getEntityName());
        context.put("TABLENAME", table);
        context.put("BIZFILEDNAME", tfc.getBizFieldName());  
        context.put("BIZPROPERTYNAME", tfc.getBizPropertyName());  
        context.put("fieldMetaList", fieldMetaList);  
        context.put("relationList", tfc.getRelationList());
        context.put("customUpdateList", tfc.getCustomUpdateList());
        context.put("hasLookupField", hasLookupField);
        if(hasLookupField) {
        	context.put("lookupList", tfc.getListLookup());
        }
        context.put("MapperVersion", GeneratorConfig.getVersion("MapperVersion"));
        
        StringWriter writer = new StringWriter();  
        t.merge( context, writer );  
        
        SaveMapperFile(writer.toString(), GeneratorConfig.getMapperOutputPath(), tfc.getEntityName());
	}

	private void SaveMapperFile(String content, String outputPath, String entityName) {
		FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String path = String.format("%s/%sMapper.xml", outputPath, entityName);
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

	private static String getConditinType(String table, String columnName, String typeName) {
		String type = typeName.toUpperCase();
		String result = "";
		if(type.startsWith("INT")) {
			result = FieldMeta.PT_INT_EQUAL;
		} else if(type.indexOf("CHAR") != -1) {
			
			if(GeneratorConfig.getLike(table, columnName)) {
				result = FieldMeta.PT_STR_LIKE;
			} else {
				result = FieldMeta.PT_STR_EQUAL;
			}
		} else {
			result = FieldMeta.PT_OBJ_EQUAL;
		}
		
		return result;
	}
}
