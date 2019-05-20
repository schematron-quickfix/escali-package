package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T11NodeCreationTemplateBody extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test11-node-creation-template-body";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-template-body.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/node-creation-template-body.xml";
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
