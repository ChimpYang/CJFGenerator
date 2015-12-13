package org.cjf.util.generator.run;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.cjf.util.generator.GeneratorConfig;
import org.cjf.util.generator.TableFieldMetaUtil;
import org.cjf.util.generator.action.ActionMetaConfig;
import org.cjf.util.generator.action.CustomModifyMeta;
import org.cjf.util.generator.entity.PropertyMeta;
import org.cjf.util.generator.mapper.CustomUpdateMeta;
import org.cjf.util.generator.mapper.TableFieldMetaConfig;
import org.cjf.util.generator.test.TestVelocity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunGeneratorAction {
	private static Logger log = Logger.getLogger(RunGeneratorAction.class);
	
	public static void main(String[] args) throws SQLException {
		RunGeneratorAction generator = new RunGeneratorAction();
		
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
		
		processAll();		
	}

	private void processAll() {
		//1. 得到需要生成Action的EntityList
		//2. 得到该Action需要处理的CustomUpdateField（在Mapper配置里）
		//3. 生成多个Action
		
		String tableName = "";
		ActionMetaConfig actionMetaCofing = null;
		Map<String, ActionMetaConfig> actionConfigMap = GeneratorConfig.getActionConfigMap();
		for(Map.Entry<String, ActionMetaConfig> entry : actionConfigMap.entrySet()) {
			actionMetaCofing = entry.getValue();
			tableName = actionMetaCofing.getTableName();
			
			log.info("customUpdate - tableName: " + tableName);
			
			List<CustomUpdateMeta> customUpdateList = getCustomUpdateList(tableName);
			processOneEntity(actionMetaCofing, customUpdateList);
		}
	}

	private void processOneEntity(ActionMetaConfig actionMetaCofing, List<CustomUpdateMeta> customUpdateList) {
		//一个一个处理
		ProcessOne("CreateAction", actionMetaCofing, null);
		ProcessOne("GetListAction", actionMetaCofing, null);
		ProcessOne("GetOneAction", actionMetaCofing, null);
		ProcessOne("ModifyAction", actionMetaCofing, null);
		ProcessOne("RemoveAction", actionMetaCofing, null);
		
		CustomModifyMeta cmm = null;
		String type = "";
		for(CustomUpdateMeta item : customUpdateList) {
			type = getTypeFromMap(actionMetaCofing.getTableName(), item.getFieldName());
			
			cmm = CustomModifyMeta.cloneFromCustomUpdateMeta(item);
			cmm.setPropertyType(type);
			
			ProcessOne("ModifyCustomAction", actionMetaCofing, cmm);
		}
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

	private void ProcessOne(String action, ActionMetaConfig actionMetaCofing, CustomModifyMeta cmm) {
		System.out.println("*** " + action);
		
		String fileDir = TestVelocity.class.getResource(GeneratorConfig.getActionTemplateFilePath()).getPath();
		
		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
		
		VelocityEngine ve = new VelocityEngine();
		ve.init(properties);
        Template t = ve.getTemplate( action + ".vm" );
        
        VelocityContext context = new VelocityContext();  
        context.put("EntityName", actionMetaCofing.getEntityName());
        context.put("ActionPackage", actionMetaCofing.getActionPackage());
        context.put("BizPackage", actionMetaCofing.getBizPackage());
        context.put("BizInterface", actionMetaCofing.getBizInterface());
        context.put("hasLastModifyDate", actionMetaCofing.isHasLastModifyDate());
        if(null != cmm) {
        	context.put("CustomStatementId", cmm.getCustomStatementId());
            context.put("PropertyFunctionName", cmm.getPropertyFunctionName());
            context.put("PropertyType", cmm.getPropertyType());
        }
        String versionTag = String.format("%sVersion", action);
        context.put(versionTag, GeneratorConfig.getVersion(versionTag));
        
        StringWriter writer = new StringWriter();  
        t.merge( context, writer );  
        
        String fileName = "";
        if(null == cmm) {
        	fileName = String.format("%s%s", actionMetaCofing.getEntityName(), action);
        } else {
        	fileName = String.format("%sModify%sAction", actionMetaCofing.getEntityName(), cmm.getPropertyFunctionName());
        }
        
        SaveActionFile(writer.toString(), GeneratorConfig.getActionOutputPath(), actionMetaCofing.getEntityName(), fileName);
	}

	private void SaveActionFile(String content, String outputPath, String entityName, String fileName) {
		FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String path = String.format("%s/%s/%s.java", outputPath, entityName, fileName);
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

	private List<CustomUpdateMeta> getCustomUpdateList(String table) {
		TableFieldMetaConfig tfc = GeneratorConfig.getTableFieldMetaConfig(table);
		if(null == tfc) {
			return null;
		}
		
		List<CustomUpdateMeta> list = tfc.getCustomUpdateList();
		return list;
	}
	
}
