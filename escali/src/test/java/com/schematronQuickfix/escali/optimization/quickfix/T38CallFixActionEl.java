package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T38CallFixActionEl extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test38-call-fix-actionEl";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-actionEl.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-actionEl.xml";
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
		doTest(new ExecutionSetup(1, "replaceFoo"));
		
	}
	
}
