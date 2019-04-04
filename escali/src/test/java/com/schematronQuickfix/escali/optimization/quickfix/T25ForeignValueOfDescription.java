package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T25ForeignValueOfDescription extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test25-foreign-value-of-description";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-value-of-description.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-value-of-description.xml";
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
	public void test(){
		doTest(new ExecutionSetup(1, "replaceFoo"));
		
	}
	
}
