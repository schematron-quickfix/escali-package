package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T27NamespaceHandling extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test27-namespace-handling";
	}

	@Test
	public void test_1_nsConvertionPrx(){
		doTest(new ExecutionSetup(1, "nsConvertionPrx"));
	}

	@Test
	public void test_1_nsConvertion(){
		doTest(new ExecutionSetup(1, "nsConvertion"));
	}

	@Test
	public void test_2_nsConvertionPrx(){
		doTest(new ExecutionSetup(2, "nsConvertionPrx"));
	}

	@Test
	public void test_2_nsConvertion(){
		doTest(new ExecutionSetup(2, "nsConvertion"));
	}

	@Test
	public void test_3_nsConvertionNull(){
		doTest(new ExecutionSetup(3, "nsConvertionNull"));
	}

}
