package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T40CallFixPendingCalls extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test40-call-fix-pending-calls";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-pending-calls.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-pending-calls.xml";
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
