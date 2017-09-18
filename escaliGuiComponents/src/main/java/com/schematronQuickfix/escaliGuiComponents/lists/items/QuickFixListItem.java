package com.schematronQuickfix.escaliGuiComponents.lists.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.lists.DefaultTheme;
import com.github.oxygenPlugins.common.gui.lists.items.AbstractListItem;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.buttons.QFRadioButton;
import com.schematronQuickfix.escaliGuiComponents.lists.QuickFixList;

public class QuickFixListItem extends AbstractListItem<_QuickFix> implements Comparable<QuickFixListItem> {
	private final QFRadioButton radio;

	public QuickFixListItem(EscaliMessangerAdapter ema, QuickFixList qfList, _QuickFix node,
			final ButtonGroup buttonGroup, boolean isSelected) {
		super(node, getDefaultIcon(ema, node), new DefaultTheme());
		this.radio = new QFRadioButton(qfList, node);
		buttonGroup.add(radio);
		radio.setSelected(isSelected);
		
		JPanel leftPanel = new JPanel();
		GridBagLayout gblLeftPanel = new GridBagLayout();
		leftPanel.setLayout(gblLeftPanel);
		
		JLabel ueIconLabel = new JLabel(node.hasParameter() ? ema.getIcon(IconMap.USER_ENTRY) : ema.getIcon(IconMap.NOICON)); 
		
		SwingUtil.addComponent(leftPanel, gblLeftPanel, radio, 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH);

		SwingUtil.addComponent(leftPanel, gblLeftPanel, ueIconLabel, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH);
		
		this.controlPanel.add(leftPanel);
		
//		radio.setHorizontalAlignment(SwingConstants.CENTER);
//		radio.setContentAreaFilled(false);
//
//		radio.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent ie) {
//				if (radio.isSelected()) {
//					QuickFixListItem.this.ema
//							.notifySelectedFix(QuickFixListItem.this.node);
//				} else {
//					QuickFixListItem.this.ema.hideUserEntryViewer();
//				}
//			}
//		});
	}

	private static final long serialVersionUID = 1956940233846455633L;

	public static Icon getDefaultIcon(EscaliMessangerAdapter ema, _QuickFix node) {
		int role = node.getRole();
		switch (role) {
		case _QuickFix.ROLE_ADD:
			return ema.getIcon(IconMap.QUICKFIX_ADD);
		case _QuickFix.ROLE_DELETE:
			return ema.getIcon(IconMap.QUICKFIX_DELETE);
		case _QuickFix.ROLE_REPLACE:
			return ema.getIcon(IconMap.QUICKFIX_REPLACE);
		case _QuickFix.ROLE_STRINGREPLACE:
			return ema.getIcon(IconMap.QUICKFIX_STRING_REPLACE);
		case _QuickFix.ROLE_MIX:
			return ema.getIcon(IconMap.QUICKFIX_REPLACE);
		default:
			return ema.getIcon(IconMap.NOICON);
		}
	}
	
	public boolean isChoosen(){
		return this.radio.isSelected();
	}
	public void setAsChoosen(){
		this.radio.setSelected(true);
	}

	@Override
	public int compareTo(QuickFixListItem o) {
		return 0;
	}

}
