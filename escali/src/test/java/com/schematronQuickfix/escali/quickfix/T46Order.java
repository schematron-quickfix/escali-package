package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class T46Order extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test46-order";
	}


	@Test
	public void test_1_addBefore(){
		doTest(new ExecutionSetup(1, "addBefore"));
	}


	@Test
	public void test_1_addAfter(){
//    	PENDING! Not clear specified in SQF spec!
    	assumeTrue("PENDING: Not clear specified in SQF spec!", false);
		doTest(new ExecutionSetup(1, "addAfter"));
	}


	@Test
	public void test_1_addChild(){
		doTest(new ExecutionSetup(1, "addChild"));
	}


	@Test
	public void test_1_addLastChild(){
		doTest(new ExecutionSetup(1, "addLastChild"));
	}

}
