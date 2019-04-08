package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T42UseForEach4Current extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test42-use-for-each4-current";
	}


	@Test
	public void test_1_use_for_each_1(){
		doTest(new ExecutionSetup(1, "use_for_each#1"));
	}

	@Test
	public void test_1_use_for_each_2(){
		doTest(new ExecutionSetup(1, "use_for_each#2"));
	}

	@Test
	public void test_1_use_for_each_3(){
		doTest(new ExecutionSetup(1, "use_for_each#3"));
	}


}
