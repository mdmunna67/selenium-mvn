package businessComponents;

import com.sel.framework.Status;
import com.sel.others.DriverScript;
import com.sel.others.ReusableLibrary;
import com.sel.others.ScriptHelper;

import pageObjects.PersonalInfoScreen;
import ru.yandex.qatools.allure.annotations.Step;


/**
 * Class for storing business components related to the TrustID functionality
 *
 */
public class TrustID extends ReusableLibrary {
	
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public TrustID(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	@Step
	public void navigatetoPersonalInfoScreen() throws InterruptedException {	
		boolean Elmnextbtn = waitForEl(PersonalInfoScreen.nextbtn);
		if(Elmnextbtn){
			click(PersonalInfoScreen.nextbtn);
			click(PersonalInfoScreen.createaccountbtn);
			driver.findElement(PersonalInfoScreen.scrolltoPDFConfirmationtxt); 
			driver.findElement(PersonalInfoScreen.scrolltoJapanOnlybtn);
			click(PersonalInfoScreen.JapanOnlybtn);
		}
		boolean ElmphoneNumber = waitForEl(PersonalInfoScreen.phoneNumber);
		if(ElmphoneNumber){
			report.updateTestLog("Verify whether PersonalInfo Screen is getting displayed.", "PersonalInfoScreen is getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether PersonalInfo Screen is getting displayed.", "unable to navigate to PersonalInfo Screen.", Status.FAIL);
		}
	}

	@Step
	public void enterCellPhoneNumber()  {
		String DphoneNumber = dataTable.getData("PersonalInfo_Data", "phoneNumber").trim();
		try {
			if(!DphoneNumber.equals("")){
				sendKeys(PersonalInfoScreen.phoneNumber, DphoneNumber);
				report.updateTestLog("Enter CellPhoneNumber", "CellPhoneNumber is entered successfully.", Status.PASS);
			}
		} catch (Exception e) {
			report.updateTestLog("Enter CellPhoneNumber", "CellPhoneNumber is NOT entered/blank.", Status.FAIL);
		}
	}
	
	@Step
	public void enterMailAddresss()  {
		String DmailAddress1 = dataTable.getData("PersonalInfo_Data", "mailAddress1").trim();
		String DmailAddress2 = dataTable.getData("PersonalInfo_Data", "mailAddress2").trim();
		try {
			scroll(ScrollDirection.DOWN, 0.3);
			if(!DmailAddress1.equals("") && !DmailAddress2.equals("")){
				sendKeys(PersonalInfoScreen.mailAddress1, DmailAddress1);
				sendKeys(PersonalInfoScreen.mailAddress2, DmailAddress2);
				report.updateTestLog("Enter MailAddresss", "MailAddresss are entered successfully.", Status.PASS);
			}
		} catch (Exception e) {
			report.updateTestLog("Enter MailAddresss", "MailAddresss are NOT entered.", Status.FAIL);
		}
	}
	
	@Step
	public void enterPassword()  {
		try {
			String Dpassword1 = dataTable.getData("PersonalInfo_Data", "password1").trim();
			String Dpassword2 = dataTable.getData("PersonalInfo_Data", "password2").trim();
			driver.findElement(PersonalInfoScreen.scrolltoNextbtn);
			if(!Dpassword1.equals("") && !Dpassword2.equals("")){
				sendKeys(PersonalInfoScreen.password1, Dpassword1);
				sendKeys(PersonalInfoScreen.password2, Dpassword2);
				report.updateTestLog("Enter Password", "Password is entered successfully.", Status.PASS);
			}
		} catch (Exception e) {
			report.updateTestLog("Enter Password", "Password is NOT entered/blank.", Status.FAIL);
		}
	}
	
	@Step
	public void confirmationscreen()  {
		driver.findElement(PersonalInfoScreen.nextbtn).click();
		boolean Elmconfirmationtxt =  waitForEl(PersonalInfoScreen.confirmationscreen);
		if(Elmconfirmationtxt){
			report.updateTestLog("Verify whether confirmationscreen is getting displayed.", "confirmationscreen is getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether confirmationscreen is getting displayed.", "unable to submit Personal information details.", Status.FAIL);
		}
	}
	
	@Step
	public void smsInputscreen()  {
		click(PersonalInfoScreen.nextbtn);
		boolean Elmsmsinput =  waitForEl(PersonalInfoScreen.SMSinput);
		if(Elmsmsinput){
			report.updateTestLog("smsInputscreen", "SMS input entered successfully.", Status.PASS);
		}else{
			report.updateTestLog("smsInputscreen", "unable to enter SMS input.", Status.FAIL);
		}
	}
	
	@Step
	public void blankCellPhoneNumber()  {
		enterCellPhoneNumber();
		boolean ErrphoneNumber = false;
		try {
				ErrphoneNumber = waitForEl(PersonalInfoScreen.blankPhoneNumberErrmsg);
				if(ErrphoneNumber){
					report.updateTestLog("Verify whether error message for blank CellPhoneNumber is getting display.", "Error message for blank CellPhoneNumber is getting displayed.", Status.PASS);
				}else{
					report.updateTestLog("Verify whether error message for blank CellPhoneNumber is getting display.", "Error message for blank CellPhoneNumber is NOT getting displayed.", Status.FAIL);
				}
		}catch (Exception e) {
			report.updateTestLog("Verify whether error message for blank CellPhoneNumber is getting display.", "Element not found.", Status.FAIL);
		}
	}
	
	@Step
	public void incorrectCellPhoneNumber()  {
		enterCellPhoneNumber();
		boolean ErrphoneNumber =  waitForEl(PersonalInfoScreen.incorrectPhoneNumberErrmsg);
		if(ErrphoneNumber){
			report.updateTestLog("Verify whether error message for incorrect CellPhoneNumber is getting display.", "Error message for incorrect CellPhoneNumber is getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether error message for incorrect CellPhoneNumber is getting display.", "Error message for incorrect CellPhoneNumber is NOT getting displayed.", Status.FAIL);
		}
	}
	
	@Step
	public void blankMailAddresss()  {
		enterMailAddresss();
		boolean ErrMailAddress1 = false;
		boolean ErrMailAddress2 = false;
		try {
				ErrMailAddress1 = waitForEl(PersonalInfoScreen.blankMailAddress1Errmsg);
			    ErrMailAddress2 = waitForEl(PersonalInfoScreen.blankMailAddress2Errmsg);
				if(ErrMailAddress1 && ErrMailAddress2){
					report.updateTestLog("Verify whether error message for blank MailAddress is getting display.", "Error message for blank MailAddress is getting displayed.", Status.PASS);
				}else{
					report.updateTestLog("Verify whether error message for blank MailAddress is getting display.", "Error message for blank MailAddress is NOT getting displayed.", Status.FAIL);
				}
		}catch (Exception e) {
			report.updateTestLog("Verify whether error message for blank MailAddress is getting display.", "Element not found.", Status.FAIL);
		}
	}
	
	@Step
	public void incorrectMailAddresss()  {
		enterMailAddresss();
		boolean incorrectMailAddress1 =  waitForEl(PersonalInfoScreen.incorrectMailAddress1Errmsg);
		boolean incorrectMailAddress2 =  waitForEl(PersonalInfoScreen.incorrectMailAddress2Errmsg);
		if(incorrectMailAddress1 && incorrectMailAddress2){
			report.updateTestLog("Verify whether error message for incorrect MailAddress is getting display.", "Error message for incorrect MailAddress is getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether error message for incorrect MailAddress is getting display.", "Error message for incorrect MailAddress is NOT getting displayed.", Status.FAIL);
		}
	}
	
	@Step
	public void unmatchMailAddresss()  {
		enterMailAddresss();
		boolean unmatchMailAddress =  waitForEl(PersonalInfoScreen.unmatchMailAddressErrmsg);
		if(unmatchMailAddress){
			report.updateTestLog("Verify whether error message for unmatchMailAddress is getting display.", "Error message for unmatch MailAddress is getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether error message for unmatchMailAddress is getting display.", "Error message for unmatch MailAddress is NOT getting displayed.", Status.FAIL);
		}
	}
	
	@Step
	public void blankPassword()  {
		enterPassword();
		boolean ErrPassword1 = false;
		boolean ErrPassword2 = false;
		try {
				ErrPassword1 = waitForEl(PersonalInfoScreen.blankPassword1Errmsg);
				ErrPassword2 = waitForEl(PersonalInfoScreen.blankPassword2Errmsg);
				if(ErrPassword1 && ErrPassword2){
					report.updateTestLog("Verify whether error message for blank Password is getting display.", "Error message for blank Password is getting displayed.", Status.PASS);
				}else{
					report.updateTestLog("Verify whether error message for blank Password is getting display.", "Error message for blank Password is NOT getting displayed.", Status.FAIL);
				}
		}catch (Exception e) {
			report.updateTestLog("Verify whether error message for blank Password is getting display.", "Element not found.", Status.FAIL);
		}
	}
	
	@Step
	public void unmatchPassword()  {
		enterPassword();
		boolean ErrunmatchPassword =  waitForEl(PersonalInfoScreen.unmatchPasswordErrmsg);
		if(ErrunmatchPassword){
			report.updateTestLog("Verify whether error message for unmatch Password is getting display.", "Error message for unmatch Password is getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether error message for unmatch Password is getting display.", "Error message for unmatch Password is NOT getting displayed.", Status.FAIL);
		}
	}
	
	@Step
	public void invalidConfirmationscreen()  {
		driver.findElement(PersonalInfoScreen.nextbtn).click();
		boolean Elmconfirmationtxt =  waitForEl(PersonalInfoScreen.confirmationscreen);
		if(!Elmconfirmationtxt){
			report.updateTestLog("Verify whether confirmationscreen is NOT getting displayed.", "confirmationscreen is NOT getting displayed.", Status.PASS);
		}else{
			report.updateTestLog("Verify whether confirmationscreen is NOT getting displayed.", "confirmationscreen is getting displayed.", Status.FAIL);
		}
	}
	
	@Step
	public void closeApp()  {
		driver.closeApp();
//		driver.launchApp();
	}
	
}