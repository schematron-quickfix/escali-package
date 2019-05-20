package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T45UseForEachLet extends ValidationTestBase {

	@Override
	public String getFolder() {
		return "../test45-use-for-each-let";
	}


	@Override
	public String getSchemaPath() {
		return "input/use-for-each-let.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/use-for-each-let.xml";
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
