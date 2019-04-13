package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T09BasicGlobalQuickfix extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test09-basic-global-quickfix";
	}

	@Override
	public String getSchemaPath() {
		return "input/basic-global-quickfix.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-global-quickfix.xml";
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
	public void test(){
		doTest(new ExecutionSetup(1, "deleteFoo"));
		
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "replaceFoo"));

	}
	
}
