package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T26ForeignValueOfActionEl extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test26-foreign-value-of-actionEl";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-value-of-actionEl.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-value-of-actionEl.xml";
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