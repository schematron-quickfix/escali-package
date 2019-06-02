package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T02NsDeclarationsValTest extends ValidationTestBase {

		@Override
		public String getFolder() {
			return "test02-ns-declarations";
		}

	@Override
	public String getInstancePath() {
		return "input/ns-declarations.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/ns-declarations.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}