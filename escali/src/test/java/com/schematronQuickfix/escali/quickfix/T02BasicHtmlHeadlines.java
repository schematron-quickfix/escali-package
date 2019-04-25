package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T02BasicHtmlHeadlines extends FixingTestBaseChangePi {


    @Override
	public String getFolder() {
		return "test02-basic-html-headlines";
	}

	@Test
	public void test_delete(){
		doTest(new ExecutionSetup(1, "delete"));
	}

	@Test
	public void test1_add2bodyH1(){
		doTest(
				new ExecutionSetup(1, "add2bodyH1")
				.addUE("h1", "This is a new headline")
				.addExecProp("SUFFIX", "_1")
		);
	}

	@Test
	public void test_addBodyH1_2(){
		doTest(
				new ExecutionSetup(1, "add2bodyH1")
				.addUE("h1", "Headlines")
						.addExecProp("SUFFIX", "_2")
		);
	}

	@Test
	public void test_setH1(){
		doTest(new ExecutionSetup(1, "setH1"));
	}


}
