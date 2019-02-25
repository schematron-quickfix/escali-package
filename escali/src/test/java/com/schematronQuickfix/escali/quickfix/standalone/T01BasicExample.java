package com.schematronQuickfix.escali.quickfix.standalone;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T01BasicExample extends FixingTestBase {

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
