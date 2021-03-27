package pageObjects;

import org.openqa.selenium.By;

import io.appium.java_client.MobileBy;

/**
 * UI Map for TrustID_PersonalInfoScreen
 */
public class PersonalInfoScreen {
	
	public static final By nextbtn = By.xpath("//android.widget.Button[@text='次へ']");
	public static final By createaccountbtn = By.xpath("//android.widget.Button[@text='アカウントを作成する']");
	public static final By scrolltoPDFConfirmationtxt = MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"PDF外国政府等における重要な公人等のご確認について\").instance(0));");
	public static final By scrolltoJapanOnlybtn = MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"日本のみ\").instance(0));");
	public static final By JapanOnlybtn = By.xpath("//android.widget.Button[@text='日本のみ']");
	
	public static final By phoneNumber = By.xpath("//*[@resource-id='phoneNumber']/android.widget.EditText");
	public static final By mailAddress1 = By.xpath("(//*[@resource-id='mailAddress']/android.widget.EditText)[1]");
	public static final By mailAddress2 = By.xpath("(//*[@resource-id='mailAddress']/android.widget.EditText)[2]");
	public static final By scrolltoPassword = MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"パスワード\").instance(0));");
	public static final By scrolltoMail = MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"メールアドレス\").instance(0));");
	
	public static final By scrolltoNextbtn = MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"次へ\").instance(0));");
	public static final By password1 = By.xpath("(//*[@resource-id='password']/android.widget.EditText)[1]");
	public static final By password2 = By.xpath("(//*[@resource-id='password']/android.widget.EditText)[2]");
	
	public static final By confirmationscreen = By.xpath("//android.view.View[@text='以下の内容でよろしいでしょうか？']");
	public static final By SMSinput = By.xpath("//android.view.View[@text='SMS入力']");
	
	
	public static final By blankPhoneNumberErrmsg = By.xpath("//android.view.View[@text='携帯電話番号を入力してください。']");
	public static final By incorrectPhoneNumberErrmsg = By.xpath("//android.view.View[@text='携帯電話番号を正しく入力してください。']");
	public static final By blankMailAddress1Errmsg = By.xpath("(//android.view.View[@text='メールアドレスを入力してください。'])[1]");
	public static final By blankMailAddress2Errmsg = By.xpath("(//android.view.View[@text='メールアドレスを入力してください。'])[2]");
	public static final By incorrectMailAddress1Errmsg = By.xpath("(//android.view.View[@text='メールアドレスを正しく入力してください。'])[1]");
	public static final By incorrectMailAddress2Errmsg = By.xpath("(//android.view.View[@text='メールアドレスを正しく入力してください。'])[2]");
	public static final By unmatchMailAddressErrmsg = By.xpath("//android.view.View[@text='メールアドレスが一致しません']");
	
	public static final By blankPassword1Errmsg = By.xpath("(//android.view.View[@text='パスワードを入力してください。'])[1]");
	public static final By blankPassword2Errmsg = By.xpath("(//android.view.View[@text='パスワードを入力してください。'])[2]");
	public static final By unmatchPasswordErrmsg = By.xpath("//android.view.View[@text='パスワードが一致しません']");
	public static final By shortPasswordErrmsg = By.xpath("(//android.view.View[@text='パスワードは10～64文字で、英大文字、英小文字、数値、記号の内、最低3つは混在させてください。'])[1]");
}