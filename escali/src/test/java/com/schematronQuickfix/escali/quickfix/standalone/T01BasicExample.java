package com.schematronQuickfix.escali.quickfix.standalone;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T01BasicExample extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test01-basic-example";
	}


	@Test
	public void test_1_fix(){
		doTest(new ExecutionSetup(1, "first_standalone"));
	}

	@Override
	public String getSchemaPath() {
		return "input/fixes.sqf";
	}
}
