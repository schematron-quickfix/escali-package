package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T06MatchingForeignNamespace extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test06-matching-foreign-namespace";
	}


	@Override
	public String getSchemaPath() {
		return "input/matching-foreign-namespace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/matching-foreign-namespace.xml";
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
		doTest(new ExecutionSetup(1, "replaceEl"));
	}

}
