package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T07AddAfterNsContextChange extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test07-add-after-ns-context-change";
	}


	@Override
	public String getSchemaPath() {
		return "input/add-after-ns-context-change.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/add-after-ns-context-change.xml";
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
		doTest(new ExecutionSetup(1, "addBarAfter"));
		
	}
	
}
