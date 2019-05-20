package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T50NsNullNamespace extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test50-ns-null-namespace";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-null-namespace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-null-namespace.xml";
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
