- 👋 Hi, I’m @saliha-sbisec
- 👀 I’m interested in ...
- 🌱 I’m currently learning ...
- 💞️ I’m looking to collaborate on ...
- 📫 How to reach me ...


# Automated Tests - Getting Started

### Pre-Requisites

	- Oracle JDK 11.0.5 is installed
	- Maven 3.6.3 is installed
	- Google Chrome 79.0.3945.36 or above is installed
	- Lambok Plugin Installed

## Overview

This is the automation test suite for project Scorpio-Wallet. The goal of automation on this project is to make the QA teams job easier, and conduct testing faster.

This suite focuses on front end testing, using Selenium WebDriver to interact with browser elements directly imitating a user.

For more detailed information on the test framework visit the [QA automation knowledge base](https://sbicryptosec.atlassian.net/wiki/spaces/SAFEWALLET/pages/389513334/Automation+Knowledge+Sharing+WIP).


## Test Tracking

During development it is important to track automation coverage and what manual tests you have automated. There are three documents used for tracking.

1. Test Tracker: This tracks every script written, and how long they take to run
2. IWC and Web Admin Happy Path Tracker
3. IWC and Web Admin Full Test Suite Tracker

2 & 3: Exported from test rail, these are all the manual tests QA runs. The main purpose of these are to determine
which (if any) script automates a test, what is left to automate, what will not be automated, what is blocked etc.

Please ask the QA lead for these documents.

---
## Chrome Driver Setup
This project uses WebDriver Manager, an extension that automatically retrieves the latest Chrome driver before tests are executed.

Currently, the latest Chrome driver has several bugs. So instead, we explicitly instruct the manager to retrieve version `79.0.3945.36`.

This version is fine to use with the latest version of Chrome, version `80.0.3987.149`.

Drivers beyond version `79.0.3945.36` have not been tested. If you want to update the Chrome driver version:

 1. Open `DriverConfig.java`

 2. In the `configureSeleniumExtension` method remove or edit this line

 ```java
 seleniumExtension.getConfig().chromedriver().version("79.0.3945.36");
 ```

  - Remove the line to always dynamically retrieve the latest driver

  - Edit the line with the latest driver to always retrieve the specified driver

---

## Test Environment Setup
Before tests can be run on an environment certain pre-conditions have to be met. For example, wallets with money already in them have to be
established. Then the configuration of these dependencies have to be specified in the properties files.

There are currently three environments `(qa, qa2, and selenium)` , with most automated testing happening in selenium.

For any test to run you need to have the selenium.credentials file added to `test-automation/src/main/resources` (Ask QA Lead for the files).

When starting testing on a new, un-configured environment run `test.runner.TestEnvironmentSetup.java` to setup the environment and wait till all transactions are complete before
testing.

You can run this manually, or set by setting a system environment via the following steps:

Via CLI:

1. `export INITIALIZE_WALLET=true`

2. Run the jar as usual. (See Run Tests Via Command Line Locally section below)

Via IDE:

1. In your IDE go to 'Run' tab

2. Go to 'Edit Configurations'

3. In the left pane make sure you have the correct class selected, these changes will only apply to the specified class

4. Click 'Add Environment Variables'

5. Add: `INITIALIZE_WALLET=true`

Next, go to the respective {environment}.properties file and fill in the environment details. For example wallet indices and wallet ID's.

---
## Switching Test Environment During Development

1. Open the `EnvironmentConfig.java class`

2. Comment out this line:

 ```java
 "classpath:" + "${SBI_ENVIRONMENT_FOR_TESTING}" +  ".properties" // qa,qa2,selenium
 ```

3. Uncomment the following line and edit the classpath with whichever environment you want to test (qa, qa2, selenium) here:

```java
"classpath:{environment}.properties" //replace {environment} with the property file name of environment you are testing
```

Note: Remember to reverse this process when running `mvn clean install` or pushing to the remote repository.

## Additional Environment Configuration

#### Input Blockio Credentials:

For testing transactions from an external wallet the automation scripts use a Blockio API. Keeping these credentials in the repository for security reasons is not ideal.
So you will need to enter the Scorpio Blockio account credentials before running any tests.

1. Log into Blockio

2. Navigate to Bitcoin Testnet

3. Click the API Keys button and copy the bitcoin key

4. Navigate to `BlockIOElements.java` fill in these fields:

```java
    public static String apiKey = "";
    public static String secretPin = "";
```

#### To Edit Test Environment Specifics:

1. Go to the respective environment property folder.
2. Edit any properties there

*NOTE: If you add a new property to the file, you MUST add the same property to the other environment property files and assign some value. Otherwise the variables will not be able to intialize.
Consult https://github.com/lviggiano/owner for more info on how to set up a new property.*


#### To Edit Which IWC User Tests Are Run On:
You must edit the usernames in the .properties file and the corresponding
password in the .credentials file

---
## Usage

#### To Run Tests Locally Via CLI

1. Navigate to test automation folder
```
cd ...\safewallet\test-automation
```
2. Generate a JAR file

 ```
 mvn clean install
 ```

3. Assign the test environment (qa, qa2, stgeap, or selenium)

 ```
 export SBI_ENVIRONMENT_FOR_TESTING={test_environment}
 ```

4. Assign Which tests you wish to run. By default this runs IWC core HappyPath Only.
The options are iwcHP, webAdminHP, or HappyPath which runs the full HappyPath.
```
export TESTS_TO_RUN={package-to-run}
```
5. Decide if you want to run Chrome Headless or not. Default is false.
```
export BROWSER_GUI={true/false}
```

6. To verify the environment variables are set.

 ```
  export
 ```

7. Extract the environment specific .credentials folder to the resources
directory.

8. Run the JAR with java -jar command

```
java -jar {generatedJAR}.jar
```

9. (OPTIONAL). Test results can be reported to TestRail when running happypath tests.
This will create a new happypath test run and report the results. To enable this set the below
environment variable. Else it will be set to false. (Note) This option is only available when
running the full HappyPath ie, when `TESTS_TO_RUN=HappyPath`

```
export TEST_RAIL=true
```

NOTE: The export command only sets the environment variable temporarily. If you
open a new terminal tab, or lock your device you will have to reassign the variable via the
export command.

For more user information consult the [QA automation knowledge base](https://sbicryptosec.atlassian.net/wiki/spaces/SAFEWALLET/pages/389513334/Automation+Knowledge+Sharing+WIP).


---
