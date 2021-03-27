package testscripts.MobileTestingScenario;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sel.framework.IterationOptions;
import com.sel.framework.ext.ExecutionMode;
import com.sel.framework.ext.MobileExecutionPlatform;
import com.sel.framework.ext.MobileToolName;
import com.sel.framework.ext.SeleniumTestParameters;
import com.sel.others.DriverScript;
import com.sel.others.SelTestCase;

/**
 * Test for login with newly registered user
 * 
 * 
 */
 
public class TrustIDTestMobileApp extends SelTestCase {

		@Test(dataProvider = "TrustIDTestMobileApp")
		public void testRunner(String testInstance, ExecutionMode executionMode, MobileToolName mobileToolName,
				MobileExecutionPlatform executionPlatform, String deviceName,String mobilemodel) {
			SeleniumTestParameters testParameters = new SeleniumTestParameters(currentScenario, currentTestcase);
			testParameters.setCurrentTestDescription("TrustID Test Mobile App");
			testParameters.setCurrentTestInstance(testInstance);
			
//			testParameters.setExecutionMode(executionMode);
			String RC = System.getProperty("RUN_CONF");
			if(RC != null){
				System.out.println("RUN_CONF : "+RC);
				testParameters.setExecutionMode(ExecutionMode.valueOf(RC));
	        }else{
	        	testParameters.setExecutionMode(executionMode);
	        }
			
//			testParameters.setMobileToolName(mobileToolName);
			
			String mobileTool = System.getProperty("MOBILE_TOOL");
			if(mobileTool != null){
				System.out.println("mobileTool : "+mobileTool);
				testParameters.setMobileToolName(MobileToolName.valueOf(mobileTool));
	        }else{
	        	testParameters.setMobileToolName(mobileToolName);
	        }
			
			
//			testParameters.setMobileExecutionPlatform(executionPlatform);
			
			String mobilePlatform = System.getProperty("MOBILE_PLATFORM");
			if(mobilePlatform != null){
				System.out.println("mobilePlatform : "+mobilePlatform);
				testParameters.setMobileExecutionPlatform(MobileExecutionPlatform.valueOf(mobilePlatform));
	        }else{
	        	testParameters.setMobileExecutionPlatform(executionPlatform);
	        }
			
//			testParameters.setDeviceName(deviceName);
			
			String devname = System.getProperty("DEVICE");
			if(devname != null){
				System.out.println("devicename : "+devname);
				testParameters.setDeviceName(deviceName.valueOf(devname));
	        }else{
	        	testParameters.setDeviceName(deviceName);
	        }
			
//			testParameters.setMobileModel(mobilemodel);
			
//			String model = System.getProperty("MODEL");
//			if(model != null){
//				System.out.println("mobilemodel : "+model);
//				testParameters.setMobileModel(mobilemodel.valueOf(model));
//	        }else{
//	        	testParameters.setMobileModel(mobilemodel);
//	        }
			
			
			testParameters.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);

			DriverScript driverScript = new DriverScript(testParameters);
			driverScript.driveTestExecution();

			tearDownTestRunner(testParameters, driverScript);
		}

		@DataProvider(name = "TrustIDTestMobileApp", parallel = true)
		public Object[][] dataTC2() {
			return new Object[][] {

					{ "Instance2", ExecutionMode.LOCAL_EMULATED_DEVICE, MobileToolName.REMOTE_WEBDRIVER, MobileExecutionPlatform.WEB_IOS,
							"iPhone X" , "iPhone X" }, };
		}

}