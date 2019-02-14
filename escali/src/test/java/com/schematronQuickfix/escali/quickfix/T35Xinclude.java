package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T35Xinclude extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test35-xinclude";
	}

	@Test
	public void test_1_addElement(){
		doTest(
				new ExecutionSetup(1, "addElement")
					.addExecProp("SUFFIX", "_main:_included")
		);
	}

	@Test
	public void test_1_addAllElement(){
		doTest(
				new ExecutionSetup(1, "addAllElement")
					.addExecProp("SUFFIX", "_main:_included")
		);
	}

	@Test
	public void test_1_deleteAllIdAttr(){
		doTest(
				new ExecutionSetup(1, "deleteAllIdAttr")
					.addExecProp("SUFFIX", "_main:_included")
		);
	}

	@Test
	public void test_1_replaceAllElement(){
		doTest(
				new ExecutionSetup(1, "replaceAllElement")
					.addExecProp("SUFFIX", "_main:_included")
		);
	}

	@Test
	public void test_2_addElement(){
		doTest(new ExecutionSetup(2, "addElement"));
	}

	@Test
	public void test_2_deleteIdAttr(){
		doTest(new ExecutionSetup(2, "deleteIdAttr"));
	}

	@Test
	public void test_2_replaceElement(){
		doTest(new ExecutionSetup(2, "replaceElement"));
	}

}
