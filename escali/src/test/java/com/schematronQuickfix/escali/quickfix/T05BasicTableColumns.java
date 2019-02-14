package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T05BasicTableColumns extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test05-basic-table-columns";
	}



	@Test
	public void test_1_proportional(){
		doTest(new ExecutionSetup(1, "proportional"));
	}

	@Test
	public void test_2_last(){
		doTest(new ExecutionSetup(2, "last"));
	}


	@Test
	public void test_2_setOneWidth(){
		doTest(
				new ExecutionSetup(2, "setOneWidth")
				.addUE("colNumber", "3")
		);
	}

}
