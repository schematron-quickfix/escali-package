package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T11DtdDecl extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test11-dtd-decl";
	}

	@Test
	public void test_1_deleteB(){
		doTest(new ExecutionSetup(1, "deleteB"));
	}

}
