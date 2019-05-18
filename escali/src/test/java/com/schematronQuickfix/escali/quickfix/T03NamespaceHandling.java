package com.schematronQuickfix.escali.quickfix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;


@RunWith(Parameterized.class)
public class T03NamespaceHandling extends FixingTestBaseChangePi {


	private String fixId;

	@Parameterized.Parameters(name = "{0}")
	public static List<String[]> fixIds() {
		return Arrays.asList(
				new String[]{"addElementWithNs1"},
				new String[]{"addElementWithPrfxConflict"},
				new String[]{"addElementWithNs2"},
				new String[]{"addElementWithNullNs"},
				new String[]{"addAttributeWithNs1"},
				new String[]{"addAttributeWithPrfxConflict"},
				new String[]{"addAttributeWithNs2"},
				new String[]{"addAttributeWithNullNs"},
				new String[]{"addElementWithNs2AsDef"}
		);
	}

    @Override
	public String getFolder() {
		return "test03-namespace-handling";
	}

	public T03NamespaceHandling(String fixId){
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
