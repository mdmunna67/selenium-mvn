package com.sel.framework;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.sel.framework.ext.SeleniumTestParameters;
import com.sel.others.RESTclient;

/**
 * Class to encapsulate all the reporting features of the framework
 * 
 * 
 */
public class Report {
	private static final String EXCEL_RESULTS = "Excel Results";
	private static final String HTML_RESULTS = "HTML Results";
	private static final String SCREENSHOTS = "Screenshots";
	private static final String PERFECTO_RESULTS = "Perfecto Results";
	private static final String SEETEST_RESULTS = "SeeTest Results";

	private ReportSettings reportSettings;
	private ReportTheme reportTheme;

	private int stepNumber;
	private int nStepsPassed, nStepsFailed;
	private int nTestsPassed, nTestsFailed;

	private List<ReportType> reportTypes = new ArrayList<ReportType>();

	private String testStatus;
	private String failureDescription;
	
	private static final String DATABASE_NAME="sbi";
    private static final String DEFAULT_COLLECTION_NAME="datas";
    private static MongoClient serverConnection = null;
    private static final String DATABASE_SERVER="localhost";
    private static final int DATABASE_PORT=27017;
    private static MongoClientURI uri = new MongoClientURI("mongodb://qaautodbadmin:qaautodbadmin@localhost:27017/?authSource=sbi");

	/**
	 * Constructor to initialize the Report
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 */
	private final SeleniumTestParameters testParameters;

	public Report(ReportSettings reportSettings, ReportTheme reportTheme, SeleniumTestParameters testParameters) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		this.testParameters = testParameters;

		nStepsPassed = 0;
		nStepsFailed = 0;
		testStatus = "Passed";
	}

	/**
	 * Function to get the current status of the test being executed
	 * 
	 * @return the current status of the test being executed
	 */
	public String getTestStatus() {
		return testStatus;
	}

	/**
	 * Function to get the description of any failure that may occur during the
	 * script execution
	 * 
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription() {
		return failureDescription;
	}

	public void initialize() {

		if (reportSettings.shouldGenerateExcelReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator() + EXCEL_RESULTS).mkdir();

			ExcelReport excelReport = new ExcelReport(reportSettings, reportTheme);
			reportTypes.add(excelReport);
		}

		if (reportSettings.shouldGenerateHtmlReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator() + HTML_RESULTS).mkdir();

			HtmlReport htmlReport = new HtmlReport(reportSettings, reportTheme);
			reportTypes.add(htmlReport);
		}

		if (reportSettings.shouldGeneratePerfectoReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator() + PERFECTO_RESULTS).mkdir();
		}

		if (reportSettings.shouldGenerateSeeTestReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator()
					+ SEETEST_RESULTS).mkdir();
		}
		
		new File(reportSettings.getReportPath() + Util.getFileSeparator() + SCREENSHOTS).mkdir();
	}

	/**
	 * Function to create a sub-folder within the Results folder
	 * 
	 * @param subFolderName
	 *            The name of the sub-folder to be created
	 * @return The {@link File} object representing the newly created sub-folder
	 */
	public File createResultsSubFolder(String subFolderName) {
		File resultsSubFolder = new File(reportSettings.getReportPath() + Util.getFileSeparator() + subFolderName);
		resultsSubFolder.mkdir();
		return resultsSubFolder;
	}

	/* TEST LOG FUNCTIONS */

	/**
	 * Function to initialize the test log
	 */
	public void initializeTestLog() {
		if ("".equals(reportSettings.getReportName())) {
			throw new FrameworkException("The report name cannot be empty!");
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeTestLog();
		}
	}

	/**
	 * Function to add a heading to the test log
	 * 
	 * @param heading
	 *            The heading to be added
	 */
	public void addTestLogHeading(String heading) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogHeading(heading);
		}
	}

	/**
	 * Function to add sub-headings to the test log (4 sub-headings present per
	 * test log row)
	 * 
	 * @param subHeading1
	 *            The first sub-heading to be added
	 * @param subHeading2
	 *            The second sub-heading to be added
	 * @param subHeading3
	 *            The third sub-heading to be added
	 * @param subHeading4
	 *            The fourth sub-heading to be added
	 */
	public void addTestLogSubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubHeading(subHeading1, subHeading2, subHeading3, subHeading4);
		}
	}

	/**
	 * Function to add the overall table headings to the test log (should be
	 * called first before adding the actual content into the test log; headings
	 * and sub-heading should be added before this)
	 */
	public void addTestLogTableHeadings() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogTableHeadings();
		}
	}

	/**
	 * Function to add a section to the test log
	 * 
	 * @param section
	 *            The section to be added
	 */
	public void addTestLogSection(String section) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSection(section);
		}

		stepNumber = 1;
	}

	/**
	 * Function to add a sub-section to the test log (should be called only
	 * within a previously created section)
	 * 
	 * @param subSection
	 *            The sub-section to be added
	 */
	public void addTestLogSubSection(String subSection) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubSection(subSection);
		}
	}

	/**
	 * Function to update the test log with the details of a particular test
	 * step
	 * 
	 * @param stepName
	 *            The test step name
	 * @param stepDescription
	 *            The description of what the test step does
	 * @param stepStatus
	 *            The status of the test step
	 */
	public void updateTestLog(String stepName, String stepDescription, Status stepStatus) {
		handleStepInvolvingPassOrFail(stepDescription, stepStatus);

		if (stepStatus.ordinal() <= reportSettings.getLogLevel()) {
			String screenshotName = handleStepInvolvingScreenshot(stepName, stepStatus);

			for (int i = 0; i < reportTypes.size(); i++) {
				reportTypes.get(i).updateTestLog(Integer.toString(stepNumber), stepName, stepDescription, stepStatus,
						screenshotName);
				/***** To Inetgrate with JIRA *****/
				Properties properties = Settings.getInstance();
				if (Boolean.parseBoolean(properties.getProperty("UpdateInJira"))) {
					if (stepStatus == Status.FAIL)
						RESTclient.defectLog(
								testParameters.getCurrentTestcase(),
								stepDescription, reportSettings.getReportPath()
										+ Util.getFileSeparator() + SCREENSHOTS
										+ Util.getFileSeparator()
										+ screenshotName);

				}
			}

			stepNumber++;
		}
	}

	private void handleStepInvolvingPassOrFail(String stepDescription, Status stepStatus) {
		if (stepStatus.equals(Status.FAIL)) {
			testStatus = "Failed";

			if (failureDescription == null) {
				failureDescription = stepDescription;
			} else {
				failureDescription = failureDescription + "; " + stepDescription;
			}

			nStepsFailed++;
		} else if (stepStatus.equals(Status.PASS)) {
			nStepsPassed++;
		}
	}

	private String handleStepInvolvingScreenshot(String stepName, Status stepStatus) {
		String screenshotName = reportSettings.getReportName() + "_"
				+ Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-")
				+ ".png";
		
//		String screenshotName = reportSettings.getReportName() + "_"
//				+ Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-")
//				+ "_" + stepName.replaceAll("[^a-zA-Z0-9]+", "") + ".png";

		if ((stepStatus.equals(Status.FAIL) && reportSettings.shouldTakeScreenshotFailedStep())
				|| (stepStatus.equals(Status.PASS) && reportSettings.shouldTakeScreenshotPassedStep())
				|| stepStatus.equals(Status.SCREENSHOT)) {

			String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator() + SCREENSHOTS
					+ Util.getFileSeparator() + screenshotName;
			if (screenshotPath.length() > 256) { // Max char limit for Windows
													// filenames
				screenshotPath = screenshotPath.substring(0, 256);
			}

			takeScreenshot(screenshotPath);
		}

		return screenshotName;
	}

	/**
	 * Function to take a screenshot
	 * 
	 * @param screenshotPath
	 *            The path where the screenshot should be saved
	 */
	protected void takeScreenshot(String screenshotPath) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle rectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);
		Robot robot;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating Robot object (for taking screenshot)");
		}

		BufferedImage screenshotImage = robot.createScreenCapture(rectangle);
		File screenshotFile = new File(screenshotPath);

		try {
			ImageIO.write(screenshotImage, "jpg", screenshotFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to .jpg file");
		}
	}

	/**
	 * Function to add a footer to the test log (The footer format is
	 * pre-defined - it contains the execution time and the number of
	 * passed/failed steps)
	 * 
	 * @param executionTime
	 *            The time taken to execute the test case
	 */
	public void addTestLogFooter(String executionTime) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogFooter(executionTime, nStepsPassed, nStepsFailed);
		}
	}

	/**
	 * Function to consolidate all screenshots into a Word document
	 */
	public void consolidateScreenshotsInWordDoc() {
		String screenshotsConsolidatedFolderPath = reportSettings.getReportPath() + Util.getFileSeparator()
				+ "Screenshots (Consolidated)";
		new File(screenshotsConsolidatedFolderPath).mkdir();

		WordDocumentManager documentManager = new WordDocumentManager(screenshotsConsolidatedFolderPath,
				reportSettings.getReportName());

		String screenshotsFolderPath = reportSettings.getReportPath() + Util.getFileSeparator() + SCREENSHOTS;
		File screenshotsFolder = new File(screenshotsFolderPath);

		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.contains(reportSettings.getReportName());
			}
		};

		File[] screenshots = screenshotsFolder.listFiles(filenameFilter);
		if (screenshots != null && screenshots.length > 0) {
			documentManager.createDocument();

			for (File screenshot : screenshots) {
				documentManager.addPicture(screenshot);
			}
		}
	}


	/* RESULT SUMMARY FUNCTIONS */

	/**
	 * Function to initialize the result summary
	 */
	public void initializeResultSummary() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeResultSummary();
		}
	}

	/**
	 * Function to add a heading to the result summary
	 * 
	 * @param heading
	 *            The heading to be added
	 */
	public void addResultSummaryHeading(String heading) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryHeading(heading);
		}
	}

	/**
	 * Function to add sub-headings to the result summary (4 sub-headings
	 * present per result summary row)
	 * 
	 * @param subHeading1
	 *            The first sub-heading to be added
	 * @param subHeading2
	 *            The second sub-heading to be added
	 * @param subHeading3
	 *            The third sub-heading to be added
	 * @param subHeading4
	 *            The fourth sub-heading to be added
	 */
	public void addResultSummarySubHeading(String subHeading1, String subHeading2, String subHeading3,
			String subHeading4) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummarySubHeading(subHeading1, subHeading2, subHeading3, subHeading4);
		}
	}

	/**
	 * Function to add the overall table headings to the result summary
	 */
	public void addResultSummaryTableHeadings() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryTableHeadings();
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link TestParameters} object containing the details of
	 *            the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public synchronized void updateResultSummary(TestParameters testParameters, String testReportName,
			String executionTime, String testStatus) {
		if ("failed".equalsIgnoreCase(testStatus)) {
			nTestsFailed++;
		} else if ("passed".equalsIgnoreCase(testStatus)) {
			nTestsPassed++;
		} else if ("aborted".equalsIgnoreCase(testStatus)) {
			reportSettings.setLinkTestLogsToSummary(false);
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).updateResultSummary(testParameters, testReportName, executionTime, testStatus);
		}
//		DBInsertExecutionDetails(testParameters,testReportName,executionTime,testStatus);
	}

	/**
	 * Function to add a footer to the result summary (The footer format is
	 * pre-defined - it contains the total execution time and the number of
	 * passed/failed tests)
	 * 
	 * @param totalExecutionTime
	 *            The total time taken to execute all the test cases
	 */
	public void addResultSummaryFooter(String totalExecutionTime) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryFooter(totalExecutionTime, nTestsPassed, nTestsFailed);
		}
		
	}
	
	 public static DBCollection getCollection(String collectionName) {
	      
	      if(serverConnection==null){
	        serverConnection = new MongoClient(uri);
	      }   
	      DB db = serverConnection.getDB(DATABASE_NAME);
	      return db.getCollection(collectionName==null?DEFAULT_COLLECTION_NAME:collectionName);
	    }
	
	public static void DBInsertExecutionDetails(TestParameters testParameters,
			String testReportName, String executionTime, String testStatus)   {
	      int oRuns = 1;
	      int nTestsFailed=0;
	      int nTestsPassed=0;
	      try {
	        Calendar calendar = Calendar.getInstance();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        String date = formatter.format(calendar.getTime());
	        
	        String scenarioName = testParameters.getCurrentScenario();
			String testcaseName = testParameters.getCurrentTestcase();
			String testInstanceName = testParameters.getCurrentTestInstance();
			String testcaseDescription = testParameters.getCurrentTestDescription();
//			String nTestsPassed = testParameters.getAdditionalDetails();

//	        JSONObject testData = (JSONObject) Control.ReportManager.pHandler.getData();
//	        String projectName = RunManager.getGlobalSettings().getProjectName();
	        
	        DBCollection Collection = getCollection(DEFAULT_COLLECTION_NAME);
	        ObjectId id = new ObjectId();
	        System.out.println(id);
	        System.out.println("Collection : "+DEFAULT_COLLECTION_NAME);
	         BasicDBObject searchObject = new BasicDBObject();
	         searchObject.put("_id", id);
	         searchObject.put("date", date);
	         searchObject.put("scenarioName", scenarioName);
	         searchObject.put("testcaseName", testcaseName);
	         searchObject.put("testInstanceName", testInstanceName);
	         searchObject.put("testcaseDescription", testcaseDescription);
	         searchObject.put("testStatus", testStatus);
	         if(testStatus.equals("Failed")){
	        	 nTestsFailed=1;
	         }else{
	        	 nTestsPassed=1;
	         }
	         searchObject.put("nTestsPassed", nTestsPassed);
	         searchObject.put("nTestsFailed", nTestsFailed);
	         searchObject.put("executionTime", executionTime);
	         BasicDBObject andQuery = new BasicDBObject();
	         List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
	         obj.add(new BasicDBObject("testcaseDescription", testcaseDescription));
	         obj.add(new BasicDBObject("date", date));
	         andQuery.put("$and", obj);
//	         System.out.println(andQuery.toString());
	         DBCursor cursor = Collection.find(andQuery);
	         while (cursor.hasNext()) {
	             System.out.println(cursor.next());
	             oRuns = oRuns + 1;
	         }
	         BasicDBObject updateFields = new BasicDBObject();
	         updateFields.put("$inc", new BasicDBObject().append("Runs", oRuns));  
	         Collection.update(searchObject, updateFields,true,false);
	      
	         System.out.println("Automation Execution Results Summary document inserted successfully in mongodb database.");
//	       DB detailed execution Collection
//	         DBdetailedexecution(id);
	      }catch (Exception e) {
	        System.out.println("unable to insert Automation Execution Results Summary document in mongodb database."); 
	      }
	    }
}
