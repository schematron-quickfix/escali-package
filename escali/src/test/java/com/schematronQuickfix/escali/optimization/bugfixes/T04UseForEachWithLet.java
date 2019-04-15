package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T04UseForEachWithLet extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test04-use-for-each-with-let";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-with-let.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-with-let.xml";
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
		doTest(new ExecutionSetup(1, "useFoo#2"));
	}
	
}
