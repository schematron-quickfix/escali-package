package com.schematronQuickfix.escali.quickfix.standalone;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T06StandaloneGlobalVariables extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test06-standalone-global-variables";
	}


	@Test
	public void test_1_first_standalone(){
		doTest(new ExecutionSetup(1, "first_standalone"));
	}

	@Override
	public String getSchemaPath() {
		return "input/fixes.sqf";
	}
}
