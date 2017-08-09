package com.schematronQuickfix.escali;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.cmdInterface.Interactive;
import com.schematronQuickfix.escali.cmdInterface.Validation;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.resources.EscaliOptions;

public class EscaliCmd {

	private CommandLine cmd;
	private TextSource result;

	private EscaliCmd(CommandLine cmd) {
		this.cmd = cmd;

	}

	public static void main(String[] args) {
		GnuParser parser = new GnuParser();
		Options options = EscaliOptions.getOptions();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
			EscaliCmd escmd = new EscaliCmd(cmd);
			escmd.process();
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("java -jar escali.jar [options]\nOptions:", options);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XSLTErrorListener e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (CancelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void process() throws XPathExpressionException, IOException, XSLTErrorListener, SAXException, URISyntaxException, XMLStreamException, CancelException {
		if (EscaliOptions.hasOption(cmd, EscaliOptions.VALIDATE_OPTION)) {
			validateProcess();
		}
	}
	
	private void validateProcess() throws IOException, XPathExpressionException, XSLTErrorListener, SAXException, URISyntaxException, XMLStreamException, CancelException{
		String[] vValues = EscaliOptions.getOptionValues(cmd, EscaliOptions.VALIDATE_OPTION);
		File schema = new File(vValues[1]);
		File instance = new File(vValues[0]); 
		Config config = ConfigFactory.createDefaultConfig();
		if(EscaliOptions.hasOption(cmd, EscaliOptions.PHASE_OPTION)){
			config.setPhase(EscaliOptions.getOptionValue(cmd, EscaliOptions.PHASE_OPTION));
		}
		Validation cmdValidation = new Validation(schema, config, new DefaultProcessLoger());
		
		
		SVRLReport report = cmdValidation.validate(instance);
		if(EscaliOptions.hasOption(cmd, EscaliOptions.VALIDATE_OPTION)){
			Interactive menu = new Interactive(report);
			menu.process();
		} else {
			viewReport(report);
		}
		try {
			finishProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void finishProcess() throws IOException{
		if(this.result == null){
			return;
		} else if (EscaliOptions.hasOption(cmd, EscaliOptions.OUTPUT_OPTION)){
			File outputfile = new File(EscaliOptions.getOptionValue(cmd, EscaliOptions.OUTPUT_OPTION));
			TextSource.write(outputfile, this.result);
		} else {
			System.out.println(this.result.toString());
		}
	}

	public void viewReport(SVRLReport report) {
		result = report.getSVRL();
		if(EscaliOptions.hasOption(cmd, EscaliOptions.OUTPUT_TYPE_OPTION)){
			String type = EscaliOptions.getOptionValue(cmd, EscaliOptions.OUTPUT_TYPE_OPTION);
			result = report.getFormatetReport(type);
		}
		
		
	}

}
