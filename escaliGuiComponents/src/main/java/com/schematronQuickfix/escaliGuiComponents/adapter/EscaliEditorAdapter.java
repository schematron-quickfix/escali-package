package com.schematronQuickfix.escaliGuiComponents.adapter;

import java.io.File;
import java.util.ArrayList;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.process.queues.VoidWorker;
import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.report._Report;
import com.schematronQuickfix.escali.control.report._SVRLMessage;


public interface EscaliEditorAdapter {
	File getInstanceFile();
	public TextSource getInstance();
	public String[] askForPhase(String[] phases, String defaultPhase);
	public String[] askForLanguage(String[] languages, String defaultLang);
//	void setInstance(TextSource instance);
	void markMessages(_Report report);
	void markMessages(ArrayList<_Report> report);
	VoidWorker setInstance(TextSource instance, ProcessLoger loger);
	int getInstanceSteps();
	boolean hasChanges();
	void selectMessage(_SVRLMessage msg, boolean forceFocus)
			throws CancelException;
	
}
