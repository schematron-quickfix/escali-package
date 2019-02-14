package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T04BasicHtmlTitles extends FixingTestBase {


    @Override
	public String getFolder() {
		return "test04-basic-html-titles";
	}



	@Test
	public void test_1_deleteComment(){
		doTest(new ExecutionSetup(1, "deleteComment"));
	}

	@Test
	public void test_2_cutTitle(){
		doTest(new ExecutionSetup(2, "cutTitle"));
	}

	@Test
	public void test_2_setTitle_1(){
		doTest(new ExecutionSetup(2, "setTitle_1"));
	}

	@Test
	public void test_2_newTitle(){
		doTest(
				new ExecutionSetup(2, "newTitle")
				.addUE("newText", "This is a new title")
		);
	}

	@Test
	public void test_2_takeComment(){
		doTest(new ExecutionSetup(2, "takeComment"));
	}

	@Test
	public void test_2_setTitle_3(){
		doTest(new ExecutionSetup(2, "setTitle_3"));
	}

}
