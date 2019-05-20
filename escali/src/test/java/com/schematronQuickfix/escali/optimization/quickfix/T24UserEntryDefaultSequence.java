package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T24UserEntryDefaultSequence extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test24-user-entry-default-sequence";
	}


	@Override
	public String getSchemaPath() {
		return "input/user-entry-default-sequence.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/user-entry-default-sequence.xml";
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
	public void test_1(){
		doTest(new ExecutionSetup(1, "removeBars")
				.addUE("barId", "bar1")
				.addExecProp("SUFFIX", "_1"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "removeBars")
				.addUE("barId", "bar3")
				.addExecProp("SUFFIX", "_3"));
	}

}
