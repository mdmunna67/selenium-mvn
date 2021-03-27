package componentGroups;

import com.sel.others.DriverScript;
import com.sel.others.ReusableLibrary;
import com.sel.others.ScriptHelper;


/**
 * Class for storing component groups related to the flight reservation functionality
 * 
 */
public class CustomerWebTransWithOldRecipientGroups extends ReusableLibrary {
	/**
	 * Constructor to initialize the component group library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public CustomerWebTransWithOldRecipientGroups(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
//	public void CustomerWebTrans() throws InterruptedException {
//		CustomerWeb Customer = new CustomerWeb(scriptHelper);
//		Customer.loginRemitCustomer();
//		Customer.homepageRemitCustomer();
//		Customer.checkCustomerBalance();
//		Customer.sendMoneyWithOldRecipient();
//	}
}