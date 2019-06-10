package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

public class T13EsIgnoreableValTest extends ValidationTestBase {



	@Override
	public String getFolder() {
			return "test13-es-ignoreable";
		}

	@Override
	public String getInstancePath() {
		return "input/es-ignoreable.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/es-ignoreable.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}