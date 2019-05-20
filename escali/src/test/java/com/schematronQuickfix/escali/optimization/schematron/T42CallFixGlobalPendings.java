package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T42CallFixGlobalPendings extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test42-call-fix-global-pendings";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-global-pendings.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-global-pendings.xml";
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
