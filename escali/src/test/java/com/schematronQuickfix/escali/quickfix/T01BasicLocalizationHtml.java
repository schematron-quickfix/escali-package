package com.schematronQuickfix.escali.quickfix;

import net.sf.saxon.ma.trie.Tuple2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

public class T01BasicLocalizationHtml extends FixingTestBase {


    @Override
	public String getFolder() {
		return "test01-basic-localisation-html";
	}


	@Test
	public void test1(){
		doTest(new ExecutionSetup(1, "xml_lang"));
	}

	@Test
	public void test3(){
		doTest(new ExecutionSetup(3, "xml_lang"));
	}

	@Test
	public void test4(){
		doTest(new ExecutionSetup(4, "lang_xml_lang").addUE("lang", "it"));
	}
	
}
