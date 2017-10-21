package com.schematronQuickfix.escaliOxygen.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationException;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.Escali;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.control.SchemaInfo;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escali.resources.EscaliArchiveResources;
import com.schematronQuickfix.escali.resources.EscaliFileResources;
import com.schematronQuickfix.escaliOxygen.EscaliPlugin;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;
import com.schematronQuickfix.escaliOxygen.options.OptionPage;
import com.schematronQuickfix.escaliOxygen.options.association.ValidationInfoSet.ValidationInfo;
import com.schematronQuickfix.escaliOxygen.toolbar.main.CommitChanges;
import com.schematronQuickfix.escaliOxygen.tools.ReadWrite;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.workspace.api.editor.WSEditor;

public class ValidationEngine {
	
	private final Escali escali;
	private final TextSource xsmChangeMarker;
	
	public ValidationEngine() throws XSLTErrorListener, FileNotFoundException {
		escali =  new Escali(new EscaliArchiveResources("/"));
		xsmChangeMarker = TextSource.createVirtualTextSource(new File(EscaliPlugin.descriptor.getBaseDir(), "lib/xml/xsm/xsmChangeMarker.xsm"));
		xsmChangeMarker.setData("<xsm:manipulator xmlns:xsm=\"http://www.schematron-quickfix.com/manipulator/process\">" +
				"<xsm:replace node=\"" +
				CommitChanges.START_CHANGE_MARKER_XPATH +
				"\">" +
				"<xsm:content>" +
				"<?oxy_custom_start type=\"oxy_content_highlight\" color=\"166,171,255\" sqf:type=sqfchange?>" +
				"</xsm:content>" +
				"</xsm:replace>" +
				"<xsm:replace node=\"" +
				CommitChanges.END_CHANGE_MARKER_XPATH +
				"\">" +
				"<xsm:content>" +
				"<?oxy_custom_end sqf:type=sqfchange?>" +
				"</xsm:content>" +
				"</xsm:replace>" +
				"</xsm:manipulator>");
	}
	
	public SVRLReport validate(ValidationInfo validationInfo, WSEditor editor, ProcessLoger logger) throws CancelException {
		
		
		TextSource schemaSrc;
		
		try {
			schemaSrc = TextSource.readTextFile(validationInfo.getSchema());
			
			String[] prefLangs = EscaliPluginConfig.config.getPreferedLanguage();
			boolean useDefault = prefLangs[0].equals(OptionPage.USE_DEFAULT_LANGUAGE); 
			
			String selectLang = "#ALL";
			
			if(validationInfo.getLang() != null) {
				selectLang = validationInfo.getLang();
			} else if(!useDefault) {
				SchemaInfo schemaInfo = escali.getSchemaInfo(schemaSrc);
				
				selectLang = schemaInfo.getDefaultLanguage();
				for (String prefLang : prefLangs) {
					if(prefLang.equals(OptionPage.USE_OXYGEN_LANGUAGE)){
						prefLang = EscaliPlugin.getInstance().getWorkspace().getUserInterfaceLanguage();
					}
					String l = schemaInfo.getLang(prefLang);
					if(l != null){
						selectLang = l;
						break;
					}
				}
				
			}
			
			
			Config esConfig = escali.getConfig();
			esConfig.setPhase(validationInfo.getPhase());
			esConfig.setLanguage(selectLang);
			
			String saxonVers = EscaliPluginConfig.config.getSaxonVersion();
//			// workaround for bug Saxon EE in oXygen plugin with external documents
			esConfig.setCompactSVRL(!saxonVers.equals("EE"));
			escali.compileSchema(schemaSrc, logger);
			TextSource instanceSrc;
//			instanceSrc = ReadWrite.createTextSource(editor, new URI(model.getBaseURI()));
			instanceSrc = TextSource.readTextFile(new File(new URI(validationInfo.getBaseURI())));
			SVRLReport report = escali.validate(instanceSrc, logger);
			return report;
		} catch (Exception e) {
			logger.log(e, true);
		}
		
		return null;
		
	}

	
	public DocumentPositionedInfo convertForOxygen(_SVRLMessage msg) throws XPathExpressionException{
		DocumentPositionedInfo posInfo = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_FATAL);
		posInfo.setMessage(msg.getName());
		NodeInfo loc = msg.getLocationInIstance();
		
		posInfo.setOffset(loc.getMarkStart());
		posInfo.setLength(loc.getMarkEnd() - loc.getMarkStart());
		
		posInfo.setSeverity(msg.getErrorLevelInt());
		
		posInfo.setSystemID(msg.getInstanceFile().toURI().toString());
		
		posInfo.setLine(loc.getStart().getLineNumber());
		posInfo.setColumn(loc.getStart().getColumnNumber());
		
		posInfo.setEngineName(ValidationAdapter.ESCALI_SCH_ENGINE_NAME);
		
		return posInfo;
	}
	
	public DocumentPositionedInfo convertForOxygen(ValidationException ve){
		DocumentPositionedInfo posInfo = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_FATAL);
		posInfo.setMessage(ve.getMessage());
		
		posInfo.setLine(ve.getLineNumber());
		posInfo.setColumn(ve.getColNumber());
		
		posInfo.setSystemID(ve.getSystemId());
		
		posInfo.setSeverity(DocumentPositionedInfo.SEVERITY_FATAL);
		
		posInfo.setEngineName("Escali Schematron");
		
		return posInfo;
	}
	
	public DocumentPositionedInfo convertForOxygen(Exception exc) {
		DocumentPositionedInfo posInfo = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_FATAL);
		posInfo.setMessage(exc.getLocalizedMessage());
		posInfo.setSeverity(DocumentPositionedInfo.SEVERITY_FATAL);
		posInfo.setEngineName("Escali Schematron");
		return posInfo;
	}

	public void fix(HashMap<String, WSEditor> editors, _QuickFix[] fixes, SVRLReport report) throws Exception {
//		Set<String> baseUris = editors.keySet();
//		ArrayList<TextSource> inputs = new ArrayList<TextSource>();
		
//		for (String baseUri : baseUris) {
//			inputs.add(ReadWrite.createTextSource(editors.get(baseUri), report.getSourceFile()));
//		}
			
		ArrayList<TextSource> newInstanceSrc = escali.executeFix(fixes, report, TextSource.readTextFile(report.getSourceFile()));
		for (TextSource newInstance : newInstanceSrc) {
			newInstance = transformChangeMarkes(newInstance);
			ReadWrite.setEditorContent(editors.get(newInstance.getFile().toURI().toString()), newInstance);
		}
	}
	
	
	
	private TextSource transformChangeMarkes(TextSource src) throws XPathExpressionException, IOException, SAXException, XMLStreamException, CancelException {
		PositionalReplace xsm = new PositionalReplace(this.xsmChangeMarker, src);
		return xsm.getSource();
	}

	

	
	
}
