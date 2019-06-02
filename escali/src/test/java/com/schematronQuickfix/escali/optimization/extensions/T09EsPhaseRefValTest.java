package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T09EsPhaseRefValTest extends ValidationTestBase {


	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> basePhases() {
		return Arrays.asList(
				new String[]{"phase1"},
				new String[]{"phase2"},
				new String[]{"phase3"},
				new String[]{"phase4"}
		);
	}

	public T09EsPhaseRefValTest(String phase) {
		this.phase = phase;
	}

	@Override
	public String getFolder() {
			return "test09-es-phase-ref";
		}

	@Override
	public String getInstancePath() {
		return "input/es-phase-ref.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-phase-ref.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}