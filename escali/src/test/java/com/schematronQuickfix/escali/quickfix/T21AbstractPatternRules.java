package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T21AbstractPatternRules extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test21-Abstract-pattern-rules";
	}

	@Test
	public void test_1_fix(){
		doTest(new ExecutionSetup(1, "fix"));
	}

	@Test
	public void test_1_deleteOther(){
		doTest(new ExecutionSetup(1, "deleteOther"));
	}

	@Test
	public void test_3_delete(){
		doTest(new ExecutionSetup(3, "delete"));
	}

	@Test
	public void test_4_abstractRuleDelete(){
		doTest(new ExecutionSetup(4, "abstractRule.delete"));
	}

	@Test
	public void test_12_abstractRuleDelete(){
		doTest(new ExecutionSetup(12, "R2.F22"));
	}

}
