package com.schematronQuickfix.escaliOxygen.tools;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.validation.MessageMenuItem;
import com.schematronQuickfix.escaliOxygen.validation.QuickFixMenuItem;
import com.schematronQuickfix.escaliOxygen.validation.StandaloneHeaderMenuItem;
import com.schematronQuickfix.escaliOxygen.validation.ValidationAdapter;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.structure.AuthorPopupMenuCustomizer;
import ro.sync.exml.view.graphics.Rectangle;
import ro.sync.exml.workspace.api.editor.page.WSEditorPage;
import ro.sync.exml.workspace.api.editor.page.WSTextBasedEditorPage;
import ro.sync.exml.workspace.api.editor.page.text.TextPopupMenuCustomizer;
import ro.sync.exml.workspace.api.editor.page.text.WSTextEditorPage;

public class PopupMenuCustomizer extends TextPopupMenuCustomizer implements
		AuthorPopupMenuCustomizer, Action {
	private final ValidationAdapter va;
	private final EscaliMessanger ema;
	private final static String[] shortcuts = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};

	public PopupMenuCustomizer(ValidationAdapter va, EscaliMessanger ema){
		this.va = va;
		this.ema = ema;
		
	}
	private void customizePopUpMenu(JPopupMenu menu, WSTextBasedEditorPage page,
			boolean forcePopup) {
		if (va.isPopupCustomizing() || forcePopup) {

			ArrayList<_SVRLMessage> messages = va.getMessageByCaret(page);


			ArrayList<_SVRLMessage> hiddenMsgs = new ArrayList<>();

			for (_SVRLMessage msg:
				 messages) {
				if(msg.isHidden())
					hiddenMsgs.add(msg);
			}
			if(messages.size() > 0){
				menu.removeAll();
				int i = 0;

				if(messages.size() > hiddenMsgs.size()) {
					for (_SVRLMessage msg : messages) {
						if (msg.hasQuickFixes() && !(msg.isHidden())) {
							menu.add(new MessageMenuItem(msg, ema));

							for (_QuickFix fix : msg.getQuickFixes()) {
								String key = i >= shortcuts.length ? "" : shortcuts[i];
								menu.add(new QuickFixMenuItem(fix, va.getEditor(),
										va.getReportByMessage(msg), ema, key));
								i++;
							}

						}
					}
				}
				boolean showStandaloneHeader = messages.size() > hiddenMsgs.size();
				boolean hasStandalones = hiddenMsgs.size() > 0;

				if(showStandaloneHeader && hasStandalones){
					menu.add(new StandaloneHeaderMenuItem());
				}
				for (_SVRLMessage msg : hiddenMsgs) {

					for (_QuickFix fix : msg.getQuickFixes()) {
						String key = i >= shortcuts.length ? "" : shortcuts[i];
						menu.add(new QuickFixMenuItem(fix, va.getEditor(),
								va.getReportByMessage(msg), ema, key));
						i++;
					}

				}

				va.setPupupCustomizing(false);

			}

		}
	}

	@Override
	public void customizePopUpMenu(Object menuObj, AuthorAccess page) {
		if(menuObj instanceof JPopupMenu){
			customizePopUpMenu((JPopupMenu) menuObj, page.getEditorAccess(), false);
		}
	}

	@Override
	public void customizePopUpMenu(Object menuObj, WSTextEditorPage page) {
		if(menuObj instanceof JPopupMenu){
			customizePopUpMenu((JPopupMenu) menuObj, (WSTextBasedEditorPage) page, false);
		}
	}

	private void actionPerformed(ActionEvent arg0, WSPageAdapter pageAdap, Point position) {
		JPopupMenu menu = new JPopupMenu();


		customizePopUpMenu(menu, pageAdap, true);
		JComponent comp = pageAdap.getComponent();
		if (comp != null && menu.getComponentCount() > 0) {
			menu.show(comp, position.x, position.y);
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		WSEditorPage page = va.getEditor().getCurrentPage();
		WSPageAdapter pageAdap = WSPageAdapter.getWSEditorAdapter(page);

		Rectangle rect = pageAdap.modelToViewRectangle(pageAdap.getCaretOffset());

		actionPerformed(arg0, pageAdap, new Point(rect.x + rect.width, rect.y + rect.height));

	}

	public void actionPerformed(ActionEvent arg0, Point position) {
		WSEditorPage page = va.getEditor().getCurrentPage();
		WSPageAdapter pageAdap = WSPageAdapter.getWSEditorAdapter(page);

		actionPerformed(arg0, pageAdap, position);

	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
	}

	@Override
	public Object getValue(String arg0) {
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void putValue(String arg0, Object arg1) {

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener arg0) {

	}

	@Override
	public void setEnabled(boolean arg0) {
	}

}