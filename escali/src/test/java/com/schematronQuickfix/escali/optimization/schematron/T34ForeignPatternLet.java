package com.schematronQuickfix.escali.optimization.schematron;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T34ForeignPatternLet extends ValidationTestBase {



	@Override
	public String getFolder() {
		return "../test34-foreign-pattern-let";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-pattern-let.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-pattern-let.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setPhase("phase1");
		return config;
	}

	@Override
	public String getFormat() {
		return SVRLReport.ESCALI_FORMAT;
	}

	@Test
	public void test_phase1(){
		doTest();
	}

	@Test
	public void test_phase2(){
		Config config = getConfig();
		config.setPhase("phase2");
		doTest(config);
	}

	@Test
	public void test_phase3(){
		Config config = getConfig();
		config.setPhase("phase3");
		doTest(config);
	}

}
