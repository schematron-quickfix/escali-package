package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T04BasicStringReplace extends ValidationTestBase {



    @Override
	public String getFolder() {
		return "../test04-basic-stringReplace";
	}


	@Override
	public String getSchemaPath() {
		return "input/basic-stringReplace.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/basic-stringReplace.xml";
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
