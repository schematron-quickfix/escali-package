package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T44UseForEachGlobal extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test44-use-for-each-global";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-global.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-global.xml";
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
