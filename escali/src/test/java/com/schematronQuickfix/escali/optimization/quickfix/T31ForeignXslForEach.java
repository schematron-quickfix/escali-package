package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T31ForeignXslForEach extends FixingTestBase {


	@Override
	public String getFolder() {
		return "../test31-foreign-xsl-for-each";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-xsl-for-each.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-xsl-for-each.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "replaceBar"));
		
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "replaceBar"));

	}
	
}
