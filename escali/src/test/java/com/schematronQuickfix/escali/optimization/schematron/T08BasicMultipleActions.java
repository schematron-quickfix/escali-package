package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T08BasicMultipleActions extends ValidationTestBase {



    @Override
	public String getFolder() {
		return "../test08-basic-multiple-actions";
	}

	@Override
	public String getSchemaPath() {
		return "input/basic-multiple-actions.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-multiple-actions.xml";
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
