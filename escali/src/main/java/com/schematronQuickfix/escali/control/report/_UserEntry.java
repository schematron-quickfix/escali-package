package com.schematronQuickfix.escali.control.report;

import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;

public interface _UserEntry extends _ModelNode {
	public String getName();
	public String getParameterName();
	public Object getValue();
	public String getValueAsString();
	public void setValue(Object value);
//	public void setValue(Object value, boolean useDefault);
	public boolean hasDefault();
	public boolean usingDefault();
	public boolean isValueValid();
	public String getDataType();
	boolean isValueSet();
	void useDefaultIfAvailable();
	TypeConverter getTypeConverter();
}
