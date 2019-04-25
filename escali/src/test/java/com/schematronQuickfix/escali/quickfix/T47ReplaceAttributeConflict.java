package com.schematronQuickfix.escali.quickfix;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class T47ReplaceAttributeConflict extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test47-replaceAttributeConflict";
	}

	@Before
	public void before(){
//    	PENDING: Conflict of two replaces. Needs to be fixed.
    	assumeTrue("PENDING: Conflict of two replaces. Needs to be fixed.", false);
	}


	@Test
	public void test_1_replaceAttribut(){
		doTest(new ExecutionSetup(1, "replaceAttribut"));
	}



}
