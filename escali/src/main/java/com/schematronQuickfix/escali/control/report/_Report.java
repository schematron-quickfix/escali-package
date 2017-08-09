package com.schematronQuickfix.escali.control.report;

import java.io.File;
import java.util.ArrayList;

public interface _Report extends _MessageGroup {

	public abstract ArrayList<_Flag> getFlag();

	public abstract ArrayList<_Pattern> getPattern();
	
	public abstract ArrayList<_Pattern> getPattern(String phase);
	
	public abstract _MessageGroup getGroup(String id);

	public abstract File getSchema();

	public abstract File getInstance();
	
	public abstract void addBaseError(Exception e);
	
	public abstract boolean hasBaseError();
	
	public abstract ArrayList<Exception> getBaseErrors();

	_Phase getPhaseReport(String id);

	

}