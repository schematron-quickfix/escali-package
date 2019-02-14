package com.schematronQuickfix.escali.helpers;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;

import java.io.IOException;

public class EscaliFixingTestPair extends EscaliTestPair {

    private FixingTestBase.ExecutionSetup executionSetup;

    public EscaliFixingTestPair(SchematronInstancePair inputPair, TextSource[] expectedResult, Config escaliConfig) {
        super(inputPair, expectedResult, escaliConfig);
    }

    public EscaliFixingTestPair(SchematronInstancePair inputPair, ResourceHelper res, String[] expectPath, Config escaliConfig) throws IOException {
        super(inputPair, res, expectPath, escaliConfig);

    }

    public EscaliFixingTestPair addExecutionSetup(FixingTestBase.ExecutionSetup executionSetup){
        this.executionSetup = executionSetup;
        return this;
    }

    public FixingTestBase.ExecutionSetup getExecutionSetup() {
        return executionSetup;
    }
}
