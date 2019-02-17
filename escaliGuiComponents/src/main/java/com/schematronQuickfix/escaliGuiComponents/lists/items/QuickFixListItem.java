package com.schematronQuickfix.escaliGuiComponents.lists.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.key.KeyAdapter;
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
		radio.setFocusable(false);
		new KeyAdapter(this){
			@Override
			public void spaceTyped(KeyEvent ke) {
				setAsChoosen();
			}
			@Override
			public void downRelease(KeyEvent ke) {
				transferFocus();
			}
			@Override
			public void upRelease(KeyEvent ke) {
				transferFocusBackward();
			}
		};
		
		JPanel leftPanel = new JPanel();
		GridBagLayout gblLeftPanel = new GridBagLayout();
		leftPanel.setLayout(gblLeftPanel);
		
		JLabel ueIconLabel = new JLabel(node.hasParameter() ? ema.getIcon(IconMap.USER_ENTRY) : ema.getIcon(IconMap.NOICON));
		JLabel infoIconLabel = new JLabel(node.hasDescription() ? ema.getIcon(IconMap.DOCUMENTATION) : ema.getIcon(IconMap.NOICON));
		infoIconLabel.setToolTipText(node.getDescription());
		
		SwingUtil.addComponent(leftPanel, gblLeftPanel, radio, 2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH);

		SwingUtil.addComponent(leftPanel, gblLeftPanel, ueIconLabel, 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH);
		SwingUtil.addComponent(leftPanel, gblLeftPanel, infoIconLabel, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH);
		
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
