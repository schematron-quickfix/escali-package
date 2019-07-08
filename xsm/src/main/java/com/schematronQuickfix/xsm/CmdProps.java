package com.schematronQuickfix.xsm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.xsm.operations.PositionalReplace;



public class CmdProps {


	public static String getHelpText(){
		InputStream helpStream = CmdProps.class.getResourceAsStream("/help.txt");
		if(helpStream != null){
			try {
				return TextSource.readTextFile(helpStream).toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "No help text available!";
	}
	
	private File sheet = null;
	private File input = null;
	private File output = null;
	
	public CmdProps(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (!arg.startsWith("-")) {
				if(sheet != null){
					throw new Exception("Unkown command line parameter \"" + arg
							+ "\"!");
				} else {
					sheet = convertUri(args[i]);
				}
			} else {
				String function = args[i];
				if(function.equals("-i") || function.equals("--input")){
					input = getValue(++i, args);
				} else if(function.equals("-o") || function.equals("--output")){
					output = getValue(++i, args);
				} else if(function.equals("-f")){
					PositionalReplace.fastMode = true;
				}
			}
		}
	}
	
	public static File convertUri(String systemId) {
		try {
			URI uri = new URI(systemId);
			return new File(uri);
		} catch (Exception e){
			return new File(systemId);
		}
	}
	
	private File getValue(int i, String[] args) throws Exception {
		String value;
		if ((i < args.length) && !args[i].startsWith("-")) {
			value = args[i];
		} else {
			throw new Exception("Missing file for command line parameter " + args[i-1]
							+ "\"!");
		}
		return convertUri(value);
	}
	
	public boolean isFunctionSetted(char function){
		switch (function) {
		case 'i':
			return this.input != null;
		case 'o':
			return this.output != null;
		case 's':
			return this.sheet != null;
		default:
			return false;
		}
	}
	
	public File getSheet(){return this.sheet;}
	public File getInput(){return this.input;}
	public File getOutput(){return this.output;}

}
