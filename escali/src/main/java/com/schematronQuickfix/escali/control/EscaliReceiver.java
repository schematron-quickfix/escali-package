package com.schematronQuickfix.escali.control;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;

public interface EscaliReceiver {
	void viewReport(SVRLReport report);
	void setSchemaInfo(SchemaInfo schemaInfo);
	void compileSchemaFinish();
	void viewException(Exception e) throws CancelException;
}
