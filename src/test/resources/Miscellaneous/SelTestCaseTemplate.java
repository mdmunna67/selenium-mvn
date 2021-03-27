package Miscellaneous;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sel.Sel.SelTestCase;
import com.sel.Sel.DriverScript;
import com.sel.framework.IterationOptions;
import com.sel.framework.selenium.*;


/**
 * Test for login with invalid user credentials
 * 
 *
 */
public class SelTestCaseTemplate extends SelTestCase {

	@Test(dataProvider = "DataProviderName")
	public void testRunner(String testInstance, ExecutionMode executionMode,
			MobileToolName mobileToolName,
			MobileExecutionPlatform executionPlatform, String osVersion,
			String deviceName, Browser browser, int startIteration,
			int endIteration) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for login with invalid user credentials");
		testParameters.setCurrentTestInstance(testInstance);
		testParameters.setExecutionMode(executionMode);
		testParameters.setMobileExecutionPlatform(executionPlatform);
		testParameters.setMobileToolName(mobileToolName);
		testParameters.setmobileOSVersion(osVersion);
		testParameters.setDeviceName(deviceName);
		testParameters.setBrowser(browser);
		testParameters
				.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);
		testParameters.setStartIteration(startIteration);
		testParameters.setEndIteration(endIteration);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@DataProvider(name = "DataProviderName", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] {
				{ "Instance1", ExecutionMode.MOBILE, MobileToolName.APPIUM,
						MobileExecutionPlatform.ANDROID, "Version", "DeviceName",
						 Browser.CHROME, 1, 1 } };
	}
}