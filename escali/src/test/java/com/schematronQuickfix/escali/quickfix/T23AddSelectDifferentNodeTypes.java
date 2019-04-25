package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T23AddSelectDifferentNodeTypes extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test23-add-select-different-node-types";
	}

	@Test
	public void test_1_addToNext(){
		doTest(new ExecutionSetup(1, "addToNext"));
	}

	@Test
	public void test_1_addAsChild(){
		doTest(new ExecutionSetup(1, "addAsChild"));
	}

	@Test
	public void test_1_addBoth(){
		doTest(new ExecutionSetup(1, "addBoth"));
	}

	@Test
	public void test_1_addBothMixed(){
		doTest(new ExecutionSetup(1, "addBothMixed"));
	}

	@Test
	public void test_1_addAttAsLastChild(){
		doTest(new ExecutionSetup(1, "addAttAsLastChild"));
	}

}
