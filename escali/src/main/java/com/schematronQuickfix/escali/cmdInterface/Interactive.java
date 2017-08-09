package com.schematronQuickfix.escali.cmdInterface;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.SVRLReport;


public class Interactive {
	
	
	private final SVRLReport report;

	public Interactive(SVRLReport report){
		this.report = report;
		
	}

	public void process() {
		TextSource ts = this.report.getFormatetReport(SVRLReport.TEXT_FORMAT);
		System.out.println(ts.toString());
		
	}
	

}
