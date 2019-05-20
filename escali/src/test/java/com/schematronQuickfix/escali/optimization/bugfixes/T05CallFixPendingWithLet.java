package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T05CallFixPendingWithLet extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test05-call-fix-pending-with-let";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-pending-with-let.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-pending-with-let.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addChild"));
	}

}
