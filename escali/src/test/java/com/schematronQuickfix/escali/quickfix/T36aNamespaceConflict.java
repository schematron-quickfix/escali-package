package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T36aNamespaceConflict extends FixingTestBaseChangePi {

	private String variant;

	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> fixIds() {
		return Arrays.asList(
				new String[]{"a"},
				new String[]{"b"},
				new String[]{"c"}
		);
	}

	public T36aNamespaceConflict(String variant){
		this.variant = variant;
	}

    @Override
	public String getFolder() {
		return "test36-namespace-conflict";
	}

	@Override
	public String getSchemaPath() {
		return "input/test-" + this.variant + ".sch";
	}

	@Test
	public void test_1_replaceDefault(){
		doTest(
				new ExecutionSetup(1, "replaceDefault")
					.addExecProp("SUFFIX", "_" + variant)
		);
	}

	@Test
	public void test_1_replaceOther(){
		doTest(
				new ExecutionSetup(1, "replaceOther")
						.addExecProp("SUFFIX", "_" + variant)
		);
	}

	@Test
	public void test_1_replaceNull(){
		doTest(
				new ExecutionSetup(1, "replaceNull")
						.addExecProp("SUFFIX", "_" + variant)
		);
	}

	@Test
	public void test_1_replaceNone(){
//		Only for Variant b
		if("b".equals(variant)){
			doTest(
					new ExecutionSetup(1, "replaceNone")
							.addExecProp("SUFFIX", "_" + variant)
			);
		}
	}



}
