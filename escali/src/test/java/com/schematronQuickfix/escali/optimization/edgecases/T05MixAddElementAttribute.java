package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T05MixAddElementAttribute extends FixingTestBaseChangePi {



    @Override
	public String getFolder() {
		return "test05-mix-add-element-attribute";
	}


	@Override
	public String getSchemaPath() {
		return "input/mix-add-element-attribute.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/mix-add-element-attribute.xml";
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
		doTest(new ExecutionSetup(1, "addMix"));
		
	}
	
}
