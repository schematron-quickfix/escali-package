package com.schematronQuickfix.escali.quickfix;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.helpers.EscaliFixingTestPair;
import com.schematronQuickfix.escali.helpers.ResourceHelper;
import com.schematronQuickfix.escali.helpers.QuickFixTestStrategy;
import com.schematronQuickfix.escali.helpers.SchematronInstancePair;
import net.sf.saxon.ma.trie.Tuple2;
import org.junit.Before;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public abstract class FixingTestBase {

	public static String PENDING_PREFIX = "PENDING:";

	private boolean pending;

	protected FixingTestBase() {
		this(false);
	}
	protected FixingTestBase(boolean pending){
		this.pending = pending;
	}

	protected boolean isPending() {
		return pending;
	}

	public String checkPending(String fixId){
		this.pending = fixId.startsWith(PENDING_PREFIX);
		return fixId.replaceAll("^" + PENDING_PREFIX, "");
	}


	public static class ExecutionSetup{

		private final HashMap<String, Object> userEntries = new HashMap<>();
		private final Properties execProperties = new Properties();

		private final  Tuple2<Integer, String> tuple;

		public ExecutionSetup(Integer msgPos, String fixId) {
			this.tuple = new Tuple2<>(msgPos, fixId);
		}

		public ExecutionSetup addUE(String name, Object value){
			userEntries.put(name, value);
			return this;
		}
		public ExecutionSetup addExecProp(String name, String value){
			execProperties.setProperty(name, value);
			return this;
		}

		public int getMsgPos(){
			return this.tuple._1;
		}
		public String getFixId(){
			return this.tuple._2;
		}

		public HashMap<String, Object> getUE() {
			return this.userEntries;
		}
		public String getExecPropertyValue(String propertyName){
			if(this.execProperties.getProperty(propertyName) == null)
				return "";
			return this.execProperties.getProperty(propertyName);
		}
	}
	
	
	private QuickFixTestStrategy tester;
	private ResourceHelper resource;
	protected String phase, lang = null;
	
	
	public abstract String getFolder();


	public String getSchemaPath(){
		return "input/test.sch";
	}

	public String getInstancePath(){
		return "input/test.xml";
	}


	@Before
	public void setup(){
		tester  = new QuickFixTestStrategy(new MuteProcessLoger());
		resource = new ResourceHelper(getClass(), getFolder());
		DefaultProcessLoger.setDefaultProcessLogger(new MuteProcessLoger());
		assumeTrue(!isPending());
	}


	public Config getConfig(){
		Config config = ConfigFactory.createDefaultConfig();
		config.setCompactSVRL(false);
		return config;
	}

	public void doTest(ExecutionSetup execSetup){
		Config config = getConfig();
		if(phase != null){
			config.setPhase(phase);
		}
		if(lang != null){
			config.setLanguage(lang);
		}
		doTest(config, execSetup);
	}

	public String getExpectedPrefix(){
		return "test";
	}

	protected void doTest(Config config, ExecutionSetup execSetup) {
		String suffix = execSetup.getExecPropertyValue("SUFFIX");

		String[] suffixes = suffix.contains(":") ? suffix.split(":") : new String[]{suffix};

		String[] expPaths = new String[suffixes.length];

		String name = getExpectedPrefix();
		name += "_" + execSetup.getMsgPos();
		name += "_" + execSetup.getFixId();

		for (int i = 0; i < suffixes.length; i++) {

			expPaths[i] = "expected/" + name + suffixes[i] + ".xml";
		}


		doTest(config, expPaths, execSetup);
	}

	public void doTest(Config config, String[] expectedResult, ExecutionSetup executionSetup){
		try {
			tester.testQuickFixExecution(
					new EscaliFixingTestPair(
							new SchematronInstancePair(resource, getInstancePath(), getSchemaPath()),
							resource,
							expectedResult,
							config)
							.addExecutionSetup(executionSetup)
			);
		} catch (XSLTErrorListener
				| IOException
				| CancelException
				| XPathExpressionException
				| SAXException
				| URISyntaxException
				| XMLStreamException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	public void expectError(ExecutionSetup executionSetup, Class<?> errorClass){
		expectError(ConfigFactory.createDefaultConfig(), executionSetup, errorClass);
	}
	public void expectError(Config config, ExecutionSetup executionSetup, Class<?> errorClass){
		try {

			TextSource expectedResult = TextSource.createVirtualTextSource(new File("dummy.xml"));
			expectedResult.setData("<error>Test failed! Expected was an error from class " + errorClass.getName() + "</error>");

			tester.testQuickFixExecution(new EscaliFixingTestPair(new SchematronInstancePair(resource, getInstancePath(), getSchemaPath()),
					new TextSource[]{expectedResult}, config)
					.addExecutionSetup(executionSetup)
			);
		} catch (Exception e){
			assertEquals(e.getClass().getName(), errorClass.getName());
		}

	}
}
