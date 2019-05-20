package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T46UseForEachCallFix extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test46-use-for-each-call-fix";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-call-fix.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-call-fix.xml";
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
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	
}
