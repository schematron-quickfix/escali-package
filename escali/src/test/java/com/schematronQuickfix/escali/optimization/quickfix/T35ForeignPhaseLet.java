package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;

public class T35ForeignPhaseLet extends FixingTestBase {


	@Override
	public String getFolder() {
		return "../test35-foreign-phase-let";
	}


	@Override
	public String getSchemaPath() {
		return "input/foreign-phase-let.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/foreign-phase-let.xml";
	}


	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setInternalValidation(false);
		return config;
	}

	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1_phase1(){
		doTest(new ExecutionSetup(1, "replace")
				.addExecProp("SUFFIX", "_phase1")
		);
		
	}

	@Test
	public void test_1_phase2(){
		Config config = getConfig();
		config.setPhase("phase2");
		doTest(config, new ExecutionSetup(1, "replace")
				.addExecProp("SUFFIX", "_phase2")
		);

	}


	@Test
	public void test_1_phase3(){
		Config config = getConfig();
		config.setPhase("phase3");

		ExecutionSetup[] executions =  new ExecutionSetup[]{
				new ExecutionSetup(1, "replace"),
				new ExecutionSetup(2, "replace")

		};
		doTest(config, new String[]{"expected/expected_1_2_replace_phase3.xml"}, executions);

	}
}
