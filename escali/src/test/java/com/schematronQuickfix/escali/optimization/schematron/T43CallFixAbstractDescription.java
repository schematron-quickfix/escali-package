package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T43CallFixAbstractDescription extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test43-call-fix-abstract-description";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-abstract-description.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-abstract-description.xml";
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
