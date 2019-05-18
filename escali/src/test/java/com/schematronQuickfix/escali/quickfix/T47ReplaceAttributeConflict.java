package com.schematronQuickfix.escali.quickfix;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class T47ReplaceAttributeConflict extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test47-replaceAttributeConflict";
	}


	@Test
	public void test_1_replaceAttribut(){
		doTest(new ExecutionSetup(1, "replaceAttribut"));
	}



}
