package com.schematronQuickfix.escali.helpers;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.cmdInterface.Fixing;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;
import com.schematronQuickfix.xsm.operations.PositionalReplace;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class QuickFixTestStrategy {

    public final static String SVRL_MSG_NUMBER = "SVRL_MSG_NUMBER";
    public final static String QUICKFIXID = "QUICKFIXID";

    private final static TextSource ignorePIsSheet;

    private ProcessLoger logger;


    public QuickFixTestStrategy(ProcessLoger logger) {
        this.logger = logger;
    }

    static {
        String ignorePIsManipulatorSheet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsm:manipulator xmlns:xsm=\"http://www.schematron-quickfix.com/manipulator/process\" xmlns=\"http://www.escali.schematron-quickfix.com/null-namespace\"><xsm:replace node=\"//processing-instruction(sqfc-start)\"><xsm:content><?sqfc-start ?></xsm:content></xsm:replace><xsm:replace node=\"//processing-instruction(sqfc-end)\"><xsm:content><?sqfc-end ?></xsm:content></xsm:replace></xsm:manipulator>";
        ignorePIsSheet = TextSource.createVirtualTextSource(new File("manipulator.xsm"));
        ignorePIsSheet.setData(ignorePIsManipulatorSheet);

    }

    public void testQuickFixExecution(EscaliFixingTestPair testPair) throws SAXException, CancelException, IOException, XPathExpressionException, URISyntaxException, XSLTErrorListener, XMLStreamException {

        Fixing fixing = new Fixing(testPair.getInputPair().getSchemaDocument(), testPair.getInputPair().getInstanceDocument(), testPair.getEscaliConfig());

        FixingTestBase.ExecutionSetup exec = testPair.getExecutionSetup();
        int msgPos = exec.getMsgPos();
        String fixId = exec.getFixId();

        Properties ues = exec.getUE();
        Enumeration<?> ueNames = ues.propertyNames();
        String[] params = new String[ues.size()];
        int i = 0;
        while (ueNames.hasMoreElements()){
            String ueName = (String) ueNames.nextElement();
            String value =  ues.getProperty(ueName);
            params[i++] = ueName + "=" + value;
        }

        ArrayList<TextSource> results = fixing.executeFix(msgPos - 1, fixId, params);

        assertEquals(testPair.getExpected().size(), results.size());

        for (int j = 0; j < results.size(); j++) {

            TextSource act = results.get(j);
            TextSource exp = testPair.getExpected().get(j);


            act = new PositionalReplace(ignorePIsSheet, act).getSource();

            assertEquals(
                    exp.toString().replaceAll("\r\n", "\n"),
                    act.toString().replaceAll("\r\n", "\n")
            );
        }




    }

}
