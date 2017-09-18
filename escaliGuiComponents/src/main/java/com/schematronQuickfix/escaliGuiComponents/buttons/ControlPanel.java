package com.schematronQuickfix.escaliGuiComponents.buttons;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.lists.items.AbstractListItem.Mouse;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;


@SuppressWarnings("rawtypes")
public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 651451428874393350L;
	private final Mouse mouse;
	private GridBagLayout gbl = new GridBagLayout();


	public ControlPanel(JComponent comp, Mouse mouse) {
		this(mouse);
		
		this.setMaximumSize(new Dimension(100000, 5));
		this.add(comp);
	}
	
	public ControlPanel(Mouse mouse) {
		this.setLayout(gbl);
		this.setOpaque(false);
		this.mouse = mouse;
		
		
	}
	
	public void add(JComponent comp, Insets insets){
		this.removeAll();
		SwingUtil.addComponent(this, gbl, comp, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets);
		comp.setSize(new Dimension(comp.getWidth(), 5));
		comp.setOpaque(false);
		comp.addMouseListener(mouse);
		for (Component childComp : comp.getComponents()) {
			childComp.addMouseListener(mouse);
		}
		this.repaint();
	}

	public void add(JComponent comp) {
		this.add(comp, new Insets(0, 0, 0, 0));
	}
}
