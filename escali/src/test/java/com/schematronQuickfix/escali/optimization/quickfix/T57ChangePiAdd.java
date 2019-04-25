package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T57ChangePiAdd extends FixingTestBaseChangePi {

	@Override
	public String getFolder() {
		return "../test57-change-pi-add";
	}


	@Override
	public String getSchemaPath() {
		return "input/change-pi-add.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/change-pi-add.xml";
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
