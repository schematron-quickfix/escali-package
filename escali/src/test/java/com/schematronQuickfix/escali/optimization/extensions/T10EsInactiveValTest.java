package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T10EsInactiveValTest extends ValidationTestBase {


	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> basePhases() {
		return Arrays.asList(
				new String[]{null},
				new String[]{"phase1"},
				new String[]{"phase2"},
				new String[]{"phase3"}
		);
	}

	public T10EsInactiveValTest(String phase) {
		this.phase = phase;
	}

	@Override
	public String getFolder() {
			return "test10-es-inactive";
		}

	@Override
	public String getInstancePath() {
		return "input/es-inactive.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-inactive.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}