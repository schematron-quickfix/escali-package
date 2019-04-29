package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T69ActionOrderReplace extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test69-action-order-replace";
	}


	@Override
	public String getSchemaPath() {
		return "input/action-order-replace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/action-order-replace.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "replaceBar"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "replaceBarByBazzer"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "replaceAttribute"));
	}

	@Test
	public void test_5(){
		doTest(new ExecutionSetup(1, "replaceAllAttributes"));
	}

	@Test
	public void test_4(){
		doTest(new ExecutionSetup(1, "replaceAncestorAndChild"));
	}

}
