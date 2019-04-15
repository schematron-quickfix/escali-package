package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T45UseForEachLet extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test45-use-for-each-let";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-let.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-let.xml";
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
	public void test_1(){
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	
}
