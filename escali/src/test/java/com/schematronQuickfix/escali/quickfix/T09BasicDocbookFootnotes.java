package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T09BasicDocbookFootnotes extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test09-basic-docbook-footnotes";
	}

	@Test
	public void test_1_delete(){
		doTest(new ExecutionSetup(1, "delete"));
	}

	@Test
	public void test_1_brackets(){
		doTest(new ExecutionSetup(1, "brackets"));
	}

	@Test
	public void test_1_parentBrackets(){
		doTest(new ExecutionSetup(1, "parentBrackets"));
	}

}
