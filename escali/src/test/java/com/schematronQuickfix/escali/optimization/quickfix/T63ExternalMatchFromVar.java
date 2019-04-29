package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T63ExternalMatchFromVar extends FixingTestBaseChangePi {

	@Override
	public String getFolder() {
		return "../test63-external-match-from-var";
	}


	@Override
	public String getSchemaPath() {
		return "input/external-match-from-var.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/external-match-from-var.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addBarGlobalVar"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBarKey"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "addBarFunction"));
	}
	
}
