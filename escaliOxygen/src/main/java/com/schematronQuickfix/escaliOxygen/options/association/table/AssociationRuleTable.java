package com.schematronQuickfix.escaliOxygen.options.association.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;


public class AssociationRuleTable extends ArrayList<AssociationRule> {

	private boolean isActive;

	public AssociationRuleTable() {
		this(EscaliPluginConfig.DEFAULT_CONFIG);
	}

	public AssociationRuleTable(String configXml) {

		try {
			TextSource ts = TextSource.createVirtualTextSource(File
					.createTempFile("escaliPlugin", ".xml"));
			ts.setData(configXml);
			StringNode sn = new StringNode(ts);
			this.isActive = sn
					.getXPathBoolean("/es:escaliPluginConfig/es:rules/@active = 'true'");
			addRules(sn.getNodeSet("/es:escaliPluginConfig/es:rules/es:rule"));
		} catch (IOException e) {
		} catch (SAXException e) {
		} catch (XMLStreamException e) {
		} catch (XPathExpressionException e) {
		}

	}

	private void addRules(NodeList nodes) {
		for (int i = 0; i < nodes.getLength(); i++) {
			AssociationRule rule = AssociationRule.createRule(nodes.item(i));
			this.add(rule);
		}
	}
	
	
	public boolean isActive(){
		return this.isActive;
	}
	
	
	@Override
	public String toString() {
		String rules = ""
				+ "<es:rules xmlns:es=\"http://www.escali.schematron-quickfix.com/\"" 
				+ " active=\"" 
				+ this.isActive
				+ "\"" 
				+ ">";
		for (AssociationRule rule : this) {
			rules += rule.asXML();
		}
		rules += "</es:rules>";
		return rules;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	protected ArrayList<AssociationRule> getSelectedRows() {
		ArrayList<AssociationRule> selectedRows = new ArrayList<AssociationRule>();
		for (AssociationRule row : this) {
			if (row.isSelected()) {
				selectedRows.add(row);
			}
		}
		return selectedRows;
	}
	
	public int[] getSelectionPos(){
		ArrayList<AssociationRule> selectedRows = getSelectedRows();
		if(selectedRows.size() == 0)
			return null;
		int[] pos = new int[selectedRows.size()];
		int i = 0;
		for (AssociationRule row : selectedRows) {
			pos[i++] = this.indexOf(row);
		}
		return pos;
	}
	
	
	public void shiftSelection(AssociationRule row){
		int[] selPos = getSelectionPos();
		if(selPos == null || row.isSelected()){
			row.setSelected(!row.isSelected());
			return;
		} else {
			int newSel = this.indexOf(row);
			int from = -1;
			int to = newSel;
			for (int i = 0; i < selPos.length && from < newSel; i++) {
				if(i == 0 && selPos[i] > newSel){
					from = newSel;
					to = selPos[i];
					break;
				}
				from = selPos[i]; 
			}
			
			for (int i = 0; i < this.size(); i++) {
				if(i >= from && i <= to){
					this.get(i).setSelected(true);
				} else {
					this.get(i).setSelected(false);
				}
			}
			
			
		}
		
		
	}

}
