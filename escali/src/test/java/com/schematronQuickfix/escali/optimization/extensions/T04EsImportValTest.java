package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T04EsImportValTest extends ValidationTestBase {


	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> basePhases() {
		return Arrays.asList(
				new String[]{"#ALL"},
				new String[]{null}
		);
	}


	public T04EsImportValTest(String phase) {
		this.phase = phase;
	}

	@Override
	public String getFolder() {
			return "test04-es-import";
		}

	@Override
	public String getInstancePath() {
		return "input/es-import.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-import.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}