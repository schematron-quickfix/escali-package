package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T08BasicDocbookLists extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test08-basic-docbook-lists";
	}

	@Test
	public void test_1_delete(){
		doTest(new ExecutionSetup(1, "delete"));
	}

	@Test
	public void test_1_plain(){
		doTest(new ExecutionSetup(1, "plain"));
	}

}
