package com.schematronQuickfix.escaliOxygen.validation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;



public class MessageMenuItem extends JPanel {
	private static final long serialVersionUID = 5732189126338661715L;
	private final GridBagLayout gbl = new GridBagLayout();
	
	private static final int[] margin = {2, 4};
	private static final int sep = 5;
	private static final Color bgColor = new Color(242, 229, 225);
	

	public MessageMenuItem(_SVRLMessage msg, EscaliMessanger ema) {
		this.setLayout(gbl);
		
		JLabel label = new JLabel(msg.getName());
		Point p = IconMap.MESSAGE_ERROR;
		
		switch (msg.getErrorLevelInt()) {
		case _SVRLMessage.LEVEL_FATAL_ERROR:
			p = IconMap.MESSAGE_FATAL_ERROR;
			break;
		case _SVRLMessage.LEVEL_WARNING:
			p = IconMap.MESSAGE_WARNING;
			break;
		case _SVRLMessage.LEVEL_INFO:
			p = IconMap.MESSAGE_INFO;
			break;

		default:
			break;
		}
//		label.setIcon(ema.getIcon(p));
		JLabel icon = new JLabel(ema.getIcon(p));
		SwingUtil.addComponent(this, gbl, icon, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(margin[0], margin[1], margin[0], sep));
		SwingUtil.addComponent(this, gbl, label, 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(margin[0], sep, margin[0], margin[1]));
		
//		this.setEnabled(false);
		this.setOpaque(true);
		this.setForeground(Color.BLACK);
		this.setBackground(bgColor);
		
	}
}
