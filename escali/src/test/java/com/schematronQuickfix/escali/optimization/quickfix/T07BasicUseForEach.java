package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T07BasicUseForEach extends FixingTestBase {




	@Override
	public String getFolder() {
		return "../test07-basic-use-for-each";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-use-for-each.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/basic-use-for-each.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setInternalValidation(false);
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	
}
