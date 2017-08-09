package com.schematronQuickfix.xsm;

import java.io.File;
import java.io.IOException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

//http://apache.org/xml/features/xinclude
public class ManipulatorMain {
	private static final String VERSION = "0.1";
	public static File baseFolder = new File(ManipulatorMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();

	private static void printHelp() {
		System.out.println("XPath based string manipulator " + VERSION
				+ " from Nico Kutscherauer\n"
				+ "http://www.schematron-quickfix.com/scripts/xsm.html\n");
		try {
			System.out.println(CmdProps.getHelpText());
		} catch (IOException e) {
			System.out.println("No help text available!");
		}
	}

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		CmdProps props;
		try {
			props = new CmdProps(args);
			if (props.isFunctionSetted('s')) {
				File sheet = props.getSheet();
				PositionalReplace pr;
				if (props.isFunctionSetted('i')) {
					File input = props.getInput();
					pr = new PositionalReplace(sheet, input);
				} else {
					pr = new PositionalReplace(sheet, true);
				}
				if (props.isFunctionSetted('o')) {
					File result = props.getOutput().getAbsoluteFile();
					result.getParentFile().mkdirs();
					TextSource.write(result, pr.getSource());
				} else {
					System.out.println(pr.getSource().toString());
				}

			} else {
				printHelp();
			}
		} catch (Exception e) {
			System.err.println("Process canceled by Exception " + e.getClass().getName() + ":");
			System.err.println(e.getMessage() + "\n\n");
			
//			printHelp();
		}

	}

}
