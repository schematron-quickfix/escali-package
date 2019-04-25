package com.schematronQuickfix.escali.quickfix.standalone;

import com.schematronQuickfix.escali.quickfix.FixingTestBaseChangePi;
import org.junit.Test;

public class T04MixAsIncludeById extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test03-mix-as-library";
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
