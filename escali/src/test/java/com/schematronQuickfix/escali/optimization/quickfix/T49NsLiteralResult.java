package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T49NsLiteralResult extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test49-ns-literal-result";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-literal-result.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-literal-result.xml";
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
		doTest(new ExecutionSetup(1, "replaceBar"));
	}
	
}
