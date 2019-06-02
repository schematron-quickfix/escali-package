package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T12EsRegexTest extends FixingTestBase {

	private final int msgPos;

	@Parameterized.Parameters(name = "{0} - {1}")
	public static List<String[]> msgPos() {
		return Arrays.asList(
				new String[]{"trivial", "1"},
				new String[]{"inline markup simple", "2"},
				new String[]{"inline markup complex", "3"},
				new String[]{"comment", "4"},
				new String[]{"attribute", "5"},
				new String[]{"pi", "6"}
		);
	}

	public T12EsRegexTest(String title, String msgPos) {
		this.msgPos = Integer.parseInt(msgPos);
	}

	@Override
	public String getFolder() {
		return "test12-es-regex";
	}

	@Override
	public String getInstancePath() {
		return "input/es-regex.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-regex.sch";
	}

	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_delete(){
		doTest(new ExecutionSetup(msgPos, "delete"));
		
	}

	@Test
	public void test_replace(){
		doTest(new ExecutionSetup(msgPos, "replace"));
	}

	@Test
	public void test_add(){
		doTest(new ExecutionSetup(msgPos, "add"));

	}


	
}
