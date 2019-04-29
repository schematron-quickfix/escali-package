package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T08AddMultipleAttributes extends FixingTestBaseChangePi {



    @Override
	public String getFolder() {
		return "test08-add-multiple-attributes";
	}


	@Override
	public String getSchemaPath() {
		return "input/add-multiple-attributes.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/add-multiple-attributes.xml";
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
		doTest(new ExecutionSetup(1, "addAttr"));
	}

}
