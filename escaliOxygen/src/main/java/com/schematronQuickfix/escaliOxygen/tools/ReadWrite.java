package com.schematronQuickfix.escaliOxygen.tools;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import com.github.oxygenPlugins.common.text.TextSource;

import ro.sync.exml.workspace.api.editor.WSEditorBase;

public class ReadWrite {
	private static TextSource createTextSource(WSEditorBase editor, File file) throws IOException{
//		return TextSource.readTextFile(editor.createContentInputStream(), file);
		return TextSource.readTextFile(editor.createContentReader(), "UTF-8", file);
		
	}
	public static TextSource createTextSource(WSEditorBase editor, URI uri) throws IOException{
		return createTextSource(editor, new File(uri));
	}
	
	public static void setEditorContent(WSEditorBase editor, TextSource src){
		editor.reloadContent(new StringReader(src.toString()), false);
	}
}
