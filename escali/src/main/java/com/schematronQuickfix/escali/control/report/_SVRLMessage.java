package com.schematronQuickfix.escali.control.report;

import java.io.File;
import java.util.ArrayList;

import javax.xml.xpath.XPathExpressionException;

import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;


public interface _SVRLMessage extends _ModelNode {
	public static final int TYPE_REPORT = 0;
	public static final int TYPE_ASSERT = 1;
	public static final int LEVEL_FATAL_ERROR = 3;
	public static final int LEVEL_ERROR = 2;
	public static final int LEVEL_WARNING = 1;
	public static final int LEVEL_INFO = 0;
	public static final String[] LEVEL_NAMES = new String[]{"information", "warning", "error", "fatal error"};
	public static final int LEVEL_COUNT = LEVEL_NAMES.length;
	public static final double LEVEL_DEFAULT = 0.625;
	String toString();
    _QuickFix[] getQuickFixes();
    String getPatternId();
    String getRuleId();
    _QuickFix getDefaultFix();
    double getErrorLevel();
    int getErrorLevelInt();
    _Flag getFlag();
	ArrayList<Diagnostic> getDiagnostics();
	double getErrorWeight();
	String getLocation();
	boolean hasQuickFixes();
	boolean hasDefaultFix();
	NodeInfo getLocationInIstance();
	File getInstanceFile();
	_QuickFix getQuickFix(String fixId);
	String[] getPhases();
}
