package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T08ReplaceAttributeConflict extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test08-replace-attribute-conflict";
	}


	@Override
	public String getSchemaPath() {
		return "input/replace-attribute-conflict.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/replace-attribute-conflict.xml";
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
		doTest(new ExecutionSetup(1, "replaceBarBaz"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addFoo"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "addAndReplace"));
	}
	
}
