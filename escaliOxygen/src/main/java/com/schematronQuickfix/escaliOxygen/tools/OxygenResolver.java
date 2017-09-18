package com.schematronQuickfix.escaliOxygen.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import com.github.oxygenPlugins.common.text.uri.DefaultURIResolver;

import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class OxygenResolver extends DefaultURIResolver {
	private StandalonePluginWorkspace spw;
	
	public OxygenResolver(StandalonePluginWorkspace spw){
		super();
		this.spw = spw;
	}
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		URI baseUri = URI.create(base);
		URI uri = baseUri.resolve(href);
		
		URI catalogURI = null;
		try {
			 catalogURI = spw.getXMLUtilAccess().resolvePathThroughCatalogs(baseUri.toURL(), href, true, true).toURI();
		} catch (MalformedURLException e1) {
		} catch (URISyntaxException e) {
		}
		
		if(!uri.equals(catalogURI) && catalogURI != null){
			return super.resolve(catalogURI.toString(), base);
		}

		File f = new File(uri);
		
		try {
			URL url =  uri.toURL();
			WSEditor editor = spw.getEditorAccess(url, StandalonePluginWorkspace.MAIN_EDITING_AREA);
			if(editor != null){
				InputStream stream = editor.createContentInputStream();
				StreamSource src = new StreamSource(stream);
				src.setReader(editor.createContentReader());
				src.setSystemId(f);
				return src;
			}
			
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		
		return super.resolve(href, base);
		
//		try {
//			return new StreamSource(new FileInputStream(f));
//		} catch (FileNotFoundException e) {
//			throw new TransformerException(e.getMessage());
//		}
	}
	
}
