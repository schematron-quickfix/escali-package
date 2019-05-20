package com.schematronQuickfix.escali.optimization.bugfixes;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T03GlobalUseForEach extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test03-global-use-for-each";
	}


	@Override
	public String getSchemaPath() {
		return "input/global-use-for-each.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/global-use-for-each.xml";
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
		doTest(new ExecutionSetup(1, "useFoo#1"));
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "useFoo#2"));
	}
	
}
