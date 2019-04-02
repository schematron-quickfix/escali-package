package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T22UserEntryTypes extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test22-user-entry-types";
	}


	@Override
	public String getSchemaPath() {
		return "input/user-entry-types.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/user-entry-types.xml";
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
		doTest(new ExecutionSetup(1, "removeBars")
				.addUE("barPos", "1")
				.addExecProp("SUFFIX", "_1")
		);
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "removeBars")
				.addUE("barPos", "3")
				.addExecProp("SUFFIX", "_3")
		);
	}
	
}
