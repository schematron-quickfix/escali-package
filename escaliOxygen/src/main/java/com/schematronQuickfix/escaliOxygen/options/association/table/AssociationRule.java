package com.schematronQuickfix.escaliOxygen.options.association.table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.RegexUtil;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
//import com.nkutsche.commons.regex.RegexUtil;
import com.schematronQuickfix.escali.control.Escali;
import com.schematronQuickfix.escali.control.SchemaInfo;
import com.schematronQuickfix.escaliOxygen.EscaliPlugin;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.options.association.xmlModel.XmlModel;


public class AssociationRule {

	public static final int URL_MATCH_MODE = 0;
	public static final int ROOTNS_MATCH_MODE = 1;
	public static final int ROOTEL_MATCH_MODE = 2;
	public static final int ATTR_MATCH_MODE = 3;

	public static final String[] MATCH_MODE_LABELS = { "File Name",
			"Namespace", "Local Root Name", "Attribute" };

	private static final Escali ESCALI;

	static {
		Escali escali2 = null;
		try {
			escali2 = new Escali();
		} catch (FileNotFoundException e) {
		} catch (XSLTErrorListener e) {
		}

		ESCALI = escali2;
	}

	private boolean isSelected = false;
	private URL schema = null;
	private int matchMode = URL_MATCH_MODE;
	private String pattern = "*.*";
	private String[] phases = new String[] {};
	private int phaseSel = -1;
	private String[] langs = new String[] {};
	private int langSel = -1;
	
	private static URL getURL(File file){
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	AssociationRule(File schema, String[] phases, String[] langs) {
		this(getURL(schema), phases, langs);
	}
	
	AssociationRule(URL schema, String[] phases, String[] langs) {
		this.schema = schema;
		this.setPhases(phases);
		this.setLanguages(langs);
	}

	// Column 1: selection
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	// Column 2: schema
	public URL getSchema() {
		return this.schema;
	}

	void setSchema(Object obj) {
		if (obj instanceof SchemaCell) {
			setSchema((SchemaCell) obj);
		} else if (obj instanceof URL) {
			setSchema((URL) obj);
		}
	}

	void setSchema(SchemaCell schemaCell) {
		setSchema(schemaCell.getSchema());
	}

	
	void setSchema(URL schema) {
		if (schema == null)
			return;
		try {
			TextSource txtSrc = TextSource.readTextFile(schema);

			SchemaInfo schemaInfo = ESCALI.getSchemaInfo(txtSrc);

			setPhases(schemaInfo.getPhases());
			setPhaseSelection(schemaInfo.getDefaultPhase());

			setLanguages(schemaInfo.getLanguages());
			setLangueageSelection(schemaInfo.getDefaultLanguage());

			this.schema = schema;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (XSLTErrorListener e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

	}

	// Column 3: Match mode

	int getMatchMode() {
		return this.matchMode;
	}

	String getMatchModeLabel() {
		return MATCH_MODE_LABELS[this.matchMode];
	}

	void setMatchMode(Object matchModeObj) {
		if (matchModeObj instanceof Integer) {
			this.setMatchMode((Integer) matchModeObj);
		} else {
			this.setMatchMode(matchModeObj.toString());
		}
	}

	void setMatchMode(String matchModeLabel) {
		int idx = Arrays.asList(MATCH_MODE_LABELS).indexOf(matchModeLabel);
		if (idx > 0) {
			this.setMatchMode(idx);
		} else {
			this.setMatchMode(URL_MATCH_MODE);
		}
	}

	void setMatchMode(int matchMode) {
		this.matchMode = matchMode;
	}

	public boolean match(URL url, Document docNode) {
		Pattern wildcardMatcher = RegexUtil.wildcardToRegex(getPattern());

		XPathReader xpr = new XPathReader();
		NodeList rootAttr = null;
		Node rootEl = null;
		try {
			rootAttr = xpr.getNodeSet("/*/@*", docNode);
			rootEl = xpr.getNode("/*", docNode);
		} catch (XPathExpressionException e) {
			return false;
		}

		switch (this.getMatchMode()) {
		case URL_MATCH_MODE:
			String urlFocus = url.toString();
			String[] steps = urlFocus.split("/");
			urlFocus = steps[steps.length - 1];
			return wildcardMatcher.matcher(urlFocus).find();
		case ROOTEL_MATCH_MODE:
			return wildcardMatcher.matcher(rootEl.getLocalName()).find();
		case ROOTNS_MATCH_MODE:
			if (rootEl.getNamespaceURI() == null) {
				return false;
			}
			return wildcardMatcher.matcher(rootEl.getNamespaceURI()).find();
		case ATTR_MATCH_MODE:
			Matcher attrMatcher = XmlModel.attrPattern.matcher(getPattern());
			if(!attrMatcher.find()){
				attrMatcher = XmlModel.attrPattern.matcher(getPattern() + "='*'");
				if(!attrMatcher.find()){
					return false;
				}
			}
			
			attrMatcher.reset();
			
			while(attrMatcher.find()){
				String name = attrMatcher.group(2);
				String value = attrMatcher.group(6) != null ? attrMatcher.group(6) : attrMatcher.group(7);
				if(!match(rootAttr, name, value)){
					return false;
				}
			}
			
			return true;

		default:
			return false;
		}
	}

	private boolean match(NodeList attrs, String name, String value){
		for (int i = 0; i < attrs.getLength(); i++) {
			if(match(attrs.item(i), name, value)){
				return true;
			}
		}
		return false;
	}
	
	private boolean match(Node attr, String name, String value){
		
		if(value == null){
			value = "*";
		}
		
		Pattern namePattern = RegexUtil.wildcardToRegex(name);
		Pattern valuePattern = RegexUtil.wildcardToRegex(value);
		
		if(namePattern.matcher(attr.getNodeName()).find() && valuePattern.matcher(value).find()){
			return true;
		}
		return false;
	}

	// Column 4: Match pattern
	String getPattern() {
		return this.pattern;
	}

	void setPattern(Object patternObj) {
		this.pattern = patternObj == null ? "*.*" : patternObj.toString();
	}

	void setPattern(String pattern) {
		this.pattern = pattern;
	}

	// Column 5: Phase
	String[] getPhases() {
		return this.phases;
	}

	private void setPhases(String[] phases) {
		this.phases = phases;
		this.phaseSel = phases.length > 0 ? 0 : -1;
	}

	public String getPhaseSelectionValue() {
		if (phases.length > 0 && phaseSel <= phases.length)
			return this.phases[phaseSel];
		return "#ALL";
	}

	int getPhaseSelection() {
		return phaseSel;
	}

	void setPhaseSelection(Object selection) {
		setPhaseSelection(selection == null ? "" : selection.toString());
	}

	void setPhaseSelection(String selection) {
		int idx = Arrays.asList(this.getPhases()).indexOf(selection);
		if (idx > 0) {
			this.phaseSel = idx;
		} else if (this.getPhases().length > 0) {
			this.phaseSel = 0;
		} else {
			this.phaseSel = -1;
		}
	}

	// Column 6: Lang
	String[] getLanguages() {
		return this.langs;
	}

	private void setLanguages(String[] langs) {
		this.langs = langs;
		this.langSel = langs.length > 0 ? 0 : -1;
	}

	int getLangSelection() {
		return this.langSel;
	}

	public String getLangSelectionValue() {
		if (langs.length > 0 && langSel <= langs.length)
			return this.langs[langSel];
		return "";
	}

	void setLangueageSelection(Object selection) {
		setLangueageSelection(selection == null ? "" : selection.toString());
	}

	void setLangueageSelection(String selection) {
		int idx = Arrays.asList(this.getLanguages()).indexOf(selection);
		if (idx > 0) {
			this.langSel = idx;
		} else if (this.getLanguages().length > 0) {
			this.langSel = 0;
		} else {
			this.langSel = -1;
		}
	}

	@Override
	public String toString() {
		String name = this.schema != null ? schema.getPath() : "...";
		return name + " " + this.getPattern();
	}

	public String asXML() {
		if (schema == null)
			return "";

		String rule = "<es:rule ";
		rule += " schema=\"" + this.schema.toString() + "\"";
		rule += " matchMode=\"" + this.matchMode + "\"";
		rule += " pattern=\"" + this.getPattern().replace("\"", "&quot;") + "\"";
		rule += " phase=\"" + this.getPhaseSelectionValue() + "\"";
		rule += " lang=\"" + this.getLangSelectionValue() + "\"";

		rule += "/>";

		return rule;

	}

	public static AssociationRule getSchematronValidationRule(
			EscaliMessanger ema) {
		URL sqfSch = null;
		try {
			sqfSch = new URL(
					"http://www.schematron-quickfix.com/escali/schema/SQF/sqf.sch");
			sqfSch = 
					ema.getPluginWorkspace()
							.getXMLUtilAccess()
							.resolvePathThroughCatalogs(
									sqfSch,
									"", true, true);
		} catch (MalformedURLException e) {
		} catch (Exception e) {
		}

		AssociationRule schematronValidationRule = new AssociationRule(sqfSch,
				new String[] { "#ALL" }, new String[] { "#ALL" });
		schematronValidationRule.setMatchMode(ROOTNS_MATCH_MODE);
		schematronValidationRule.setPhaseSelection("#ALL");
		schematronValidationRule.setLangueageSelection("#ALL");
		schematronValidationRule.setPattern(ProcessNamespaces.SCH_NS);
		return schematronValidationRule;
	}

	public static AssociationRule createRule(AssociationRule preset) {
		if (preset == null)
			return createRule();
		return new AssociationRule(preset.schema, preset.phases, preset.langs);
	}

	public static AssociationRule createRule() {
		URL nullUrl = null;
		return new AssociationRule(nullUrl, new String[] {}, new String[] {});
	}

	public static AssociationRule createRule(Node item) {
		AssociationRule rule = createRule();
		XPathReader xpr = new XPathReader();
		try {
			String path = xpr.getAttributValue(item, "schema");
			URL url = new URL(path);
			rule.setSchema(url);

			rule.setMatchMode(Integer.parseInt(xpr.getAttributValue(item,
					"matchMode")));
			rule.setPattern(xpr.getAttributValue(item, "pattern"));

			rule.setLangueageSelection(xpr.getAttributValue(item, "lang"));
			rule.setPhaseSelection(xpr.getAttributValue(item, "phase"));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return rule;

	}

}