package com.schematronQuickfix.escali.schematron;

import org.junit.Test;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;

public class T17ArcheExampleTest extends ValidationTestBase {
	
    @Override
	public String getFolder() {
		return "test17-arche-example";
	}
    
	
	@Test
	public void test(){
		Config config = ConfigFactory.createDefaultConfig();
		config.setCompactSVRL(true);
		doTest(config);
	}

	
	
}
