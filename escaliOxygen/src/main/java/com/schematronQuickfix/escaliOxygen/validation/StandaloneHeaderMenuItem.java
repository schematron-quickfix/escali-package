package com.schematronQuickfix.escaliOxygen.validation;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;

import javax.swing.*;
import java.awt.*;


public class StandaloneHeaderMenuItem extends JPanel {
	private static final long serialVersionUID = 5732189126338661715L;
	private final GridBagLayout gbl = new GridBagLayout();

	private static final int[] margin = {2, 4};
	private static final int sep = 5;
	private static final Color bgColor = new Color(242, 229, 225);


	public StandaloneHeaderMenuItem() {
		this.setLayout(gbl);
		
		JLabel label = new JLabel("General QuickFixes");
		Point p = IconMap.MESSAGE_ERROR;
		
//		label.setIcon(ema.getIcon(p));
		JLabel icon = new JLabel(IconMap.ICONS.getIcon(p));
		SwingUtil.addComponent(this, gbl, icon, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(margin[0], margin[1], margin[0], sep));
		SwingUtil.addComponent(this, gbl, label, 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(margin[0], sep, margin[0], margin[1]));
		
//		this.setEnabled(false);
		this.setOpaque(true);
		this.setForeground(Color.BLACK);
		this.setBackground(bgColor);
		
	}
}
