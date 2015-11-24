package org.cjf.util.generator.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.cjf.util.generator.mapper.CustomUpdateMeta;
import org.cjf.util.generator.mapper.FieldMeta;
import org.cjf.util.generator.mapper.RelationMeta;
import org.junit.Test;

public class TestVelocity {
	
//	@Test
//	public void testVM1() {
//		String fileDir = TestVelocity.class.getResource("/resources/template").getPath();
//		System.out.println(fileDir);
//		
//		Properties properties = new Properties();
//		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir); //此处的fileDir可以直接用绝对路径来
//		
//		VelocityEngine ve = new VelocityEngine();
//		ve.init(properties);
//        Template t = ve.getTemplate( "test1.vm" ); 
//		
//		//Unable to find resource
////		VelocityEngine ve = new VelocityEngine();
////		ve.init();
////		Template t = ve.getTemplate("resources/template/test1.vm");
//        
//        VelocityContext context = new VelocityContext();  
//        context.put("name", "Chimp");  
//        context.put("site", "http://www.chimp.com");  
//        
//        StringWriter writer = new StringWriter();  
//        t.merge( context, writer );  
//        System.out.println( writer.toString() );  
//	}
//	
//	@Test
//	public void testVM2() {
//		String fileDir = TestVelocity.class.getResource("/resources/template").getPath();
//		
//		Properties properties = new Properties();
//		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
//		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8"); //important
//		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
//		
//		VelocityEngine ve = new VelocityEngine();
//		ve.init(properties);
//        Template t = ve.getTemplate( "test2.vm" ); 
//        
//        VelocityContext context = new VelocityContext();  
//        List<User> list = getDemoUsers();
//        
//        context.put("list", list);  
//        
//        StringWriter writer = new StringWriter();  
//        t.merge( context, writer );  
//        System.out.println( writer.toString() );  
//	}
//	
//	private static List<User> getDemoUsers() {
//		List<User> list = new ArrayList<User>();
//		int count = 3;
//		User item = null;
//		
//		for(int i=0;i<count;i++) {
//			item = new User();
//			item.setUserCode("code" + i);
//			item.setUserName("name" + i);
//			list.add(item);
//		}
//		
//		return list;
//	}
//	
//	@Test
//	public void testVM3() {
//		String fileDir = TestVelocity.class.getResource("/resources/template").getPath();
//		
//		Properties properties = new Properties();
//		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
//		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8"); //important
//		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
//		
//		VelocityEngine ve = new VelocityEngine();
//		ve.init(properties);
//        Template t = ve.getTemplate( "test3.vm" ); 
//        
//        VelocityContext context = new VelocityContext();  
//        
//        context.put("mapperId", "count");  
//        context.put("parameterType", "User");  
//        context.put("resultType", "int");  
//        context.put("tableName", "CJF_USER");  
//        context.put("fieldName", "userCode");  
//        context.put("propertyName", "userCode");  
//        
//        StringWriter writer = new StringWriter();  
//        t.merge( context, writer );  
//        System.out.println( writer.toString() );  
//	}
	
	@Test
	public void testVMMapper() {
		String fileDir = TestVelocity.class.getResource("/resources/template").getPath();
		
		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8"); //important
		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
		
		VelocityEngine ve = new VelocityEngine();
		ve.init(properties);
        Template t = ve.getTemplate( "mapper.vm" ); 
        
        List<FieldMeta> fieldMetaList = new ArrayList<FieldMeta>();
        FieldMeta item = null;
        
        item = new FieldMeta();
        item.setConditionType(FieldMeta.PT_INT_EQUAL);
        item.setFieldName("ID");
        item.setPropertyName("id");
        item.setCanInsert(false);
        item.setCanUpdate(false);
        fieldMetaList.add(item);
        
        item = new FieldMeta();
        item.setConditionType(FieldMeta.PT_STR_EQUAL);
        item.setFieldName("userCode");
        item.setPropertyName("userCode");
        item.setCanUpdate(false);
        fieldMetaList.add(item);
        
        item = new FieldMeta();
        item.setConditionType(FieldMeta.PT_STR_EQUAL);
        item.setFieldName("password");
        item.setPropertyName("password");
        item.setCanUpdate(false);
        item.setCanQuery(false);
        fieldMetaList.add(item);
        
        item = new FieldMeta();
        item.setConditionType(FieldMeta.PT_STR_LIKE);
        item.setFieldName("userName");
        item.setPropertyName("userName");
        fieldMetaList.add(item);
        
        item = new FieldMeta();
        item.setConditionType(FieldMeta.PT_OBJ_EQUAL);
        item.setFieldName("createDate");
        item.setPropertyName("CREATE_DATE");
        item.setCanQuery(false);
        fieldMetaList.add(item);
        
        item = new FieldMeta();
        item.setConditionType(FieldMeta.PT_INT_EQUAL);
        item.setFieldName("userDisabled");
        item.setPropertyName("userDisabled");
        item.setCanUpdate(false);
        fieldMetaList.add(item);
        
        List<RelationMeta> rmList = new ArrayList<RelationMeta>();
        RelationMeta rm = null;
        
        rm = new RelationMeta();
        rm.setRelationName("ROLEUSER");
        rm.setRelationFiledName("userCode");
        rm.setRelationTableName("CJF_ROLEUSER");
        rmList.add(rm);
        
        rm = new RelationMeta();
        rm.setRelationName("EMPUSER");
        rm.setRelationFiledName("USER_CODE");
        rm.setRelationTableName("CJF_EMPUSER");
        rmList.add(rm);
        
        List<CustomUpdateMeta> cumList = new ArrayList<CustomUpdateMeta>();
        CustomUpdateMeta cum = null;
        
        cum = new CustomUpdateMeta();
        cum.setUpdateName("UserCode");
        cum.setFieldName("userCode");
        cum.setPropertyName("userCode");
        cumList.add(cum);
        
        cum = new CustomUpdateMeta();
        cum.setUpdateName("Password");
        cum.setFieldName("password");
        cum.setPropertyName("password");
        cumList.add(cum);
        
        cum = new CustomUpdateMeta();
        cum.setUpdateName("UserDisabled");
        cum.setFieldName("userDisabled");
        cum.setPropertyName("userDisabled");
        cumList.add(cum);
        
        VelocityContext context = new VelocityContext();  
        context.put("NAMESPACE", "org.cjf.entity.mysql5");
        context.put("ENTITYNAME", "User");
        context.put("TABLENAME", "CJF_USER");
        context.put("BIZFILEDNAME", "userCode");
        context.put("BIZPROPERTYNAME", "userCode"); 
        context.put("fieldMetaList", fieldMetaList);
        context.put("relationList", rmList);
        context.put("customUpdateList", cumList);
        
        StringWriter writer = new StringWriter();  
        t.merge( context, writer );  
        System.out.println( writer.toString() );  
	}
}
