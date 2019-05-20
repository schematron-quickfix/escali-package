package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T18NodeCreationPositionLastChild extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test18-node-creation-position-last-child";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-position-last-child.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/node-creation-position-last-child.xml";
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
	public void test(){
		doTest(new ExecutionSetup(1, "addBaz"));
		
	}
	
}
