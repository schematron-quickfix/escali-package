package com.schematronQuickfix.escaliOxygen.tools;

import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;

public class Highlighter {
	private final WSAuthorEditorPage page;

	public Highlighter(WSAuthorEditorPage page){
		this.page = page;
		page.getPersistentHighlighter().getHighlights();
	}
	
	public static void createHighlighter(WSPageAdapter pageAdapt){
		if(pageAdapt.getPageType() == WSPageAdapter.AUTHOR_PAGE){
			
		}
	}
}
