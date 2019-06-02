package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T08EsMatchTypeValTest extends ValidationTestBase {


	@Override
	public String getFolder() {
			return "test08-es-matchType";
		}

	@Override
	public String getInstancePath() {
		return "input/es-matchType.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-matchType.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}