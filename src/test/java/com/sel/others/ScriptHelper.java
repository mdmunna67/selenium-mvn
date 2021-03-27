package com.sel.others;

import org.openqa.selenium.WebDriver;

import com.sel.framework.SelDataTable;
import com.sel.framework.ext.SelDriver;
import com.sel.framework.ext.SeleniumReport;
import com.sel.framework.ext.SeleniumTestParameters;
import com.sel.framework.ext.WebDriverUtil;

/**
 * Wrapper class for common framework objects, to be used across the entire test
 * case and dependent libraries
 * 
 * 
 */
public class ScriptHelper {

	private final SelDataTable dataTable;
	private final SeleniumReport report;
	private SelDriver selDriver;
	private WebDriverUtil driverUtil;
	private SeleniumTestParameters testParameters;

	/**
	 * Constructor to initialize all the objects wrapped by the
	 * {@link ScriptHelper} class
	 * 
	 * @param dataTable
	 *            The {@link SelDataTable} object
	 * @param report
	 *            The {@link SeleniumReport} object
	 * @param driver
	 *            The {@link WebDriver} object
	 * @param driverUtil
	 *            The {@link WebDriverUtil} object
	 * @param testParameters
	 *            The {@link SeleniumTestParameters} object
	 */

	public ScriptHelper(SelDataTable dataTable, SeleniumReport report, SelDriver selDriver,
			WebDriverUtil driverUtil, SeleniumTestParameters testParameters) {
		this.dataTable = dataTable;
		this.report = report;
		this.selDriver = selDriver;
		this.driverUtil = driverUtil;
		this.testParameters = testParameters;
	}

	/**
	 * Function to get the {@link SelDataTable} object
	 * 
	 * @return The {@link SelDataTable} object
	 */
	public SelDataTable getDataTable() {
		return dataTable;
	}

	/**
	 * Function to get the {@link SeleniumReport} object
	 * 
	 * @return The {@link SeleniumReport} object
	 */
	public SeleniumReport getReport() {
		return report;
	}

	/**
	 * Function to get the {@link SelDriver} object
	 * 
	 * @return The {@link SelDriver} object
	 */
	public SelDriver getselDriver() {
		return selDriver;
	}

	/**
	 * Function to get the {@link WebDriverUtil} object
	 * 
	 * @return The {@link WebDriverUtil} object
	 */
	public WebDriverUtil getDriverUtil() {
		return driverUtil;
	}

	/**
	 * Function to get the {@link SeleniumTestParameters} object
	 * 
	 * @return The {@link SeleniumTestParameters} object
	 */
	public SeleniumTestParameters getTestParameters() {
		return testParameters;
	}

}