package com.schematronQuickfix.escali.quickfix.standalone;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T07GroupCallFix extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test07-group-call-fix";
	}


	@Test
	public void test_1_first_standalone(){
		doTest(new ExecutionSetup(1, "first_standalone"));
	}

	@Test
	public void test_2_second_standalone(){
		doTest(new ExecutionSetup(2, "second_standalone"));
	}

	@Override
	public String getSchemaPath() {
		return "input/fixes.sqf";
	}
}
