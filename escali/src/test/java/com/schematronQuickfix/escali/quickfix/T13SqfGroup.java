package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T13SqfGroup extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test13-sqf-group";
	}

	@Test
	public void test_1_delete(){
		doTest(new ExecutionSetup(1, "delete"));
	}

	@Test
	public void test_1_add2bodyH1(){
		doTest(
				new ExecutionSetup(1, "add2bodyH1")
					.addUE("h1", "This is a new headline")
					.addExecProp("SUFFIX", "_1")
		);
	}

	@Test
	public void test_1_add2bodyH1_2(){
		doTest(
				new ExecutionSetup(1, "add2bodyH1")
					.addUE("h1", "Headlines")
					.addExecProp("SUFFIX", "_2")
		);
	}

	@Test
	public void test_2_add2(){
		doTest(
				new ExecutionSetup(2, "add2")
						.addUE("h2", "new headline 2")
						.addUE("h3", "new headline 3")
		);
	}

}
