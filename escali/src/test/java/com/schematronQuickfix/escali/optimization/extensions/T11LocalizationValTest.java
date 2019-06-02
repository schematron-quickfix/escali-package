package com.schematronQuickfix.escali.optimization.extensions;

import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class T11LocalizationValTest extends ValidationTestBase {


	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> baseLang() {
		return Arrays.asList(
				new String[]{null},
				new String[]{"de"},
				new String[]{"en"},
				new String[]{"fr"}
		);
	}

	public T11LocalizationValTest(String lang) {
		this.lang = lang;
	}

	@Override
	public String getFolder() {
			return "test11-localization";
		}

	@Override
	public String getInstancePath() {
		return "input/localization.xml";
	}

	@Override
	public String getSchemaPath() {
		return "input/localization.sch";
	}

	@Test
		public void test(){
			doTest();

		}
	}