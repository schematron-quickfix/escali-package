package com.schematronQuickfix.escaliOxygen.toolbar.main;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliEditorAdapter;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

public class CommitChanges extends OxygenToolbarButton {
	

	private final EscaliMessanger ema;
	
	public static String CHANGE_MARKER_XPATH = "//processing-instruction()[name() = 'sqfc-start' or name() = 'sqfc-end']";
	public static String START_CHANGE_MARKER_XPATH = "//processing-instruction()[name() = 'sqfc-start']";
	public static String END_CHANGE_MARKER_XPATH = "//processing-instruction()[name() = 'sqfc-end']";

	public static String OXY_CHANGE_MARKER_XPATH = "//processing-instruction()[name() = 'oxy_custom_start' or name() = 'oxy_custom_end'][contains(., 'sqf:type=sqfchange')]";
	public static String OXY_START_CHANGE_MARKER_XPATH = "//processing-instruction()[name() = 'oxy_custom_start'][contains(., 'sqf:type=sqfchange')]";
	public static String OXY_END_CHANGE_MARKER_XPATH = "//processing-instruction()[name() = 'oxy_custom_end'][contains(., 'sqf:type=sqfchange')]";

	public CommitChanges(final EscaliMessanger ema) {
		super(ema.getIcon(2, 10), "Commit changes");
		this.ema = ema;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		EscaliEditorAdapter editor = ema.getEditor();
		try {
			PositionalReplace xsm = new PositionalReplace(editor.getInstance());
			xsm.deleteNode(OXY_CHANGE_MARKER_XPATH);
			editor.setInstance(xsm.getSource(), new DefaultProcessLoger()).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isEnable() {
		if(ema == null){
			return true;
		}
		if(ema.getEditor() == null){
			return true;
		}
		try {
			StringNode sn = new StringNode(ema.getEditor().getInstance(), new MuteProcessLoger());
			return sn.getXPathBoolean(OXY_CHANGE_MARKER_XPATH);
		} catch (Exception e) {
			return true;
		}
//		return true;
	}
	
}
