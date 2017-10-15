package com.schematronQuickfix.escaliOxygen.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import com.schematronQuickfix.escaliOxygen.EscaliPluginExtension;


public class AboutEscaliPluginMenuItem extends JMenuItem {

	private static final long serialVersionUID = 1L;
	private AboutEscaliPlugin aop;
	
	public AboutEscaliPluginMenuItem(JFrame parentFrame){
		super("About Escali Plugin");
		aop = new AboutEscaliPlugin(parentFrame);
		this.setIcon(EscaliPluginExtension.ICONS.getIcon(13, 23));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aop.pack();
				aop.setVisible(true);
			}
		});
	}

}
