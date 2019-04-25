package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T10BasicStringReplace extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test10-basic-stringReplace";
	}

	@Test
	public void test_1_autoStringReplace(){
		doTest(new ExecutionSetup(1, "autoStringReplace"));
	}

	@Test
	public void test_2_autoStringReplace(){
		doTest(new ExecutionSetup(2, "autoStringReplace"));
	}

	@Test
	public void test_3_autoStringReplace(){
		doTest(new ExecutionSetup(3, "autoStringReplace"));
	}

}
