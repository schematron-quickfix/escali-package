package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T20ActionOrder extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test20-action-order";
	}

	@Test
	public void test_1_fix(){
		doTest(new ExecutionSetup(1, "fix"));
	}

	@Test
	public void test_1_deleteOther(){
		doTest(new ExecutionSetup(1, "deleteOther"));
	}

	@Test
	public void test_2_replace(){
		doTest(new ExecutionSetup(2, "replace"));
	}

	@Test
	public void test_3_delete(){
		doTest(new ExecutionSetup(3, "delete"));
	}

	@Test
	public void test_4_delete2(){
		doTest(new ExecutionSetup(4, "delete2"));
	}

}