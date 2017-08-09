package com.schematronQuickfix.escali;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.staxParser.StringNodeComparer;
import com.schematronQuickfix.escali.cmdInterface.Validation;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

public class SchematronTest {
	private static File rootFolder = new File("src/test/resources/sch/schematron-tests/");
	
	
	@Test
	public void test1a() {
		File schema = new File(rootFolder, "test01/input/test1.sch");
		File instance = new File(rootFolder, "test01/input/test1.xml");
		File expectedSVRL = new File(rootFolder, "test01/expected/test1a.svrl");
		testValidation(schema, instance, expectedSVRL, ConfigFactory.createDefaultConfig());
	}

	
	@Test
	public void test1b() {
		File schema = new File(rootFolder, "test01/input/test1.sch");
		File instance = new File(rootFolder, "test01/input/test1.xml");
		File expectedSVRL = new File(rootFolder, "test01/expected/test1b.svrl");
		testValidation(schema, instance, expectedSVRL, "#ALL");
	}
	
	private static void testValidation(File schema, File instance, File expectedSVRL, String phase){
		Config config = ConfigFactory.createDefaultConfig();
		config.setPhase(phase);
		testValidation(schema, instance, expectedSVRL, config);
	}
	private static void testValidation(File schema, File instance, File expectedSVRL, Config config){
		try {
			Validation val = new Validation(schema, config, new MuteProcessLoger());
			SVRLReport svrl = val.validate(instance);
			TextSource svrlTxt = svrl.getFormatetReport(SVRLReport.SVRL_FORMAT);
			
			TextSource expectedSvrlTxt = TextSource.readTextFile(expectedSVRL);
			
			
			StringNode expectedSN = ignore(expectedSvrlTxt);
			StringNode actualSN = ignore(svrlTxt);
			
			compareStringNodes(actualSN, expectedSN);
			
		} catch (XPathExpressionException | IOException | XSLTErrorListener | SAXException | URISyntaxException
				| XMLStreamException | CancelException e) {
			fail(e.getMessage());
		}
	}
	
//	private static String[] SVRL_CHECK_XPATHS = {
//			"/svrl:schematron-output/@title",
//			"/svrl:schematron-output/@phase",
//			"count(//*)",
//			"count(//@*)",
//			"count(//comment())",
//			"count(//processing-instruction())",
//			"string-join((//@* except //@xml:*)/concat(name(), '=\"', ., '\"'), '#')",
//			"string-join(//*/concat('<', name(), '>', normalize-space(.), '</', name(), '>'), '#')",
//
//			"string-join(//processing-instruction()/concat('<?', name(), ' ', normalize-space(.), '?>'), '#')",
//			"string-join(//comment()/concat('<!--', normalize-space(.), '-->'), '#')"
//			
//	};
	
	private static String[] XML_CHECK_XPATHS = {
			"count(//*)",
			"count(//@*)",
			"count(//comment())",
			"count(//processing-instruction())",
			"string-join((//@* except //@xml:*)/concat(name(), '=\"', ., '\"'), '#')",
			"string-join(//*/concat('<', name(), '>', normalize-space(.), '</', name(), '>'), '#')",

			"string-join(//processing-instruction()/concat('<?', name(), ' ', normalize-space(.), '?>'), '#')",
			"string-join(//comment()/concat('<!--', normalize-space(.), '-->'), '#')"
			
	};
	
	private static void compareStringNodes(StringNode result, StringNode expected){
		
		String actualStr = StringNodeComparer.getCompareString(result, new StringNodeComparer.CompareConfig());
		String expectedStr = StringNodeComparer.getCompareString(expected, new StringNodeComparer.CompareConfig());
		
		assertEquals(expectedStr, actualStr);
	}
	
	private static void checkXPaths(StringNode result, StringNode expected, String[] XPaths){
		for (String xpath : XPaths) {
			checkXPaths(result, expected, xpath);
		}
	}
	private static void checkXPaths(StringNode result, StringNode expected, String xpath){
		try {
			String resVal= xpath + ":" + result.getNodeValue(xpath);
			String expVal = xpath + ":" + expected.getNodeValue(xpath);
			assertEquals(expVal, resVal);
		} catch (XPathExpressionException e) {
			fail(e.getMessage());
		}
	}
	
	
	private static StringNode ignore(TextSource xml){
		try {
			PositionalReplace pr = new PositionalReplace(xml, false);
			pr.deleteNode("//@es:ref|//@es:id | /svrl:schematron-output/@es:schema | /svrl:schematron-output/@es:instance | //@es:roleLabel|//@role");
			return pr.getSourceAsStringNode();
		} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
			e.printStackTrace();
			return null;
		}
	}

}
