package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T23UserEntryDefault extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test23-user-entry-default";
	}


	@Override
	public String getSchemaPath() {
		return "input/user-entry-default.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/user-entry-default.xml";
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
	public void test(){
		doTest(new ExecutionSetup(1, "removeBars"));
	}

}
