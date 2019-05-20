package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T14NodeCreationNodeTypeAttribute extends ValidationTestBase {


	@Override
	public String getFolder() {
		return "../test14-node-creation-node-type-attribute";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-node-type-attribute.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/node-creation-node-type-attribute.xml";
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
