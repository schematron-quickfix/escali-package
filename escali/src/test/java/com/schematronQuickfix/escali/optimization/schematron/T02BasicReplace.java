package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T02BasicReplace extends ValidationTestBase {



    @Override
	public String getFolder() {
		return "../test02-basic-replace";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-replace.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/basic-replace.xml";
	}

	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setInternalValidation(false);
		return config;
	}

	@Override
	public String getFormat() {
		return SVRLReport.ESCALI_FORMAT;
	}

	@Test
	public void test(){
		doTest();
		
	}
	
}
