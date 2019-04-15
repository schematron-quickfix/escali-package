package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T43CallFixAbstractDescription extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test43-call-fix-abstract-description";
	}


	@Override
	public String getSchemaPath() {
		return "input/call-fix-abstract-description.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/call-fix-abstract-description.xml";
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
		doTest(new ExecutionSetup(1, "addChild"));
		
	}

	@Test
	public void test2(){
		doTest(new ExecutionSetup(1, "addChilds"));

	}

}
