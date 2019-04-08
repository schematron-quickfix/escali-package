package com.schematronQuickfix.escali.cmdInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.Escali;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.control.report.MessageGroup;
import com.schematronQuickfix.escali.control.report._MessageGroup;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escali.control.report._UserEntry;
import com.schematronQuickfix.escali.resources.EscaliArchiveResources;

public class Fixing {

	private Escali escali;
	private final SVRLReport report;
	private final TextSource source;

	public Fixing(File schema, File sourceFile, Config config) throws XSLTErrorListener, XPathExpressionException,
			IOException, SAXException, URISyntaxException, XMLStreamException, CancelException {
		this(TextSource.readTextFile(schema), TextSource.readTextFile(sourceFile), config);
	}

	public Fixing(TextSource schema, TextSource source, Config config) throws XSLTErrorListener, XPathExpressionException,
			IOException, SAXException, URISyntaxException, XMLStreamException, CancelException {
		this(new Validation(schema, config, DefaultProcessLoger.getDefaultProccessLogger()).validate(source), source, config);
	}

	public Fixing(SVRLReport report, TextSource source, Config config) throws FileNotFoundException, XSLTErrorListener {
		this.report = report;
		this.source = source;
		this.escali = new Escali(config, new EscaliArchiveResources());
	}

	public ArrayList<TextSource> executeFix(int messageIdx, String fixName)
			throws XSLTErrorListener, IOException {
		return executeFix(messageIdx, fixName, new HashMap<String, Object>());
	}
	public ArrayList<TextSource> executeFix(int messageIdx, String fixName, String[] parameters)
			throws XSLTErrorListener, IOException {
		HashMap<String, Object> parameterMap = new HashMap<>();
		for (String param:
			 parameters) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			parameterMap.put(name, value);
		}
		return this.executeFix(messageIdx, fixName, parameterMap);
	}

	public ArrayList<TextSource> executeFix(int messageIdx, String fixName, HashMap<String, Object> parameters)
			throws XSLTErrorListener, IOException {
		return executeFix(new int[]{messageIdx}, new String[]{fixName}, parameters);
	}

	public ArrayList<TextSource> executeFix(int[] messageIdx, String[] fixNames, HashMap<String, Object> parameters)
			throws XSLTErrorListener, IOException {
		ArrayList<_SVRLMessage> messages = report.getReport().getSortedMessages(_MessageGroup.SVRL_SORTING);

		_QuickFix[] fixes = new _QuickFix[messageIdx.length];

		int i = 0;
		for (int msgIdx:
			 messageIdx) {
			if(messages.size() > msgIdx){
				String fixName = fixNames[i];
				_SVRLMessage msg = messages.get(msgIdx);
				_QuickFix selFix = null;
				for (_QuickFix fix : msg.getQuickFixes()) {
					if (fix.getFixId().equals(fixName)) {
						selFix = fix;
						break;
					}
				}
				if(selFix != null){
					fixes[i] = selFix;

					if (parameters != null) {
						HashMap<String, _UserEntry> userEntries = new HashMap<String, _UserEntry>();
						for (_UserEntry ue : selFix.getParameter()) {
							userEntries.put(ue.getParameterName(), ue);
						}
						for (String name : userEntries.keySet()) {
							if(parameters.containsKey(name)){
								userEntries.get(name).setValue(parameters.get(name));
							}
						}
					}
				} else {
					ArrayList<TextSource> errorResults = new ArrayList<TextSource>();
					errorResults.add(source);
					return errorResults;
				}
			} else {
				throw new IOException("No message with index " + msgIdx);
			}
			i++;
		}

		return executeFix(fixes);
	}

	private ArrayList<TextSource> executeFix(_QuickFix[] fixes) throws XSLTErrorListener {
		ArrayList<TextSource> results = escali.executeFix(fixes, report, source);
		return results;
	}

	// public void executeFix(String fixId) throws XSLTErrorListener,
	// IOException {
	//
	//
	//// _Report reportObj = this.report.getReport();
	//// _ModelNode node = reportObj.getChildById(fixId);
	//// _QuickFix[] fixes;
	//// if(node != null && node instanceof _QuickFix){
	//// fixes = new _QuickFix[]{(_QuickFix) node};
	//// } else {
	//// fixes = new _QuickFix[]{};
	//// }
	//// escali.executeFix(fixes, this.report, this.report.getInput());
	// }
}
