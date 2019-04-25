package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T61ChangePiDeleteAttribute extends FixingTestBaseChangePi {

	@Override
	public String getFolder() {
		return "../test61-change-pi-deleteAttribute";
	}


	@Override
	public String getSchemaPath() {
		return "input/change-pi-deleteAttribute.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/change-pi-deleteAttribute.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "deleteBar"));
	}
	
}
