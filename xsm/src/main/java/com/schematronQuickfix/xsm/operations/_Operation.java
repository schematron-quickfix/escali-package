package com.schematronQuickfix.xsm.operations;

public interface _Operation {

	public abstract int getStart();

	public abstract int getEnd();

	public abstract String getReplace();

	public abstract int getPrio();

	OperationsSet getCorrectionOperations();
	String getCorrectionId();

}