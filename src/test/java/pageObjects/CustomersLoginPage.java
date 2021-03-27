package pageObjects;

import org.openqa.selenium.By;

/**
 * UI Map for BookFlightPage 
 */
public class CustomersLoginPage {

//	#loginPage
	public static final By mobNavbarCollapsed = By.xpath("//*[@class='navbar-toggler collapsed']");
	public static final By lstlanguage = By.xpath("//*[@id='headerLaunages']");
	public static final By optlanguage = By.xpath("//*[@id='menu-'] //*[text()='English']");
	public static final By txtloginemail = By.xpath("//*[@id='exampleInputEmail1']");
	public static final By txtloginpassword = By.xpath("//*[@id='exampleInputPassword1']");
	public static final By btnsignin = By.xpath("//*[text() = 'Sign in' and @type='submit']");

	
}