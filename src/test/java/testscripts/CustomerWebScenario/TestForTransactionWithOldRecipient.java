package testscripts.CustomerWebScenario;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sel.framework.IterationOptions;
import com.sel.framework.ext.Browser;
import com.sel.framework.ext.ExecutionMode;
import com.sel.framework.ext.SeleniumTestParameters;
import com.sel.others.DriverScript;
import com.sel.others.SelTestCase;

/**
 * Test for login with newly registered user
 * 
 * 
 */
public class TestForTransactionWithOldRecipient extends SelTestCase {

	@Test(dataProvider = "TransactionOldRecipientUserConfiguration")
	public void testRunner(String testInstance, ExecutionMode executionMode,
			Browser browser, int startIteration, int endIteration) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for Transaction With OldRecipient");
		testParameters.setCurrentTestInstance(testInstance);
		
//		testParameters.setExecutionMode(executionMode);
		String RC = System.getProperty("RUN_MODE");
		if(RC != null){
			testParameters.setExecutionMode(ExecutionMode.valueOf(RC));
        }else{
        	testParameters.setExecutionMode(executionMode);
        }
		
//		testParameters.setBrowser(browser);
		
		String BROWSER = System.getProperty("BROWSER");
		if(BROWSER != null){
			testParameters.setBrowser(Browser.valueOf(BROWSER));
        }else{
        	testParameters.setBrowser(browser);
        }
		
		testParameters.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);
		testParameters.setStartIteration(startIteration);
		testParameters.setEndIteration(endIteration);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@DataProvider(name = "TransactionOldRecipientUserConfiguration", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] { { "Instance1", ExecutionMode.LOCAL,
				Browser.CHROME, 1, 1 }, };
	}
}