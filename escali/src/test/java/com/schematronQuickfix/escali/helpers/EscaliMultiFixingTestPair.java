package com.schematronQuickfix.escali.helpers;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.quickfix.FixingTestBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EscaliMultiFixingTestPair extends EscaliFixingTestPair {

    private ArrayList<FixingTestBase.ExecutionSetup> executionSetups = new ArrayList<>();

    public EscaliMultiFixingTestPair(SchematronInstancePair inputPair, TextSource[] expectedResult, Config escaliConfig) {
        super(inputPair, expectedResult, escaliConfig);
    }

    public EscaliMultiFixingTestPair(SchematronInstancePair inputPair, ResourceHelper res, String[] expectPath, Config escaliConfig) throws IOException {
        super(inputPair, res, expectPath, escaliConfig);

    }

    public EscaliMultiFixingTestPair addExecutionSetup(FixingTestBase.ExecutionSetup[] executionSetups){
        this.executionSetups.addAll(Arrays.asList(executionSetups));
        return this;
    }

    public EscaliMultiFixingTestPair addExecutionSetup(ArrayList<FixingTestBase.ExecutionSetup> executionSetups){

        this.executionSetups.addAll(executionSetups);
        return this;
    }
    public EscaliMultiFixingTestPair addExecutionSetup(FixingTestBase.ExecutionSetup executionSetup){
        this.executionSetups.add(executionSetup);
        return this;
    }

    @Override
    public FixingTestBase.ExecutionSetup getExecutionSetup() {
        return executionSetups.get(0);
    }

    public ArrayList<FixingTestBase.ExecutionSetup> getAllExecutionSetups() {
        return executionSetups;
    }
}
