package com.schematronQuickfix.escali.quickfix;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class T48QueryBinding extends FixingTestBaseChangePi {

    @Override
	public String getFolder() {
		return "test48-querybinding";
	}


	private String schema = "test.sch";

	@Test
	public void test_1_xslt1_fix(){
    	this.schema = "test_1.sch";
    	expectError(new ExecutionSetup(1, "fix").addExecProp("SUFFIX", "_2"), CancelException.class);
	}

	@Test
	public void test_1_xslt2_fix(){
    	this.schema = "test_2.sch";
		doTest(new ExecutionSetup(1, "fix").addExecProp("SUFFIX", "_2"));
	}

	@Test
	public void test_1_xslt3_fix(){
    	this.schema = "test_3.sch";
		doTest(new ExecutionSetup(1, "fix").addExecProp("SUFFIX", "_3"));
	}


	@Override
	public String getSchemaPath() {
		return "input/" + this.schema;
	}
}
