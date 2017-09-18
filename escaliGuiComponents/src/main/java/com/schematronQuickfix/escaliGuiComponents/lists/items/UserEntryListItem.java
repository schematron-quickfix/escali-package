package com.schematronQuickfix.escaliGuiComponents.lists.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JDialog;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.lists.DefaultTheme;
import com.github.oxygenPlugins.common.gui.lists.items.AbstractListItem;
import com.github.oxygenPlugins.common.gui.types.VerifierFactory;
import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;
import com.github.oxygenPlugins.common.gui.types.panels.FocusTraversalField;
import com.schematronQuickfix.escali.control.report._UserEntry;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;


public class UserEntryListItem extends AbstractListItem<_UserEntry> implements Comparable<UserEntryListItem> {

	private static final long serialVersionUID = 1956940233846455633L;
	
	private final UserEntryTextField textField;


	private final _UserEntry userEntry;

	private JComponent nextComp;

	private JComponent prevComp;

	
	public UserEntryListItem(EscaliMessangerAdapter ema, final _UserEntry userEntry) {
		this(ema, userEntry, null);


	}
	
	public UserEntryListItem(EscaliMessangerAdapter ema, final _UserEntry userEntry, JDialog parentDialog) {
		
		super(userEntry, ema.getIcon(IconMap.USER_ENTRY), new DefaultTheme());
		this.userEntry = userEntry;
		textField = new UserEntryTextField(userEntry.getTypeConverter());
		textField.setMinimumSize(new Dimension(100, 0));
		textField.setParentComponent(this);
		this.controlPanel.setMinimumSize(new Dimension(100, 0));

		if(userEntry.hasDefault()){
			textField.setText(userEntry.getValueAsString());
		} else {
			textField.setText(null);
			textField.setEnabled(false);
		}
		
		VerifierFactory.addVerifier(userEntry.getTypeConverter(), textField, parentDialog == null ? this.controlPanel : parentDialog);
		textField.setColumns(5);
		textField.setFocusable(true);
		this.controlPanel.add(textField, new Insets(0, 0, 0, 10));
		this.textField.setPrevComponent(this);
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()=='\n'){
					textField.requestFocus();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent ke) {
				
				int kecode = ke.getKeyCode();
				switch (kecode) {
				case KeyEvent.VK_F2:
					textField.requestFocus();
					break;
				default:
					break;
				}
			}
		});
	}

	private class UserEntryTextField extends FocusTraversalField {
		private static final long serialVersionUID = -3689652845068149389L;
		private final TypeConverter converter;
		
		public UserEntryTextField(TypeConverter typeConverter) {
			this.converter = typeConverter;
		}
		public void setText(Object value){
			String text = converter.convertToString(value);
			super.setText(text == null ? "#click#" : text);
			userEntry.setValue(value);
		}
		
		@Override
		public void setText(String text) {
			super.setText(text == null ? "#click#" : text);
			userEntry.setValue(converter.convertValue(text));
			
		}
		@Override
		public String toString() {
			return getText();
		}
		@Override
		public void requestFocus() {
			this.setEnabled(true);
			super.requestFocus();
		}
		
		@Override
		public void setEditable(boolean editable) {
			if(editable){
				this.setForeground(Color.BLACK);
			} else {
				this.setBackground(this.getDisabledTextColor());
			}
			super.setEditable(editable);
		}
	}
	
//	public void setNextComponent(UserEntryListItem nextItem){
//		this.textField.setNextComponent(nextItem);
//	}
//	
//	public void setPrevComponent(UserEntryListItem prevItem){
//		this.textField.setPrevComponent(prevItem);
//	}
	
	public void setNextComponent(JComponent nextComp){
		this.nextComp = nextComp;
		this.textField.setNextComponent(nextComp);
	}
	
	public void setPrevComponent(JComponent prevComp){
		this.prevComp = prevComp;
	}
	
	
	
//	@Override
//	public Icon getDefaultIcon() {
////		return new ImageIcon();
//		// TODO Auto-generated method stub
//		return ema.getIcon(IconMap.USER_ENTRY);
//	}

	@Override
	public int compareTo(UserEntryListItem o) {
		// TODO Auto-generated method stub
		return 0;
	}
	


}
