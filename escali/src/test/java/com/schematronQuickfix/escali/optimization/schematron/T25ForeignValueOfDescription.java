package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T25ForeignValueOfDescription extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test25-foreign-value-of-description";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-value-of-description.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-value-of-description.xml";
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
