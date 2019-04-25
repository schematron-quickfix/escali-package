package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T53NsDefaultNamespaceinSch extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test53-ns-default-namespace-in-sch";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-default-namespace-in-sch.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-default-namespace-in-sch.xml";
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
		doTest(new ExecutionSetup(1, "replaceBar"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "replaceBarTarget"));
	}
	
}
