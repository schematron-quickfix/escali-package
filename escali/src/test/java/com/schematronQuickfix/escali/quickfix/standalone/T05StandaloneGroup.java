package com.schematronQuickfix.escali.quickfix.standalone;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T05StandaloneGroup extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test05-standalone-group";
	}


	@Test
	public void test_1_first_standalone(){
		doTest(new ExecutionSetup(1, "first_standalone"));
	}

	@Test
	public void test_2_second_standalone(){
		doTest(new ExecutionSetup(1, "second_standalone"));
	}

	@Override
	public String getSchemaPath() {
		return "input/fixes.sqf";
	}
}
