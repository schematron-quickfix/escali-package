package com.schematronQuickfix.escali.quickfix;

import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import org.junit.Test;

public class T17Errors extends FixingTestBase {

    @Override
	public String getFolder() {
		return "test17-errors";
	}

	@Test
	public void test_1_replaceAtt(){
		expectError(new ExecutionSetup(1, "replaceAtt"), XSLTErrorListener.class);
	}

}
