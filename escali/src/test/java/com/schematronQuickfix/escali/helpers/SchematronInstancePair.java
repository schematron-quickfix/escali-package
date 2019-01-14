package com.schematronQuickfix.escali.helpers;

import java.io.IOException;


import com.github.oxygenPlugins.common.text.TextSource;

public class SchematronInstancePair {
	
	private TextSource instanceDocument;
	private TextSource schemaDocument;

	public SchematronInstancePair(TextSource instanceDocument, TextSource schemaDocument) {

        if (instanceDocument == null || schemaDocument == null) {
            throw new IllegalArgumentException("both input and output document must be given");
        }

        this.instanceDocument = instanceDocument;
        this.schemaDocument = schemaDocument;
    }
	
	public SchematronInstancePair(ResourceHelper res, String instancePath, String schemaPath) throws IOException {
        this(
                res.textSource(instancePath),
                res.textSource(schemaPath)
        );
    }
    
	
	/**
     * generates a MessagePair using default names: input/test.xml and input/test.sch
     * @param res
     */
    public SchematronInstancePair(ResourceHelper res) throws IOException {
        this(res, "input/test.xml", "input/test.sch");
    }
	
	public TextSource getInstanceDocument() {
		return instanceDocument;
	}
	
	public TextSource getSchemaDocument() {
		return schemaDocument;
	}
	
}
