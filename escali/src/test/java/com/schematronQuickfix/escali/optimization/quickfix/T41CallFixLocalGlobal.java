package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T41CallFixLocalGlobal extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test41-call-fix-local-global";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-local-global.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-local-global.xml";
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
		doTest(new ExecutionSetup(1, "addLastChilds"));
		
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBaz"));

	}

}
