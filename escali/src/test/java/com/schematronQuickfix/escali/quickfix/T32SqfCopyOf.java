package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T32SqfCopyOf extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test32-sqf-copy-of";
	}

	@Test
	public void test_1_copyOf(){
		doTest(new ExecutionSetup(1, "copy-of"));
	}


}
