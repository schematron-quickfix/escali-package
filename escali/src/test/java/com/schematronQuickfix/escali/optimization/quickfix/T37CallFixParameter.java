package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T37CallFixParameter extends FixingTestBase {


	@Override
	public String getFolder() {
		return "../test37-call-fix-parameter";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-parameter.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-parameter.xml";
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
		doTest(new ExecutionSetup(1, "replaceFoo"));
		
	}
	
}
