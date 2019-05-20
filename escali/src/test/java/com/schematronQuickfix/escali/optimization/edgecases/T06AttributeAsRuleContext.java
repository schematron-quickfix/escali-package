package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T06AttributeAsRuleContext extends FixingTestBaseChangePi {



    @Override
	public String getFolder() {
		return "test06-attribute-as-rule-context";
	}


	@Override
	public String getSchemaPath() {
		return "input/attribute-as-rule-context.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/attribute-as-rule-context.xml";
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
		doTest(new ExecutionSetup(1, "replaceBar"));
		
	}
	
}
