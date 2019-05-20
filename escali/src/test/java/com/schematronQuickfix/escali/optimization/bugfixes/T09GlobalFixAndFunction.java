package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T09GlobalFixAndFunction extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test09-global-fix-and-function";
	}


	@Override
	public String getSchemaPath() {
		return "input/global-fix-and-function.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/global-fix-and-function.xml";
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
	public void test_1(){
		doTest(new ExecutionSetup(1, "replaceBar"));
	}

}
