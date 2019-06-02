package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T12EsRegexValTest extends ValidationTestBase {


	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> basePhases() {
		return Arrays.asList(
//				new String[]{null},
//				new String[]{"de"},
//				new String[]{"en"},
				new String[]{"phase1"},
				new String[]{"phase2"},
				new String[]{"phase3a"},
				new String[]{"phase3b"},
				new String[]{"phase4"},
				new String[]{"phase5"},
				new String[]{"phase6"}
		);
	}

	public T12EsRegexValTest(String phase) {
		this.phase = phase;
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

	@Test
		public void test(){
			doTest();

		}
	}