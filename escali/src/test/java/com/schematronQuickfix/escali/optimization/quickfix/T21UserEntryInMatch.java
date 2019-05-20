package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T21UserEntryInMatch extends FixingTestBase {



	@Override
	public String getFolder() {
		return "../test21-user-entry-in-match";
	}


	@Override
	public String getSchemaPath() {
		return "input/user-entry-in-match.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/user-entry-in-match.xml";
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
				.addExecProp("SUFFIX", "_bar1")
		);
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(1, "removeBars")
				.addUE("barId", "bar3")
				.addExecProp("SUFFIX", "_bar3")
		);
	}
	
}
