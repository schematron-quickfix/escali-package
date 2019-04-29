package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T66SchInclude extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test66-sch-include";
	}


	@Override
	public String getSchemaPath() {
		return "input/sch/sch-include.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/xml/sch-include.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "replaceBar"));
	}

}
