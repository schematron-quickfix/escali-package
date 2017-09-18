package com.schematronQuickfix.escaliOxygen.tools;

import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.text.BadLocationException;

import org.w3c.dom.Document;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.view.graphics.Point;
import ro.sync.exml.view.graphics.Rectangle;
import ro.sync.exml.workspace.api.editor.page.WSEditorPage;
import ro.sync.exml.workspace.api.editor.page.WSTextBasedEditorPage;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;
import ro.sync.exml.workspace.api.editor.page.text.xml.WSXMLTextEditorPage;

public abstract class WSPageAdapter implements WSTextBasedEditorPage {
	public static final int XML_PAGE = 0;
	public static final int AUTHOR_PAGE = 1;
	private final WSTextBasedEditorPage textPage;
	
	public WSPageAdapter(WSTextBasedEditorPage page){
		this.textPage = page;
		
	}
	
//	
//	ABSTRACT
//	
	
	public abstract int getPageType();
	
	public abstract Object[] evaluateXPath(String xpath);
	
	public abstract void addPopUpMenuCustomizer(PopupMenuCustomizer customizer);
	public abstract void removePopUpMenuCustomizer(PopupMenuCustomizer customizer);
	public abstract Document getDocument();
	
	
	public void addKeyListener(KeyListener l) {
		this.getComponent().addKeyListener(l);
	}

	public void removeKeyListener(KeyListener l) {
		this.getComponent().removeKeyListener(l);
	}
	
	public abstract JComponent getComponent();
	
//	
//	TEXT PAGE IMPL
//	
	
	
	@Override
	public void deleteSelection() {
		textPage.deleteSelection();
	}

	@Override
	public int getCaretOffset() {
		return textPage.getCaretOffset();
	}

	@Override
	public Point getLocationOnScreenAsPoint(int arg0, int arg1) {
		return textPage.getLocationOnScreenAsPoint(arg0, arg1);
	}

	@Override
	public Point getLocationRelativeToEditorFromScreen(int arg0, int arg1) {
		return textPage.getLocationRelativeToEditorFromScreen(arg0, arg1);
	}

	@Override
	public String getSelectedText() {
		return textPage.getSelectedText();
	}

	@Override
	public int getSelectionEnd() {
		return textPage.getSelectionEnd();
	}

	@Override
	public int getSelectionStart() {
		return textPage.getSelectionStart();
	}

	@Override
	public int[] getStartEndOffsets(DocumentPositionedInfo arg0)
			throws BadLocationException {
		return textPage.getStartEndOffsets(arg0);
	}

	@Override
	public int[] getWordAtCaret() {
		return textPage.getWordAtCaret();
	}

	@Override
	public boolean hasSelection() {
		return textPage.hasSelection();
	}

	@Override
	public boolean isEditable() {
		return textPage.isEditable();
	}

	@Override
	public Rectangle modelToViewRectangle(int arg0) {
		return textPage.modelToViewRectangle(arg0);
	}

	@Override
	public void select(int arg0, int arg1) {
		textPage.select(arg0, arg1);
	}

	@Override
	public void selectWord() {
		textPage.selectWord();
	}

	@Override
	public void setCaretPosition(int arg0) {
		textPage.setCaretPosition(arg0);
	}

	@Override
	public void setEditable(boolean arg0) {
		textPage.setEditable(arg0);
	}

	@Override
	public int viewToModelOffset(int arg0, int arg1) {
		return textPage.viewToModelOffset(arg0, arg1);
	}
	
	
//	
//	STATIC
//	
	
	
	public static WSPageAdapter getWSEditorAdapter(WSEditorPage page){
		if(page instanceof WSAuthorEditorPage){
			return new WSAuthorPageAdapter((WSAuthorEditorPage) page);
		} else if (page instanceof WSXMLTextEditorPage) {
			return new WSXMLTextPageAdapter((WSXMLTextEditorPage) page);
		} else {
			return null;
		}
	}
	
	
}
