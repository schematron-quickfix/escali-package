package com.schematronQuickfix.escaliOxygen.editors;

import javax.swing.JFrame;

import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliGuiAdapter;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class OxygenEscaliGui implements EscaliGuiAdapter {
	private final StandalonePluginWorkspace pwa;
	public OxygenEscaliGui(StandalonePluginWorkspace pwa){
		this.pwa = pwa;
		
	}
	@Override
	public void viewException(Exception e) {
		pwa.showErrorMessage(e.getLocalizedMessage());
	}
	@Override
	public JFrame asJFrame() {
		Object objFram = pwa.getParentFrame();
		if(objFram instanceof JFrame){
			return (JFrame) pwa.getParentFrame();
		} else {
			return null;
		}
	}

}