package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T24AddSelectWithAtomicValues extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test24-add-select-with-atomic-values";
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
	public void test_1_addUserEntry(){
		doTest(
				new ExecutionSetup(1, "addUserEntry")
						.addUE("ue", "user-entry-value")
		);
	}

}
