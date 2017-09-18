package com.schematronQuickfix.escaliGuiComponents.panels;


import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class PhaseChooser extends MultipleValuesChooser {
	private static final long serialVersionUID = 7855895975287851608L;
	
	private static final HashMap<String, String> valueMapping = new HashMap<String, String>();
	static {
		valueMapping.put("#ALL", "All patterns");
	}
	public PhaseChooser(List<String> list, JFrame owner, String defaultPhase, ImageIcon icon) {
		super(list, owner, "Choose the phase", "Choose the phase:", defaultPhase, valueMapping, icon);
	}
	
	public PhaseChooser(List<String> list, String defaultPhase, ImageIcon icon) {
		this(list, null, defaultPhase, icon);
	}
	
//	public PhaseChooser(ArrayList<String> phases, JFrame owner) {
//		this.owner = owner;
//		this.setTitle("Choose the phase");
//		this.setIconImage(IconMap.getIcon(9, 19).getImage());
//		this.setLayout(gbl);
//		JLabel label = new JLabel("Choose the phase:");
//		SwingUtil.addComponent(this, gbl, label, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5));
//		int i = 1;
//		for (Iterator<String> iterator = phases.iterator(); iterator.hasNext();i++) {
//			final String phaseId = iterator.next();
//			JButton btn = new JButton(phaseId);
//			if(phaseId.equals("#all"))
//				btn.setText("All patterns");
//			SwingUtil.addComponent(this, gbl, btn, 0, i, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5));
//			btn.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					PhaseChooser.this.choosedPhase = phaseId;
//					PhaseChooser.this.setVisible(false);
//					PhaseChooser.this.setModal(false);
//				}
//			});
//		}
//	}

//	public String getPhase() {
//		this.setModal(true);
//		this.pack();
//		this.setSize(300, this.getHeight());
//		this.setResizable(false);
//		Point ownerLoc	= owner.getLocation();
//		this.setLocation(ownerLoc.x + (owner.getWidth() / 2) - (this.getWidth() / 2),
//									 ownerLoc.y + (owner.getHeight() / 2) - (this.getHeight() / 2));
//		this.setVisible(true);
//		return choosedPhase;
//	}
//	
	
}
