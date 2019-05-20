package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T02GroupAndVariable extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test02-group-and-variable";
	}


	@Override
	public String getSchemaPath() {
		return "input/group-and-variable.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/group-and-variable.xml";
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
		doTest(new ExecutionSetup(1, "addBar"));
		
	}
	
}
