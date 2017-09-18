package com.schematronQuickfix.escaliGuiComponents.panels;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;


public class MultipleValuesChooser extends JDialog {
	private static final long serialVersionUID = 7855895975287851608L;
	private ArrayList<String> choosedValue = new ArrayList<String>();
	private GridBagLayout gbl = new GridBagLayout();
	private final JFrame owner;
	private final ButtonGroup bgroup = new ButtonGroup();
	
	private final HashMap<String, JRadioButton> radioBtnByValue = new HashMap<String, JRadioButton>();
	private final HashMap<String, JCheckBox> checkBoxByValue = new HashMap<String, JCheckBox>();
	
	
	private boolean allowMoreValues = false;
	
	public MultipleValuesChooser(final List<String> values, JFrame owner, String title, String description, final String defaultValue, HashMap<String, String> valueMapping, ImageIcon icon) {
		this.owner = owner;
		this.setTitle(title);
		this.setIconImage(icon.getImage());
		this.setLayout(gbl);
		this.choosedValue.add(defaultValue);
		
		int i = 0;
		String[] descArr = description.split("\\n");
		
		for (; i < descArr.length; i++) {
			JLabel label = new JLabel(descArr[i]);
			int paddingTop = i == 0 ? 5 : 0;
			Insets insets = new Insets(paddingTop, 5, 5, 5);
			SwingUtil.addComponent(this, gbl, label, 0, i, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, insets);
		}
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();i++) {
			final String value = iterator.next();
			String valueTitle = valueMapping.containsKey(value) ? valueMapping.get(value) : value;
			
			JLabel label = new JLabel(valueTitle);
			
			final JRadioButton radioBtn = new JRadioButton();
			final JCheckBox checkBox = new JCheckBox();
			radioBtn.setToolTipText(valueTitle);
			checkBox.setToolTipText(valueTitle);
			bgroup.add(radioBtn);
			
			SwingUtil.addComponent(this, gbl, label, 	0, i, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5));
			SwingUtil.addComponent(this, gbl, radioBtn, 1, i, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5));
			SwingUtil.addComponent(this, gbl, checkBox, 1, i, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5));
			
			radioBtn.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent ev) {
					if(radioBtn.isSelected()){
						choosedValue = new ArrayList<String>();
						choosedValue.add(value);
					}
				}
			});
			checkBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					if(checkBox.isSelected()){
						choosedValue.add(value);
					} else {
						choosedValue.remove(value);
						if(choosedValue.size() == 0){
							checkBoxByValue.get(defaultValue).setSelected(true);
						}
					}
				}
			});
			
			this.radioBtnByValue.put(value, radioBtn);
			this.checkBoxByValue.put(value, checkBox);
			
			checkBox.setVisible(false);
			radioBtn.setVisible(true);
			if(value.equals(defaultValue)){
				radioBtn.setSelected(true);
			}
			
//			label.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					ButtonChooser.this.choosedValue = value;
//				}
//			});
		}
		final JCheckBox moreValues = new JCheckBox("Allow more values");
		moreValues.setSelected(this.allowMoreValues);
		moreValues.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				switchMoreValues(values, moreValues.isSelected());
			}
		});
		
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					MultipleValuesChooser.this.setVisible(false);
					MultipleValuesChooser.this.setModal(false);
			}
		});
		SwingUtil.addComponent(this, gbl, moreValues, 		0, i, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5));
		SwingUtil.addComponent(this, gbl, okBtn, 		1, i, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5));
	}

	public MultipleValuesChooser(List<String> values, String title, String description, String defaultValue, HashMap<String, String> valueMapping, ImageIcon icon) {
		this(values, null, title, description, defaultValue, valueMapping, icon);
	}

	public String[] getValue() {
		this.setModal(true);
		this.pack();
		this.setSize(300, this.getHeight());
		this.setResizable(false);
		SwingUtil.centerFrame(this, owner);
		
		this.setVisible(true);
		return choosedValue.toArray(new String[choosedValue.size()]);
	}
	
	private void switchMoreValues(List<String> values, boolean allowMoreValues){
		choosedValue = new ArrayList<String>();
		
		boolean isFirstSelected = true;
		for (String value : values) {
			JRadioButton radio = radioBtnByValue.get(value);
			JCheckBox box = checkBoxByValue.get(value);
			
			radio.setVisible(!allowMoreValues);
			box.setVisible(allowMoreValues);
			
			this.allowMoreValues = allowMoreValues;
			if(allowMoreValues){
				box.setSelected(radio.isSelected());
			} else if(box.isSelected() && isFirstSelected){
				radio.setSelected(true);
				isFirstSelected = false;
			}
		}
		if(allowMoreValues){
			this.bgroup.clearSelection();
		}
		
	}
	
	
}
