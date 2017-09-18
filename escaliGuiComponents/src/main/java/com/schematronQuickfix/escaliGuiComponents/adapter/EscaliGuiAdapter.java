package com.schematronQuickfix.escaliGuiComponents.adapter;

import javax.swing.JFrame;

import com.github.oxygenPlugins.common.process.exceptions.ErrorViewer;


public interface EscaliGuiAdapter extends ErrorViewer{

	JFrame asJFrame();
}
