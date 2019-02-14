package com.schematronQuickfix.escali.quickfix;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import org.junit.Test;

public class T12HideChangeMarker extends FixingTestBase {

	private static Config escaliConfig;

	static {
		escaliConfig = ConfigFactory.createDefaultConfig();
		escaliConfig.setChangePrefix("");
	}

    @Override
	public String getFolder() {
		return "test12-hide-changeMarker";
	}

	@Test
	public void test_1_lang(){
		doTest(escaliConfig, new ExecutionSetup(1, "lang"));
	}

	@Test
	public void test_3_lang_xml_lang(){
		doTest(
				escaliConfig,
				new ExecutionSetup(3, "lang_xml_lang")
						.addUE("lang", "it")
		);
	}

	@Test
	public void test_1_xml_lang(){
		doTest(escaliConfig, new ExecutionSetup(5, "xml_lang"));
	}

}
