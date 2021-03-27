package businessComponents;

import org.openqa.selenium.By;

import com.sel.framework.Status;
import com.sel.others.DriverScript;
import com.sel.others.ReusableLibrary;
import com.sel.others.ScriptHelper;

import pageObjects.CustomersHomePage;
import pageObjects.CustomersLoginPage;
import pageObjects.CustomersTransactionPage;
import ru.yandex.qatools.allure.annotations.Step;


/**
 * Class for storing business components related to the CustomerWeb functionality
 *
 */
public class CustomerWeb extends ReusableLibrary {
	
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public CustomerWeb(ScriptHelper scriptHelper) {
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
	public void loginRemitCustomer()  {
		
		try {			
			driver.findElement(CustomersLoginPage.lstlanguage).click();
			driver.findElement(CustomersLoginPage.optlanguage).click();
			String loginemail = dataTable.getData("General_Data", "loginemail").trim();
			String loginpassword = dataTable.getData("General_Data", "loginpassword").trim();
			driver.findElement(CustomersLoginPage.txtloginemail).sendKeys(loginemail);
			driver.findElement(CustomersLoginPage.txtloginpassword).sendKeys(loginpassword);
			driver.findElement(CustomersLoginPage.btnsignin).click();
			report.updateTestLog("Login to Customer site", "Customers Login Successful", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Login to Customer site", "Customers Login unsuccessful", Status.FAIL);
		}
		
	}
	
	@Step
	public void homepageRemitCustomer() throws InterruptedException {
		boolean Elmstatus = businessComponents.GeneralComponents.waitforvisibilityOfElement(driver,CustomersHomePage.txtaccount,10);
		if(Elmstatus){
			report.updateTestLog("Customer Home page should be displayed", "Customer Home page is getting displayed", 
						Status.PASS);
		}else{
			report.updateTestLog("Customer Home page should be displayed", "Customer Home page is NOT getting displayed", 
						Status.FAIL);
		}
	}

	@Step
	public void sendMoneyWithOldRecipient() throws InterruptedException {
		
		String dRecipientname = dataTable.getData("Customers_Data", "recipient");
		String dAmountOption = dataTable.getData("Customers_Data", "amountoption");
		String dSendingAmount = dataTable.getData("Customers_Data", "amount");
		String dDepositOption = dataTable.getData("Customers_Data", "depositoption");
		String dReasonSendingMoney = dataTable.getData("Customers_Data", "reasonsendingmoney");
		String dFundsource = dataTable.getData("Customers_Data", "fundsource");
		String dPin = dataTable.getData("Customers_Data", "pin");
		String balance = "";
		String iSendingAmount = "";
		String dCurrentBalance = "";
		
		driver.findElement(CustomersHomePage.sidebarSendMoney).click();
		boolean Elmstatus = businessComponents.GeneralComponents.waitforvisibilityOfElement(driver,CustomersTransactionPage.lblRecipient,10);

		iSendingAmount = dSendingAmount.replaceAll(",","");
		Long SendingAmount = Long.valueOf(iSendingAmount.trim()).longValue();
		
		balance = driver.findElement(CustomersHomePage.valBalance).getText();
		dCurrentBalance = balance.replaceAll(",","");
		Long CurrentBalance = Long.valueOf(dCurrentBalance.trim()).longValue();
		
		if(!(SendingAmount>CurrentBalance)){
			if(Elmstatus){
				driver.findElement(CustomersTransactionPage.selectRecipient).click();
				driver.findElement(By.xpath("//*[@id='menu-'] //*[text()='"+ dRecipientname +"']")).click();
				if(dAmountOption.equalsIgnoreCase("jpy")){
					driver.findElement(CustomersTransactionPage.amountOptionJpy).click();
				}else{
					driver.findElement(CustomersTransactionPage.amountOptionNonJpy).click();
				}
				
				driver.findElement(CustomersTransactionPage.sendingAmount).sendKeys(dSendingAmount);
				
				if(dDepositOption.equalsIgnoreCase("My Wallet")){
					driver.findElement(CustomersTransactionPage.depositOptionMyWallet).click();
				}else{
					driver.findElement(CustomersTransactionPage.depositOptionFamilyMart).click();
				}
				
				driver.findElement(CustomersTransactionPage.reasonSendingMoney).click();
				driver.findElement(By.xpath("//*[@id='menu-'] //*[text()='"+ dReasonSendingMoney +"']")).click();
				driver.findElement(CustomersTransactionPage.fundSource).click();
				driver.findElement(By.xpath("//*[@id='menu-'] //*[text()='"+ dFundsource +"']")).click();
				driver.findElement(CustomersTransactionPage.btncontinue).click();
				report.updateTestLog("Screenshot", "Transaction form", Status.SCREENSHOT);
				try {
					boolean Elmmsg = businessComponents.GeneralComponents.waitforvisibilityOfElement(driver,CustomersTransactionPage.msgDailyRemitLimit,10);
					if(Elmmsg){
						report.updateTestLog("Customer should be able to do transaction.", "Daily Remittance Limit Exceeded.", 
								Status.FAIL);
					}else{
						boolean Elmconfirmbtn = businessComponents.GeneralComponents.waitforvisibilityOfElement(driver, CustomersTransactionPage.btnconfirm,10);
						
						if(Elmconfirmbtn){
							driver.findElement(CustomersTransactionPage.btnconfirm).click();
							report.updateTestLog("Customer should be able to click on Confirm button.", "Successfully clicked on Confirm button.", 
									Status.DONE);
						}else{
							report.updateTestLog("Customer should be able to click on Confirm button.", "Confirm button is NOT visible.", 
									Status.FAIL);
						}
						
						boolean ElmtxtPIN = businessComponents.GeneralComponents.waitforvisibilityOfElement(driver, CustomersTransactionPage.txtPIN1,10);
						String[] pin = dPin.split(",");
						
						if(ElmtxtPIN){
							driver.findElement(CustomersTransactionPage.txtPIN1).sendKeys(pin[0]);
							driver.findElement(CustomersTransactionPage.txtPIN2).sendKeys(pin[1]);
							driver.findElement(CustomersTransactionPage.txtPIN3).sendKeys(pin[2]);
							driver.findElement(CustomersTransactionPage.txtPIN4).sendKeys(pin[3]);
							driver.findElement(CustomersTransactionPage.txtPIN5).sendKeys(pin[4]);
							driver.findElement(CustomersTransactionPage.txtPIN6).sendKeys(pin[5]);
							driver.findElement(CustomersTransactionPage.btnSubmit).click();
							report.updateTestLog("Customer should be able to Enter 6 digit PIN.", "Successfully entered 6 digit PIN.", 
									Status.DONE);
						}else{
							report.updateTestLog("Customer should be able to Enter 6 digit PIN.", "Unable to enter 6 digit PIN", 
									Status.FAIL);
						}
						
						Thread.sleep(10000);
						String AftTransCurrentbal = driver.findElement(CustomersHomePage.valBalance).getText();
						dataTable.putData("Customers_Data", "AfterTransactionbal", AftTransCurrentbal);
						report.updateTestLog("Customer should be able to do transaction.", "Current balance: "+ AftTransCurrentbal +". Customer is able to send money successfully.", 
								Status.PASS);
					}
				
				} catch (Exception e) {
					report.updateTestLog("Customer should be able to do transaction.", "Customer is NOT able to send money.", 
							Status.FAIL);
				}

			}else{
				report.updateTestLog("Customer should be able to do transaction.", "Customer is NOT able to send money (or) unable to reach Send Money/Transcation page.", 
						Status.FAIL);
			}
		}else{
			report.updateTestLog("Customer should be able to do transaction.", "Transaction money "+ dSendingAmount + " should be less than current balance "+balance, 
					Status.FAIL);
		}
	}
    
}