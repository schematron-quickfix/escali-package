package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T04EsImportTest extends FixingTestBase {



	@Override
	public String getFolder() {
		return "test04-es-import";
	}

	@Override
	public String getInstancePath() {
		return "input/es-import.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-import.sch";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setPhase("#ALL");
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "pattern1Fix"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(4, "importPatternFix"));
	}


	
}
