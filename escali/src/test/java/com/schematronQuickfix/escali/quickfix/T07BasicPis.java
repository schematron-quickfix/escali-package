package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T07BasicPis extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test07-basic-pis";
	}

	@Test
	public void test_1_delete(){
		doTest(new ExecutionSetup(1, "delete"));
	}

	@Test
	public void test_1_replace(){
		doTest(new ExecutionSetup(1, "replace"));
	}

	@Test
	public void test_1_setName(){
		doTest(
				new ExecutionSetup(1, "setName")
				.addUE("newName","sqf_new-pi-name")
		);
	}


}
