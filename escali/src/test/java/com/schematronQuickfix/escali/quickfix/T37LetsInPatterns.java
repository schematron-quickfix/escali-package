package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T37LetsInPatterns extends FixingTestBaseChangePi {

	private String variant;

	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> fixIds() {
		return Arrays.asList(
				new String[]{"a"},
				new String[]{"b"}
		);
	}

	public T37LetsInPatterns(String variant){
		this.variant = variant;
	}

    @Override
	public String getFolder() {
		return "test37-lets-in-patterns";
	}

	@Override
	public String getSchemaPath() {
		return "input/test-" + this.variant + ".sch";
	}

	@Test
	public void test_1_replace(){
		doTest(
				new ExecutionSetup(1, "replace")
					.addExecProp("SUFFIX", "_" + variant)
		);
	}

}
