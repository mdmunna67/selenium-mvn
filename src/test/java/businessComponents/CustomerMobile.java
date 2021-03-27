package businessComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sel.framework.Status;
import com.sel.others.DriverScript;
import com.sel.others.ReusableLibrary;
import com.sel.others.ScriptHelper;

import pageObjects.CustomersHomePage;
import pageObjects.CustomersLoginPage;
import ru.yandex.qatools.allure.annotations.Step;


/**
 * Class for storing business components related to the CustomerMobile functionality
 *
 */
public class CustomerMobile extends ReusableLibrary {
	
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public CustomerMobile(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	@Step
	public void invokeApplication() {
	
		String url ="";
		url = System.getProperty("URL");
		if(url != null){
			driver.get(url);
		}else{
			url = dataTable.getData("General_Data", "URL").trim();
			driver.get(url);
		}
		report.updateTestLog("Invoke Application", "Invoke the application under test @ " + url, Status.DONE);
	}
	
	@Step
	public void loginMobRemitCustomer()  {
		
		try {			
			WebElement element = driver.findElement(CustomersLoginPage.mobNavbarCollapsed);
			element.click();
			
			driver.findElement(CustomersLoginPage.lstlanguage).click();
			driver.findElement(CustomersLoginPage.optlanguage).click();
			
			WebElement loginemail = driver.findElement(CustomersLoginPage.txtloginemail);
			loginemail.click();
			String dataloginemail = dataTable.getData("General_Data", "loginemail").trim();
			loginemail.sendKeys(dataloginemail);
//			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginemail);
			
			WebElement loginpassword = driver.findElement(CustomersLoginPage.txtloginpassword);
			loginpassword.click();
			String dataloginpassword = dataTable.getData("General_Data", "loginpassword").trim();
			loginpassword.sendKeys(dataloginpassword);
			
			WebElement signin = driver.findElement(CustomersLoginPage.btnsignin);
			signin.click();
			
			report.updateTestLog("Login to Customer site", "Customers Login Successful", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Login to Customer site", "Customers Login unsuccessful", Status.FAIL);
		}
		
	}
	
	@Step
	public void homepageMobRemitCustomer() throws InterruptedException {
		boolean Elmstatus = waitforvisibilityOfElement(CustomersHomePage.mobHPNavbar,10);
		WebElement mobHPNavbar = driver.findElement(CustomersHomePage.mobHPNavbar);
		mobHPNavbar.click();
		if(Elmstatus){
			report.updateTestLog("Customer Home page should be displayed", "Customer Home page is getting displayed", 
						Status.PASS);
		}else{
			report.updateTestLog("Customer Home page should be displayed", "Customer Home page is NOT getting displayed", 
						Status.FAIL);
		}
	}

	
	public  boolean waitforvisibilityOfElement(By Element, int sec) {
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