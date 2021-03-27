// update the below testng dependency in pom.xml file
		<dependency>
		    <groupId>org.testng</groupId>
		    <artifactId>testng</artifactId>
		    <version>7.4.0</version>
		    <scope>test</scope>
		</dependency>

public void m1() {
		
        // call that General Component / Reusable Library
		boolean abc = assertEquals("", "");
		
		if(abc){
			report.updateTestLog("something", "something ", Status.PASS);
		}else{
			report.updateTestLog("something", "something", Status.FAIL);
		}

	}
	
// General Component / Create a new Reusable Library
public static boolean assertEquals(String actualString, String expectedString) {
		boolean oflag=false;
	     try {
	       Assert.assertEquals(actualString, expectedString);
	       oflag=true;
	     } catch (AssertionError e) {
	       oflag=false;
	     }
		return oflag;
}