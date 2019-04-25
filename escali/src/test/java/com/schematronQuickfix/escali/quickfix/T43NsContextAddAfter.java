package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T43NsContextAddAfter extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test43-ns-context-add-after";
	}


	@Test
	public void test_1_vb_elementType_allComplex(){
		doTest(new ExecutionSetup(1, "vb.elementType.allComplex"));
	}

}
