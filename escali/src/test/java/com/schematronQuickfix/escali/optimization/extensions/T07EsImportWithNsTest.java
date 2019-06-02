package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T07EsImportWithNsTest extends FixingTestBase {



	@Override
	public String getFolder() {
		return "test07-es-import-with-ns";
	}

	@Override
	public String getInstancePath() {
		return "input/es-import-with-ns.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-import-with-ns.sch";
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
		doTest(new ExecutionSetup(1, "delete"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "delete"));
	}


	
}
