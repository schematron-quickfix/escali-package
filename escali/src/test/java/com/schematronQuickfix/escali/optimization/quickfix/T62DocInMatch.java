package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T62DocInMatch extends FixingTestBaseChangePi {

	@Override
	public String getFolder() {
		return "../test62-doc-in-match";
	}


	@Override
	public String getSchemaPath() {
		return "input/doc-in-match.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/doc-in-match.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addBar")
				.addExecProp("SUFFIX", "_2"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBarExtAndThis")
				.addExecProp("SUFFIX", "_1:_2"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "addBarAll")
				.addExecProp("SUFFIX", "_1:_2:_3"));
	}
	
}
