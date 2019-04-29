package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T71ActionOrderStringReplace extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test71-action-order-stringReplace";
	}


	@Override
	public String getSchemaPath() {
		return "input/action-order-stringReplace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/action-order-stringReplace.xml";
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
		doTest(new ExecutionSetup(1, "replaceFooBar"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "replaceFooBarConflict"));
	}

}
