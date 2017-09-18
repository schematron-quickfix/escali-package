package com.schematronQuickfix.escaliGuiComponents.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JToggleButton;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.queues.WatchFactory;
import com.github.oxygenPlugins.common.process.queues.WatchTask;


public abstract class ToolbarToggleButton extends JToggleButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2047460244976221117L;

	private boolean isOn = false;

	private final String toolTipTextOn;
	private final String toolTipTextOff;

	public ToolbarToggleButton(Icon icon, String toolTipTextOn,
			String toolTipTextOff) {
		this(icon, toolTipTextOn, toolTipTextOff, false);
	}

	public ToolbarToggleButton(Icon icon, final String toolTipTextOff,
			final String toolTipTextOn, boolean isOn) {
		super(icon, isOn);
		this.toolTipTextOff = toolTipTextOff;
		this.toolTipTextOn = toolTipTextOn;

		this.isOn = isOn;

		this.setToolTipText(toolTipTextOff);
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				switchOnOff(ae);
			}
		});
		WatchFactory.addWatchTask(new WatchTask() {

			@Override
			public void watch() {
				ToolbarToggleButton.this.setEnabled(isEnable());
				if (ToolbarToggleButton.this.isOn ^ shouldBeOn()) {
					switchOnOff(null);
				}
			}
		});
	}

	private void switchOnOff(ActionEvent ae) {
		synchronized (this) {
			if (isOn) {
				try {
					switchToOff(ae);
					this.setToolTipText(toolTipTextOff);
					this.isOn = false;
					this.setSelected(false);
				} catch (CancelException e) {
					this.isOn = true;
					this.setSelected(true);
				}
			} else {
				try {
					switchToOn(ae);
					this.setToolTipText(toolTipTextOn);
					this.isOn = true;
					this.setSelected(true);
				} catch (CancelException e) {
					this.isOn = false;
					this.setSelected(false);
				}
			}
		}
	}

	public abstract void switchToOn(ActionEvent ev) throws CancelException;

	public abstract void switchToOff(ActionEvent ev) throws CancelException;

	public abstract boolean isEnable();

	public boolean shouldBeOn() {
		return this.isSelected();
	}
}
