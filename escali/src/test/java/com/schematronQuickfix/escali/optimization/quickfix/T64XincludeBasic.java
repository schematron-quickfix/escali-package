package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T64XincludeBasic extends FixingTestBaseChangePi  {

	@Override
	public String getFolder() {
		return "../test64-xinclude-basic";
	}


	@Override
	public String getSchemaPath() {
		return "input/xinclude-basic.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/xinclude-basic.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "replaceBar"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "replaceBar"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "replaceAllBar")
		.addExecProp("SUFFIX", "_1:_2")
		);
	}

	@Test
	public void test_4(){
		doTest(new ExecutionSetup(1, "deleteIdAttr")
		.addExecProp("SUFFIX", "_1:_2")
		);
	}
	
}
