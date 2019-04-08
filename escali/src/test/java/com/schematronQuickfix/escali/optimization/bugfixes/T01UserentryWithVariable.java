package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T01UserentryWithVariable extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test01-userentry-with-variable";
	}


	@Override
	public String getSchemaPath() {
		return "input/userentry-with-variable.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/userentry-with-variable.xml";
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
		doTest(new ExecutionSetup(1, "addBar")
				.addUE("element", "USERENTRY_element")
		);
		
	}
	
}
