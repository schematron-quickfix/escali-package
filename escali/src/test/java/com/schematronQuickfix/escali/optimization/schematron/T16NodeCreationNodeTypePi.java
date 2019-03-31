package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T16NodeCreationNodeTypePi extends ValidationTestBase {


	@Override
	public String getFolder() {
		return "../test16-node-creation-node-type-pi";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-node-type-pi.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/node-creation-node-type-pi.xml";
	}

	@Override
	public String getFormat() {
		return SVRLReport.ESCALI_FORMAT;
	}

	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setInternalValidation(false);
		return config;
	}

	@Test
	public void test(){
		doTest();
		
	}
	
}
