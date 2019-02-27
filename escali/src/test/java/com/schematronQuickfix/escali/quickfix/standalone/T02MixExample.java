package com.schematronQuickfix.escali.quickfix.standalone;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T02MixExample extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test02-mix-example";
	}


	@Test
	public void test_1_uppercase_first(){
		doTest(new ExecutionSetup(1, "uppercase_first"));
	}

	@Test
	public void test_2_mixed_standalone(){
		doTest(new ExecutionSetup(2, "mixed_standalone"));
	}
}
