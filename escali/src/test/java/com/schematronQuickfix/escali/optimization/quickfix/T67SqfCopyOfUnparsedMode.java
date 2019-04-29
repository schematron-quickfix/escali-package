package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T67SqfCopyOfUnparsedMode extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test67-sqf-copy-of-unparsed-mode";
	}


	@Override
	public String getSchemaPath() {
		return "input/sqf-copy-of-unparsed-mode.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/sqf-copy-of-unparsed-mode.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addBar"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBarWithLet"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(1, "addBarWithSeq"));
	}

}
