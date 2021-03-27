public  void scrollDown(){
        Dimension dimension = driver.getWebDriver().manage().window().getSize();
        int scrollStart = (int) (dimension.getHeight() * 0.5);
        int scrollEnd = (int) (dimension.getHeight() * 0.2);

        new TouchAction((PerformsTouchActions) driver.getWebDriver())
                .press(PointOption.point(0, scrollStart))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(0, scrollEnd))
                .release().perform();
    }

    public static void scrollNClick(By listItems, String Text){
        boolean flag = false;

        while(true){
            for(WebElement el: driver.getWebDriver().findElements(listItems)){
                if(el.getAttribute("text").equals(Text)){
                    el.click();
                    flag=true;
                    break;
                }
            }
            if(flag)
                break;
            else
                scrollDown();
        }
    }

    public  void scrollNClick(WebElement el){
        int retry = 0;
        while(retry <= 5){
            try{
                el.click();
                break;
            }catch (org.openqa.selenium.NoSuchElementException e){
                scrollDown();
                retry++;
            }
        }
    }

    public  void scrollIntoView(String Text){
        //https://developer.android.com/reference/androidx/test/uiautomator/UiSelector


        String mySelector = "new UiSelector().text(\"" + Text + "\").instance(0)";
        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(" + mySelector + ");";
        ((AndroidDriver<?>) driver.getWebDriver()).findElementByAndroidUIAutomator(command);

        /*((AndroidDriver<MobileElement>) AppDriver.getDriver()).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\"" + Text + "\").instance(0))").click();
                */
    }

    public  void scrollTo(String Text){
        //https://appium.io/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures/

        if(driver.getWebDriver() instanceof AndroidDriver<?>){
            scrollIntoView(Text);
        }else
            if(driver.getWebDriver() instanceof IOSDriver<?>){
                final HashMap<String, String> scrollObject = new HashMap<String, String>();
                scrollObject.put("predicateString", "value == '" + Text + "'");
                scrollObject.put("toVisible", "true");
                ((IOSDriver<?>) driver.getWebDriver()).executeScript("mobile: scroll", scrollObject);
            }
    }


    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }

    private  Dimension getWindowSize() {
        if (windowSize == null) {
            windowSize = driver.getWebDriver().manage().window().getSize();
        }
        return windowSize;
    }

    public  void scroll(ScrollDirection dir, double distance) {
        if (distance < 0 || distance > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }
        Dimension size = getWindowSize();
        Point midPoint = new Point((int)(size.width * 0.5), (int)(size.height * 0.5));
        int top = midPoint.y - (int)((size.height * distance) * 0.5);
        int bottom = midPoint.y + (int)((size.height * distance) * 0.5);
        int left = midPoint.x - (int)((size.width * distance) * 0.5);
        int right = midPoint.x + (int)((size.width * distance) * 0.5);
        if (dir == ScrollDirection.UP) {
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);
        } else if (dir == ScrollDirection.DOWN) {
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);
        } else if (dir == ScrollDirection.LEFT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);
        } else {
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);
        }
    }

    protected  void swipe(Point start, Point end, Duration duration) {
        boolean isAndroid = driver.getWebDriver() instanceof AndroidDriver<?>;

        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        if (isAndroid) {
            duration = duration.dividedBy(ANDROID_SCROLL_DIVISOR);
        } else {
            swipe.addAction(new Pause(input, duration));
            duration = Duration.ZERO;
        }
        swipe.addAction(input.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver<?>) driver.getWebDriver()).perform(ImmutableList.of(swipe));
    }

    public  void click(By byEl){
        new WebDriverWait(driver.getWebDriver(), 20).until(ExpectedConditions.presenceOfElementLocated(byEl)).click();
    }

    public  void sendKeys(By byEl, String text){
        waitForEl(byEl);
        driver.getWebDriver().findElement(byEl).sendKeys(text);
    }

    public  boolean waitForEl(By byEl){
        boolean visible = false;
		try{
			new WebDriverWait(driver.getWebDriver(), 20).until(ExpectedConditions.presenceOfElementLocated(byEl));
		    visible = true;
		}catch (Exception e) {
			visible = false;
		}
		return visible;
    }