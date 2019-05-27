package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;

@RunWith(Parameterized.class)
public class T12EsParamTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhase() {
        return Arrays.asList(
                new String[]{"config.xml"},
                new String[]{"configNo.xml"}
        );
    }


	private String param;
	
    public T12EsParamTest(String param) {
		this.param = param;
    }
    
    @Override
	public String getFolder() {
		return "test12-es-param";
	}
    
	
	@Test
	public void test(){
		Config config = ConfigFactory.createDefaultConfig();
		config.setCompactSVRL(false);
		config.addValidationParam("config", param);
		doTest(config, "expected/" + param + ".es");
		
	}

	
	
}
