package com.schematronQuickfix.escali.optimization.quickfix;

import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assume.assumeTrue;

@RunWith(Parameterized.class)
public class T73InconsistencyReplace extends FixingTestBase {

	private final String fixId;
	private Integer msgIdx;

	@Parameterized.Parameters(name = "{0} - {1}")
	public static List<String[]> fixIds() {
		return Arrays.asList(
				new String[]{"1", "replaceByAttr"},
				new String[]{"2", "replaceByAttr"},
				new String[]{"4", "replaceByAttr"},
				new String[]{"5", "replaceByAttr"},
				new String[]{"3", "replaceByEl"},
				new String[]{"3", "replaceByPi"},
				new String[]{"3", "replaceByComment"},
				new String[]{"3", "replaceByText"}
				);
	}

	public T73InconsistencyReplace(String index, String fixId){
		this.msgIdx = Integer.parseInt(index);
		this.fixId = checkPending(fixId);
	}

	@Override
	public String getFolder() {
		return "../test73-inconsistency-replace";
	}


	@Override
	public String getSchemaPath() {
		return "input/inconsistency-replace.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/inconsistency-replace.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_1_replaceAtt(){
		expectError(new ExecutionSetup(this.msgIdx, this.fixId), XSLTErrorListener.class);
	}

}
