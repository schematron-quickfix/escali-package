package com.schematronQuickfix.escali.schematron;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.EscaliCmd;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.helpers.EscaliFixingTestPair;
import com.schematronQuickfix.escali.helpers.SchematronInstancePair;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class T19QueryBinding extends ValidationTestBase {

    @Override
	public String getFolder() {
		return "test19-querybinding";
	}
    
	
//	@Test
////	public void test(){
////		doTest();
////	}


	private String schema = "input/test.sch";

	@Test
	public void test_1_xslt1_fix(){
		this.schema = "input/test_1.sch";

		expectError(ConfigFactory.createDefaultConfig(), CancelException.class);

	}

	@Test
	public void test_2_xslt2_fix(){
		this.schema = "input/test_2.sch";

		doTest(ConfigFactory.createDefaultConfig(), "expected/test_2.svrl");

	}

	@Test
	public void test_3_xslt3_fix(){
		this.schema = "input/test_3.sch";

		doTest(ConfigFactory.createDefaultConfig(), "expected/test_3.svrl");

	}

	@Override
	public String getSchemaPath() {
		return schema;
	}

	//	@Test
//	public void test_1_xslt2_fix(){
//		this.schema = "test_2.sch";
//		doTest(new FixingTestBase.ExecutionSetup(1, "fix").addExecProp("SUFFIX", "_2));
//	}
//
//	@Test
//	public void test_1_xslt3_fix(){
//		this.schema = "test_3.sch";
//		doTest(new FixingTestBase.ExecutionSetup(1, "fix").addExecProp("SUFFIX", "_3"));
//	}


//	@Override
//	public String getSchemaPath() {
//		return "input/" + this.schema;
//	}


	
}
