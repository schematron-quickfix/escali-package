package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T01BasicDelete extends FixingTestBase {



    @Override
	public String getFolder() {
		return "../test01-basic-delete";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-delete.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-delete.xml";
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
		doTest(new ExecutionSetup(1, "deleteFoo"));
		
	}
	
}
