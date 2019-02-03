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

public class EscaliExtTest {
	private static File rootFolder = new File("src/test/resources/sch/escali-ext-tests/");

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
		private String[] suffixes = new String[] { null };
		private int status;
		private Config escaliConfig = ConfigFactory.createDefaultConfig();
		private String title;
		private Exception exception = null;

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, int status) {
			this(title, schema, source, msgPos, fixName, null, status);
		}

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, String[] parameter,
				int status) {
			this(title, schema, source, msgPos, fixName, parameter, new String[] { null }, status);
		}

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, String[] parameter,
				String[] suffixes, int status) {
			this.title = title;
			this.schema = schema;
			this.source = source;
			this.msgPos = msgPos;
			this.fixName = fixName;
			this.parameter = parameter;
			if (suffixes != null) {
				this.suffixes = suffixes;
			}
			this.status = status;

			if (status == FOCUS) {
				focused.add(this);
			}
		}

		public QuickFixParameter(String title, File schema, File source, int msgPos, String fixName, String[] parameter,
				Exception exception, int status) {
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

	private static ArrayList<QuickFixParameter> focused = new ArrayList<EscaliExtTest.QuickFixParameter>();

	private static class QuickFixTestGroup extends ArrayList<QuickFixParameter> {
		private File schema;
		private File source;
		private String title;
		private Config esConf = ConfigFactory.createDefaultConfig();

		private int status;

		public QuickFixTestGroup(String folderName, int status) {
			this(folderName, "input/test.sch", "input/test.xml", status);
		}

		public QuickFixTestGroup(String folderName) {
			this(folderName, AUTO);
		}

		public QuickFixTestGroup(String folderName, String schema, String source, int status) {
			this.status = status;
			this.schema = new File(rootFolder, folderName + "/" + schema);
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
			createTest(msgPos, fixName, parameter, new String[] { suffix });
		}

		public void createTest(int msgPos, String fixName, String[] parameter, String[] suffix) {
			this.add(
					new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, suffix, status));
		}

		public void createTest(int msgPos, String fixName, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter, String suffix, int status) {
			createTest(msgPos, fixName, parameter, new String[] { suffix }, status);
		}

		public void createTest(int msgPos, String fixName, String[] parameter, String[] suffix, int status) {
			this.add(
					new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, suffix, status));
		}

		public void createTest(int msgPos, String fixName, String[] parameter, Exception exception, int status) {
			this.add(new QuickFixParameter(title, this.schema, this.source, msgPos, fixName, parameter, exception,
					status));
		}

		public void addToList(List<Object[]> list) {
			for (QuickFixParameter test : this) {
				test.escaliConfig = this.esConf;
				list.add(new Object[] { test });
			}
		}

		public void setConfig(Config escaliConfig) {
			this.esConf = escaliConfig;
		}

		public Config getConfig() {
			return esConf;
		}

	}

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> getLabels() {
		DefaultProcessLoger.setDefaultProcessLogger(new MuteProcessLoger());
		List<Object[]> labels = new ArrayList<Object[]>();

		// test01 with phase1
		QuickFixTestGroup test01a = new QuickFixTestGroup("test01-es-regex");
		String phase = "phase1";
		test01a.title += " - " + phase;
		test01a.getConfig().setPhase(phase);

		test01a.createTest(1, "replace", null, phase);
		test01a.createTest(1, "addBefore", null, phase);
		test01a.createTest(1, "addAfter", null, phase);
		test01a.createTest(1, "addBeforeSpec", null, phase);
		test01a.createTest(1, "addAfterSpec", null, phase);
		test01a.createTest(1, "delete", null, phase);
		test01a.createTest(1, "replaceSpec", null, phase);

		test01a.addToList(labels);

		// test01 with phase2
		QuickFixTestGroup test01b = new QuickFixTestGroup("test01-es-regex");
		phase = "phase2";
		test01b.title += " - " + phase;
		test01b.getConfig().setPhase(phase);

		test01b.createTest(1, "replace", null, phase);
		test01b.createTest(1, "addBefore", null, phase);
		test01b.createTest(1, "addAfter", null, phase);
		test01b.createTest(1, "addBeforeSpec", null, phase);
		test01b.createTest(1, "addAfterSpec", null, phase);
		test01b.createTest(1, "delete", null, phase);
		test01b.createTest(1, "replaceSpec", null, phase);

		test01b.addToList(labels);

		// test01 with phase3
		// phase3 will not match as u.a. is not in the same text node!

		// test01 with phase4
		QuickFixTestGroup test01d = new QuickFixTestGroup("test01-es-regex", PENDING);
		phase = "phase4";
		test01d.title += " - " + phase;
		test01d.getConfig().setPhase(phase);

		test01d.createTest(1, "replace", null, phase);
		test01d.createTest(1, "addBefore", null, phase);
		test01d.createTest(1, "addAfter", null, phase);
		test01d.createTest(1, "addBeforeSpec", null, phase);
		test01d.createTest(1, "addAfterSpec", null, phase);
		test01d.createTest(1, "delete", null, phase);
		test01d.createTest(1, "replaceSpec", null, phase);

		test01d.addToList(labels);

		// test01 with phase5
		QuickFixTestGroup test01e = new QuickFixTestGroup("test01-es-regex");
		phase = "phase5";
		test01e.title += " - " + phase;
		test01e.getConfig().setPhase(phase);

		test01e.createTest(1, "replace", null, phase);
		test01e.createTest(1, "addBefore", null, phase);
		test01e.createTest(1, "addAfter", null, phase);
		test01e.createTest(1, "addBeforeSpec", null, phase);
		test01e.createTest(1, "addAfterSpec", null, phase);
		test01e.createTest(1, "delete", null, phase);
		test01e.createTest(1, "replaceSpec", null, phase);

		test01e.addToList(labels);

		// test01 with phase6
		QuickFixTestGroup test01f = new QuickFixTestGroup("test01-es-regex");
		phase = "phase6";
		test01f.title += " - " + phase;
		test01f.getConfig().setPhase(phase);

		test01f.createTest(1, "replace", null, phase);
		test01f.createTest(1, "addBefore", null, phase);
		test01f.createTest(1, "addAfter", null, phase);
		test01f.createTest(1, "addBeforeSpec", null, phase);
		test01f.createTest(1, "addAfterSpec", null, phase);
		test01f.createTest(1, "delete", null, phase);
		test01f.createTest(1, "replaceSpec", null, phase);

		test01f.addToList(labels);
		// test01 with phase7
		QuickFixTestGroup test01g = new QuickFixTestGroup("test01-es-regex");
		phase = "phase7";
		test01g.title += " - " + phase;
		test01g.getConfig().setPhase(phase);

		test01g.createTest(1, "replace", null, phase);
//		test01g.createTest(1, "addBefore", null, phase);
//		test01g.createTest(1, "addAfter", null, phase);
//		test01g.createTest(1, "addBeforeSpec", null, phase);
//		test01g.createTest(1, "addAfterSpec", null, phase);
		test01g.createTest(1, "delete", null, phase);
		test01g.createTest(1, "replaceSpec", null, phase);

		test01g.addToList(labels);
		
		// test02 with phase1
		QuickFixTestGroup test02a = new QuickFixTestGroup("test02-es-regex-withEnt");
		phase = "phase1";
		test02a.title += " - " + phase;
		test02a.getConfig().setPhase(phase);

		test02a.createTest(1, "replace", null, phase);
		test02a.createTest(1, "addBefore", null, phase);
		test02a.createTest(1, "addAfter", null, phase);
		test02a.createTest(1, "addBeforeSpec", null, phase);
		test02a.createTest(1, "addAfterSpec", null, phase);
		test02a.createTest(1, "delete", null, phase);
		test02a.createTest(1, "replaceSpec", null, phase);

		test02a.addToList(labels);
		

		// test02 with phase2
		QuickFixTestGroup test02b = new QuickFixTestGroup("test02-es-regex-withEnt");
		phase = "phase2";
		test02b.title += " - " + phase;
		test02b.getConfig().setPhase(phase);

		test02b.createTest(1, "replace", null, phase);
		test02b.createTest(1, "addBefore", null, phase);
		test02b.createTest(1, "addAfter", null, phase);
		test02b.createTest(1, "addBeforeSpec", null, phase);
		test02b.createTest(1, "addAfterSpec", null, phase);
		test02b.createTest(1, "delete", null, phase);
		test02b.createTest(1, "replaceSpec", null, phase);

		test02b.addToList(labels);
		

		// test02 with phase3
		QuickFixTestGroup test02c = new QuickFixTestGroup("test02-es-regex-withEnt");
		phase = "phase3";
		test02c.title += " - " + phase;
		test02c.getConfig().setPhase(phase);

		test02c.createTest(1, "replace", null, phase);
		test02c.createTest(1, "addBefore", null, phase);
		test02c.createTest(1, "addAfter", null, phase);
		test02c.createTest(1, "addBeforeSpec", null, phase);
		test02c.createTest(1, "addAfterSpec", null, phase);
		test02c.createTest(1, "delete", null, phase);
		test02c.createTest(1, "replaceSpec", null, phase);

		test02c.addToList(labels);
		
		
		return labels;

	}

	// public QuickFixTest(String folder, String msgPosStr, String fixName,
	// String[] parameter, String suffix){

	public EscaliExtTest(QuickFixParameter qfParam) {
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
			expected += "_" + testParams.msgPos;
			expected += testParams.fixName != null ? "_" + testParams.fixName : "";
			expected += suffix != null ? "_" + suffix : "";

			this.expectedResult[i++] = new File(testParams.schema, "../../expected/test" + expected + ".xml");

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

			if (result.size() < expectedResult.length) {
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
			if (testParams.exception != null) {
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
			if (testParams.exception == null) {
				fail(e.getMessage());
			} else {
				assertEquals(e.getClass().getName(), testParams.exception.getClass().getName());
			}
		}
	}
}
