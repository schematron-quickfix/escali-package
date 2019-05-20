package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T44UseForEachGlobal extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test44-use-for-each-global";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-global.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-global.xml";
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
