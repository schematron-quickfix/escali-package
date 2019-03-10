package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T49GroupIdConflict extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test49-group-id-conflict";
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
