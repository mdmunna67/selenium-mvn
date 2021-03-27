## Overview
​
SBISECSOL QA Automation - Selenium ( Hybrid Framework for Web and mobile test automation )
​
- [Pre-Requisites](#pre-requisites)
- [Git Checkout](#checkout)
- [Install mvn dependencies](#install-mvn-dependencies)
- [Usage](#usage)
  - [Usage-Example](#example)
- [Maven dependencies](#maven-dependencies)
- [Automation Coverage](#automation-coverage)
- [Framework Customization](#framework-customization)
- [Contributors](#contributors)
​
### Pre-Requisites
​
```
​
    # system should have the below softwares | tools to develop / run automation test scripts.
​
    - Java 8
    - Maven 
    - Appium
    - Browser [ Chrome ]
    - Eclipse [ neon.3 ] 
    - Docker [^19.03.13] and docker-compose[^1.27.4]
​
```
### Checkout
​
```bash
​
    git clone <URL>
    cd <FolderName>
​
```
​
### Install mvn dependencies  
​
```bash
​
mvn clean install  # to run test using default args (given in Run Manager sheet and Global properties)
​
```
​
### Usage
​
```bash
# --------------------------------------------------------------------------------------------------------
    mvn clean install -P[PROFILE] -D[ARGS]...
# ---------------------------------------------[PROFILE]--------------------------------------------------
    # maven profiles in pom.xml file.
    PROFILE         = regression | smoke | smokemobile | appiumweb | appiumnative
# ----------------------------------------------[ARGS]----------------------------------------------------
    RUN_CONF        = REMOTE | LOCAL | MOBILE | REMOTE_EMULATED_DEVICE | LOCAL_EMULATED_DEVICE
    BROWSER         = CHROME | FIREFOX
    MOBILE_TOOL     = REMOTE_WEBDRIVER | APPIUM
    MOBILE_PLATFORM = WEB_ANDROID | WEB_IOS | ANDROID | IOS
    DEVICE          = iPhone X | iPad Pro | Nexus 7 | Pixel 2 | IP:PORT
    MODEL           = iPhone X | iPad Pro | Nexus 7 | Pixel 2
# --------------------------------------------------------------------------------------------------------
```
​
### Example:
​
```bash
​
# Run test using mvn & allocator :
mvn -e exec:java -Pallocator -Dexec.args="Regression LOCAL 5 CHROME https://www.google.com/"

# Run test using mvn & testng :
mvn clean install -Psmoke -DRUN_MODE=LOCAL -DTHREADS=2 -DBROWSER=CHROME -DURL=https://www.google.com

​
```
​
### Maven dependencies:
​
```
    - webdrivermanager      = 4.2.2
    - selenium-java         = 3.141.59
    - java-client           = 6.1.0
    - testng                = 6.14.3
    - selenium-api          = 3.141.59
    - jersey-client         = 1.19
    - mongo-java-driver     = 3.12.7
    - jersey-core           = 1.19
    - javax.ws.rs-api       = 2.0
    - poi                   = 3.14
    - poi-ooxml             = 3.14
    - poi-ooxml-schemas     = 3.14
    - stax-api              = 1.0.1
    - xmlbeans              = 2.6.0
    - allure-testng-adaptor = 1.5.4
​
```
​
## Automation Coverage
​
| Modules - Phase     | # of TC Planned | # of TC Completed  | Overall Status |
| ------------------- | -------------   | -----------        | -----------    |
|                     |                 |                    |                |
|                     |                 |                    |                |
​
## Framework Customization 
​
| Features List  | Planned      | In-Progress     | Completed   
| -------------  | -------------| --------------- | ----------
| 1.             |  &#9989;     | &#9989;         | &#10060;     
​
## Contributors
​
> - 