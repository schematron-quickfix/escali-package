package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T31ForeignXslForEach extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test31-foreign-xsl-for-each";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-xsl-for-each.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-xsl-for-each.xml";
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
