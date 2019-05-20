package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T49NsLiteralResult extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test49-ns-literal-result";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-literal-result.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-literal-result.xml";
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
