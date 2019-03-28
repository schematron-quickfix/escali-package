package com.schematronQuickfix.escali.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xslt.Parameter;
import com.github.oxygenPlugins.common.xml.xslt.XSLTPipe;
import com.github.oxygenPlugins.common.xml.xslt.XSLTStep;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._UserEntry;
import com.schematronQuickfix.escali.resources.EscaliResourcesInterface;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

public class Executor {

	private XSLTPipe extractor = new XSLTPipe("Extractor");
	private XSLTStep[] manipulatorGenSteps = null;

	public Executor(EscaliResourcesInterface resource) throws XSLTErrorListener, FileNotFoundException {
		extractor.addStep(resource.getExtractor());
		manipulatorGenSteps = new XSLTStep[] { new XSLTStep(resource.getManipulator()[0], new ArrayList<Parameter>()) };
	}

	// public TextSource execute(_QuickFix[] fixes, SVRLReport report, Config
	// config) throws XSLTErrorListener{
	// return execute(fixes, report.getInput(), report.getSVRL(), config);
	// }
	public ArrayList<TextSource> execute(_QuickFix[] fixes, TextSource input, TextSource svrl,
			final HashMap<String, TextSource> fixParts, Config config) throws XSLTErrorListener {
		String[] ids = new String[fixes.length];
		ArrayList<Parameter> ueParams = new ArrayList<Parameter>();
		for (int i = 0; i < ids.length; i++) {
			ids[i] = fixes[i].getId();
			_UserEntry[] settedUEs = fixes[i].getSettedParameter();
			for (_UserEntry entry : settedUEs) {
				ueParams.add(new Parameter(entry.getId(), entry.getValue()));
			}
		}
		XSLTPipe manipulator = new XSLTPipe("", new XSLTErrorListener());

		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("id", ids));
		params.add(new Parameter("markChanges", !config.getChangePrefix().equals("")));

		extractor.setURIResolver(new URIResolver() {

			@Override
			public Source resolve(String href, String base) throws TransformerException {
				URI uri = URI.create(base);
				uri = uri.resolve(href);
				File absFile = new File(uri);
				String systemId = absFile.toURI().toString();
				if (fixParts.containsKey(systemId)) {
					TextSource fixPart = fixParts.get(systemId);
					StringReader reader = new StringReader(fixPart.toString());
					StreamSource src = new StreamSource(reader);
					src.setSystemId(systemId);
					return src;
				} else {
					return TextSource.getResolver().resolve(href, base);
				}
			}
		});

		TextSource extractorXSL = extractor.pipeMain(svrl, params);
		manipulator.addStep(extractorXSL, ueParams);
		manipulator.addStep(manipulatorGenSteps[0]);
		ArrayList<TextSource> extractorResult = manipulator.pipe(input, config.createManipulatorParams());

		if (config.isXmlSaveMode()) {
			ArrayList<TextSource> xsmResults = new ArrayList<TextSource>();
			for (TextSource result : extractorResult) {
				try {
					PositionalReplace pr = new PositionalReplace(result, true);
					xsmResults.add(pr.getSource());
				} catch (IOException e) {
					xsmResults.add(input);
				} catch (SAXException e) {
					xsmResults.add(input);
				} catch (XMLStreamException e) {
					xsmResults.add(input);
				} catch (XPathExpressionException e) {
					xsmResults.add(input);
				} catch (CancelException e) {
					xsmResults.add(input);
				}
			}
			return xsmResults;
		} else {
			return extractorResult;
		}
		// return extractorXSL;
	}
}
