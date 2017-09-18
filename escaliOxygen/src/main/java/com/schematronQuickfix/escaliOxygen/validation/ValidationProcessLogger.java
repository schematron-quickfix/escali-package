package com.schematronQuickfix.escaliOxygen.validation;

import java.util.ArrayList;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationSummaryException;


public class ValidationProcessLogger extends DefaultProcessLoger implements ProcessLoger {
	
	ArrayList<ValidationSummaryException> validationExceptions = new ArrayList<ValidationSummaryException>();
	ArrayList<Exception> allExceptions = new ArrayList<Exception>();
	
	public void log(ValidationSummaryException exception) throws CancelException {
		validationExceptions.add(exception);
		allExceptions.add(exception);
		throw new CancelException("Validation canceld because of internal error!");
	}
	
	@Override
	public void log(Exception exception) throws CancelException {
		allExceptions.add(exception);
		throw new CancelException("Validation canceld because of internal error!");
	}
	
	@Override
	public void log(Exception exception, boolean forceEnd)
			throws CancelException {
		this.log(exception);
	}
	
	public boolean hasExceptions(){
		return !allExceptions.isEmpty();
	}
	
	public boolean hasValidationException(){
		return !validationExceptions.isEmpty();
	}
	
	public ArrayList<ValidationSummaryException> getAllValidationExceptions(){
		return new ArrayList<ValidationSummaryException>(this.validationExceptions);
	}
	
	public ArrayList<Exception> getAllExceptions(){
		return new ArrayList<Exception>(this.allExceptions);
	}
	
	
//	@Override
//	public void log(String message, boolean taskEnd) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void log(String message) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void log(Exception exception) throws CancelException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void log(Exception exception, boolean forceEnd)
//			throws CancelException {
//
//	}
//
//	@Override
//	public void end() {
//		// TODO Auto-generated method stub
//
//	}

}
