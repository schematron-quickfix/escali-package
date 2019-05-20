package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T48NsTarget extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test48-ns-target";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-target.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-target.xml";
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
