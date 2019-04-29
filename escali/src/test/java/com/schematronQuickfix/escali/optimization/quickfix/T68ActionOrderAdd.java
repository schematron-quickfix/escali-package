package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T68ActionOrderAdd extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test68-action-order-add";
	}


	@Override
	public String getSchemaPath() {
		return "input/action-order-add.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/action-order-add.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addMix"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBefore"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "addAfter"));
	}

	@Test
	public void test_4(){
		doTest(new ExecutionSetup(1, "addChild"));
	}

	@Test
	public void test_5(){
		doTest(new ExecutionSetup(1, "addLastChild"));
	}

}
