package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T05NestedEsImportValTest extends ValidationTestBase {


	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> basePhases() {
		return Arrays.asList(
				new String[]{"#ALL"},
				new String[]{"importPhase"}
		);
	}


	public T05NestedEsImportValTest(String phase) {
		this.phase = phase;
	}

	@Override
	public String getFolder() {
			return "test05-nested-es-import";
		}

	@Override
	public String getInstancePath() {
		return "input/nested-es-import.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/nested-es-import.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}