package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T09BasicGlobalQuickfix extends ValidationTestBase {



    @Override
	public String getFolder() {
		return "../test09-basic-global-quickfix";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-global-quickfix.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/basic-global-quickfix.xml";
	}

	@Override
	public String getFormat() {
		return SVRLReport.ESCALI_FORMAT;
	}

	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		return config;
	}

	@Test
	public void test(){
		doTest();
		
	}
	
}
