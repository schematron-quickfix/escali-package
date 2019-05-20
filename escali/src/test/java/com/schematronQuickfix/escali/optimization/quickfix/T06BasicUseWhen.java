package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T06BasicUseWhen extends FixingTestBase {




	@Override
	public String getFolder() {
		return "../test06-basic-use-when";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-use-when.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-use-when.xml";
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
		doTest(new ExecutionSetup(1, "addBar"));
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "addBar"));
	}
	
}
