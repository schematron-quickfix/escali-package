package com.schematronQuickfix.escali.resources;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class EscaliOptions {
	public static Option VALIDATE_OPTION = new Option("v", "validate", true, "Validate XML file <arg1> by Schematron schema <arg2>. Returns SVRL file.");
	public static Option FIX_OPTION = new Option("f", "fix", true, "Fix the XML file after validation. Choose a fix in the CMD interface. The Return file will be the fixed XML file.");
	public static Option OUTPUT_OPTION = new Option("o", "output", true, "Output file. The return file will be saved into this file.");
	public static Option PHASE_OPTION = new Option("p", "phase", true, "Used phase for validation");
	public static Option OUTPUT_TYPE_OPTION = new Option("t", "output-type", true, "Type of the Output. Possible values are svrl, text and html.");
//	public static Option OUTPUT_OPTION = new Option("o", "output", true, "Output file. The return file will be saved into this file.");
	
	static {
		VALIDATE_OPTION.setArgs(2);
		VALIDATE_OPTION.setRequired(true);
		
		FIX_OPTION.setArgs(0);
		FIX_OPTION.setRequired(false);
		
		OUTPUT_OPTION.setArgs(1);
		OUTPUT_OPTION.setRequired(false);
		
		PHASE_OPTION.setArgs(1);
		PHASE_OPTION.setRequired(false);
		
		OUTPUT_TYPE_OPTION.setArgs(1);
		OUTPUT_TYPE_OPTION.setRequired(false);
	}
	
	public static Options getOptions(){
		Options opts = new Options();
		opts.addOption(VALIDATE_OPTION);
		opts.addOption(FIX_OPTION);
		opts.addOption(OUTPUT_OPTION);
		opts.addOption(PHASE_OPTION);
		opts.addOption(OUTPUT_TYPE_OPTION);
		return opts;
	}
	
	private static String getOptionName(Option opt){
		return opt.getOpt();
	}
	
	public static boolean hasOption(CommandLine cmd, Option opt){
		return cmd.hasOption(getOptionName(opt));
	}
	
	public static String getOptionValue(CommandLine cmd, Option opt){
		return cmd.getOptionValue(getOptionName(opt));
	}
	
	public static String[] getOptionValues(CommandLine cmd, Option opt){
		return cmd.getOptionValues(getOptionName(opt));
	}
}
