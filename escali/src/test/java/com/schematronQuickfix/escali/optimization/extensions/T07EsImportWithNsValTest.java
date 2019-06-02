package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T07EsImportWithNsValTest extends ValidationTestBase {


	@Override
	public String getFolder() {
			return "test07-es-import-with-ns";
		}

	@Override
	public String getInstancePath() {
		return "input/es-import-with-ns.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-import-with-ns.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}