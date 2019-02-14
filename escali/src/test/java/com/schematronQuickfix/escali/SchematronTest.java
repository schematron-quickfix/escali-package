package com.schematronQuickfix.escali;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
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

public class SchematronTest {
	private static File rootFolder = new File("src/test/resources/sch/schematron-tests/");
	
	private final File folder;
	private String phase = null;
	private String lang = null;
	
	@Parameters(name = "{index}: {0} - {1} - {2}")
    public static Collection<Object[]> getLabels() {
    	DefaultProcessLoger.setDefaultProcessLogger(new MuteProcessLoger());
    	
        List<Object[]> labels = new ArrayList<Object[]>();
        labels.add(new Object[] {"test01-es-import", "#ALL", null});
        labels.add(new Object[] {"test01-es-import", null, null});
        labels.add(new Object[] {"test02-nested-es-import", "#ALL", null});
        labels.add(new Object[] {"test02-nested-es-import", "importPhase", null});
        
        labels.add(new Object[] {"test03-es-phase-inactive", "phase1", null});
        labels.add(new Object[] {"test03-es-phase-inactive", "phase3", null});
        labels.add(new Object[] {"test03-es-phase-inactive", "phase4", null});

        labels.add(new Object[] {"test04-abstract-patterns", "#ALL", null});
        labels.add(new Object[] {"test04-abstract-patterns", "phase1", null});
        labels.add(new Object[] {"test04-abstract-patterns", "phase2", null});
        
        labels.add(new Object[] {"test05-localization", null, "de"});
        labels.add(new Object[] {"test05-localization", null, "en"});
        
        labels.add(new Object[] {"test06-rule-extends-test", "phase1", null});
        labels.add(new Object[] {"test06-rule-extends-test", "phase2", null});
        labels.add(new Object[] {"test06-rule-extends-test", "phase3", null});
        
        labels.add(new Object[] {"test07-es-matchType", null, null});
        labels.add(new Object[] {"test08-es-import-phase", "phase1", null});
        
        labels.add(new Object[] {"test09-es-inactive", null, null});
        labels.add(new Object[] {"test09-es-inactive", "phase1", null});
        labels.add(new Object[] {"test09-es-inactive", "phase2", null});
        labels.add(new Object[] {"test09-es-inactive", "phase3", null});

        labels.add(new Object[] {"test10-es-getPhase-funct", "phase1", null});
        labels.add(new Object[] {"test10-es-getPhase-funct", "#ALL", null});
        labels.add(new Object[] {"test10-es-getPhase-funct", null, null});

        labels.add(new Object[] {"test11-es-getLang-funct", null, null});
        labels.add(new Object[] {"test11-es-getLang-funct", null, "#ALL"});
        labels.add(new Object[] {"test11-es-getLang-funct", null, "en"});
        
        labels.add(new Object[] {"test13-es-import-with-ns", null, null});
        
        labels.add(new Object[] {"test14-ns", null, null});
        
        labels.add(new Object[] {"test15-es-ignorable", "phase1", null});
        labels.add(new Object[] {"test15-es-ignorable", "phase2", null});
        labels.add(new Object[] {"test15-es-ignorable", "phase3", null});
        
        labels.add(new Object[] {"test16-document-uri-funct", null, null});
        

        labels.add(new Object[] {"test18-language", null, "de"});
        labels.add(new Object[] {"test18-language", null, "en"});
        
//        labels.add(new Object[] {"test17-arche-example", null, null});
        
        return labels;
        
    }
	
	public SchematronTest(String folder, String phase, String lang){
		this.folder = new File(rootFolder, folder);
		this.phase = phase;
		this.lang = lang;
	}


	@Before
	public void before() {
//		PENDING ALL
		assumeTrue("PENDING: replaced by test cases in package com.schematronQuickfix.escali.schematron", false);
	}
	
	@Test
	public void test(){
		
		String expected = "";
		expected += phase != null ? "_" + phase : "";
		expected += lang != null ? "_" + lang : "";
		
		File schema = new File(folder, "input/test.sch");
		File instance = new File(folder, "input/test.xml");
		File expectedSVRL = new File(folder, "expected/test" + expected + ".svrl");
		
		Config config = ConfigFactory.createDefaultConfig();
		if(this.phase != null){
			config.setPhase(phase);
		}
		if(this.lang != null){
			config.setLanguage(lang);
		}
		config.setCompactSVRL(false);
		
		
		testValidation(schema, instance, expectedSVRL, config);
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
