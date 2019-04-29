package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T18MultipleDocuments extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test18-multiple-documents";
	}

	@Test
	public void test_1_setLang_1(){
		doTest(new ExecutionSetup(1, "setLang#1"));
	}

	@Test
	public void test_1_addToConfig(){
		doTest(
				new ExecutionSetup(1, "addToConfig")
					.addExecProp("SUFFIX", "_config")
		);
	}

}
