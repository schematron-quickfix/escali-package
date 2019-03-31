package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T10BasicMatch extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test10-basic-match";
	}

	@Override
	public String getSchemaPath() {
		return "input/basic-match.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-match.xml";
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
		doTest(new ExecutionSetup(1, "deleteAtt"));
		
	}
	
}
