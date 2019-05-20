package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T03BasicAdd extends FixingTestBase {



    @Override
	public String getFolder() {
		return "../test03-basic-add";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-add.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-add.xml";
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
		doTest(new ExecutionSetup(1, "addBar"));
		
	}
	
}
