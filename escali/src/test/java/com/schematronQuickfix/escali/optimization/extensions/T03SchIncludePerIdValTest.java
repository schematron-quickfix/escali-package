package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T03SchIncludePerIdValTest extends ValidationTestBase {

		@Override
		public String getFolder() {
			return "test03-sch-include-per-id";
		}

	@Override
	public String getInstancePath() {
		return "input/xml/sch-include-per-id.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/sch/sch-include-per-id.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}