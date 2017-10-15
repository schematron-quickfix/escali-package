package com.schematronQuickfix.escaliOxygen.options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escaliOxygen.options.association.table.AssociationRuleTable;

import ro.sync.exml.workspace.api.options.WSOptionChangedEvent;
import ro.sync.exml.workspace.api.options.WSOptionListener;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.util.XMLUtilAccess;

public class EscaliPluginConfig extends WSOptionListener {

	//
	// STATIC SECTION
	//
	public final static String DEFAULT_CONFIG = "<es:escaliPluginConfig isActive=\"true\" saxonVersion=\"PE\" "
			+ "xmlns:es=\"http://www.escali.schematron-quickfix.com/\">"
			+ "<es:detectSchema pi=\"true\" esPattern=\"true\"/>"
			+ "<es:rules active=\"false\"/>" + "</es:escaliPluginConfig>";
	public final static String[] SAXON_VERSIONS = new String[] { "Home Edition (HE)", "Professional Edition (PE)",
			"Enterprise Edition (EE)" };
	
	public final static int[] SAXON_VERSIONS_OXYGEN = {
		XMLUtilAccess.TRANSFORMER_SAXON_HOME_EDITION,
		XMLUtilAccess.TRANSFORMER_SAXON_PROFESSIONAL_EDITION,
		XMLUtilAccess.TRANSFORMER_SAXON_ENTERPRISE_EDITION
	};
	
	public final static EscaliPluginConfig config = new EscaliPluginConfig(
			DEFAULT_CONFIG);
	public static final String ESCALI_PLUGIN_OPTION_KEY = "net.sqf.oxygen.escaliPluginOptions";

	public static void implementConfig(final StandalonePluginWorkspace spw) {
		WSOptionsStorage optionsStorage = spw.getOptionsStorage();
		String xml = optionsStorage.getOption(ESCALI_PLUGIN_OPTION_KEY,
				DEFAULT_CONFIG);
		config.updateXML(xml);
		optionsStorage.addOptionListener(config);
		config.spw = spw;
	}

	protected static EscaliPluginConfig createTempPluginFromXML(String xml) {
		return new EscaliPluginConfig(xml);
	}

	public static interface ConfigChangeListener {
		boolean configChanged(EscaliPluginConfig newConfig,
				EscaliPluginConfig oldConfig);
	}

	//
	// OBJECT SECTION
	//
	private boolean isActive = true;
	private String saxonVersion = "PE";
	private boolean useXmlModel = true;
	private boolean useEscaliPattern = true;
	private StandalonePluginWorkspace spw = null;

	private final ArrayList<ConfigChangeListener> changeListeners = new ArrayList<EscaliPluginConfig.ConfigChangeListener>();
	private boolean updateSpw = true;
	private AssociationRuleTable ruleTable = new AssociationRuleTable();

	private EscaliPluginConfig() {
		super(ESCALI_PLUGIN_OPTION_KEY);
	}

	private EscaliPluginConfig(String xml) {
		super(ESCALI_PLUGIN_OPTION_KEY);
		this.updateXML(xml);
	}

	@Override
	public void optionValueChanged(WSOptionChangedEvent wsoce) {
		config.updateXML(wsoce.getNewValue());
	}

	private void updateXML(String xml) {
		EscaliPluginConfig tempConf = new EscaliPluginConfig();
		try {
			TextSource ts = TextSource.createVirtualTextSource(File
					.createTempFile("escaliPlugin", ".xml"));
			ts.setData(xml);
			StringNode sn = new StringNode(ts);
			tempConf.saxonVersion = sn
					.getNodeValue("/es:escaliPluginConfig/@saxonVersion");
			tempConf.isActive = sn
					.getXPathBoolean("not(/es:escaliPluginConfig/@isActive = 'false')");
			tempConf.useXmlModel = sn
					.getXPathBoolean("not(/es:escaliPluginConfig/es:detectSchema/@pi = 'false')");
			tempConf.useEscaliPattern = sn
					.getXPathBoolean("not(/es:escaliPluginConfig/es:detectSchema/@esPattern = 'false')");
			tempConf.ruleTable = new AssociationRuleTable(xml);

		} catch (IOException e) {
		} catch (SAXException e) {
		} catch (XMLStreamException e) {
		} catch (XPathExpressionException e) {
		}
		boolean takeOver = true;
		for (ConfigChangeListener ccl : this.changeListeners) {
			if (!ccl.configChanged(tempConf, this)) {
				takeOver = false;
			}
		}
		if (takeOver) {
			this.isActive = tempConf.isActive;
			this.saxonVersion = tempConf.saxonVersion;
			this.useXmlModel = tempConf.useXmlModel;
			this.useEscaliPattern = tempConf.useEscaliPattern;
			this.ruleTable = tempConf.ruleTable;
		}
		if (this.updateSpw) {
			if (spw != null) {
				this.updateSpw = false;
				spw.getOptionsStorage().setOption(ESCALI_PLUGIN_OPTION_KEY,
						this.toString());
			}
		} else {
			this.updateSpw = true;
		}
	}

	public boolean isActive() {
		return this.isActive;
	}
	
	public String getSaxonVersion() {
		return this.saxonVersion;
	}
	
	
	
	public int getSaxonVersionForOxy(){
		int idx = Arrays.asList(SAXON_VERSIONS).indexOf(saxonVersion);
		if(idx < 0 || idx >= SAXON_VERSIONS_OXYGEN.length){
			idx = 0;
		}
		return SAXON_VERSIONS_OXYGEN[idx];
	}

	public boolean useXmlModel() {
		return this.useXmlModel;
	}

	public boolean useEscaliPattern() {
		return this.useEscaliPattern;
	}

	public AssociationRuleTable getRuleTable() {
		return this.ruleTable;
	}

	public void addChangeListener(ConfigChangeListener changeListener) {
		this.changeListeners.add(changeListener);
	}

	public void removeChangeListener(ConfigChangeListener changeListener) {
		this.changeListeners.remove(changeListener);
	}

	@Override
	public String toString() {
		String xml = "<es:escaliPluginConfig isActive=\"" + this.isActive
				+ "\" saxonVersion=\"" + this.saxonVersion
				+ "\" xmlns:es=\"http://www.escali.schematron-quickfix.com/\">"
				+ "<es:detectSchema " + "pi=\"" + this.useXmlModel + "\" "
				+ "esPattern=\"" + this.useEscaliPattern + "\"/>"
				+ this.ruleTable.toString() + "</es:escaliPluginConfig>";
		return xml;
	}

}
