package com.schematronQuickfix.escali;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
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

@RunWith(value = Parameterized.class)

public class SqfSchTest {
	private static File rootFolder = new File("src/test/resources/sch/sqf.sch-tests/");
	private static File sqfSch = new File("src/main/schema/SQF/sqf.sch");
	private static Validation baseValidator = null;
	static {
		Config config = ConfigFactory.createDefaultConfig();
		config.setCompactSVRL(false);
		Validation val;
		try {
			 val = new Validation(sqfSch, config, new MuteProcessLoger());
			 baseValidator = val;
		} catch (XSLTErrorListener | IOException | CancelException e) {
		    e.printStackTrace();
			val = null;
		}
		
		
		
	}
	
	private final File schema;
	private String phase = null;
	private String lang = null;

	private boolean isValidTest;
	private String baseId;
	
	@Parameters(name = "{index}: {0} - {1} - {2}")
    public static Collection<Object[]> getLabels() {
    	DefaultProcessLoger.setDefaultProcessLogger(new MuteProcessLoger());
    	
        List<Object[]> labels = new ArrayList<Object[]>();
        String folder = "embedding/embedding_1";
        boolean isValid = false;
        String testId = "embedding_1-1";
        labels.add(new Object[] {folder, isValid, "embedding_1-1-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "embedding_1-1-i-b.sch", testId});
        
        isValid = true;
        
        labels.add(new Object[] {folder, isValid, "embedding_1-1-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "embedding_1-1-v-b.sch", testId});
        
        folder = "embedding/embedding_2";
        testId = "embedding_2-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, "embedding_2-1-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "embedding_2-1-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, "embedding_2-1-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, "embedding_2-1-i-d.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, "embedding_2-1-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "embedding_2-1-v-b.sch", testId});
        

        folder = "references/references_1";
        testId = "references_1-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, "references_1-1-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_1-1-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_1-1-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_1-1-i-d.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, "references_1-1-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_1-1-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_1-1-v-c.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_1-1-v-d.sch", testId});
        

        folder = "references/references_2";
        testId = "references_2-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, "references_2-1-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_2-1-i-b.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, "references_2-1-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, "references_2-1-v-b.sch", testId});
        
        folder = "quickfix-id/quickfix-id_1";
        testId = "quickfix-id_1-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-e.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-f.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        
        folder = "default-fix/default-fix_1";
        testId = "default-fix_1-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        
        testId = "default-fix_1-2";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        
        folder = "activity-elements/activity-elements_1";
        testId = "activity-elements_1-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        isValid = true;
        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-d.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-e.sch", testId});

        folder = "activity-elements/activity-elements_2";
        testId = "activity-elements_2-1";
        isValid = false;
        

        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-d.sch", testId});
        
        testId = "activity-elements_2-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-d.sch", testId});

        folder = "activity-elements/activity-elements_3";
        testId = "activity-elements_3-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-e.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-f.sch", testId});
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});

        labels.add(new Object[] {folder, isValid, testId + "-v-d.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-e.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-f.sch", testId});
        

        folder = "activity-elements2/activity-elements2_1";
        testId = "activity-elements2_1-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        
        folder = "activity-elements3/activity-elements3_1";
        testId = "activity-elements3_1-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        
        folder = "activity-elements3/activity-elements3_2";
        testId = "activity-elements3_2-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        
        testId = "activity-elements3_2-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        

        
        folder = "generic-fixes/generic-fixes_1";
        testId = "generic-fixes_1-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-d.sch", testId});
        

        testId = "generic-fixes_1-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        

        folder = "generic-fixes/generic-fixes_2";
        testId = "generic-fixes_2-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        

        testId = "generic-fixes_2-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        

        testId = "generic-fixes_2-3";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-d.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-e.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-f.sch", testId});
        

        folder = "generic-fixes/generic-fixes_3";
        testId = "generic-fixes_3-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        
        
        folder = "descriptions/descriptions_1";
        testId = "descriptions_1-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-e.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-f.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});

        testId = "descriptions_1-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        

        folder = "descriptions/descriptions_2";
        testId = "descriptions_2-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});

        testId = "descriptions_2-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-c.sch", testId});
        
        folder = "localisation/localisation_1";
        testId = "localisation_1-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        
        folder = "localisation/localisation_2";
        testId = "localisation_2-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        
        folder = "localisation/localisation_3";
        testId = "localisation_3-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-c.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-d.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-v-b.sch", testId});
        

        folder = "localisation/localisation_4";
        testId = "localisation_4-1";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        labels.add(new Object[] {folder, isValid, testId + "-i-b.sch", testId});
        
        
        isValid = true;

        labels.add(new Object[] {folder, isValid, testId + "-v-a.sch", testId});
        

        testId = "localisation_4-2";
        isValid = false;
        
        labels.add(new Object[] {folder, isValid, testId + "-i-a.sch", testId});
        
        return labels;
        
    }
	
	public SqfSchTest(String folder, boolean isValidTest, String file, String baseId){
		this.baseId = baseId;
		String filePath = folder+ "/" + (isValidTest ? "valid" : "invalid") + "/" + file;
		this.schema = new File(rootFolder, filePath);
		this.isValidTest = isValidTest;
	}
	
	
	@Test
	public void test(){
		
		
		
		
		if(baseValidator != null){
			try {
				SVRLReport svrl = baseValidator.validate(schema);
				StringNode svrlNode = new StringNode(svrl.getSVRL());
				NodeList nodes = svrlNode.getNodeSet("//(*:failed-assert|*:successful-report)[@es:base-id = '" + this.baseId + "']");
				
				if(this.isValidTest){
					assertEquals(0, nodes.getLength());
				} else {
					assertEquals("At least one error should occur!", 1, nodes.getLength());
				}
				
			} catch (XPathExpressionException | IOException | XSLTErrorListener | SAXException | URISyntaxException
					| XMLStreamException e) {
				fail(e.getMessage());
			}
		} else {
			fail("Failed to compile sqf.sch!");
		}
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
	
	
	private static void compareStringNodes(StringNode result, StringNode expected){
		
		String actualStr = StringNodeComparer.getCompareString(result, new StringNodeComparer.CompareConfig());
		String expectedStr = StringNodeComparer.getCompareString(expected, new StringNodeComparer.CompareConfig());
		
		assertEquals(expectedStr, actualStr);
	}
	
	
	
	private static StringNode ignore(TextSource xml){
		try {
			PositionalReplace pr = new PositionalReplace(xml, false);
			pr.deleteNode("//@es:ref|//@es:id | /svrl:schematron-output/@es:schema | /svrl:schematron-output/@es:instance | //@es:roleLabel|//@role");
			pr.deleteNode("/*/sqf:topLevel | //sqf:sheet | //sqf:fix/@*");
			return pr.getSourceAsStringNode();
		} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
			e.printStackTrace();
			return null;
		}
	}

}
