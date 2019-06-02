package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T03SchIncludePerIdTest extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test03-sch-include-per-id";
	}


	@Override
	public String getSchemaPath() {
		return "input/sch/sch-include-per-id.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/xml/sch-include-per-id.xml";
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
