package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T39CallFixMultiCalls extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test39-call-fix-multi-calls";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-multi-calls.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-multi-calls.xml";
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
		doTest(new ExecutionSetup(1, "addChilds"));
		
	}
	
}
