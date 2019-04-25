package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T45MutlipleAttributes extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test45-mutlipleAttributes";
	}


	@Test
	public void test_1_addMultipleAttributes(){
		doTest(new ExecutionSetup(1, "addMultipleAttributes"));
	}

}
