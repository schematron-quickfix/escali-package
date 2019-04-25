package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T60ChangePiTwoAttributesSameNode extends FixingTestBaseChangePi {

	@Override
	public String getFolder() {
		return "../test60-change-pi-twoAttributesSameNode";
	}


	@Override
	public String getSchemaPath() {
		return "input/change-pi-twoAttributesSameNode.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/change-pi-twoAttributesSameNode.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addBar"));
	}
	
}
