package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T46UseForEachCallFix extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test46-use-for-each-call-fix";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-call-fix.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-call-fix.xml";
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
