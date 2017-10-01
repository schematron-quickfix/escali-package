package com.schematronQuickfix.escali.control;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.github.oxygenPlugins.common.xml.xslt.XSLTPipe;
import com.schematronQuickfix.escali.resources.EscaliResourcesInterface;

public class SchemaInfo {
	private XSLTPipe infoGenerator = new XSLTPipe("Schema anlayzer");
	private static XPathReader xpr = new XPathReader();
	
	
	private String[] phases;
	private String defaultPhase;
	private String[] languages;
	private String defaultLang;
	private final TextSource schema;
	
	protected SchemaInfo(TextSource source, EscaliResourcesInterface resource) throws XSLTErrorListener, IOException, SAXException, XMLStreamException, XPathExpressionException{
		this.schema = source;
		infoGenerator.addStep(resource.getSchemaInfo());
		StringNode schemaInfoDoc = new StringNode(infoGenerator.pipeMain(source));
		
		NodeList phaseNodes = xpr.getNodeSet("/es:schemaInfo/es:phases/es:phase", schemaInfoDoc.getDocument());
		phases = new String[phaseNodes.getLength()];
		for (int i = 0; i < phases.length; i++) {
			phases[i] = xpr.getString("@id", phaseNodes.item(i));
		}
		this.defaultPhase = xpr.getString("/es:schemaInfo/es:phases/@default", schemaInfoDoc.getDocument());
		
		NodeList langNodes = xpr.getNodeSet("/es:schemaInfo/es:languages/es:lang", schemaInfoDoc.getDocument());
		languages = new String[langNodes.getLength()];
		for (int i = 0; i < languages.length; i++) {
			languages[i] = xpr.getString("@code", langNodes.item(i));
		}
		
		this.defaultLang = xpr.getString("/es:schemaInfo/es:languages/@default", schemaInfoDoc.getDocument());
	}
	
	public TextSource getSchema() {
		return schema;
	}

	public String[] getPhases(){
		return this.phases;
	}
	public String getDefaultPhase(){
		return this.defaultPhase;
	}
	public String[] getLanguages(){
		return this.languages;
	}
	public String getDefaultLanguage(){
		return this.defaultLang;
	}
}
