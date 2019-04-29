package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T70ActionOrderMix extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test70-action-order-mix";
	}


	@Override
	public String getSchemaPath() {
		return "input/action-order-mix.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/action-order-mix.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addBazBeforeAndReplaceBar"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBazAsChildAndReplaceBar"));
	}

}
