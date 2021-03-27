//package businessComponents;
//
//import org.openqa.selenium.By;
//
//import com.sel.framework.Status;
//import com.sel.others.*;
//
///**
// * Class for storing general purpose business components
// * 
// * 
// */
//public class GeneralComponentsMobileWeb extends ReusableLibrary {
//	/**
//	 * Constructor to initialize the component library
//	 * 
//	 * @param scriptHelper
//	 *            The {@link ScriptHelper} object passed from the
//	 *            {@link DriverScript}
//	 */
//	public GeneralComponentsMobileWeb(ScriptHelper scriptHelper) {
//		super(scriptHelper);
//	}
//
//	public void searchGoogle() {
//		driver.get("http://google.com");
//		driver.findElement(By.id("lst-ib")).sendKeys("Cognizant");
//		report.updateTestLog("SearchText", "Searching the text", Status.PASS);
//	}
//
//}