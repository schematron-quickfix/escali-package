package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T02NsDeclarationsTest extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test02-ns-declarations";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-declarations.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-declarations.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "delete"));
		
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "delete"));

	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(3, "delete"));

	}


	
}
