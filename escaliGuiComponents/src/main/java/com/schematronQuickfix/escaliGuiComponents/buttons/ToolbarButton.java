package com.schematronQuickfix.escaliGuiComponents.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import com.github.oxygenPlugins.common.process.queues.WatchFactory;
import com.github.oxygenPlugins.common.process.queues.WatchTask;


public abstract class ToolbarButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2047460244976221117L;
	
	public ToolbarButton(String label){
		super(label);
		newInstance(label);
	}
	
	public ToolbarButton(Icon icon, String toolTipText){
		super(icon);
		newInstance(toolTipText);
	}
	
	private void newInstance(String toolTipText){
		this.setToolTipText(toolTipText);
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ToolbarButton.this.actionPerformed(arg0);
			}
		});
		WatchFactory.addWatchTask(new WatchTask() {
			
			@Override
			public void watch() {
				ToolbarButton.this.setEnabled(isEnable());
			}
		});
	}
	
	public abstract void actionPerformed(ActionEvent ev);
	
	public abstract boolean isEnable();
	
	
}
