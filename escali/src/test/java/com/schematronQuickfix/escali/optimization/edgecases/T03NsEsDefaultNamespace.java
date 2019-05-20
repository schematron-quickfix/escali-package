package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T03NsEsDefaultNamespace extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test03-ns-es-default-namespace";
	}


	@Override
	public String getSchemaPath() {
		return "input/ns-es-default-namespace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/ns-es-default-namespace.xml";
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
