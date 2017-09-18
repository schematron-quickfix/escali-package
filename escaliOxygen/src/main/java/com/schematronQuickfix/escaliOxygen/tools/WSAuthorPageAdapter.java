package com.schematronQuickfix.escaliOxygen.tools;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.text.BadLocationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escaliOxygen.toolbar.main.CommitChanges;

import de.schlichtherle.io.File;
import ro.sync.document.DocumentPositionedInfo;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.access.AuthorEditorAccess;
import ro.sync.ecss.extensions.api.highlights.AuthorHighlighter;
import ro.sync.ecss.extensions.api.highlights.HighlightPainter;
import ro.sync.ecss.extensions.api.highlights.HighlightPainterInfo;
import ro.sync.exml.view.graphics.Color;
import ro.sync.exml.view.graphics.Graphics;
import ro.sync.exml.view.graphics.Rectangle;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;

public class WSAuthorPageAdapter extends WSPageAdapter {

	private final WSAuthorEditorPage page;

	public WSAuthorPageAdapter(WSAuthorEditorPage page){
		super(page);
		this.page = page;
	}
	
	@Override
	public Object[] evaluateXPath(String xpath) {
		// TODO Auto-generated method stub
		try {
			return page.getAuthorAccess().evaluateXPath(xpath, true, true, true);
		} catch (AuthorOperationException e) {
			return new Object[]{};
		}
	}

	@Override
	public int getPageType() {
		// TODO Auto-generated method stub 
		return AUTHOR_PAGE;
	}

	@Override
	public void addPopUpMenuCustomizer(PopupMenuCustomizer customizer) {
		page.addPopUpMenuCustomizer(customizer);
		AuthorEditorAccess editor = page.getAuthorAccess().getEditorAccess();
		
		try {
			TextSource src = TextSource.readTextFile(new File(editor.getEditorLocation().toURI()));
			StringNode sn = new StringNode(src);
			ArrayList<NodeInfo> nodeInfos = sn.getNodeSetInfo(CommitChanges.START_CHANGE_MARKER_XPATH);
			AuthorHighlighter highlighter = page.getHighlighter();

			for (NodeInfo nis : nodeInfos) {
				NodeInfo nie = sn.getNodeInfo(CommitChanges.END_CHANGE_MARKER_XPATH + "[. = '" + nis.getNode().getNodeValue() + "']");
				
				DocumentPositionedInfo dpi = new DocumentPositionedInfo(0);
				dpi.setOffset(nis.getEndOffset() + 1);
				dpi.setLength(nie.getStartOffset() - 1);
				dpi.setSystemID(nis.getNode().getBaseURI());
				int[] se = page.getStartEndOffsets(dpi);
				
				
				highlighter.addHighlight(se[0], se[1], new HighlightPainter() {
					
					@Override
					public void paint(HighlightPainterInfo hpi) {
						Rectangle rs = page.modelToViewRectangle(hpi.getStartOffset());
						Rectangle re = page.modelToViewRectangle(hpi.getEndOffset());
						rs.width = hpi.getLength();
						rs.height = re.y + re.height - rs.y;
						Graphics g = hpi.getGraphics();
						g.setFillColor(Color.COLOR_YELLOW);
						g.fillRect(rs);
						
					}
				}, null);
			}
		} catch (IOException e1) {
		} catch (URISyntaxException e1) {
		} catch (XPathExpressionException e) {
		} catch (SAXException e) {
		} catch (XMLStreamException e) {
		} catch (BadLocationException e) {
		}
	}

	@Override
	public void removePopUpMenuCustomizer(PopupMenuCustomizer customizer) {
		page.removePopUpMenuCustomizer(customizer);
	}
	
	public JComponent getComponent(){
		return (JComponent) page.getAuthorComponent();
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
			xpathRes = page.getDocumentController().evaluateXPath("/", false, false, false);
			if(xpathRes.length > 0){
				if(xpathRes[0] instanceof Document){
					return (Document) xpathRes[0];
				}
			}
		} catch (AuthorOperationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
