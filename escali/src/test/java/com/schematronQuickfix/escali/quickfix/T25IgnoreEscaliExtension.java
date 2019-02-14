package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T25IgnoreEscaliExtension extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test25-ignore-escali-extension";
	}

	@Test
	public void test_1_ignore_attrValue(){
		doTest(new ExecutionSetup(1, "ignore_attrValue"));
	}

	@Test
	public void test_5_ignore_badContent(){
		doTest(new ExecutionSetup(5, "ignore_badContent"));
	}

}
