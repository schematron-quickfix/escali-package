package com.schematronQuickfix.escaliOxygen.options;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.oxygen.Misc;

import ro.sync.exml.plugin.option.OptionPagePluginExtension;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class OxygenOptionDialog extends JDialog {

	GridBagLayout gbl = new GridBagLayout();

	public OxygenOptionDialog(final OptionPagePluginExtension optionPlugin, final StandalonePluginWorkspace spw) {
		super(Misc.getParentFrame(spw));
		this.setLayout(gbl);
		this.setTitle(optionPlugin.getTitle());

		GridBagLayout bottomLineGbl = new GridBagLayout();
		JPanel bottomLine = new JPanel(bottomLineGbl);
		
		int border = 8;
		
		SwingUtil.addComponent(this, gbl, optionPlugin.init(spw), 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(20, border, 0, border));
		SwingUtil.addComponent(this, gbl, bottomLine, 0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, border, border, border));

		JButton okBtn = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				optionPlugin.apply(spw);
				hideDialog();
			}
		});
		okBtn.setText("OK");

		JButton cnclBtn = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hideDialog();
			}
		});
		cnclBtn.setText("Cancel");

		SwingUtil.addComponent(bottomLine, bottomLineGbl, new JPanel(), 0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH);
		SwingUtil.addComponent(bottomLine, bottomLineGbl, okBtn, 1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.VERTICAL);
		SwingUtil.addComponent(bottomLine, bottomLineGbl, cnclBtn, 2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.VERTICAL);

		this.pack();
		this.setMinimumSize(this.getSize());
		Dimension dimMax = this.getMaximumSize();
		dimMax.height = this.getSize().height;
		this.setMaximumSize(dimMax);
		

//		this.addComponentListener(new ComponentAdapter() {
//			@Override
//			public void componentResized(ComponentEvent e) {
//				setSize(new Dimension(getWidth(), maxHeight));
//				super.componentResized(e);
//			}
//		});

		SwingUtil.centerFrame(this, this.getOwner());
		this.setModal(true);
		this.setVisible(true);
	}

	public void hideDialog() {
		this.setVisible(false);
	}
	
	@Override
	public void paint(Graphics g) {
		Dimension d = getSize();
        Dimension m = getMaximumSize();
        boolean resize = d.width > m.width || d.height > m.height;
        d.width = Math.min(m.width, d.width);
        d.height = Math.min(m.height, d.height);

        if (resize) {
            Point p = getLocation();
//            setVisible(false);
            setSize(d);
            setLocation(p);
//            setVisible(true);
        }
        super.paint(g);
    }
}
