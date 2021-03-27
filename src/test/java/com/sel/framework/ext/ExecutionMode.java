package com.sel.framework.ext;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Enumeration to represent the mode of execution
 * 
 * 
 */
public enum ExecutionMode {
	/**
	 * Execute on the local machine
	 */
	LOCAL,

	/**
	 * Execute on a Perfecto MobileCloud device using {@link RemoteWebDriver}
	 */
	
	REMOTE,
	/**
	 * Execute on a remote machine
	 */
	
	LOCAL_EMULATED_DEVICE,
	/**
	 * Execute on a emulated device using local machine
	 */

	REMOTE_EMULATED_DEVICE,
	/**
	 * Execute on a emulated device using remote machine
	 */
	
	GRID,
	/**
	 * Execute on a selenium grid
	 */
	
	PERFECTO,

	/**
	 * Execute on a mobile device using Appium
	 */
	MOBILE, 

	/**
	 * Execute on a mobile device using Appium
	 */
	SEETEST,
	/**
	 * Execute on a mobile device using SeeTest
	 */
	MINT,

	/**
	 * Execute on a mobile device using mint
	 */
	
	SAUCELABS,

	/**
	 * Execute on a mobile device using SauceLabs
	 */
	
	BROWSERSTACK,
	
	/**
	 * Execute on a mobile device using MobileCentre
	 */
	
	MOBILECENTRE,
	
	/**
	 * Execute on a mobile device using MobileCentre
	 */	
	
	
	MOBILELABS,
	/**
	 * Execute on a mobile device using MobileCentre
	 */
	
	
	
	
}