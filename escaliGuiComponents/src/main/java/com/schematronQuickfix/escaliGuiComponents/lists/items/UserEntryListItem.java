package com.schematronQuickfix.escaliGuiComponents.lists.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.lists.DefaultTheme;
import com.github.oxygenPlugins.common.gui.lists.items.AbstractListItem;
import com.github.oxygenPlugins.common.gui.types.LabelField;
import com.github.oxygenPlugins.common.gui.types.VerifierFactory;
import com.github.oxygenPlugins.common.gui.types._ValueListener;
import com.schematronQuickfix.escali.control.report._UserEntry;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;

public class UserEntryListItem extends AbstractListItem<_UserEntry> implements Comparable<UserEntryListItem> {

	private static final long serialVersionUID = 1956940233846455633L;

	private final LabelField textField;

	private final _UserEntry userEntry;

	private JComponent nextComp;

	private JComponent prevComp;

	public UserEntryListItem(EscaliMessangerAdapter ema, final _UserEntry userEntry) {
		this(ema, userEntry, null);

	}

	public UserEntryListItem(EscaliMessangerAdapter ema, final _UserEntry userEntry, final JDialog parentDialog) {

		super(userEntry, ema.getIcon(IconMap.USER_ENTRY), new DefaultTheme());
		this.userEntry = userEntry;
		textField = VerifierFactory.createEntryLabel(null, null, userEntry.getTypeConverter(),
				parentDialog == null ? this.controlPanel : parentDialog);
		textField.setTitle(userEntry.toString());
		textField.setText(userEntry.getValueAsString());
		textField.addValueListener(new _ValueListener() {
			@Override
			public void valueChanged(Object newValue, Object oldValue) {
				userEntry.setValue(newValue);
			}
		});
		
		textField.setMinimumSize(new Dimension(70, 20));
		textField.setParentComponent(this);

		textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		this.controlPanel.setMinimumSize(new Dimension(200, 50));

		textField.setFocusable(false);

		this.controlPanel.add(textField, new Insets(0, 0, 0, 10));

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent ke) {
			}

			@Override
			public void keyReleased(KeyEvent ke) {

				int kechar = ke.getKeyCode();
				switch (kechar) {

				case KeyEvent.VK_ESCAPE:
					parentDialog.requestFocus();
					break;
				case KeyEvent.VK_ENTER:
					break;
				case KeyEvent.VK_DOWN:
					UserEntryListItem.this.transferFocus();
					break;
				case KeyEvent.VK_UP:
					UserEntryListItem.this.transferFocusBackward();
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_F2:
					textField.activateVerifier();
					break;
				default:
					break;
				}

			}

			@Override
			public void keyPressed(KeyEvent ke) {
				int kecode = ke.getKeyCode();
				switch (kecode) {

				default:
					break;
				}
			}
		});

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				// textField.activateVerifier();
			}
		});
	}

	// public void setNextComponent(UserEntryListItem nextItem){
	// this.textField.setNextComponent(nextItem);
	// }
	//
	// public void setPrevComponent(UserEntryListItem prevItem){
	// this.textField.setPrevComponent(prevItem);
	// }

	public void setNextComponent(UserEntryListItem nextUE) {
		this.textField.setNextComponent(nextUE.textField);
	}

	public void setNextComponent(JComponent nextComp) {
		this.textField.setNextComponent(nextComp);
	}

	public void setPrevComponent(UserEntryListItem prevUE) {
		this.textField.setPrevComponent(prevUE.textField);
	}

	public void setPrevComponent(JComponent prevComp) {
		this.textField.setPrevComponent(prevComp);
	}

	// @Override
	// public Icon getDefaultIcon() {
	//// return new ImageIcon();
	// // TODO Auto-generated method stub
	// return ema.getIcon(IconMap.USER_ENTRY);
	// }

	@Override
	public int compareTo(UserEntryListItem o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
