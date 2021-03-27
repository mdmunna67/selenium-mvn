package businessComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sel.framework.Status;
import com.sel.others.DriverScript;
import com.sel.others.ReusableLibrary;
import com.sel.others.ScriptHelper;
import pageObjects.CustomersHomePage;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Class for storing general purpose business components
 * 
 * 
 */
public class GeneralComponents extends ReusableLibrary {
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public GeneralComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	@Step
	public void checkCustomerBalance() throws InterruptedException {
		String balance = driver.findElement(CustomersHomePage.valBalance).getText();
		try {
			dataTable.putData("Customers_Data", "balance", balance);
			report.updateTestLog("Check Customer balance", "Customer available balance is "+balance, 
					Status.DONE);
		} catch (Exception e) {
			report.updateTestLog("Check Customer balance", "Exception : unable to get customer balance value.", 
					Status.FAIL);
		}
	}
	
	public static  boolean waitforvisibilityOfElement(WebDriver driver,By Element, int sec) {
		boolean visible = false;
		try{
			WebDriverWait wait = new WebDriverWait(driver,sec);
		    wait.until(ExpectedConditions.visibilityOfElementLocated(Element));
		    visible = true;
		}catch (Exception e) {
			visible = false;
		}
		return visible;
	}
}