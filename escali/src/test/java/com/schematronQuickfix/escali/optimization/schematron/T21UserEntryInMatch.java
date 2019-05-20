package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T21UserEntryInMatch extends ValidationTestBase {


	@Override
	public String getFolder() {
		return "../test21-user-entry-in-match";
	}


	@Override
	public String getSchemaPath() {
		return "input/user-entry-in-match.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/user-entry-in-match.xml";
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
