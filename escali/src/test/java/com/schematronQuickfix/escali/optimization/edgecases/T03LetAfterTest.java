package com.schematronQuickfix.escali.optimization.edgecases;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T03LetAfterTest extends FixingTestBase {



    @Override
	public String getFolder() {
		return "test03-let-after-test";
	}


	@Override
	public String getSchemaPath() {
		return "input/let-after-test.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/let-after-test.xml";
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
		doTest(new ExecutionSetup(1, "fix_1"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "fix_2"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "fix_3"));
	}

}
