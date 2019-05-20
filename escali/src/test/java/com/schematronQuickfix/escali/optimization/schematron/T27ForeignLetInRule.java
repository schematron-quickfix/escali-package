package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T27ForeignLetInRule extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test27-foreign-let-in-rule";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-let-in-rule.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-let-in-rule.xml";
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
