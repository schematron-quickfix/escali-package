package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T28ForeignLetInFix extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test28-foreign-let-in-fix";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-let-in-fix.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-let-in-fix.xml";
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
