package com.schematronQuickfix.escali.optimization.quickfix;

import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assume.assumeTrue;

@RunWith(Parameterized.class)
public class T72InconsistencyAdd extends FixingTestBase {

	private final String fixId;

	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> fixIds() {
		return Arrays.asList(
				new String[]{"addFChild"},
				new String[]{"addLChild"},
				new String[]{"addBefore"},
				new String[]{"addAfter"},
				new String[]{"addAttr"},
				new String[]{"addAttrBar"}
				);
	}

	public T72InconsistencyAdd(String fixId){
		this.fixId = checkPending(fixId);
	}

	@Override
	public String getFolder() {
		return "../test72-inconsistency-add";
	}


	@Override
	public String getSchemaPath() {
		return "input/inconsistency-add.sch";
	}

	@Override
	public String getInstancePath() {
		return "input/inconsistency-add.xml";
	}


	@Override
	public String getExpectedPrefix() {
		return "expected";
	}

	@Test
	public void test_processing_instruction(){
		assumeTrue(!"addAttrBar".equals(fixId));
		doTest(new ExecutionSetup(1, fixId));
	}

	@Test
	public void test_attribute(){
		doTest(new ExecutionSetup(2, fixId));
	}

	@Test
	public void test_comment(){
		assumeTrue(!"addAttrBar".equals(fixId));
		doTest(new ExecutionSetup(3, fixId));
	}
	@Test
	public void test_text(){
		assumeTrue(!"addAttrBar".equals(fixId));
		doTest(new ExecutionSetup(4, fixId));
	}

}
