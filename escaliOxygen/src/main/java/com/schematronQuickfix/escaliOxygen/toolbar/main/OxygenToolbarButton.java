package com.schematronQuickfix.escaliOxygen.toolbar.main;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import com.github.oxygenPlugins.common.process.queues.WatchFactory;
import com.github.oxygenPlugins.common.process.queues.WatchTask;

import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;


public abstract class OxygenToolbarButton extends ToolbarButton implements Action {
	

	private static final long serialVersionUID = 2047460244976221117L;

	public OxygenToolbarButton(Icon icon, String toolTipText){
		super(null, true);
		this.setToolTipText(toolTipText);
		this.setAction(this);
		this.setIcon(icon);
		WatchFactory.addWatchTask(new WatchTask() {
			
			@Override
			public void watch() {
				OxygenToolbarButton.this.setEnabled(isEnable());
			}
		});
	}
	
	public abstract void actionPerformed(ActionEvent ev);
	
	public abstract boolean isEnable();

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
