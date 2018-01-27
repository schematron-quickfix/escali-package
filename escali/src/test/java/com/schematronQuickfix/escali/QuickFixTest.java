package com.schematronQuickfix.escali;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
import org.xml.sax.SAXParseException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.cmdInterface.Fixing;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

@RunWith(value = Parameterized.class)

public class QuickFixTest {
	private static File rootFolder = new File("src/test/resources/sch/sqf-tests/");

	private static TextSource ignorePIsSheet;

	// private final File folder;
	// private int msgPos = -1;
	// private String fixName = null;
	//
	private File schema;
	private File instance;
	private File[] expectedResult;

	//
	// private String[] parameter;
	//
	// private String suffix;

	private QuickFixParameter testParams;

	static {
		String ignorePIsManipulatorSheet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsm:manipulator xmlns:xsm=\"http://www.schematron-quickfix.com/manipulator/process\" xmlns=\"http://www.escali.schematron-quickfix.com/null-namespace\"><xsm:replace node=\"//processing-instruction(sqfc-start)\"><xsm:content><?sqfc-start ?></xsm:content></xsm:replace><xsm:replace node=\"//processing-instruction(sqfc-end)\"><xsm:content><?sqfc-end ?></xsm:content></xsm:replace></xsm:manipulator>";
		ignorePIsSheet = TextSource.createVirtualTextSource(new File("manipulator.xsm"));
		ignorePIsSheet.setData(ignorePIsManipulatorSheet);

	}

	private static class QuickFixParameter {
		private File schema;
		private File source;
		private int msgPos;
		private String fixName;
		private String[] parameter;
		private String[] suffixes = new String[]{null};
		private int status;
		private Config escaliConfig = ConfigFactory.createDefaultConfig();
		private String title;
		private Exception exception = null;

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, int status) {
			this(title, schema, source, msgPos, fixName, null, status);
		}

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, String[] parameter, int status) {
			this(title, schema, source, msgPos, fixName, parameter, new String[]{null}, status);
		}

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, String[] parameter, String[] suffixes,
				int status) {
			this.title = title;
			this.schema = schema;
			this.source = source;
			this.msgPos = msgPos;
			this.fixName = fixName;
			this.parameter = parameter;
			if(suffixes != null){
				this.suffixes = suffixes;
			}
			this.status = status;

			if (status == FOCUS) {
				focused.add(this);
			}
		}
		
		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, String[] parameter, Exception exception,
				int status) {
			this.title = title;
			this.schema = schema;
			this.source = source;
			this.msgPos = msgPos;
			this.fixName = fixName;
			this.parameter = parameter;
			this.status = status;
			this.exception = exception;
			if (status == FOCUS) {
				focused.add(this);
			}
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String name = "";
			
			name += status == FOCUS ? "FOCUS - " : status == PENDING ? "SKIPPED - " : "";
			
			name += title + " - ";
			name += msgPos + " - ";
			name += fixName + " - ";
			if (parameter != null) {
				for (String param : parameter) {
					name += param + " - ";
				}
			}
			name += suffixes != null ? suffixes : "";

			return name;
		}
	}

	public static int AUTO = 0;
	public static int FOCUS = 1;
	public static int PENDING = 2;

	private static ArrayList<QuickFixParameter> focused = new ArrayList<QuickFixTest.QuickFixParameter>();

	private static class QuickFixTestGroup extends ArrayList<QuickFixParameter> {
		private File schema;
		private File source;
		private String title;

		private int status;

		public QuickFixTestGroup(String folderName, int status) {
			this(folderName, "input/test.sch", "input/test.xml", status);
		}

		public QuickFixTestGroup(String folderName) {
			this(folderName, AUTO);
		}
		
		public QuickFixTestGroup(String folderName, String schema, String source, int status) {
			this.status = status;
			this.schema = new File(rootFolder, folderName + "/" + schema );
			this.source = new File(rootFolder, folderName + "/" + source);
			this.title = folderName;
		}
		
		public QuickFixTestGroup(String folderName, String schema, String source) {
			this(folderName, schema, source, AUTO);
		}

		public void createTest(int msgPos, String fixName) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter, String suffix) {
			createTest(msgPos, fixName, parameter, new String[]{suffix});
		}

		public void createTest(int msgPos, String fixName, String[] parameter, String[] suffix) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, suffix, status));
		}

		public void createTest(int msgPos, String fixName, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter, String suffix, int status) {
			createTest(msgPos, fixName, parameter, new String[]{suffix}, status);
		}
		
		public void createTest(int msgPos, String fixName, String[] parameter, String[] suffix, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, suffix, status));
		}
		
		public void createTest(int msgPos, String fixName, String[] parameter, Exception exception, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, exception, status));
		}

		public void addToList(List<Object[]> list) {
			for (QuickFixParameter test : this) {
				list.add(new Object[] { test });
			}
		}

		public void setConfig(Config escaliConfig) {
			for (QuickFixParameter testParam : this) {
				testParam.escaliConfig = escaliConfig;
			}
		}

	}

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> getLabels() {
		List<Object[]> labels = new ArrayList<Object[]>();
		DefaultProcessLoger.setDefaultProcessLogger(new MuteProcessLoger());

		// test01
		QuickFixTestGroup test01 = new QuickFixTestGroup("test01-basic-localisation-html");

		test01.createTest(1, "xml_lang");
		test01.createTest(3, "xml_lang");
		test01.createTest(4, "lang_xml_lang", new String[] { "lang=it" });

		test01.addToList(labels);

		// test02
		QuickFixTestGroup test02 = new QuickFixTestGroup("test02-basic-html-headlines");

		test02.createTest(1, "delete");
		test02.createTest(1, "add2bodyH1", new String[] { "h1=This is a new headline" }, "1");
		test02.createTest(1, "add2bodyH1", new String[] { "h1=Headlines" }, "2");
		test02.createTest(1, "setH1");

		test02.addToList(labels);
		

		// test03
		QuickFixTestGroup test03 = new QuickFixTestGroup("test03-namespace-handling");

		test03.createTest(1, "addElementWithNs1");
		test03.createTest(2, "addElementWithNs1");
		test03.createTest(3, "addElementWithNs1");

		test03.createTest(1, "addElementWithPrfxConflict");
		test03.createTest(2, "addElementWithPrfxConflict");
		test03.createTest(3, "addElementWithPrfxConflict");
		
		test03.createTest(1, "addElementWithNs2");
		test03.createTest(2, "addElementWithNs2");
		test03.createTest(3, "addElementWithNs2");
		
		test03.createTest(1, "addElementWithNullNs");
		test03.createTest(2, "addElementWithNullNs");
		test03.createTest(3, "addElementWithNullNs");
		
		test03.createTest(1, "addAttributeWithNs1");
		test03.createTest(2, "addAttributeWithNs1");
		test03.createTest(3, "addAttributeWithNs1");

//		Implementation of xmlns:prx_{x} is missing
		test03.createTest(1, "addAttributeWithPrfxConflict", PENDING);
		test03.createTest(2, "addAttributeWithPrfxConflict", PENDING);
		test03.createTest(3, "addAttributeWithPrfxConflict", PENDING);
		
		test03.createTest(1, "addAttributeWithNs2");
		test03.createTest(2, "addAttributeWithNs2");
		test03.createTest(3, "addAttributeWithNs2");
		
		test03.createTest(1, "addAttributeWithNullNs");
		test03.createTest(2, "addAttributeWithNullNs");
		test03.createTest(3, "addAttributeWithNullNs");
		
		test03.createTest(3, "addElementWithNs2AsDef");
		
		test03.addToList(labels);

		// test04
		QuickFixTestGroup test04 = new QuickFixTestGroup("test04-basic-html-titles");

		test04.createTest(2, "cutTitle");
		test04.createTest(1, "deleteComment");
		test04.createTest(2, "setTitle_1");
		test04.createTest(2, "newTitle", new String[] { "newText=This is a new title" });
		test04.createTest(2, "takeComment");
		test04.createTest(2, "setTitle_3");

		test04.addToList(labels);

		// test05
		QuickFixTestGroup test05 = new QuickFixTestGroup("test05-basic-table-columns");

		test05.createTest(2, "last");
		test05.createTest(1, "proportional");
		test05.createTest(2, "setOneWidth", new String[] { "colNumber=3" });

		test05.addToList(labels);

		// test06

		QuickFixTestGroup test06 = new QuickFixTestGroup("test06-basic-table-spans");

		test06.createTest(1, "deleteColspan");
		test06.createTest(1, "deleteRowspan");
		test06.createTest(4, "addLost");
		test06.createTest(6, "addLost");
		test06.createTest(9, "delete‹berschuss");

		test06.addToList(labels);

		// test07

		QuickFixTestGroup test07 = new QuickFixTestGroup("test07-basic-pis");

		test07.createTest(1, "delete");
		test07.createTest(1, "replace");
		test07.createTest(1, "setName", new String[] { "newName=sqf_new-pi-name" });

		test07.addToList(labels);

		// test08

		QuickFixTestGroup test08 = new QuickFixTestGroup("test08-basic-docbook-lists");

		test08.createTest(1, "delete");
		test08.createTest(1, "plain");

		test08.addToList(labels);

		// test09

		QuickFixTestGroup test09 = new QuickFixTestGroup("test09-basic-docbook-footnotes");

		test09.createTest(1, "delete");
		test09.createTest(1, "brackets");
		test09.createTest(1, "parentBrackets");

		test09.addToList(labels);

		// test10

		QuickFixTestGroup test10 = new QuickFixTestGroup("test10-basic-stringReplace");

		test10.createTest(1, "autoStringReplace");
		test10.createTest(2, "autoStringReplace");
		test10.createTest(3, "autoStringReplace");

		test10.addToList(labels);

		// test11

		QuickFixTestGroup test11 = new QuickFixTestGroup("test11-dtd-decl");

		test11.createTest(1, "deleteB");

		test11.addToList(labels);

		// test12
		QuickFixTestGroup test12 = new QuickFixTestGroup("test12-hide-changeMarker");

		Config test12Config = ConfigFactory.createDefaultConfig();
		test12Config.setChangePrefix("");

		test12.createTest(1, "lang");
		test12.createTest(3, "lang_xml_lang", new String[] { "lang=it" });
		test12.createTest(5, "xml_lang");


		test12.setConfig(test12Config);

		test12.addToList(labels);

		// test13
		QuickFixTestGroup test13 = new QuickFixTestGroup("test13-sqf-group");

		test13.createTest(1, "delete");
		test13.createTest(1, "add2bodyH1", new String[] { "h1=This is a new headline" }, "1");
		test13.createTest(1, "add2bodyH1", new String[] { "h1=Headlines" }, "2");
		test13.createTest(2, "add2", new String[] { "h2=new headline 2", "h3=new headline 3" });

		test13.addToList(labels);

		// test15
		QuickFixTestGroup test15 = new QuickFixTestGroup("test15-use-for-each");

		test15.createTest(1, "lang_xml_lang_foreach_1");
		test15.createTest(1, "lang_xml_lang_foreach_2");

		test15.addToList(labels);

		// test16
		QuickFixTestGroup test16 = new QuickFixTestGroup("test16-inconsistency");
		
//		depending on Saxon 9.4
		test16.createTest(1, "addFirstChild");
		test16.createTest(1, "addLastChild");
		test16.createTest(2, "addFirstChild");
		test16.createTest(2, "addLastChild");
		test16.createTest(2, "addBefore");
		test16.createTest(2, "addAfter");
		test16.createTest(1, "addAttr");
		test16.createTest(2, "addAttr");
		

		QuickFixTestGroup test16_2 = new QuickFixTestGroup("test16-inconsistency", "input/test.sch", "input/test_2.xml");
		
		test16_2.createTest(1, "addLastChild", null, "2");
		test16_2.createTest(1, "addFirstChild", null, "2");
		test16_2.createTest(1, "addAttr", null, "2");

		test16.addToList(labels);
		test16_2.addToList(labels);
		

		// test17

		QuickFixTestGroup test17 = new QuickFixTestGroup("test17-errors");

		test17.createTest(1, "replaceAtt", null, new XSLTErrorListener(), AUTO);

		test17.addToList(labels);
		
		// test18

		QuickFixTestGroup test18 = new QuickFixTestGroup("test18-multiple-documents");

		test18.createTest(1, "setLang_1");
		test18.createTest(1, "addToConfig", null, new String[]{"main", "config"});

		test18.addToList(labels);


		// test19
		QuickFixTestGroup test19 = new QuickFixTestGroup("test19");

		test19.addToList(labels);
		
		// test20

		QuickFixTestGroup test20 = new QuickFixTestGroup("test20-oXygen-bugs");

		test20.createTest(1, "fix");
		test20.createTest(1, "deleteOther");
		test20.createTest(2, "replace");
		test20.createTest(3, "delete");
		test20.createTest(4, "delete2");

		test20.addToList(labels);

		// test21

		QuickFixTestGroup test21 = new QuickFixTestGroup("test21-Abstract-pattern-rules");

		test21.createTest(1, "fix");
		test21.createTest(1, "deleteOther");
		test21.createTest(3, "delete");
		test21.createTest(4, "abstractRule.delete");
		test21.createTest(12, "R2.F22");

		test21.addToList(labels);
		
		// test22

		QuickFixTestGroup test22 = new QuickFixTestGroup("test22-pending group 22");

		test22.addToList(labels);
		
		
		// test23

		QuickFixTestGroup test23 = new QuickFixTestGroup("test23-add-select-different-node-types");

		test23.createTest(1, "addToNext");
		test23.createTest(1, "addAsChild");
		test23.createTest(1, "addBoth");
		test23.createTest(1, "addBothMixed");
		test23.createTest(1, "addAttAsLastChild");

		test23.addToList(labels);


		// test24

		QuickFixTestGroup test24 = new QuickFixTestGroup("test24-add-select-with-atomic-values");

		test24.createTest(1, "addToNext");
		test24.createTest(1, "addAsChild");
		test24.createTest(1, "addBoth");
		test24.createTest(1, "addBothMixed");
		test24.createTest(1, "addUserEntry", new String[]{"ue=user-entry-value"});

		test24.addToList(labels);

		// test25

		QuickFixTestGroup test25 = new QuickFixTestGroup("test25-ignore-escali-extension");

		test25.createTest(1, "ignore_attrValue");
		test25.createTest(5, "ignore_badContent");

		test25.addToList(labels);

		// test26

		QuickFixTestGroup test26 = new QuickFixTestGroup("test26-description-with-call-fix");

		test26.addToList(labels);

		// test27

		QuickFixTestGroup test27 = new QuickFixTestGroup("test27-namespace-handling");

		test27.createTest(1, "nsConvertionPrx");
		test27.createTest(1, "nsConvertion");
		test27.createTest(2, "nsConvertionPrx");
		test27.createTest(2, "nsConvertion");
		test27.createTest(3, "nsConvertionNull");

		test27.addToList(labels);

		// test28

		QuickFixTestGroup test28 = new QuickFixTestGroup("test28-oxygen-bugs-2");

		test28.createTest(1, "fix");
		test28.createTest(1, "deleteOther");
		test28.createTest(2, "replace");
		test28.createTest(3, "delete");
		test28.createTest(4, "delete2");

		test28.addToList(labels);


		// test29

		QuickFixTestGroup test29 = new QuickFixTestGroup("test29-pending group 29");

		test29.addToList(labels);

		// test30

		QuickFixTestGroup test30 = new QuickFixTestGroup("test30-StringReplace-with-flags");

		test30.createTest(1, "replaceQF");
		test30.createTest(1, "replaceQFwithFlags");

		test30.addToList(labels);

		// test31

		QuickFixTestGroup test31 = new QuickFixTestGroup("test31-pending group 31");

		test31.addToList(labels);
		
		// test32

		QuickFixTestGroup test32 = new QuickFixTestGroup("test32-sqf-copy-of");

		test32.createTest(1, "copy-of");

		test32.addToList(labels);


		// test35

		QuickFixTestGroup test35 = new QuickFixTestGroup("test35-xinclude");

		test35.createTest(1, "addElement", null, new String[]{"main", "included"});
		test35.createTest(1, "addAllElement", null, new String[]{"main", "included"});
		test35.createTest(1, "deleteAllIdAttr", null, new String[]{"main", "included"});
		test35.createTest(1, "replaceAllElement", null, new String[]{"main", "included"});
		test35.createTest(2, "addElement");
		test35.createTest(2, "deleteIdAttr");
		test35.createTest(2, "replaceElement");

		test35.addToList(labels);
		

		// test36

		QuickFixTestGroup test36a = new QuickFixTestGroup("test36-namespace-conflict", "input/test-a.sch", "input/test.xml");

		test36a.createTest(1, "replaceDefault");
		test36a.createTest(1, "replaceOther");
		test36a.createTest(1, "replaceNull");
		
		QuickFixTestGroup test36b = new QuickFixTestGroup("test36-namespace-conflict", "input/test-b.sch", "input/test.xml");

		test36b.createTest(1, "replaceDefault");
		test36b.createTest(1, "replaceOther");
		test36b.createTest(1, "replaceNull");
		test36b.createTest(1, "replaceNone");

		QuickFixTestGroup test36c = new QuickFixTestGroup("test36-namespace-conflict", "input/test-c.sch", "input/test.xml");

		test36c.createTest(1, "replaceDefault");
		test36c.createTest(1, "replaceOther");
		test36c.createTest(1, "replaceNull");
		
		test36a.addToList(labels);
		test36b.addToList(labels);
		test36c.addToList(labels);
		

		QuickFixTestGroup test37a = new QuickFixTestGroup("test37-lets-in-patterns", "input/test-a.sch", "input/test.xml");

		test37a.createTest(1, "replace");
		
		QuickFixTestGroup test37b = new QuickFixTestGroup("test37-lets-in-patterns", "input/test-b.sch", "input/test.xml");

		test37b.createTest(1, "replace");
		
		test37a.addToList(labels);
		test37b.addToList(labels);
		
		return labels;

	}

	// public QuickFixTest(String folder, String msgPosStr, String fixName,
	// String[] parameter, String suffix){

	public QuickFixTest(QuickFixParameter qfParam) {
		this.testParams = qfParam;
	}

	@Before
	public void before() {
		assumeTrue(testParams.status != PENDING);
		assumeTrue(testParams.status == FOCUS || focused.size() == 0);
	}

	@Test
	public void test() {
		
		this.expectedResult = new File[this.testParams.suffixes.length];
		int i = 0;
		for (String suffix : this.testParams.suffixes) {
			String expected = "";
			expected += testParams.schema.getName().replaceAll("\\.sch$", "");
			expected += "_" + testParams.msgPos;
			expected += testParams.fixName != null ? "_" + testParams.fixName : "";
			expected += suffix != null ? "_" + suffix : "";
			
			this.expectedResult[i++] = new File(testParams.schema, "../../expected/" + expected + ".xml");
			
		}

		testQuickFix(testParams.escaliConfig);

		// }

		// Config config = ConfigFactory.createDefaultConfig();
		// if(this.msgPosStr != null){
		// config.setPhase(msgPosStr);
		// }
		// if(this.fixName != null){
		// config.setLanguage(fixName);
		// }
		// config.setCompactSVRL(false);

	}

	private void testQuickFix(Config config) {
		Fixing fix;
		try {

			fix = new Fixing(this.testParams.schema, this.testParams.source, config);
			
			ArrayList<TextSource> result = fix.executeFix(this.testParams.msgPos - 1, this.testParams.fixName,
					this.testParams.parameter);
			
			if(result.size() < expectedResult.length){
				System.out.println("EXPECTED: ");
				for (File eresult : expectedResult) {
					System.out.println(eresult.getAbsolutePath());
				}
				System.out.println("RESULT: ");
				for (TextSource r : result) {
					System.out.println(r.getFile().getAbsolutePath());
				}
				
				fail("Some Results are missing!");
			}
			if(testParams.exception != null){
				fail("The result should stop with an exception " + testParams.exception.getClass().getName());
			}
			for (int i = 0; i < expectedResult.length; i++) {
				TextSource expected = TextSource.readTextFile(this.expectedResult[i]);
				PositionalReplace ignorePIs = new PositionalReplace(ignorePIsSheet, result.get(i));
				
				assertEquals(expected.toString(), ignorePIs.getSource().toString());
				assertArrayEquals(expected.toString().toCharArray(), ignorePIs.getSource().toString().toCharArray());
				
			}


		} catch (XPathExpressionException | XSLTErrorListener | IOException | SAXException | URISyntaxException
				| XMLStreamException | CancelException e) {
			if(testParams.exception == null){
				fail(e.getMessage());
			} else {
				assertEquals(e.getClass().getName(), testParams.exception.getClass().getName());
			}
		}
	}

	// private static void testValidation(File schema, File instance, File
	// expectedSVRL, String phase){
	// Config config = ConfigFactory.createDefaultConfig();
	// config.setPhase(phase);
	// testValidation(schema, instance, expectedSVRL, config);
	// }
	// private static void testValidation(File schema, File instance, int
	// msgPos, String fixName, File expectedSVRL, Config config){
	// try {
	// Fixing fix = new Fixing(schema, instance, config);
	// ArrayList<TextSource> result = fix.executeFix(msgPos, fixName);
	//
	//
	// Validation val = new Validation(schema, config, new MuteProcessLoger());
	// SVRLReport svrl = val.validate(instance);
	// TextSource svrlTxt = svrl.getFormatetReport(SVRLReport.SVRL_FORMAT);
	//
	// TextSource expectedSvrlTxt = TextSource.readTextFile(expectedSVRL);
	//
	//
	// StringNode expectedSN = ignore(expectedSvrlTxt);
	// StringNode actualSN = ignore(svrlTxt);
	//
	// compareStringNodes(actualSN, expectedSN);
	//
	// } catch (XPathExpressionException | IOException | XSLTErrorListener |
	// SAXException | URISyntaxException
	// | XMLStreamException | CancelException e) {
	// fail(e.getMessage());
	// }
	// }
	//
	//
	// private static void compareStringNodes(StringNode result, StringNode
	// expected){
	//
	// String actualStr = StringNodeComparer.getCompareString(result, new
	// StringNodeComparer.CompareConfig());
	// String expectedStr = StringNodeComparer.getCompareString(expected, new
	// StringNodeComparer.CompareConfig());
	//
	// assertEquals(expectedStr, actualStr);
	// }
	//
	//
	//
	// private static StringNode ignore(TextSource xml){
	// try {
	// PositionalReplace pr = new PositionalReplace(xml, false);
	// pr.deleteNode("//@es:ref|//@es:id | /svrl:schematron-output/@es:schema |
	// /svrl:schematron-output/@es:instance | //@es:roleLabel|//@role");
	// pr.deleteNode("/*/sqf:topLevel | //sqf:sheet | //sqf:fix/@*");
	// return pr.getSourceAsStringNode();
	// } catch (XPathExpressionException | IOException | SAXException |
	// XMLStreamException | CancelException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

}
