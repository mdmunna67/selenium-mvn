package com.sel.others;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

//import com.experitest.client.Client;
//import com.experitest.selenium.MobileWebDriver;
import com.sel.framework.ExcelDataAccess;
import com.sel.framework.FrameworkException;
import com.sel.framework.FrameworkParameters;
import com.sel.framework.IterationOptions;
import com.sel.framework.OnError;
import com.sel.framework.ReportSettings;
import com.sel.framework.ReportTheme;
import com.sel.framework.ReportThemeFactory;
import com.sel.framework.SelDataTable;
import com.sel.framework.Settings;
import com.sel.framework.Status;
import com.sel.framework.TimeStamp;
import com.sel.framework.Util;
import com.sel.framework.ReportThemeFactory.Theme;
import com.sel.framework.ext.AppiumDriverFactory;
import com.sel.framework.ext.Browser;
import com.sel.framework.ext.BrowserStackDriverFactory;
import com.sel.framework.ext.ExecutionMode;
import com.sel.framework.ext.HPMobileCenterDriverFactory;
import com.sel.framework.ext.MintDriverFactory;
import com.sel.framework.ext.MobileExecutionPlatform;
import com.sel.framework.ext.MobileLabsDriverFactory;
import com.sel.framework.ext.MobileToolName;
import com.sel.framework.ext.PerfectoDriverFactory;
import com.sel.framework.ext.RemoteWebDriverUtils;
import com.sel.framework.ext.SauceLabsDriverFactory;
import com.sel.framework.ext.SelDriver;
import com.sel.framework.ext.SeleniumReport;
import com.sel.framework.ext.SeleniumTestParameters;
import com.sel.framework.ext.WebDriverFactory;
import com.sel.framework.ext.WebDriverUtil;

import io.appium.java_client.AppiumDriver;

/**
 * Driver script class which encapsulates the core logic of the framework
 * 
 * 
 */
public class DriverScript {
	private List<String> businessFlowData;
	private int currentIteration, currentSubIteration;
	private Date startTime, endTime;
	private String executionTime;

	private SelDataTable dataTable;
	private ReportSettings reportSettings;
	private SeleniumReport report;

	private SelDriver driver;
//	private Client client;

	private WebDriverUtil driverUtil;
	private ScriptHelper scriptHelper;

	private Properties properties;
	private Properties mobileProperties;
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	private Boolean linkScreenshotsToTestLog = true;

	private String seeTestResultPath;
	private final SeleniumTestParameters testParameters;
	private String reportPath;

	/**
	 * DriverScript constructor
	 * 
	 * @param testParameters
	 *            A {@link SeleniumTestParameters} object
	 */
	public DriverScript(SeleniumTestParameters testParameters) {
		this.testParameters = testParameters;
	}

	/**
	 * Function to configure the linking of screenshots to the corresponding
	 * test log
	 * 
	 * @param linkScreenshotsToTestLog
	 *            Boolean variable indicating whether screenshots should be
	 *            linked to the corresponding test log
	 */
	public void setLinkScreenshotsToTestLog(Boolean linkScreenshotsToTestLog) {
		this.linkScreenshotsToTestLog = linkScreenshotsToTestLog;
	}

	/**
	 * Function to get the name of the test report
	 * 
	 * @return The test report name
	 */
	public String getReportName() {
		return reportSettings.getReportName();
	}

	/**
	 * Function to get the status of the test case executed
	 * 
	 * @return The test status
	 */
	public String getTestStatus() {
		return report.getTestStatus();
	}

	/**
	 * Function to get the decription of any failure that may occur during the
	 * script execution
	 * 
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription() {
		return report.getFailureDescription();
	}

	/**
	 * Function to get the execution time for the test case
	 * 
	 * @return The test execution time
	 */
	public String getExecutionTime() {
		return executionTime;
	}

	/**
	 * Function to execute the given test case
	 */
	public void driveTestExecution() {
		startUp();
		initializeTestIterations();
		initializeWebDriver();
		initializeTestReport();
		initializeDatatable();
		executeSel();

		quitWebDriver();
		wrapUp();
	}

	private void executeSel() {
		initializeTestScript();
		executeSelTestIterations();
	}

	private void startUp() {
		startTime = Util.getCurrentTime();

		properties = Settings.getInstance();
		mobileProperties = Settings.getMobilePropertiesInstance();

		setDefaultTestParameters();
	}

	private void setDefaultTestParameters() {
		if (testParameters.getIterationMode() == null) {
			testParameters.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
		}

		if (testParameters.getExecutionMode() == null) {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties.getProperty("DefaultExecutionMode")));
		}

		if (testParameters.getMobileExecutionPlatform() == null) {
			testParameters.setMobileExecutionPlatform(
					MobileExecutionPlatform.valueOf(mobileProperties.getProperty("DefaultMobileExecutionPlatform")));
		}

		if (testParameters.getMobileToolName() == null) {
			testParameters
					.setMobileToolName(MobileToolName.valueOf(mobileProperties.getProperty("DefaultMobileToolName")));
		}

		if (testParameters.getDeviceName() == null) {
			testParameters.setDeviceName(mobileProperties.getProperty("DefaultDevice"));
		}

		if (testParameters.getBrowser() == null) {
			testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
		}
		
		if(testParameters.getSeeTestPort() == null){
			testParameters.setSeeTestPort(mobileProperties
					.getProperty("SeeTestDefaultPort"));
		}

		testParameters.setInstallApplication(
				Boolean.parseBoolean(mobileProperties.getProperty("InstallApplicationInDevice")));
		testParameters.setSeeTestPort(mobileProperties.getProperty("SeeTestDefaultPort"));

	}

	private void initializeTestIterations() {
		switch (testParameters.getIterationMode()) {
		case RUN_ALL_ITERATIONS:
			int nIterations = getNumberOfIterations();
			testParameters.setEndIteration(nIterations);

			currentIteration = 1;
			break;

		case RUN_ONE_ITERATION_ONLY:
			currentIteration = 1;
			break;

		case RUN_RANGE_OF_ITERATIONS:
			if (testParameters.getStartIteration() > testParameters.getEndIteration()) {
				throw new FrameworkException("Error", "StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = testParameters.getStartIteration();
			break;

		default:
			throw new FrameworkException("Unhandled Iteration Mode!");
		}
	}

	private int getNumberOfIterations() {
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src"
				+ Util.getFileSeparator() + "test" + Util.getFileSeparator() + "resources" + Util.getFileSeparator()
				+ "Datatables";
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, testParameters.getCurrentScenario());
		testDataAccess.setDatasheetName(properties.getProperty("DefaultDataSheet"));

		int startRowNum = testDataAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
		int nTestcaseRows = testDataAccess.getRowCount(testParameters.getCurrentTestcase(), 0, startRowNum);
		int nSubIterations = testDataAccess.getRowCount("1", 1, startRowNum); // Assumption:
																				// Every
																				// test
																				// case
																				// will
																				// have
																				// at
																				// least
																				// one
																				// iteration
		return nTestcaseRows / nSubIterations;

	}

	@SuppressWarnings("rawtypes")
	private void initializeWebDriver() {
		
		String host = "localhost";
		String port = "4723";
		String completeMobileUrl ;
        if(System.getProperty("HUB_HOST") != null){
        	System.out.println("System HUB_HOST = "+System.getProperty("HUB_HOST"));
        	System.out.println("System HUB_PORT = "+System.getProperty("HUB_PORT"));
            host = System.getProperty("HUB_HOST");
            port = System.getProperty("HUB_PORT");
        }
        String completeUrl = "http://" + host + ":4444/wd/hub";
        if(System.getProperty("HUB_HOST") != null && System.getProperty("HUB_PORT")!= null){
        	completeMobileUrl = "http://" + host+":"+port+"/wd/hub";
        }else{
        	completeMobileUrl = "http://"+ host+":4723/wd/hub";
        }
        
//        System.out.println("completeMobileUrl : "+ completeMobileUrl);

		switch (testParameters.getExecutionMode()) {

		case LOCAL:
			WebDriver webDriver = WebDriverFactory.getWebDriver(testParameters.getBrowser());
			driver = new SelDriver(webDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case REMOTE:
			WebDriver remoteWebDriver = WebDriverFactory.getRemoteWebDriver(testParameters.getBrowser(),completeUrl);
			driver = new SelDriver(remoteWebDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case LOCAL_EMULATED_DEVICE:
			testParameters.setBrowser(Browser.CHROME);// mobile emulation
														// supported only
														// on chrome
			WebDriver localEmulatedDriver = WebDriverFactory.getEmulatedWebDriver(testParameters.getDeviceName());
			driver = new SelDriver(localEmulatedDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case REMOTE_EMULATED_DEVICE:
			testParameters.setBrowser(Browser.CHROME);// mobile emulation
														// supported only
														// on chrome
			WebDriver remoteEmulatedDriver = WebDriverFactory.getEmulatedRemoteWebDriver(testParameters.getDeviceName(),
					completeUrl);
			driver = new SelDriver(remoteEmulatedDriver);
			driver.setTestParameters(testParameters);
			break;

		case GRID:
			WebDriver remoteGridDriver = WebDriverFactory.getRemoteWebDriver(testParameters.getBrowser(),
					testParameters.getBrowserVersion(), testParameters.getPlatform(),
					properties.getProperty("RemoteUrl"));
			driver = new SelDriver(remoteGridDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case MOBILE:
			if ((testParameters.getMobileToolName().equals(MobileToolName.APPIUM))) {
				WebDriver appiumDriver = AppiumDriverFactory.getAppiumDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						testParameters.getMobileOSVersion(), testParameters.shouldInstallApplication(),
						mobileProperties.getProperty("AppiumURL"));
				driver = new SelDriver(appiumDriver);
				driver.setTestParameters(testParameters);
			} else if (testParameters.getMobileToolName().equals(MobileToolName.REMOTE_WEBDRIVER)) {
				WebDriver remoteAppiumDriver = AppiumDriverFactory.getAppiumRemoteWebDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						testParameters.getMobileOSVersion(), testParameters.shouldInstallApplication(),
						completeMobileUrl);
//				mobileProperties.getProperty("AppiumURL")
				driver = new SelDriver(remoteAppiumDriver);
				driver.setTestParameters(testParameters);
			}

			break;

//		case SEETEST:
//			testParameters.setMobileToolName(MobileToolName.DEFAULT);
//			MobileWebDriver seeTestDriver = SeeTestDriverFactory.getSeeTestDriver(
//					mobileProperties.getProperty("SeeTestHost", "localhost"),
//					Integer.parseInt(testParameters.getSeeTestPort()),
//					mobileProperties.getProperty("SeeTestProjectBaseDirectory"),
//					mobileProperties.getProperty("SeeTestReportType", "xml"), "report", "Test Name from Driver Init",
//					testParameters.getMobileExecutionPlatform(),
//					mobileProperties.getProperty("SeeTestAndroidApplicationName"),
//					mobileProperties.getProperty("SeeTestiOSApplicationName"),
//					mobileProperties.getProperty("SeeTestAndroidWebApplicationName"),
//					mobileProperties.getProperty("SeeTestiOSWebApplicationName"), testParameters.getDeviceName());
//			driver = new SelDriver(seeTestDriver);
//			client = seeTestDriver.client;
//			driver.setSeeTestdriver(seeTestDriver);
//			driver.setTestParameters(testParameters);
//			break;

		case PERFECTO:

			if (testParameters.getMobileToolName().equals(MobileToolName.APPIUM)) {
				WebDriver appiumPerfectoDriver = PerfectoDriverFactory.getPerfectoAppiumDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						mobileProperties.getProperty("PerfectoHost"), testParameters);
				driver = new SelDriver(appiumPerfectoDriver);
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(MobileToolName.REMOTE_WEBDRIVER)) {
				WebDriver remotePerfectoDriver = PerfectoDriverFactory.getPerfectoRemoteWebDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						mobileProperties.getProperty("PerfectoHost"), testParameters.getBrowser());
				driver = new SelDriver(remotePerfectoDriver);
				driver.setTestParameters(testParameters);
			}

			break;

		case SAUCELABS:

			if (testParameters.getMobileToolName().equals(MobileToolName.APPIUM)) {
				AppiumDriver appiumSauceDriver = SauceLabsDriverFactory.getSauceAppiumDriver(
						testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
						mobileProperties.getProperty("SauceHost"), testParameters);
				driver = new SelDriver(appiumSauceDriver);
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(MobileToolName.REMOTE_WEBDRIVER)) {
				WebDriver remoteSauceDriver = SauceLabsDriverFactory.getSauceRemoteWebDriver(
						mobileProperties.getProperty("SauceHost"), testParameters.getBrowser(),
						testParameters.getBrowserVersion(), testParameters.getPlatform(), testParameters);

				driver = new SelDriver(remoteSauceDriver);
				driver.setTestParameters(testParameters);
			}
			break;

		case MINT:

			testParameters.setMobileToolName(MobileToolName.APPIUM);
			WebDriver mintAppiumDriver = MintDriverFactory.getmintAppiumDriver(
					testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
					mobileProperties.getProperty("mintHost"), testParameters.getMobileOSVersion());
			driver = new SelDriver(mintAppiumDriver);
			driver.setTestParameters(testParameters);
			break;

		case BROWSERSTACK:
			if (testParameters.getMobileToolName().equals(MobileToolName.REMOTE_WEBDRIVER)) {
				WebDriver browserstackRemoteDrivermobile = BrowserStackDriverFactory
						.getBrowserStackRemoteWebDriverMobile(testParameters.getMobileExecutionPlatform(),
								testParameters.getDeviceName(), mobileProperties.getProperty("BrowserStackHost"),
								testParameters);
				driver = new SelDriver(browserstackRemoteDrivermobile);
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(MobileToolName.DEFAULT)) {
				WebDriver browserstackRemoteDriver = BrowserStackDriverFactory.getBrowserStackRemoteWebDriver(
						mobileProperties.getProperty("BrowserStackHost"), testParameters.getBrowser(),
						testParameters.getBrowserVersion(), testParameters.getPlatform(), testParameters);

				driver = new SelDriver(browserstackRemoteDriver);
				driver.setTestParameters(testParameters);
			}

			break;

		case MOBILECENTRE:

			testParameters.getMobileToolName().equals(MobileToolName.APPIUM);
			WebDriver mobileCentreDriver = HPMobileCenterDriverFactory.getMobileCenterAppiumDriver(
					testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
					mobileProperties.getProperty("MobileCenterHost"));
			driver = new SelDriver(mobileCentreDriver);
			driver.setTestParameters(testParameters);

			break;

		case MOBILELABS:

			testParameters.getMobileToolName().equals(MobileToolName.APPIUM);
			WebDriver mobilelabsDriver = MobileLabsDriverFactory.getMobileLabsDriver(
					testParameters.getMobileExecutionPlatform(), testParameters.getDeviceName(),
					mobileProperties.getProperty("AppiumURL"), testParameters.getMobileOSVersion());
			driver = new SelDriver(mobilelabsDriver);
			driver.setTestParameters(testParameters);

			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}
		implicitWaitForDriver();

	}

	private void implicitWaitForDriver() {
		long objectSyncTimeout = Long.parseLong(properties.get("ObjectSyncTimeout").toString());
		driver.manage().timeouts().implicitlyWait(objectSyncTimeout, TimeUnit.SECONDS);
	}

	private void WaitPageLoad() {
		long pageLoadTimeout = Long.parseLong(properties.get("PageLoadTimeout").toString());
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	private void initializeTestReport() {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory
				.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));

		report = new SeleniumReport(reportSettings, reportTheme, testParameters);

		report.initialize();
		report.setDriver(driver);
//		if(testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)){
//			report.setClient(client);
//		}
		report.initializeTestLog();
		createTestLogHeader();
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(reportPath, testParameters.getCurrentScenario() + "_"
				+ testParameters.getCurrentTestcase() + "_" + testParameters.getCurrentTestInstance());

		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setLogLevel(Integer.parseInt(properties.getProperty("LogLevel")));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties.getProperty("HtmlReport")));
		reportSettings.setGenerateSeeTestReports(
				Boolean.parseBoolean(mobileProperties.getProperty("SeeTestReportGeneration")));
		reportSettings.setGeneratePerfectoReports(
				Boolean.parseBoolean(mobileProperties.getProperty("PerfectoReportGeneration")));
		reportSettings
				.setTakeScreenshotFailedStep(Boolean.parseBoolean(properties.getProperty("TakeScreenshotFailedStep")));
		reportSettings
				.setTakeScreenshotPassedStep(Boolean.parseBoolean(properties.getProperty("TakeScreenshotPassedStep")));
		reportSettings.setConsolidateScreenshotsInWordDoc(
				Boolean.parseBoolean(properties.getProperty("ConsolidateScreenshotsInWordDoc")));
		reportSettings.setisMobileExecution(isMobileAutomation());

		reportSettings.setLinkScreenshotsToTestLog(this.linkScreenshotsToTestLog);

	}

	private void createTestLogHeader() {
		report.addTestLogHeading(reportSettings.getProjectName() + " - " + reportSettings.getReportName()
				+ " Automation Execution Results");
		report.addTestLogSubHeading("Date & Time",
				": " + Util.getFormattedTime(startTime, properties.getProperty("DateFormatString")), "Iteration Mode",
				": " + testParameters.getIterationMode());
		report.addTestLogSubHeading("Start Iteration", ": " + testParameters.getStartIteration(), "End Iteration",
				": " + testParameters.getEndIteration());

		switch (testParameters.getExecutionMode()) {
		case LOCAL:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Execution on", ": " + "Local Machine");
			break;
		case LOCAL_EMULATED_DEVICE:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Execution on", ": " + "Emulated Mobile Device on Local Machine");
			report.addTestLogSubHeading("Emulated Device Name", ":" + testParameters.getDeviceName(), "", "");
			break;

		case REMOTE:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Execution on", ": " + "Remote Machine @" + properties.getProperty("RemoteUrl"));
			break;

		case REMOTE_EMULATED_DEVICE:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Execution on",
					": " + "Emulated Mobile Device on Remote Machine @" + properties.getProperty("RemoteUrl"));
			report.addTestLogSubHeading("Emulated Device Name", ":" + testParameters.getDeviceName(), "", "");
			break;

		case GRID:
			report.addTestLogSubHeading("Browser/Platform", ": " + testParameters.getBrowserAndPlatform(),
					"Execution on", ": " + "Grid @" + properties.getProperty("RemoteUrl"));
			break;

		case MOBILE:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());
			break;

		case PERFECTO:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());
			report.addTestLogSubHeading("Executed on",
					": " + "Perfecto MobileCloud @ " + mobileProperties.getProperty("PerfectoHost"), "Perfecto User",
					": " + mobileProperties.getProperty("PerfectoUser"));
			break;

		case SEETEST:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());
			break;

		case MINT:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device OS Version",
					": " + testParameters.getMobileOSVersion());
			report.addTestLogSubHeading("Executed on",
					": " + "mint cloud @ " + mobileProperties.getProperty("mintHost"), "mint User",
					": " + mobileProperties.getProperty("mintUsername"));
			break;

		case SAUCELABS:

			if (testParameters.getMobileToolName().toString().equalsIgnoreCase("REMOTE_WEBDRIVER")) {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Browser",
						": " + testParameters.getBrowser());
			} else {
				report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
						"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
						": " + testParameters.getDeviceName());
			}

			break;

		case BROWSERSTACK:
			if (testParameters.getMobileToolName().toString().equalsIgnoreCase("REMOTE_WEBDRIVER")) {
				report.addTestLogSubHeading("ExecutionPatform", ": " + testParameters.getExecutionMode(),
						"Execution on", ": " + testParameters.getMobileExecutionPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
						": " + testParameters.getDeviceName());
			} else {
				report.addTestLogSubHeading("ExecutionPlatform", ": " + testParameters.getExecutionMode(),
						"Execution on", ": " + testParameters.getPlatform());
				report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Browser",
						": " + testParameters.getBrowser());
			}

			break;

		case MOBILECENTRE:
			report.addTestLogSubHeading("ExecutionPlatform", ": " + testParameters.getExecutionMode(), "Execution on",
					": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());

			break;

		case MOBILELABS:
			report.addTestLogSubHeading("Execution Mode", ": " + testParameters.getExecutionMode(),
					"Execution Platform", ": " + testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used", ": " + testParameters.getMobileToolName(), "Device Name/ID",
					": " + testParameters.getDeviceName());
			report.addTestLogSubHeading("Executed on",
					": " + "MobileLabs Cloud @ " + mobileProperties.getProperty("HostIP"), "MobileLabs User",
					": " + mobileProperties.getProperty("UserName"));
			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}

		report.addTestLogTableHeadings();
	}

	private synchronized void initializeDatatable() {
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src"
				+ Util.getFileSeparator() + "test" + Util.getFileSeparator() + "resources" + Util.getFileSeparator()
				+ "Datatables";

		String runTimeDatatablePath;
		Boolean includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));
		if (includeTestDataInReport) {
			runTimeDatatablePath = reportPath + Util.getFileSeparator() + "Datatables";

			File runTimeDatatable = new File(
					runTimeDatatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");
			if (!runTimeDatatable.exists()) {
				File datatable = new File(
						datatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");

				try {
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the datatable failed...");
				}
			}

			File runTimeCommonDatatable = new File(
					runTimeDatatablePath + Util.getFileSeparator() + "Common Testdata.xls");
			if (!runTimeCommonDatatable.exists()) {
				File commonDatatable = new File(datatablePath + Util.getFileSeparator() + "Common Testdata.xls");

				try {
					FileUtils.copyFile(commonDatatable, runTimeCommonDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the common datatable failed...");
				}
			}
		} else {
			runTimeDatatablePath = datatablePath;
		}

		dataTable = new SelDataTable(runTimeDatatablePath, testParameters.getCurrentScenario());
		dataTable.setDataReferenceIdentifier(properties.getProperty("DataReferenceIdentifier"));

	}

	private void initializeTestScript() {
		driverUtil = new WebDriverUtil(driver);
		scriptHelper = new ScriptHelper(dataTable, report, driver, driverUtil, testParameters);
		driver.setRport(report);
		initializeBusinessFlow();
	}

	private void initializeBusinessFlow() {
		ExcelDataAccess businessFlowAccess = new ExcelDataAccess(
				frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src" + Util.getFileSeparator()
						+ "test" + Util.getFileSeparator() + "resources" + Util.getFileSeparator() + "Datatables",
				testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("Business_Flow");

		int rowNum = businessFlowAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
		if (rowNum == -1) {
			throw new FrameworkException("The test case \"" + testParameters.getCurrentTestcase()
					+ "\" is not found in the Business Flow sheet!");
		}

		String dataValue;
		businessFlowData = new ArrayList<String>();
		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if ("".equals(dataValue)) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}

		if (businessFlowData.isEmpty()) {
			throw new FrameworkException(
					"No business flow found against the test case \"" + testParameters.getCurrentTestcase() + "\"");
		}
	}

	private void executeSelTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: " + Integer.toString(currentIteration));

			// Evaluate each test iteration for any errors
			try {
				executeTestcase(businessFlowData);
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (InvocationTargetException ix) {
				exceptionHandler((Exception) ix.getCause(), "Error");
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
		}
	}

	private void executeTestcase(List<String> businessFlowData)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		Map<String, Integer> keywordDirectory = new HashMap<String, Integer>();

		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum).split(",");
			String currentKeyword = currentFlowData[0];

			int nKeywordIterations;
			if (currentFlowData.length > 1) {
				nKeywordIterations = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIterations = 1;
			}

			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIterations; currentKeywordIteration++) {
				if (keywordDirectory.containsKey(currentKeyword) && nKeywordIterations != 1) {
					keywordDirectory.put(currentKeyword, keywordDirectory.get(currentKeyword) + 1);
				} else {
					keywordDirectory.put(currentKeyword, 1);
				}
				currentSubIteration = keywordDirectory.get(currentKeyword);

				dataTable.setCurrentRow(testParameters.getCurrentTestcase(), currentIteration, currentSubIteration);

				if (currentSubIteration > 1) {
					report.addTestLogSubSection(currentKeyword + " (Sub-Iteration: " + currentSubIteration + ")");
				} else {
					report.addTestLogSubSection(currentKeyword);
				}

				invokeBusinessComponent(currentKeyword);
			}
		}
	}

	private void invokeBusinessComponent(String currentKeyword)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		File[] packageDirectories = {
				new File(frameworkParameters.getRelativePath() + Util.getFileSeparator() + "target"
						+ Util.getFileSeparator() + "test-classes" + Util.getFileSeparator() + "businessComponents"),
				new File(frameworkParameters.getRelativePath() + Util.getFileSeparator() + "target"
						+ Util.getFileSeparator() + "test-classes" + Util.getFileSeparator() + "componentGroups") };

		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();

			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();

				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length() - CLASS_FILE_EXTENSION.length());

					Class<?> reusableComponents = Class.forName(packageName + "." + className);
					Method executeComponent;

					try {
						// Convert the first letter of the method to lowercase
						// (in line with java naming conventions)
						currentKeyword = currentKeyword.substring(0, 1).toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the
						// next class
						continue;
					}

					isMethodFound = true;

					Constructor<?> ctor = reusableComponents.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);

					executeComponent.invoke(businessComponent, (Object[]) null);

					break;
				}
			}
		}

		if (!isMethodFound) {
			throw new FrameworkException("Keyword " + currentKeyword + " not found within any class "
					+ "inside the businesscomponents package");
		}
	}

	private void exceptionHandler(Exception ex, String exceptionName) {
		// Error reporting
		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null) {
			exceptionDescription = ex.toString();
		}

		if (ex.getCause() != null) {
			report.updateTestLog(exceptionName, exceptionDescription + " <b>Caused by: </b>" + ex.getCause(),
					Status.FAIL);
		} else {
			report.updateTestLog(exceptionName, exceptionDescription, Status.FAIL);
		}

		// Print stack trace for detailed debug information
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		String stackTrace = stringWriter.toString();
		report.updateTestLog("Exception stack trace", stackTrace, Status.DEBUG);

		// Error response
		if (frameworkParameters.getStopExecution()) {
			report.updateTestLog("Sel Info", "Test execution terminated by user! All subsequent tests aborted...",
					Status.DONE);
			currentIteration = testParameters.getEndIteration();
		} else {
			OnError onError = OnError.valueOf(properties.getProperty("OnError"));
			switch (onError) {
			// Stop option is not relevant when run from QC
			case NEXT_ITERATION:
				report.updateTestLog("Sel Info",
						"Test case iteration terminated by user! Proceeding to next iteration (if applicable)...",
						Status.DONE);
				break;

			case NEXT_TESTCASE:
				report.updateTestLog("Sel Info",
						"Test case terminated by user! Proceeding to next test case (if applicable)...", Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;

			case STOP:
				frameworkParameters.setStopExecution(true);
				report.updateTestLog("Sel Info", "Test execution terminated by user! All subsequent tests aborted...",
						Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;

			default:
				throw new FrameworkException("Unhandled OnError option!");
			}
		}
	}

	private void quitWebDriver() {
		switch (testParameters.getExecutionMode()) {
		case LOCAL:
		case REMOTE:
		case LOCAL_EMULATED_DEVICE:
		case REMOTE_EMULATED_DEVICE:
		case GRID:
		case MOBILE:
		case MINT:
		case SAUCELABS:
		case BROWSERSTACK:
		case MOBILECENTRE:
		case MOBILELABS:
			driver.quit();
			break;
//		case SEETEST:
//			// client.applicationClose(properties.getProperty("SeeTestAndroidApplicationName"));
//			client.releaseDevice("*", true, false, true);
//			seeTestResultPath = client.generateReport(true);
//			downloadAddtionalReport();
//			client.releaseClient();
//			driver.quit();
//			break;

		case PERFECTO:
			downloadAddtionalReport();
			driver.quit();
			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}

	}

	private void wrapUp() {
		endTime = Util.getCurrentTime();
		closeTestReport();

	}

	private void closeTestReport() {
		executionTime = Util.getTimeDifference(startTime, endTime);
		report.addTestLogFooter(executionTime);

		if (reportSettings.shouldConsolidateScreenshotsInWordDoc()) {
			report.consolidateScreenshotsInWordDoc();
		}
	}

	private void downloadAddtionalReport() {
		if (testParameters.getExecutionMode().equals(ExecutionMode.PERFECTO)
				&& reportSettings.shouldGeneratePerfectoReports()) {
			try {
				driver.close();
				RemoteWebDriverUtils.downloadReport((RemoteWebDriver) driver.getWebDriver(), "pdf",
						reportPath + Util.getFileSeparator() + "Perfecto Results" + Util.getFileSeparator()
								+ testParameters.getCurrentScenario() + "_" + testParameters.getCurrentTestcase() + "_"
								+ testParameters.getCurrentTestInstance() + ".pdf");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		else if (testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)
				&& reportSettings.shouldGenerateSeeTestReports()) {
			new File(reportPath + Util.getFileSeparator() + "SeeTest Results" + Util.getFileSeparator()
					+ testParameters.getCurrentTestcase() + "_" + testParameters.getCurrentTestInstance()).mkdir();
			File source = new File(seeTestResultPath);
			File dest = new File(reportPath + Util.getFileSeparator() + "SeeTest Results" + Util.getFileSeparator()
					+ testParameters.getCurrentTestcase() + "_" + testParameters.getCurrentTestInstance());

			try {
				FileUtils.copyDirectoryToDirectory(source, dest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isMobileAutomation() {
		boolean isMobileAutomation = false;
		if (testParameters.getExecutionMode().equals(ExecutionMode.MOBILE)
				|| testParameters.getExecutionMode().equals(ExecutionMode.PERFECTO)
				|| testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)
				|| testParameters.getExecutionMode().equals(ExecutionMode.SAUCELABS)
				|| testParameters.getExecutionMode().equals(ExecutionMode.MINT)
				|| testParameters.getExecutionMode().equals(ExecutionMode.BROWSERSTACK)
				|| testParameters.getExecutionMode().equals(ExecutionMode.MOBILECENTRE)
				|| testParameters.getExecutionMode().equals(ExecutionMode.MOBILELABS)) {
			isMobileAutomation = true;
		}
		return isMobileAutomation;

	}
}