package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;

public class T15UseForEach extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test15-use-for-each";
	}

	@Test
	public void test_1_lang_xml_lang_foreach_1(){
		doTest(new ExecutionSetup(1, "lang_xml_lang_foreach_1"));
	}

	@Test
	public void test_1_lang_xml_lang_foreach_2(){
		doTest(new ExecutionSetup(1, "lang_xml_lang_foreach_2"));
	}

}
