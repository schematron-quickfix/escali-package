package com.schematronQuickfix.escaliOxygen;

import java.net.URISyntaxException;

import com.schematronQuickfix.escaliOxygen.options.association.xmlModel.XmlModel;


public class Run {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * 
	 */
	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		String nodeValue = "href=\"quickFix1.sch\" type='application/xml' schematypens = \"http://www.schematron-quickfix.com/validator/process\"";
		
		XmlModel model = XmlModel.getModel(nodeValue, "file:/D:/nico/Work/Java/net.sqf.tester/xml/samples/example1/instance1.xml");
		
		
		System.out.println("base = " + model.getBaseURI());
		System.out.println("href = " + model.getHref());
		System.out.println("type = " + model.getType());
		System.out.println("schematypens = " + model.getSchematypens());
		
		
	}

}
