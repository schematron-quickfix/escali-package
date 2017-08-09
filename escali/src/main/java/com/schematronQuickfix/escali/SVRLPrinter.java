package com.schematronQuickfix.escali;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;

public class SVRLPrinter {
	
	private final StringNode svrl;
	public SVRLPrinter(TextSource svrl) throws IOException, SAXException, XMLStreamException{
		this.svrl = new StringNode(svrl);
	}
	
	@Override
	public String toString() {
		return "SVRL report:\n" + this.svrl.getTextSource().toString();
	}
}
