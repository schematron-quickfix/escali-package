package com.schematronQuickfix.escaliOxygen.tools;

import javax.swing.JComponent;

import org.w3c.dom.Document;

import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.text.xml.WSXMLTextEditorPage;
import ro.sync.exml.workspace.api.editor.page.text.xml.XPathException;

public class WSXMLTextPageAdapter extends WSPageAdapter {
	
	private WSXMLTextEditorPage page;

	public WSXMLTextPageAdapter(WSXMLTextEditorPage page){
		super(page);
		this.page = page;
		
	}
	
	@Override
	public Object[] evaluateXPath(String xpath) {
		try {
			return page.evaluateXPath(xpath);
		} catch (XPathException e) {
			return new Object[]{};
		}
	}

	@Override
	public int getPageType() {
		// TODO Auto-generated method stub
		return XML_PAGE;
	}

	@Override
	public void addPopUpMenuCustomizer(PopupMenuCustomizer customizer) {
		page.addPopUpMenuCustomizer(customizer);
	}

	@Override
	public void removePopUpMenuCustomizer(PopupMenuCustomizer customizer) {
		page.removePopUpMenuCustomizer(customizer);
	}

	

	@Override
	public JComponent getComponent() {
		return (JComponent) page.getTextComponent();
	}

	@Override
	public void scrollCaretToVisible() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WSEditor getParentEditor() {
		// TODO Auto-generated method stub
		return page.getParentEditor();
	}

	@Override
	public void setReadOnly(String arg0) {
	}

	@Override
	public Document getDocument(){
		Object[] xpathRes;
		try {
			xpathRes = page.evaluateXPath("/");
			if(xpathRes.length > 0){
				if(xpathRes[0] instanceof Document){
					return (Document) xpathRes[0];
				}
			}
		} catch (XPathException e) {
		}
		return null;
	}
	

}
