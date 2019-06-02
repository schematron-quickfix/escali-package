package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

public class T01NsEsDefaultNamespaceValTest extends ValidationTestBase {

		@Override
		public String getFolder() {
			return "test01-ns-es-default-namespace";
		}

	@Override
	public String getInstancePath() {
		return "input/ns-es-default-namespace.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/ns-es-default-namespace.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}