package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T39CallFixMultiCalls extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test39-call-fix-multi-calls";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-multi-calls.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-multi-calls.xml";
	}



	@Override
	public Config getConfig() {
		Config config = super.getConfig();
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
