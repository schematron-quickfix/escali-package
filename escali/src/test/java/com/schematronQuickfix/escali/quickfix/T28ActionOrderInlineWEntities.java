package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T28ActionOrderInlineWEntities extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test28-action-order-inline-w-entities";
	}

	@Test
	public void test_1_fix(){
		doTest(new ExecutionSetup(1, "fix"));
	}

	@Test
	public void test_1_deleteOther(){
		doTest(getConfig(), new String[]{}, new ExecutionSetup(1, "deleteOther"));
	}

	@Test
	public void test_2_replace(){
		doTest(new ExecutionSetup(2, "replace"));
	}

	@Test
	public void test_3_delete(){
		doTest(new ExecutionSetup(3, "delete"));
	}

	@Test
	public void test_3_delete2(){
		doTest(new ExecutionSetup(4, "delete2"));
	}

}
