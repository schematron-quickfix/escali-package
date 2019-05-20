package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T51NsTriggersNamespaceDecl extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test51-ns-triggers-namespace-decl";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-triggers-namespace-decl.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-triggers-namespace-decl.xml";
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
