package com.schematronQuickfix.escali.quickfix;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.helpers.*;
import net.sf.saxon.ma.trie.Tuple2;
import org.junit.Before;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public abstract class FixingTestBaseChangePi extends FixingTestBase {

	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setChangePrefix("sqfc");
		return config;
	}

}
