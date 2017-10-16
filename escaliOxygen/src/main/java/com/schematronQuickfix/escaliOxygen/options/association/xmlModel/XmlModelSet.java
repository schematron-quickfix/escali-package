package com.schematronQuickfix.escaliOxygen.options.association.xmlModel;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.xerces.dom.ProcessingInstructionImpl;

import com.github.oxygenPlugins.common.collections.MultiValueHashMap;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.collections.ArrayUtil;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.tools.WSPageAdapter;

import ro.sync.ecss.dom.wrappers.AuthorPIDomWrapper;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.WSEditorPage;

public class XmlModelSet {
	private final ArrayList<XmlModel> models = new ArrayList<XmlModel>();
	private final MultiValueHashMap<String, XmlModel> modelBySchemaType = new MultiValueHashMap<String, XmlModel>();
	
	public XmlModelSet(){}
	
	public XmlModelSet(Object[] pis) throws URISyntaxException {
		this(ArrayUtil.castArray(pis, new ProcessingInstructionImpl[pis.length]));
	}
	public XmlModelSet(ProcessingInstructionImpl[] pis) throws URISyntaxException {
		if(pis != null && pis.length > 0){
			for (int i = 0; i < pis.length; i++) {
				try {
					this.addModel(XmlModel.getModel(pis[i]));
				} catch (MalformedURLException e) {
					try {
						DefaultProcessLoger.getDefaultProccessLogger().log(e);
					} catch (CancelException e1) {
					}
				}
			}
		}
	}
	
	public XmlModelSet(AuthorPIDomWrapper[] pis) throws URISyntaxException {
		if(pis != null && pis.length > 0){
			for (int i = 0; i < pis.length; i++) {
				try {
					this.addModel(XmlModel.getModel(pis[i]));
				} catch (MalformedURLException e) {
					try {
						DefaultProcessLoger.getDefaultProccessLogger().log(e);
					} catch (CancelException e1) {
					}
				}
			}
		}
	}
	
	private void addModel(XmlModel model){
		this.models.add(model);
		this.modelBySchemaType.put(model.getSchematypens(), model);
	}
	
	public void createVirtualModel(String piValue, String baseURI){
		try {
			this.addModel(XmlModel.getModel(piValue, baseURI));
		} catch (URISyntaxException e) {
			
		} catch (MalformedURLException e) {
		}
	}
	
	public ArrayList<XmlModel> getModels(){
		return this.models;
	}
	
	
	public ArrayList<XmlModel> getModels(String schemaType){
		return this.modelBySchemaType.getAll(schemaType);
	}
	
	public boolean hasModel(){
		return this.models.size() > 0;
	}
	
	public boolean hasModel(String schemaType){
		return this.modelBySchemaType.containsKey(schemaType);
	}
	
	
	public static XmlModelSet getXmlModelSet(WSEditor editor, EscaliMessanger ema){
		
		
		XmlModelSet models = new XmlModelSet();
		if(editor != null){
			
			WSEditorPage page = editor.getCurrentPage();
			
			WSPageAdapter pageAdapter = WSPageAdapter.getWSEditorAdapter(page);
			Object[] pis = pageAdapter.evaluateXPath("/processing-instruction()[name()='xml-model']");
			
			try {
				if(pageAdapter.getPageType() == WSPageAdapter.XML_PAGE){
					models = new XmlModelSet(pis);
				} else {
					models = new XmlModelSet(ArrayUtil.castArray(pis, new AuthorPIDomWrapper[pis.length]));
				}
			} catch (URISyntaxException e) {
				try {
					ema.viewException(e);
				} catch (CancelException e1) {
				}
			}
			
		}
		
		return models;
	}
	
	
}
