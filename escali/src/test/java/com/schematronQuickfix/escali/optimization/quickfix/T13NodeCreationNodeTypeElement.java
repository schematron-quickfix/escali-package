package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T13NodeCreationNodeTypeElement extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test13-node-creation-node-type-element";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-node-type-element.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/node-creation-node-type-element.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setInternalValidation(false);
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
