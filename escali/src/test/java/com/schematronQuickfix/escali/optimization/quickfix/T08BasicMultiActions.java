package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T08BasicMultiActions extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test08-basic-multiple-actions";
	}

	@Override
	public String getSchemaPath() {
		return "input/basic-multiple-actions.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-multiple-actions.xml";
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
		doTest(new ExecutionSetup(1, "copyBaz"));
	}
	
}