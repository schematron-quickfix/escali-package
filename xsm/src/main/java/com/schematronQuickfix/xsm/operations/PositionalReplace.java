package com.schematronQuickfix.xsm.operations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationSummaryException;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.github.oxygenPlugins.common.xml.xsd.Xerces;
import com.github.oxygenPlugins.common.xml.xsd.XercesI;
import com.github.oxygenPlugins.common.xml.xsd.XercesStream;
import com.schematronQuickfix.xsm.CmdProps;

public class PositionalReplace {
	private StringNode sourceStringNode;
	private StringNode sheetStringNode;
	// private Document docNode;
	// private String docString;
	static private XPathReader xpr = new XPathReader();
	static PositionalXMLReader pxr = new PositionalXMLReader();
	private static File baseFolder = new File(PositionalReplace.class
			.getProtectionDomain().getCodeSource().getLocation().getPath())
			.getParentFile();
//	public static final File XSM_SCHEMA = new File(baseFolder,
//			"src/main/xml/com/schematronQuickfix/xml/xsd/xpath-based-string-manipulator.xsd")
//			.getAbsoluteFile();
//	public static final InputStream XSM_SCHEMA_SRC = new Object().getClass().getResourceAsStream("/xml/xsd/xpath-based-string-manipulator.xsd");
	private XercesI xerces;

	// private class StringNode {
	// private TextSource textSource;
	// private Document docNode;
	// private String absPath;
	// public StringNode(File file) throws IOException, SAXException,
	// XMLStreamException{
	// this.absPath = file.getAbsolutePath();
	// setTextReader(TextSource.readTextFile(file, false));
	// }
	//
	// @SuppressWarnings("unused")
	// private StringNode(TextSource textReader, Document docNode, String
	// absPath){
	// this.textSource = textReader;
	// this.docNode = docNode;
	// this.absPath = absPath;
	// }
	// public void setTextReader(TextSource source) throws IOException,
	// SAXException, XMLStreamException{
	// TextSource backupSource = this.textSource;
	// try {
	// this.textSource = source;
	// timeStemp("Finished text reading, start parsing xml");
	// actualizeNode();
	// timeStemp("Finished parsing xml");
	// } catch (IOException e){
	// this.textSource = backupSource;
	// throw e;
	// } catch(SAXException e) {
	// this.textSource = backupSource;
	// throw e;
	// }
	// }
	// public void setString(String string, boolean parse) throws IOException,
	// SAXException, XMLStreamException {
	// String backupString = this.textSource.toString();
	// try {
	// this.textSource.setData(string);
	// if(parse){
	// timeStemp("Start parsing xml");
	// actualizeNode();
	// }
	// timeStemp("Finished parsing xml");
	// } catch (IOException e){
	// this.textSource.setData(backupString);
	// throw e;
	// } catch(SAXException e) {
	// this.textSource.setData(backupString);
	// throw e;
	// }
	// }
	// private void actualizeNode() throws IOException, SAXException,
	// XMLStreamException{
	// this.docNode = pxr.readXML(this.textSource);
	// }
	//
	// public TextSource getTextSource(){
	// return this.textSource;
	// }
	// public Document getDocument(){
	// return this.docNode;
	// }
	// public String toString(){
	// return this.textSource.toString();
	// }
	// }
	public static boolean debugMode = false;
	public static boolean fastMode = false;

	public static void setProcessLoger(ProcessLoger loger) {
		logger = loger;
	}

	private static ProcessLoger logger = new DefaultProcessLoger() {

		@Override
		public void log(String message) {
			if (debugMode) {
				System.out.println(new Date().toString() + ": " + message);
			}
		}
	};

	public PositionalReplace(TextSource document, boolean isSheet)
			throws XPathExpressionException, IOException, SAXException,
			XMLStreamException, CancelException {
		this(new StringNode(document, logger), isSheet);
	}

	public PositionalReplace(File document, boolean isSheet)
			throws IOException, SAXException, XPathExpressionException,
			XMLStreamException, CancelException {
		this(new StringNode(document, logger), isSheet);
	}

	private PositionalReplace(StringNode document, boolean isSheet)
			throws IOException, SAXException, XPathExpressionException,
			XMLStreamException, CancelException {
		if (isSheet) {
			if (!fastMode && getXSMSchema() != null) {
				xerces = new XercesStream(getXSMSchema());
				try {
					xerces.validateSource(document.getTextSource());
				} catch (ValidationSummaryException e) {
					logger.log(e);
				} catch (IOException e){
					logger.log(e);
				}
			}
			String xpath = "resolve-uri(/xsm:manipulator/@document, '"
					+ document.getDocument().getDocumentURI() + "')";
			File documentAtrFile = CmdProps.convertUri(xpr.getString(xpath,
					document.getDocument()));
			this.sheetStringNode = document;

			File sourceFile;
			if (documentAtrFile.isAbsolute()) {
				sourceFile = documentAtrFile;
			} else {
				sourceFile = new File(document.getFile().getParentFile(),
						documentAtrFile.getPath());
			}

			logger.log("Parse Instance");
			this.sourceStringNode = new StringNode(sourceFile, logger);
			logger.log("Start operation");
			operate();
			logger.log("End operation");
		} else {
			this.sourceStringNode = document;
		}
	}

	public PositionalReplace(File sheet, File source) throws IOException,
			SAXException, XPathExpressionException, XMLStreamException,
			CancelException {
		if (!fastMode && getXSMSchema() != null) {
			xerces = new XercesStream(getXSMSchema());
			try {
				xerces.validateSource(sheet);
			} catch (ValidationSummaryException e) {
				logger.log(e);
			}
		}
		this.sheetStringNode = new StringNode(sheet, logger);
		this.sourceStringNode = new StringNode(source, logger);
		operate();
	}

	public PositionalReplace(TextSource sheet, TextSource source)
			throws IOException, SAXException, XMLStreamException,
			XPathExpressionException, CancelException {
		if (!fastMode && getXSMSchema() != null) {
			xerces = new XercesStream(getXSMSchema());
			try {
				xerces.validateSource(sheet);
			} catch (ValidationSummaryException e) {
				logger.log(e);
			}
		}
		this.sheetStringNode = new StringNode(sheet);
		this.sourceStringNode = new StringNode(source);
		operate();
	}

	public PositionalReplace(TextSource sheet) throws IOException,
			SAXException, XMLStreamException {
		if (!fastMode && getXSMSchema() != null) {
			xerces = new XercesStream(getXSMSchema());
		}
		this.sourceStringNode = new StringNode(sheet);
	}

	public void deleteNode(String xpath) throws XPathExpressionException,
			IOException, SAXException, XMLStreamException {
		NodeList nodes = xpr.getNodeSet(xpath,
				this.sourceStringNode.getDocument());
		OperationsSet ops = new OperationsSet();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			ops.add(new DeleteOp(node, 0));
		}
		this.operate(ops);
	}

	private void operate() throws XPathExpressionException, IOException,
			SAXException, XMLStreamException {
		OperationsSet operations = OperationsSet.createOperations(
				this.sourceStringNode, this.sheetStringNode,
				xpr.getNodeSet("/*/*", this.sheetStringNode.getDocument()));
		this.operate(operations);
	}

	private void operate(OperationsSet operations) throws IOException,
			SAXException, XMLStreamException {
		if (operations.size() > 0) {
			String[] docStrings = new String[operations.size()];
			int marker = 0;
			int idx = 0;
			for (Iterator<_Operation> iterator = operations.iterator(); iterator
					.hasNext(); idx++) {
				_Operation op = iterator.next();
				docStrings[idx] = this.sourceStringNode.getTextSource()
						.toString().substring(marker, op.getStart())
						+ op.getReplace();
				if (!iterator.hasNext()) {
					docStrings[idx] = docStrings[idx]
							+ this.sourceStringNode.getTextSource().toString()
									.substring(op.getEnd());
				} else {
					marker = op.getEnd();
				}
			}
			String docString = "";
			for (int i = 0; i < docStrings.length; i++) {
				docString = docString + docStrings[i];
			}
			this.sourceStringNode.setString(docString, !fastMode);
		}
	}

	public TextSource getSource() {
		return this.sourceStringNode.getTextSource();
	}
	
	public StringNode getSourceAsStringNode() {
		return this.sourceStringNode;
	}
	

	public static InputStream getXSMSchema(){
		return new Object().getClass().getResourceAsStream("/xml/xsd/xpath-based-string-manipulator.xsd");
	}

}
