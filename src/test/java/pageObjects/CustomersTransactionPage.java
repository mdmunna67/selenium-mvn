package pageObjects;

import org.openqa.selenium.By;

/**
 * UI Map for BookFlightPage 
 */
public class CustomersTransactionPage {
	
// #SendMoneyPage
	public static final By lblRecipient = By.xpath("//*[text()='Select Recipient']");
	public static final By selectRecipient = By.xpath("//*[text()='Select Recipient']//following-sibling::div//*[@class='MuiSelect-root MuiSelect-select MuiSelect-selectMenu MuiInputBase-input MuiInput-input']");
	public static final By amountOptionJpy = By.xpath("//*[text()='Enter Amount in JPY']");
	public static final By amountOptionNonJpy = By.xpath("//*[text()='Enter Amount in Receiving Currency']");
	public static final By sendingAmount = By.xpath("//*[@class='form-control megaFontSize font-weight-bold' and @type='number' and @value='']");
	
	public static final By depositOptionMyWallet = By.xpath("//*[text()='My Wallet']");
	public static final By depositOptionFamilyMart = By.xpath("//*[text()='Family Mart']");
	
	public static final By reasonSendingMoney = By.xpath("//*[text()='Reason for Sending Money']//following-sibling::div//*[@class='MuiSelect-root MuiSelect-select MuiSelect-selectMenu MuiInputBase-input MuiInput-input']");
	public static final By fundSource = By.xpath("//*[text()='Source of fund']//following-sibling::div//*[@class='MuiSelect-root MuiSelect-select MuiSelect-selectMenu MuiInputBase-input MuiInput-input']");
	public static final By btncontinue = By.xpath("//*[@id='root'] //*[text()='Continue']");
	
	public static final By msgDailyRemitLimit = By.xpath("//*[text()='Daily Remittance Limit Exceeded']");
	public static final By btnconfirm = By.xpath("//*[@id='root'] //*[text()='Confirm']");
	public static final By txtPIN1 = By.xpath("(//*[@class='txtMargin'] //*[@type='number'])[1]");
	public static final By txtPIN2 = By.xpath("(//*[@class='txtMargin'] //*[@type='number'])[2]");
	public static final By txtPIN3 = By.xpath("(//*[@class='txtMargin'] //*[@type='number'])[3]");
	public static final By txtPIN4 = By.xpath("(//*[@class='txtMargin'] //*[@type='number'])[4]");
	public static final By txtPIN5 = By.xpath("(//*[@class='txtMargin'] //*[@type='number'])[5]");
	public static final By txtPIN6 = By.xpath("(//*[@class='txtMargin'] //*[@type='number'])[6]");
	public static final By btnSubmit = By.xpath("//*[text()='Submit']");
	
}