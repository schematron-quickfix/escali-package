package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T65DocWithRelativePaths extends FixingTestBase {

	@Override
	public String getFolder() {
		return "../test65-doc-with-relative-paths";
	}


	@Override
	public String getSchemaPath() {
		return "input/sch/doc-with-relative-paths.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/xml/doc-with-relative-paths.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, "addBarRTS"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "addBarRTX"));
	}

}
