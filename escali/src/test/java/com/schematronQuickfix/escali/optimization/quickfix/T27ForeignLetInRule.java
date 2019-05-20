package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T27ForeignLetInRule extends FixingTestBase {


	@Override
	public String getFolder() {
		return "../test27-foreign-let-in-rule";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-let-in-rule.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-let-in-rule.xml";
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
		doTest(new ExecutionSetup(1, "addBarId"));
		
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "addBarId"));

	}
	
}
