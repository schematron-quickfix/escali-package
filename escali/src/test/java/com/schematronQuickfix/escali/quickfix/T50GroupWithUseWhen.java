package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T50GroupWithUseWhen extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test50-group-with-use-when";
	}



	@Test
	public void test_1_first_standalone(){
		doTest(new ExecutionSetup(1, "first_standalone"));
	}

	@Test
	public void test_1_second_standalone(){
		doTest(new ExecutionSetup(1, "second_standalone"));
	}

}
