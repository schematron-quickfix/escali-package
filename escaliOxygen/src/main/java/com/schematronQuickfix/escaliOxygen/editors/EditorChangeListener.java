package com.schematronQuickfix.escaliOxygen.editors;

import java.net.URL;
import java.util.HashMap;

import com.schematronQuickfix.escaliOxygen.validation.ValidationAdapter;

import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.WSEditorPage;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;
import ro.sync.exml.workspace.api.listeners.WSEditorChangeListener;
import ro.sync.exml.workspace.api.listeners.WSEditorListener;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class EditorChangeListener extends WSEditorChangeListener {
	private final EscaliMessanger ema;
	private WSEditor actualEditor = null;
	private static int main_area = StandalonePluginWorkspace.MAIN_EDITING_AREA;
	private final HashMap<URL, ValidationAdapter> valAdapterByURL = new HashMap<URL, ValidationAdapter>();

	public EditorChangeListener(EscaliMessanger ema) {
		this.ema = ema;
		ema.getPluginWorkspace().addEditorChangeListener(this, main_area);
	}
	
	@Override
	public void editorOpened(URL url) {
		openEditor(url);
		super.editorOpened(url);
	}

	@Override
	public void editorActivated(URL url) {
		notifySelection(url);
		super.editorActivated(url);
	}

	@Override
	public void editorSelected(URL url) {
		notifySelection(url);
		super.editorSelected(url);
	}

	@Override
	public void editorClosed(URL url) {
		this.valAdapterByURL.remove(url);
		super.editorClosed(url);
	}

	@Override
	public void editorPageChanged(URL url) {
		super.editorPageChanged(url);
	}
	private void openEditor(URL url) {
		openEditor(url, true);
	}
	private void openEditor(URL url, boolean notify) {
		WSEditor editor = ema.getPluginWorkspace().getEditorAccess(url, main_area);
		
		if(ValidationAdapter.isSQFevailable(editor)){
			
			ValidationAdapter valAdap = ValidationAdapter.checkForSQFValidation(editor, ema);
			this.valAdapterByURL.put(url, valAdap);
			if(notify){
				notifySelection(url);
			}
		}
	}
	
	private void notifySelection(URL url){
		if(valAdapterByURL.containsKey(url)){
			ValidationAdapter valAdap = valAdapterByURL.get(url);
			this.ema.setEditor(valAdap);
			valAdap.notifySelection();
		} else if(url == null){
			this.ema.getMessageList().removeAllItems();
		}
	}

	public ValidationAdapter getAdapter(URL fixURL) {
		if(!valAdapterByURL.containsKey(fixURL)){
			this.ema.getPluginWorkspace().open(fixURL);
		}
		return valAdapterByURL.get(fixURL);
	}
	
	public void removeAllAdapter(){
		for (URL url : valAdapterByURL.keySet()) {
			removeAdapter(url);
		}
		notifySelection(null);
	}
	
	private void removeAdapter(URL url){
		ValidationAdapter adapter = valAdapterByURL.get(url);
		if(adapter != null){
			adapter.getEditor().removeValidationProblemsFilter(adapter);
			valAdapterByURL.remove(url);
		}
	}
	
	public void checkForSQFValidations(){
		URL[] urls = this.ema.getPluginWorkspace().getAllEditorLocations(main_area);
		for (URL url : urls) {
			openEditor(url, false);
		}
		notifySelection(this.ema.getPluginWorkspace().getCurrentEditorAccess(main_area).getEditorLocation());
		
	}
	
	
	
}
