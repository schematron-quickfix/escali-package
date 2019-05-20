package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T10BasicMatch extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test10-basic-match";
	}

	@Override
	public String getSchemaPath() {
		return "input/basic-match.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/basic-match.xml";
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
