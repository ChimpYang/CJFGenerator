package org.cjf.util.generator.test;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.JVM)
//@ContextConfiguration("classpath:resources/template/app4mysql5.xml")
@ContextConfiguration("classpath:resources/template/app4mssql2012.xml")
public class TestJDBCMeta {
	
	@Resource
	private DataSource dataSource;
	
	@Test
	public void test001() throws SQLException {
		DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
		
		String catalog = dataSource.getConnection().getCatalog();
		//sqlserver->dbo
		//mysql->root
		ResultSet rs = metaData.getTables(catalog, "dbo", null, new String[]{"TABLE"});
		while(rs.next()) {
		   showTableColumns(metaData, catalog, rs.getString("TABLE_NAME"));
		}
	}
	
	private static void showTableColumns(DatabaseMetaData metaData, String catalog, String table) throws SQLException {
		System.out.println("*** " + table + " ***");
		//sqlserver->dbo
		//mysql->root
		ResultSet rs = metaData.getColumns(null, "dbo", table, null);
        while(rs.next()){  
            String columnName = rs.getString("COLUMN_NAME");
            String dataType  = rs.getString("DATA_TYPE");
            String typeName = rs.getString("TYPE_NAME");
            
            System.out.println(String.format("%s, %s, %s", columnName, dataType, typeName));
        }  
	}
}
