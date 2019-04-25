package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T44EmbededXslt extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test44-embeded-xslt";
	}


	@Test
	public void test_1_vb_elementType_allComplex(){
		doTest(new ExecutionSetup(1, "vb.elementType.allComplex"));
	}

}
