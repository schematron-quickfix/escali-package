package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T12NodeCreationSelectAttribute extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test12-node-creation-select-attribute";
	}


	@Override
	public String getSchemaPath() {
		return "input/node-creation-select-attribute.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/node-creation-select-attribute.xml";
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
		doTest(new ExecutionSetup(1, "replaceFoo"));
		
	}
	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "replaceByAtomic"));

	}
	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "replaceByAtomicSeq"));

	}
	
}
