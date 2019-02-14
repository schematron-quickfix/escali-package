package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T06BasicTableSpans extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test06-basic-table-spans";
	}

	@Test
	public void test_1_deleteColspan(){
		doTest(new ExecutionSetup(1, "deleteColspan"));
	}

	@Test
	public void test_1_deleteRowspan(){
		doTest(new ExecutionSetup(1, "deleteRowspan"));
	}

	@Test
	public void test_4_addLost(){
		doTest(new ExecutionSetup(4, "addLost"));
	}

	@Test
	public void test_6_addLost(){
		doTest(new ExecutionSetup(6, "addLost"));
	}

	@Test
	public void test_9_deleteUeberschuss(){
		doTest(new ExecutionSetup(9, "deleteUeberschuss"));
	}

}
