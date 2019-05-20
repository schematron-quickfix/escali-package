package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T17NodeCreationPositionFirstChild extends ValidationTestBase {



    @Override
	public String getFolder() {
		return "../test17-node-creation-position-first-child";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-position-first-child.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/node-creation-position-first-child.xml";
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
