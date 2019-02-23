package com.schematronQuickfix.escali.helpers;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
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
	
	public ValidationTestStrategy(ProcessLoger logger) {
		this.logger = logger;
	}



	public SVRLReport executeStandardValidation(EscaliTestPair testPair) throws XSLTErrorListener, IOException, CancelException, SAXException, XMLStreamException, XPathExpressionException, URISyntaxException {
		SchematronInstancePair inputPair = testPair.getInputPair();
		Validation val = new Validation(inputPair.getSchemaDocument(), testPair.getEscaliConfig(), logger);
		SVRLReport report = val.validate(inputPair.getInstanceDocument());
		return report;
	}

	public void testStandardValidation(EscaliTestPair testPair){

		try {

			SVRLReport report = executeStandardValidation(testPair);

			TextSource svrlTxt = report.getFormatetReport(SVRLReport.SVRL_FORMAT);
			
			StringNode expectedSN = ignore(testPair.getExpected().get(0));
			StringNode actualSN = ignore(svrlTxt);
			
			assertThat(
                    actualSN.getDocument(),
                    isIdenticalTo(expectedSN.getDocument())
                            .normalizeWhitespace()
                            .ignoreComments()
                            .withAttributeFilter(noBaseAttribute())
                            .throwComparisonFailure()
            );
			
			
		} catch (XSLTErrorListener 
				| IOException 
				| CancelException 
				| XPathExpressionException 
				| SAXException 
				| URISyntaxException 
				| XMLStreamException e) {
			fail(e.getMessage());
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
	
	private static StringNode ignore(TextSource xml){
		try {
			PositionalReplace pr = new PositionalReplace(xml, false);
			pr.deleteNode("//@es:ref|//@es:id "
					+ "| /svrl:schematron-output/@base"
					+ "| /svrl:schematron-output/@es:schema "
					+ "| /svrl:schematron-output/@es:instance "
					+ "| //@es:roleLabel|//@role "
					);
			pr.deleteNode("/*/sqf:topLevel "
					+ "| //sqf:sheet "
					+ "| //sqf:fix/@* "
					+ "| //svrl:*/@sqf:default-fix "
					+ "| //sqf:param/@param-id");
			return pr.getSourceAsStringNode();
		} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
			e.printStackTrace();
			return null;
		}
	}
}
