package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T30StringReplaceWithFlags extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test30-StringReplace-with-flags";
	}

	@Test
	public void test_1_replaceQF(){
		doTest(new ExecutionSetup(1, "replaceQF"));
	}

	@Test
	public void test_1_replaceQFwithFlags(){
		doTest(new ExecutionSetup(1, "replaceQFwithFlags"));
	}


}
