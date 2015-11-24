package org.cjf.util.generator.test;

import org.cjf.util.generator.GeneratorConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.JVM)
@ContextConfiguration("classpath:resources/template/generatorApp.xml")
public class TestGetGeneratorConfig {
	
	@Test
	public void testPrint() {
		GeneratorConfig.print();
	}
	
}
