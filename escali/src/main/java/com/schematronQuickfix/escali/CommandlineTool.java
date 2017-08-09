package com.schematronQuickfix.escali;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.cmdInterface.Menus;
import com.schematronQuickfix.escali.cmdInterface.Validation;
import com.schematronQuickfix.escali.control.Escali;
import com.schematronQuickfix.escali.control.SVRLReport;

public class CommandlineTool {
	
	public final static double VERSION = 0.2;
	
	private Escali escali;
	private File input;
	private File outFile;
	private SVRLReport report;

	private Scanner cmdInput = new Scanner(System.in);
	
	
	public void start(File input, File schema, File outFile, File config) throws XPathExpressionException, IOException, SAXException, XMLStreamException, XSLTErrorListener, URISyntaxException, CancelException{
		this.input = input;
		this.outFile = outFile;
		this.escali = new Escali();
		this.escali.compileSchema(TextSource.readTextFile(schema), new DefaultProcessLoger());
		validate();
		
		
		
	}
	
	private void validate() throws XPathExpressionException, FileNotFoundException, XSLTErrorListener, IOException, SAXException, URISyntaxException, XMLStreamException{
		this.report = this.escali.validate(TextSource.readTextFile(this.input), new DefaultProcessLoger());
		System.out.println(printValidationReport());
		System.out.println(Menus.firstMenu);
		System.out.print("Type your selection: ");
		String sel = cmdInput.next();
		if(sel.equals("0")){
			System.out.println("Exit Escali Schematron commandline tool v" + VERSION);
			System.exit(0);
		} else if (sel.equals("1")){
			System.out.println("Validating again...");
			validate();
		} else if (sel.equals("2")){
			fix();
		} else if (sel.equals("3")){
//			System.out.println(outFile.getAbsolutePath());
			TextSource.write(outFile, report.getSVRL());
			System.exit(0);
		}
	}
	
	private void fix() throws XPathExpressionException, IOException{
//		NodeList fixNodes = this.report.getFixes();
//		for (int i = 0; i < fixNodes.getLength(); i++) {
//			System.out.println((i + 1) + "\t" + SVRLReport.XPR.getString("sqf:description/svrl:text", fixNodes.item(i)));
//		}
//		System.out.println("0\texit");
//		System.out.print("Choose the QuickFix: ");
//		int sel = Integer.parseInt(cmdInput.next()) - 1;
//		if(sel == - 1){
//			System.exit(0);
//		}
//		
//		Node fix = fixNodes.item(sel);
//		System.out.println("Id of the quickFix:");
//		TextSource result = escali.executeFix(new String[]{SVRLReport.XPR.getString("@id", fix)});
//		TextSource.write(this.outFile, result);
	}
	
	private String printValidationReport() {
		return report.getFormatetReport(SVRLReport.TEXT_FORMAT).toString();
	}
	
	
	private static class CmdParser {
		private Options options;
		CommandLine cmd;
		
		public CmdParser(){
			Option validate = new Option("v", "validate", true, "Validate with <schema> and <xml> document");
			validate.setArgs(2);
			validate.setRequired(false);
			Option output = new Option("o", "output", true, "Output of the validation/quickFix");
			output.setRequired(false);
			options = new Options();
			options.addOption(validate);
			options.addOption(output);
		}
		
		public void parseCommandline(String[] args) throws ParseException{
			GnuParser parser = new GnuParser();
			cmd = parser.parse(options, args);
			
		}
		public void getHelp(){
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("hello", options);
		}
		
	}
	
	private static CmdParser cmdParser = new CmdParser();
	
	/**
	 * @param args
	 * args[1]  input
	 * args[2]  schema
	 * args[3+]? parameters pattern: name=value or {namespace-uri}local-name=value 
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws URISyntaxException 
	 * @throws XMLStreamException 
	 */
	public static void main(String[] args) throws TransformerException, IOException, SAXException, XPathExpressionException, ParserConfigurationException, URISyntaxException, XMLStreamException {
		try {
			cmdParser.parseCommandline(args);
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			cmdParser.getHelp();
		}
		
		if(cmdParser.cmd.hasOption('v')){
			File input = new File(cmdParser.cmd.getOptionValues('v')[0]);
			File schema = new File(cmdParser.cmd.getOptionValues('v')[1]);
			System.out.println(input + " | " + schema);
			try {
				Validation val = new Validation(schema, new MuteProcessLoger());
				val.validate(input);
			} catch (XSLTErrorListener e) {
				e.printStackTrace();
			} catch (CancelException e) {
				e.printStackTrace();
			}
			
			
		} else {
			System.out.println("no command!");
			cmdParser.getHelp();
		}
		
		
		
//		System.out.println("Starting Escali Schematron commandline tool v" + CommandlineTool.VERSION);
//		
//		File input = new File(args[0]);
//		File schema = new File(args[1]);
//		File out = new File(args[2]);
//		File config = EscaliResources.getConfig();
////		File svrl = new File("temp/java_temp.svrl");
//		CommandlineTool cmdt = new CommandlineTool();
//		cmdt.start(input, schema, out, config);
//		
////		Validate val = new Validate(schema);
////		System.out.println(val.validateSVRL(input));
	}
}
