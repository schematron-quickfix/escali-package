package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

public class T06EsImportPhaseValTest extends ValidationTestBase {


	@Override
	public String getFolder() {
			return "test06-es-import-phase";
		}

	@Override
	public String getInstancePath() {
		return "input/es-import-phase.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-import-phase.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}