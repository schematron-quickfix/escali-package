package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

public class T13EsIgnoreableTest extends FixingTestBase {



	@Override
	public String getFolder() {
		return "test13-es-ignoreable";
	}

	@Override
	public String getInstancePath() {
		return "input/es-ignoreable.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-ignoreable.sch";
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
		doTest(new ExecutionSetup(1, "ignore_id_t1"));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, "ignore_id_t2"));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(3, "ignore_id_t3a"));
	}

	@Test
	public void test_4(){
		doTest(new ExecutionSetup(4, "ignore_id_t4"));
	}

	@Test
	public void test_5(){
		doTest(new ExecutionSetup(5, "ignore_id_t5"));
	}

	@Test
	public void test_6(){
		doTest(new ExecutionSetup(6, "ignore_id_t6"));
	}


	
}
