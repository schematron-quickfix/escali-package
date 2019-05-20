package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T01CallFixMultiCallsVariable extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test01-call-fix-multi-calls-variable";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-multi-calls-variable.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-multi-calls-variable.xml";
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
	public void test(){
		doTest(new ExecutionSetup(1, "addChilds"));
		
	}
	
}
