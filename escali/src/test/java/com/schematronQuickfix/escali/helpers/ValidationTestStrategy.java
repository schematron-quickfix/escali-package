package com.schematronQuickfix.escali.helpers;

import static com.schematronQuickfix.escali.control.SVRLReport.ESCALI_FORMAT;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlunit.assertj.ValueAssert;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.util.Predicate;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escali.cmdInterface.Validation;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

public class ValidationTestStrategy {
	
	
	private ProcessLoger logger;

	private int testCounter = 0;
	
	public ValidationTestStrategy(ProcessLoger logger) {
		this.logger = logger;
	}



	public SVRLReport executeStandardValidation(EscaliValidationTestPair testPair) throws XSLTErrorListener, IOException, CancelException, SAXException, XMLStreamException, XPathExpressionException, URISyntaxException {
		SchematronInstancePair inputPair = testPair.getInputPair();
		Validation val = new Validation(inputPair.getSchemaDocument(), testPair.getEscaliConfig(), logger);
		SVRLReport report = val.validate(inputPair.getInstanceDocument());
		return report;
	}
	public SVRLReport testStandardValidation(EscaliValidationTestPair testPair){
		return testStandardValidation(testPair, SVRLReport.ESCALI_FORMAT);
	}
	public SVRLReport testStandardValidation(EscaliValidationTestPair testPair, String format){
		this.testCounter = 0;
		try {

			SVRLReport report = executeStandardValidation(testPair);

			ExpectedReportData expected = testPair.getExpected();
			String actualXml =  report.getFormatetReport(format).toString();

			if(expected.title != null){
				isEqualTo(
						assertThatXPathValue(actualXml, "/es:escali-reports/es:meta/@title")
						, expected.title)
				;
			}

			if(expected.phase != null){
				isEqualTo(
						assertThatXPathValue(actualXml, "/es:escali-reports/es:meta/@phase")
						, expected.phase)
				;
			}

			if(expected.queryBinding != null){
				isEqualTo(
						assertThatXPathValue(actualXml, "/es:escali-reports/es:meta/@queryBinding")
						, expected.queryBinding)
				;
			}

//			Check message count
			if(expected.getMessageCount() > 0){

				isEqualTo(
						assertThatXPathValue(actualXml, "count(//es:assert|//es:report)")
						, expected.getMessageCount())
				;
			}

//			Check asserts
			if(expected.asserts != null) {
				validateSchTests(actualXml, expected.asserts, "assert");
			}

//			Check reports
			if(expected.reports != null) {
				validateSchTests(actualXml, expected.reports, "report");
			}


			if(testCounter == 0){
				fail("This unit test has no active tests!");
			}

			return report;

		} catch (XSLTErrorListener
				| IOException 
				| CancelException 
				| XPathExpressionException 
				| SAXException 
				| URISyntaxException 
				| XMLStreamException e) {
			fail(e.getMessage());
			return null;
		}



	}

	private void validateSchTests(String xml, ArrayList<ExpectedReportData.Test> tests, String assertOrReport){
		String basicXPath = "//es:" + assertOrReport;

		ValueAssert valueAssert = assertThatXPathValue(xml, "count(" + basicXPath + ")")
				.as("Ammount of " + assertOrReport + " tests does not match!");

		isEqualTo(valueAssert, tests.size());



		for (int i = 0; i < tests.size(); i++) {
			ExpectedReportData.Test test = tests.get(i);
			String basicXPathTest = "(" + basicXPath + ")[" + (i + 1) + "]";
			validateSchTest(xml, test, basicXPathTest);



		}
	}
	private void validateSchTest(String xml, ExpectedReportData.Test test, String basicXPath){

		if(test.getMessage() != null){
			assertThatXPathValue(xml, basicXPath + "/@message | " + basicXPath + "/es:text")
					.isEqualTo(test.getMessage());
		}
		if(test.getLocation() != null){
			assertThatXPathValue(xml, basicXPath + "/@location")
					.isEqualTo(test.getLocation());
		}
		if(test.getLabel() != null){
			assertThatXPathValue(xml, basicXPath + "/@roleLabel")
					.isEqualTo(test.getLabel());
		}
		if(test.getId() != null){
			assertThatXPathValue(xml, basicXPath + "/@base-id")
					.isEqualTo(test.getId());
		}
		if(test.isSubstring()){
			assertThatXPathValue(xml, "substring-before(" + basicXPath + "/@substring, ' ')")
					.isEqualTo(test.getSubstringStart());
			assertThatXPathValue(xml, "substring-after(" + basicXPath + "/@substring, ' ')")
					.isEqualTo(test.getSubstringEnd());
		}

		validateTestQFs(xml, test.getQuickFixes(), basicXPath);

	}


	private void validateTestQFs(String xml, ArrayList<ExpectedReportData.QF> qfs, String basicXPath){

		String basicQFXPath = basicXPath + "/sqf:fix";

		ValueAssert valueAssert = assertThatXPathValue(xml, "count(" + basicQFXPath + ")")
				.as("Ammount of quick fixes does not match! (Test: " + basicXPath + ")");

		isEqualTo(valueAssert, qfs.size());



		for (int i = 0; i < qfs.size(); i++) {
			ExpectedReportData.QF qf = qfs.get(i);

			validateTestQF(xml, qf, "(" + basicQFXPath + ")[" + (i + 1) + "]");

		}
	}

	private void validateTestQF(String xml, ExpectedReportData.QF qf, String basicXPath){
		if(qf.getDescription() != null){
			assertThatXPathValue(xml, basicXPath + "/@title | " + basicXPath + "/es:text")
					.isEqualTo(qf.getDescription());
		}

		if(qf.getRole() != null){
			assertThatXPathValue(xml, basicXPath + "/@role")
					.isEqualTo(qf.getRole());
		}

		if(qf.getFixId() != null){
			assertThatXPathValue(xml, basicXPath + "/@fixId")
					.isEqualTo(qf.getFixId());
		}
	}

	public static Predicate<Attr> noBaseAttribute() {
        return new Predicate<Attr>() {
            @Override
            public boolean test(Attr toTest) {

                Node grandParent = toTest.getOwnerElement().getParentNode();
				return !(
                        grandParent instanceof Document
                                && ("base".equals(toTest.getLocalName()))
                        );
            }

        };

    }

	private ValueAssert isEqualTo(ValueAssert vAssert, int expected){
		try {
			testCounter++;
			return vAssert.isEqualTo(expected);
		} catch (Throwable e){
			addMessage(e, vAssert.info.descriptionText());
			throw e;
		}
	}

	private ValueAssert isEqualTo(ValueAssert vAssert, String expected){
		try {
			this.testCounter++;
			return vAssert.isEqualTo(expected);
		} catch (Throwable e){
			addMessage(e, vAssert.info.descriptionText());
			throw e;
		}
	}

	private static void addMessage(Throwable e, String message){
		try {
			Field messageField = Throwable.class.
					getDeclaredField("detailMessage");
			messageField.setAccessible(true);
			messageField.set(e, message);
		} catch (NoSuchFieldException | IllegalAccessException e1) {
			System.err.println(e1.getLocalizedMessage());
			return;
		}
	}

	private static ValueAssert assertThatXPathValue(String xml, String xpath){
		return assertThat(xml).valueByXPath(xpath);
	}

    private static XmlAssert assertThat(String xml){
		return XmlAssert.assertThat(xml).withNamespaceContext(namespaceMap());
	}



	private static HashMap<String, String> namespaceMap(){
		HashMap<String, String> nsMap = new HashMap<>();
		nsMap.put("es", ProcessNamespaces.ES_NS);
		nsMap.put("sqf", ProcessNamespaces.SQF_NS);
		return nsMap;
	}
	
	private static StringNode ignore(TextSource xml){
		try {
			PositionalReplace pr = new PositionalReplace(xml, false);
			pr.deleteNode("//@es:ref|//@es:id "
					+ "| /svrl:schematron-output/@base"
					+ "| /svrl:schematron-output/@es:schema "
					+ "| /svrl:schematron-output/@es:instance "
					+ "| //@es:roleLabel|//@role "
					+ "| /es:escali-reports/es:meta/@instance "
					+ "| /es:escali-reports/es:meta/@schema "
					+ "| /es:escali-reports/es:meta/es:schema "
					+ "| /es:escali-reports/es:meta/es:instance "
					+ "| //es:meta/@id "
					+ "| //es:assert/@id "
					+ "| //es:report/@id "
					);

			pr.deleteNode("/*/sqf:topLevel "
					+ "| //sqf:sheet "
					+ "| //sqf:fix/@contextId "
					+ "| //es:assert/sqf:fix/@id "
					+ "| //es:report/sqf:fix/@id "
					+ "| //sqf:user-entry/@name "
					+ "| //sqf:with-param[@name != 'sqf:current']/@select "
					+ "| //sqf:fix/@messageId "
					+ "| //svrl:*/@sqf:default-fix "
					+ "| //sqf:param/@param-id");
			return pr.getSourceAsStringNode();
		} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
			e.printStackTrace();
			return null;
		}
	}
}
