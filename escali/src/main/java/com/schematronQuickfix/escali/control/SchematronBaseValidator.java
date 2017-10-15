package com.schematronQuickfix.escali.control;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationException;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationSummaryException;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xsd.Xerces;
import com.schematronQuickfix.escali.control.report._Report;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escali.resources.EscaliResourcesInterface;

public class SchematronBaseValidator {
	private Xerces xerces;
	private Escali internEscali;

	public SchematronBaseValidator(EscaliResourcesInterface resource, Config config) throws SAXNotRecognizedException,
			SAXNotSupportedException, XSLTErrorListener, IOException, CancelException {
		xerces = new Xerces(ProcessNamespaces.SCH_NS, resource.getSchematronSchema());
		this.internEscali = new Escali(config, resource, false);
		TextSource sqfSch = TextSource.readXmlFile(resource.getSchematronForSchematron());
		internEscali.compileSchema(sqfSch, DefaultProcessLoger.getDefaultProccessLogger());

	}

	public void validate(TextSource schema, TextSource precompiled) throws ValidationSummaryException {
		ValidationSummaryException vse = new ValidationSummaryException(
				"Validation of schema " + schema.getFile().getAbsolutePath(), new ArrayList<ValidationException>());
		try {
			xerces.validateSource(precompiled);
		} catch (ValidationSummaryException e) {
			vse.addException(e);
		} catch (Exception e) {
			vse.addException(e);
		}
		_Report report;
		try {
			report = internEscali.validate(schema, DefaultProcessLoger.getDefaultProccessLogger()).getReport();
			double mel = report.getMaxErrorLevel();
			if (mel >= _SVRLMessage.LEVEL_ERROR) {
				ArrayList<_SVRLMessage> errors = report.getMessages(_SVRLMessage.LEVEL_ERROR,
						_SVRLMessage.LEVEL_FATAL_ERROR);
				vse.addException(getValidationSummary(report.getName(), errors));
			}
		} catch (Exception e) {
			vse.addException(e);
		}
		ArrayList<ValidationException> exList = vse.getExceptionList();
		if (exList.size() > 0) {
			throw vse;
		}
	}

	private ValidationSummaryException getValidationSummary(String title, ArrayList<_SVRLMessage> msgs) {
		ArrayList<ValidationException> velist = new ArrayList<ValidationException>();
		for (_SVRLMessage msg : msgs) {
			velist.add(new SVRLException(msg));
		}
		return new ValidationSummaryException(title, velist);
	}

	private static class SVRLException extends ValidationException {
		private static final long serialVersionUID = 5177247209030123257L;

		private static int getLineNumber(_SVRLMessage msg) {
			return msg.getLocationInIstance().getStart().getLineNumber();
		}

		private static int getColumnNumber(_SVRLMessage msg) {
			return msg.getLocationInIstance().getStart().getColumnNumber();
		}

		public SVRLException(_SVRLMessage msg) {
			super(msg.getName(), msg.getInstanceFile().toURI().toString(), getLineNumber(msg), getColumnNumber(msg),
					ValidationException.TYPE_SCHEMATRON);
		}
	}
}
