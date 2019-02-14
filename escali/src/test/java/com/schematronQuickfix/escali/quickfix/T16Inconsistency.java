package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;


@RunWith(Parameterized.class)
public class T16Inconsistency extends FixingTestBase {


	private String fixId;

	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> fixIds() {
		return Arrays.asList(
				new String[]{"addFirstChild"},
				new String[]{"addLastChild"},
				new String[]{"addBefore"},
				new String[]{"addAfter"},
				new String[]{"addAttr"}
		);
	}

	@Override
	public String getFolder() {
		return "test16-inconsistency";
	}

	public T16Inconsistency(String fixId){
		this.fixId = checkPending(fixId);
	}



	@Test
	public void test_1(){
		doTest(new ExecutionSetup(1, this.fixId));
	}

	@Test
	public void test_2(){
		doTest(new ExecutionSetup(2, this.fixId));
	}

	@Test
	public void test_3(){
		doTest(new ExecutionSetup(3, this.fixId));
	}

}