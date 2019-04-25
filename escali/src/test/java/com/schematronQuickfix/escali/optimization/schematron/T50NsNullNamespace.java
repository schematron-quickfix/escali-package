package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T50NsNullNamespace extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test50-ns-null-namespace";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-null-namespace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-null-namespace.xml";
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
