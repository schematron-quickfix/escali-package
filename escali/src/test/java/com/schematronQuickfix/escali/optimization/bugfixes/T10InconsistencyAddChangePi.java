package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T10InconsistencyAddChangePi extends FixingTestBaseChangePi {



    @Override
	public String getFolder() {
		return "test10-inconsistency-add-changePi";
	}


	@Override
	public String getSchemaPath() {
		return "input/inconsistency-add-changePi.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/inconsistency-add-changePi.xml";
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
		doTest(new ExecutionSetup(1, "addAttr"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addAttrBar"));
	}

}
