/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumwebautomator.utils;

/**
 * @author amoi
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static seleniumwebautomator.utils.FileUtilities.*;
import static seleniumwebautomator.utils.CommonJavaUtilities.*;

/**
 * @author alexander
 */
public class SeleniumUtilities {

    /**
     *
     * specifies different web actions
     */
    public enum FieldType {
        click, text, uploadfile, select
    };

    /**
     * Web driver for the tests
     */
    public static WebDriver driver;

    /**
     * Set logging object
     */
    private static Logging log = null;

    private static final String classname = "SeleniumUtilities | ";

    private static final String logseparator = "| -1 | ";

    public static FieldType fieldtype;

    public static ArrayList stackList = new ArrayList();

    public static boolean testStatus = false;

    /**
     * number of retries on locating for web element.
     */
    private static int elementLocationRetries = 3;

    static {
        log = new Logging();
    }

    /**
     * Waits(WebDriverWait) on web element for the specified timeout in seconds.
     *
     * @param by By object for locating web element
     * @param timeout time in seconds to wait on element.
     * @return
     * @throws java.lang.Exception
     */
    public static WebElement getWebElementWithWait(By by, int timeout)
            throws Exception {

        driver = focusOnWindow(driver, driver.getWindowHandle());

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        WebElement webElement = null;

        if (!wait.until(ExpectedConditions.visibilityOfElementLocated(by)).equals(null)) {

            webElement = wait.until(ExpectedConditions.presenceOfElementLocated(by));

        }

        return webElement;
    }

    /**
     * Waits(WebDriverWait) on web elements list for the specified timeout in
     * seconds.
     *
     * @param locator web elements locator
     * @param timeout time in seconds to wait on element.
     * @return
     * @throws java.lang.Exception
     */
    public static List<WebElement> getWebElementsWithWait(String locator, int timeout)
            throws Exception {

        By by = By.id(locator);

        By by2 = By.partialLinkText("Female");

        driver = focusOnWindow(driver, driver.getWindowHandle());

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        List<WebElement> webElement = null, webElement1 = null;

        if (!wait.until(ExpectedConditions.visibilityOfElementLocated(by)).equals(null)) {

            webElement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

        }

        return webElement;
    }

    /**
     * Type text into web element-text field.
     *
     * @param text text to enter.
     * @param locator locator of the text field element.
     * @param timeout
     */
    public static void actionTypeText(String text, String locator, int timeout) {

        getWebElement(locator, timeout).clear();
        getWebElement(locator, timeout).sendKeys(text);

    }

    /**
     * gets the web element irrespective of the locator type.
     *
     * @param locator
     * @param timeout
     * @return
     */
    public static WebElement getWebElement(String locator, int timeout) {

        String preLogStr = classname + "getWebElement()" + logseparator;

        WebElement element = null;

        while (elementLocationRetries != 0) {
            try {

                element = getWebElementWithWait(By.id(locator), timeout);
                break;

            } catch (Exception | Error e) {
                try {

                    element = getWebElementWithWait(By.xpath(locator), timeout);
                    break;

                } catch (Exception | Error e2) {

                    try {

                        element = getWebElementWithWait(By.linkText(locator), timeout);
                        break;

                    } catch (Exception | Error e3) {

                        try {

                            element = getWebElementWithWait(By.partialLinkText(
                                    locator), timeout);
                            break;

                        } catch (Exception | Error e4) {

                            try {
                                element = getWebElementWithWait(By.className(
                                        locator), timeout);
                                break;

                            } catch (Exception | Error e5) {
                                try {
                                    element = getWebElementWithWait(By.cssSelector(
                                            locator), timeout);
                                    break;

                                } catch (Exception | Error e6) {
                                    try {
                                        element = getWebElementWithWait(By.tagName(
                                                locator), timeout);
                                        break;

                                    } catch (Exception | Error e7) {
                                        try {

                                            element = getWebElementWithWait(
                                                    By.name(locator), timeout);
                                            break;

                                        } catch (Error | Exception e8) {

                                            elementLocationRetries--;

                                            if (elementLocationRetries <= 0) {
                                                log.error(preLogStr + "tried "
                                                        + 3+" times with exception,"
                                                        + " details:"+e8);

                                                break;
                                            }

                                            log.info(preLogStr + "Retrying, remaining retries: " + elementLocationRetries);

                                            getWebElement(locator, timeout);

                                        }
                                    }

                                }
                            }
                        }

                    }
                }

            }
        }

        if (elementLocationRetries > 0) {
            log.info(preLogStr + "web element successfully located");
        }

        return element;
    }

    /**
     * Gets for each page , multiple rows ;for each row ,columns data id
     * fetched.
     *
     * @param locator web locator for individual column data.
     * @param columnumber column to fetch counting left-right from 1.
     * @param pages number of paginated pages to navigate
     * @param fromrow first row to fetch, top-down, on each page.
     * @param numberofrows number of row records to fetch on the page.
     * @param timeouts web element timeouts in seconds
     * @param next locator for the button to navigate to next page.
     * @return column data for multiple rows.
     */
    public static ArrayList getColumnPaginatedRecords(String locator,
            int columnumber, int pages, int fromrow, int numberofrows,
            int timeouts, String next) {

        String preString = classname + "getColumnPaginatedRecords()" + logseparator;

        ArrayList<String> columndata = new ArrayList();

        String xpath;

        for (int pgno = 0; pgno < pages; pgno++) {

            int currentPage = pgno + 1;

            for (int rownumber = fromrow; rownumber < numberofrows + fromrow;
                    rownumber++) {

                String rowDesc = "@Page " + currentPage + ",@row " + rownumber
                        + ",column " + columnumber;
                /**
                 * String to identify row & column number Appended to pre-string
                 * to complete the locator
                 */
                xpath = String.format(locator, rownumber);
                try {

                    String columnvalue = null;

                    WebElement element = getWebElement(xpath, timeouts);

                    if ((element != null) && (element.isDisplayed())) {

                        columnvalue = element.getText();
                        columndata.add(columnvalue);

                    } else {

                        log.error(preString + ".Accessing null element.Please "
                                + "confirm that locator is valid on this page-" + rowDesc);
                        break;
                    }

                    log.info(preString + rowDesc + ", column value: " + columnvalue);
                } catch (Exception | Error e) {

                    log.error(preString + rowDesc + " An error occured during "
                            + "fetch." + e);
                    break;
                }
            }
            //navigate through pages by clicking on next pages
            if ((pages > 1) && (pgno < pages)) {

                WebElement nextElement = getWebElement(next, timeouts);

                if ((nextElement != null) && (nextElement.isDisplayed())) {

                    actionClick(next, timeouts);

                    setSleepTime(3);

                } else {

                    log.info(preString + "You have either reached the maximum "
                            + "# of pages for this pagination or your next page"
                            + " locator is invalid or element is not displayed."
                            + "Current Page is " + currentPage);
                    break;
                }
            }
        }

        log.info(preString + "number of records fetched:" + columndata.size());

        return columndata;
    }

    /**
     * Gets for each page , multiple rows ;for each row , multiple columns are
     * fetched.
     *
     * @param locator web locator for individual column data.
     * @param columns String set of columns to fetch, separated by "," e.g
     * "1,2,3"
     * @param pages number of paginated pages to navigate
     * @param fromrow first row to fetch, top-down, on each page.
     * @param numberofrows number of row records to fetch on the page.
     * @param timeouts web element timeouts in seconds
     * @param next locator for the button to navigate to next page.
     * @return data set for multiple rows, and each row with multiple columns.
     */
    public static HashMap<Integer, ArrayList<String>> getColumnsPaginatedRecords(
            String locator, String columns, int pages, int fromrow,
            int numberofrows, int timeouts, String next) {

        String preLogStr = classname + "getPaginatedRecordsForMultipleColumns()"
                + logseparator;

        String[] fetchColumns = splitString(columns, ",");

        HashMap<Integer, ArrayList<String>> columnsDataList = new HashMap<>();

        int numberOfFetchColums = fetchColumns.length;

        //Initilaize column data set.
        for (int i = 0; i < numberOfFetchColums; ++i) {

            ArrayList<String> ColumnData = new ArrayList();

            columnsDataList.put(Integer.parseInt(fetchColumns[i]), ColumnData);
        }

        String xpath;

        for (int pgno = 0; pgno < pages; pgno++) {

            int currentPage = pgno + 1;

            for (int rownumber = fromrow; rownumber < numberofrows + fromrow;
                    rownumber++) {

                String rowDesc = "@Page " + currentPage + ",@row " + rownumber;

                ArrayList<String> rowValues = new ArrayList();

                for (int i = 0; i < numberOfFetchColums; ++i) {

                    //Appended to pre-string to complete the locator
                    xpath = String.format(locator, rownumber,
                            fetchColumns[i]);

                    try {

                        String columnvalue = null;

                        WebElement element = getWebElement(xpath, timeouts);

                        if ((element != null) && (element.isDisplayed())) {

                            columnvalue = element.getText();

                            //rowValues for logs purposes
                            rowValues.add(columnvalue);

                            columnsDataList.get(Integer.parseInt(fetchColumns[i])
                            ).add(columnvalue);

                        } else {

                            log.error(preLogStr + "The web element you are "
                                    + "trying to access is null.Please confirm "
                                    + "if the locator is valid in this page."
                                    + "Current Page is:-" + currentPage);

                            log.info("Records." + columnsDataList);

                            return columnsDataList;
                        }
                    } catch (Exception | Error e) {

                        log.error(preLogStr + "Exception during fetch." + e);

                        return columnsDataList;

                    }
                }

                log.info(preLogStr + rowDesc + " column values are:- "
                        + rowValues);
            }

            //navigate through pages by clicking on next pages
            if ((pages > 1) && (pgno < pages)) {

                WebElement nextElement = getWebElement(next, timeouts);

                if ((nextElement != null) && (nextElement.isDisplayed())) {

                    actionClick(next, timeouts);

                    setSleepTime(1);

                } else {

                    log.info(preLogStr + "You have either reached the maximum "
                            + "number of pages for this pagination or your next "
                            + "page locator is invalid or element is"
                            + " not displayed.Current Page is:-" + currentPage);

                    log.info("Records." + columnsDataList);

                    return columnsDataList;
                }
            }
        }

        log.info(preLogStr + ", records fetched- " + columnsDataList.
                get(fetchColumns[1]).size());

        return columnsDataList;
    }

    /**
     * Sets (by browsing to file) file path into file path webElement.
     *
     * @param filepath the absolute path (on the local system) to upload file.
     * @param webElement the web element to hold the file path
     */
    public static void actionSetFilePathFieldValue(String filepath,
            WebElement webElement) {

        webElement.sendKeys(filepath);

        log.info(classname + "actionSetFilePathFieldValue()" + logseparator
                + "upload file field loaded with file:" + filepath);
    }

    /**
     * Sets date field value by date picker.To select current datetime,
     * dateString should contain either "now" or "current"
     *
     * @param dateFieldlocator locator for date field.
     * @param navigateToPreviousMonth Element that user clicks to move to
     * previous month.
     * @param navigateToNextMonth WebElement that user clicks to move to next
     * month.
     * @param dateString users date selection on the UI.
     * @param submit locator for date picker submit button.
     * @param nowLocator if user wants to select the current date.
     * @param narration log narration.
     * @param timeout web element time outs.
     * @return date picker processing status
     */
    public static boolean actionSetDateFieldValueByDatePicker(
            String dateFieldlocator, String navigateToPreviousMonth,
            String navigateToNextMonth, String dateString, String submit,
            String nowLocator, String narration, int timeout) {

        String logPreStr = classname + "actionSetDateFieldValueByDatePicker()"
                + logseparator;

        log.info(logPreStr + narration + " about to process date:-" + dateString);

        setSleepTime(3);

        boolean status = false;
        //Click in the date text field.Date picker will popup.
        actionClick(dateFieldlocator, timeout);

        //if dateString contains "now" or "current", current date is selected.
        if ((dateString.toLowerCase()).contains("now")
                || (dateString.toLowerCase()).contains("current")) {

            actionClick(nowLocator, timeout);

            //Click on the done button, to submit the date.
            actionClick(submit, timeout);

            status = true;

            return status;
        }
        String selectedDate = dateString;
        String monthNavigator = null;
        String defaultTime = " 00:00:00";
        String date = dateString.concat(defaultTime);
        String datetime = "";//setDateTimeFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        int navigationSteps = 0;

        try {
            //Get selected year and month respectively.
            int selectedDateyear = Integer.parseInt(selectedDate.substring(0, 4));
            int selectedDateMonth = Integer.parseInt(selectedDate.substring(5, 7));

            int currentYear = Integer.parseInt(datetime.substring(0, 4));
            int currentMonth = Integer.parseInt(datetime.substring(5, 7));

            log.info(logPreStr + "Current date:" + datetime);

            //Get differences for selected year, month & current date.
            int differencesInYears = selectedDateyear - currentYear;
            int differencesInMonths = selectedDateMonth - currentMonth;

            //Navigate months foward for future months/years.
            if (differencesInYears > 0) {
                navigationSteps = differencesInYears * 12 + differencesInMonths;
                monthNavigator = navigateToNextMonth;
                log.info(logPreStr + "future months navigated from current one :-"
                        + navigationSteps + ", future years:-"
                        + (currentYear + differencesInYears));
            } //Navigate months foward/backward for current year.
            else if (differencesInYears == 0) {
                navigationSteps = differencesInMonths;
                //if differnces in month is -ve,click backward.
                if (differencesInMonths < 0) {
                    monthNavigator = navigateToPreviousMonth;
                    navigationSteps = navigationSteps * -1;
                    log.info(logPreStr + "past months navigated from current one:-"
                            + navigationSteps + ", in current year:-" + currentYear);
                } else if (differencesInMonths > 0) {//if diff in month is +ve,click foward.
                    monthNavigator = navigateToNextMonth;
                    log.info(logPreStr + "future months navigated from current one:-"
                            + navigationSteps + ", in current year:-" + currentYear);
                } else {//the date is in current month.
                    log.info(logPreStr + "date selected is in the current month:-"
                            + currentMonth + ", in current year:-" + currentYear);
                }
            } //Navigate months backward for past years 
            else {
                //Value will be -ve, multiply by -1 to get +ve navigationSteps.
                navigationSteps = (differencesInYears * 12 + differencesInMonths) * -1;
                monthNavigator = navigateToPreviousMonth;
                log.info(logPreStr + "past months navigated from current one :-"
                        + navigationSteps + ", past years:-" + (currentYear
                        + differencesInYears));
            }
            //Navigates to either X past or future months.
            if (navigationSteps > 0) {
                for (int i = 0; i < navigationSteps; i++) {

                    actionClick(monthNavigator, timeout);

                    setSleepTime(1);
                }
            } else {
                setSleepTime(1);
            }
            //Click on the month date of the selected/navigated month.
            String monthDate = date.substring(8, 10);

            if (monthDate.startsWith("0")) {

                monthDate = monthDate.replace("0", "");
            }
            log.info(logPreStr + "Date of the month selected:-" + monthDate);

            actionClick(monthDate, timeout);

            //Click on the done button, to submit the date.
            actionClick(submit, timeout);

            log.info(logPreStr + "date set:-" + dateString + ", narration:-"
                    + narration);

            status = true;
        } catch (Exception | Error e) {
            log.error(logPreStr + "An error has occured:-" + e);
        }
        return status;
    }

    /**
     * Sets month date from the date picker.
     *
     * @param dateFieldlocator locator for date field.
     * @param selectedDate
     * @param previousMonth
     * @param nextMonth
     * @param currentDatePicker
     * @param timeout web element time outs.
     */
    public static void actionSetDateByDatePicker(String dateFieldlocator,
            String selectedDate, String previousMonth, String nextMonth,
            String currentDatePicker, int timeout) {

        String logPreStr = classname + "actionSetByDateDatePicker()" + logseparator;

        String format = "dd/MM/YYYY";

        String date = selectedDate.substring(0, 2);

        int monthCount = 0;

        try {
            Date startDate = formatDate(new Date(), format);

            Date endDate = convertStringtoDate(selectedDate, format);

            actionClick(dateFieldlocator, timeout);

            setSleepTime(2);

            monthCount = getMonthsCountBetweenDates(startDate, endDate);

            log.info(logPreStr + "Month count:" + monthCount);

            if (monthCount > 0) {

                for (int i = 0; i < monthCount; i++) {

                    actionClick(nextMonth, timeout);
                }

            } else if (monthCount < 0) {

                for (int i = 0; i < monthCount; i++) {

                    actionClick(nextMonth, timeout);
                }

            } else {

                setSleepTime(1);

            }

            log.info(logPreStr + "Date:" + monthCount);

            actionClick(date, timeout);

        } catch (Exception | Error e) {

            log.error(logPreStr + "Exception on setting date:-"
                    + selectedDate + ", details:" + e);
        }

    }

    /**
     * select a value from drop down field located by id.
     *
     * @param value option value selected by users
     * @param locator drop down locator
     * @param timeout
     */
    public static void actionSelectFromDropDown(String value,
            String locator, int timeout) {

        new Select(getWebElement(locator, timeout)).
                selectByVisibleText(value);

        log.info(classname + "actionSelectFromDropDownLocatedByid()"
                + logseparator + "selected " + value + " from  drop down");
    }

    /**
     * Performs a click action on element located by locator.
     *
     * @param locator web element locator
     * @param timeout timeout in seconds
     */
    public static void actionClick(String locator,
            int timeout) {

        getWebElement(locator, timeout).click();
    }

    /**
     * Performs a click action on a HTML list element located by name locator.
     *
     * @param locator web HTML list element locator
     * @param timeout timeout in seconds
     */
    public static void actionClickOnListItem(String locator,
            int timeout) {

        String elementLocator = String.format("//*[text()='%s']", locator);

        getWebElement(elementLocator, timeout).click();
    }

    /**
     * presses enter/ tab key on the key board on element located by xpath.
     *
     * @param locator in seconds
     * @param timeout in seconds
     */
    public static void actionPressEnterKey(
            String locator, int timeout) {

        getWebElement(locator, timeout).sendKeys(Keys.ENTER);

    }

    /**
     * Selects browser to run the App i.e. FireFox,Chrome and InternetExplorer.
     *
     * @param browser browser to use: FireFox/FF, chrome, InternetExplorer/ie
     * @param isHTTPS specifies whether the site is HTTPs or plain HTTP.
     * @param driverLocation installation path to the browser driver.
     * @return web driver for running the browser.
     */
    public static WebDriver selectBrowser(String browser, String isHTTPS,
            String driverLocation) {

        String prelogStr = classname + "selectBrowser()" + logseparator;

        WebDriver driver = null;

        try {
            if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("ff")) {

                if (isHTTPS.equalsIgnoreCase("https")
                        || isHTTPS.equalsIgnoreCase("true")
                        || isHTTPS.equalsIgnoreCase("yes")) {

                    DesiredCapabilities capability = DesiredCapabilities.firefox();

                    driver = new FirefoxDriver(setSSL(capability));

                    log.info(prelogStr + "Initialized firefox driver for https.");

                } else {
                    log.info(prelogStr + "Initialized firefox driver for http.");
                    driver = new FirefoxDriver();
                }
            } else if (browser.equalsIgnoreCase("chrome")) {

                System.setProperty("webdriver.chrome.driver", driverLocation);

                if (isHTTPS.equalsIgnoreCase("https")
                        || isHTTPS.equalsIgnoreCase("true")
                        || isHTTPS.equalsIgnoreCase("yes")) {

                    DesiredCapabilities capability = DesiredCapabilities.chrome();

                    driver = new ChromeDriver(setSSL(capability));

                    log.error(prelogStr + "Initialized chrome driver for https");
                } else {

                    driver = new ChromeDriver();
                    log.info(prelogStr + "Initialising chrome  driver for http.");
                }
            } else if (browser.equalsIgnoreCase("ie")
                    || browser.equalsIgnoreCase("internetExplore")) {

                System.setProperty("webdriver.ie.driver", isHTTPS);

                if (isHTTPS.equalsIgnoreCase("https")
                        || isHTTPS.equalsIgnoreCase("true")
                        || isHTTPS.equalsIgnoreCase("yes")) {

                    DesiredCapabilities capability
                            = DesiredCapabilities.internetExplorer();

                    driver = new InternetExplorerDriver(setSSL(capability));

                    log.error(prelogStr + "Initialized IE driver for https");

                } else {

                    driver = new InternetExplorerDriver();
                    log.info(prelogStr + "Initialized IE driver for http.");
                }
            } else {

                log.info(prelogStr + "Invalid browser selected" + browser
                        + ". Using firefox by default.");
                if (isHTTPS.equalsIgnoreCase("https")
                        || isHTTPS.equalsIgnoreCase("true")
                        || isHTTPS.equalsIgnoreCase("yes")) {

                    DesiredCapabilities capability = DesiredCapabilities.firefox();

                    driver = new FirefoxDriver(setSSL(capability));

                    log.info(prelogStr + "Will run app on firefox in https mode.");

                } else {

                    log.info(prelogStr + "Will run app on firefox in http  mode.");
                    driver = new FirefoxDriver();
                }
            }
        } catch (org.openqa.selenium.WebDriverException wde) {

            log.error(prelogStr + "Driver error on instanciation  message:-" + wde);

        } catch (Exception | Error e) {

            log.error(prelogStr + "Error on driver instanciation, message:- " + e);
        }

        SeleniumUtilities.driver = driver;

        if (SeleniumUtilities.driver.equals(null)) {

            log.error(prelogStr + "driver is null,actions will not be performed");
        }

        return driver;
    }

    /**
     * Set secure(SSL) capabilities for the site driver.
     */
    private static DesiredCapabilities setSSL(DesiredCapabilities capability) {

        DesiredCapabilities capabailities = capability;
        capabailities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        return capabailities;
    }

    /**
     * Set application URL
     *
     * @param URL
     */
    public static void selectURL(String URL) {
        SeleniumUtilities.driver.get(URL);
    }

    /**
     * Quit/Kill the browser
     */
    public static void quitBrowser() {
        driver.close();
    }

    /**
     * Gets text displayed on the web page.
     *
     * @param locator locator for element with text.
     * @param timeouts timeout on element.
     * @return displayed text on the web element.
     */
    public static String getWebText(String locator, int timeouts) {

        String text = getWebElement(locator, timeouts).getText();

        log.info(classname + "getWebText()" + logseparator + ", text is:" + text);

        return text;
    }

    /**
     * Checks whether web element has the specified text.
     *
     * @param text
     * @param locator
     * @param timeout
     * @return
     */
    public static boolean assert_IsTextDisplayed(String locator, int timeout,
            String text) {

        return getWebText(locator, timeout).contains(text);

    }

    /**
     * Checks whether web element has the specified texts.
     *
     * @param locator locator for the web element.
     * @param timeout timeout.
     * @param texts list of texts expected to be displayed on the web element
     * @return
     */
    public static boolean assert_IsAllTextsDisplayed(
            String locator, int timeout, String... texts) {

        for (String text : texts) {

            if (!getWebText(locator, timeout).contains(text));
            return false;

        }
        return true;
    }

    /**
     * Method for changing editable link text.Perform a series of actions(3) as
     * follows:-click on the link, edit link info, Accept or reject the changes.
     *
     * @param linkLocator xpath locator for the link to edit.
     * @param editLocator xpath locator for value entry field.
     * @param editType Input type of the edit value.I.e. text, select option
     * etc.
     * @param editValue new value to enter the pop up text field.
     * @param SubmitLocator xpath locator for accept or reject link.
     * @param narration narration for logging purpose.
     * @param timeouts timeouts in seconds for web elements.
     * @return status of the process i.e. successful or failed.
     */
    public static boolean actionSetLinkTextFieldValue(String linkLocator,
            String editLocator, String editType, String editValue,
            String SubmitLocator, String narration, int timeouts) {

        boolean status = false;

        actionClick(linkLocator, timeouts);

        setSleepTime(timeouts);

        actionTypeText(editLocator, editValue, timeouts);

        actionClick(SubmitLocator, timeouts);

        status = true;

        return status;
    }

    /**
     * Marks the test case in the excel sheet as passed.
     *
     * @param filePath
     * @param sheetNumber sheet counted left-right from 0.
     * @param rowNumber row in the sheet counted top-down from 0.
     * @param columnNumber column in the sheet counted left-right from 0.
     * @return
     */
    public static boolean actionPassTestCase(String filePath, int sheetNumber,
            int rowNumber, int columnNumber) {

        String preStr = classname + "actionPassTestCase()" + logseparator
                + "@sheet:" + sheetNumber + ", @row:" + rowNumber + ", @column:"
                + columnNumber;

        boolean updatestatus = false;

        try {

            updateExcelCell(filePath.isEmpty() ? test_data_file : filePath,
                    sheetNumber, rowNumber, columnNumber, "pass");
            updatestatus = true;
            log.info(preStr + ", test case passed.");

        } catch (Exception | Error e) {

            log.error(preStr + "unable to update test case to passed." + e);
        }
        return updatestatus;
    }

    /**
     * Marks the test case in the excel sheet as failed.
     *
     * @param filePath
     * @param sheetNumber sheet counted left-right from 0.
     * @param rowNumber row in the sheet counted top-down from 0.
     * @param columnNumber column in the sheet counted left-right from 0.
     * @return
     */
    public static boolean actionFailTestCase(String filePath, int sheetNumber,
            int rowNumber, int columnNumber) {

        String preStr = classname + "actionFailTestCase()" + logseparator + "@sheet:"
                + sheetNumber + ", @row:" + rowNumber + ", @column:" + columnNumber;

        boolean updatestatus = false;

        try {

            updateExcelCell(filePath.isEmpty() ? test_data_file : filePath,
                    sheetNumber, rowNumber, columnNumber, "failed");
            updatestatus = true;
            log.info(preStr + ", test case failed.");

        } catch (Exception | Error e) {

            log.error(preStr + "unable to update test case status to failed." + e);
        }
        return updatestatus;
    }

    /**
     * Checks if a web element is displayed or not.
     *
     * @param webElement Web element whose display status is to be checked.
     * @return display status(true if displayed) of the web element.
     */
    public static boolean assert_IsElementDisplayed(WebElement webElement) {

        String narration = "";
        boolean displayStatus = false;
        String preStr = classname + "isElementDisplayed()" + logseparator;

        try {

            displayStatus = webElement.isDisplayed();

            log.info(preStr);

        } catch (Exception | Error e) {

            log.error(preStr + e.getMessage());
        }
        return displayStatus;
    }

    /**
     * checks whether web element located by link text is displayed
     *
     * @param locator
     * @param time
     * @return
     */
    public static boolean assert_IsElementDisplayed(
            String locator, int time) {

        return getWebElement(locator, time).isDisplayed();
    }

    /**
     * Checks if a web element is displayed or not.
     *
     * @param webElement Web element whose display status is to be checked.
     * @return display status(true if displayed) of the web element.
     */
    public static boolean assert_IsElementEnabled(WebElement webElement) {

        String narration = "";
        boolean displayStatus = false;
        String preStr = classname + "assert_IsElementEnabled()" + logseparator;

        try {

            displayStatus = webElement.isEnabled();

            log.info(preStr);

        } catch (Exception | Error e) {

            log.error(preStr + e.getMessage());
        }
        return displayStatus;
    }

    /**
     * Checks if a web element is enabled or not.
     *
     * @param locator
     * @param time
     * @return true if element is enabled.
     */
    public static boolean assert_IsElementEnabled(
            String locator, int time) {

        return getWebElement(locator, time).isEnabled();
    }

    /**
     * Checks if a web element is selected or not.
     *
     * @param locator
     * @param time
     * @return true if element is selected.
     */
    public boolean isElementSelected(String locator, int time) {

        return getWebElement(locator, time).isSelected();
    }

    /**
     * Checks multiple checklist options
     *
     * @param locator_regex
     * @param records option list to select.
     * @param timeout web element timeout in seconds.
     */
    public static void actionCheckMultipleOptions(String locator_regex,
            String records, int timeout) {

        String[] checkList = splitString(locator_regex, ",");

        String preStr = classname + "actionCheckMultipleOptions()" + logseparator;

        log.info(preStr + "List to select: " + records);

        for (int i = 0; i < checkList.length; ++i) {

            String locator = String.format(locator_regex, checkList[i]);

            actionClick(locator, timeout);

            setSleepTime(timeout);
        }

    }

    /**
     * Populates values to an array list.
     *
     * @param value Value to Add to the list.
     * @return Array List with the value.
     */
    public ArrayList addToListStack(int value) {
        stackList.add(value);
        return stackList;
    }

    /**
     * Focus web driver on a window with the supplied window handle.
     *
     * @param driver the driver instance to focus on the window.
     * @param windowHandle unique identifier of the window to be focused on.
     * @return web driver focused on the window with supplied Window handle.
     */
    public static WebDriver focusOnWindow(WebDriver driver, String windowHandle) {

        String preStr = classname + "focusOnWindow()" + logseparator;

        WebDriver webdriver = null;
        try {

            webdriver = driver.switchTo().window(windowHandle);

        } catch (Exception | Error e) {

            log.error(preStr + "Error, message:-" + e);
        }
        return webdriver;
    }

    /**
     *
     * refreshes the current page/window.
     */
    public static void actionRefreshPage() {

        String preStr = classname + "actionRefreshCurrentPage()" + logseparator;
        try {

            driver.navigate().refresh();

        } catch (Exception | Error e) {

            log.error(preStr + "Error encountered, message:-" + e.getMessage());

        }
    }

    /**
     * Refreshes page for the supplied web driver and window handle.
     *
     * @param driver web driver
     * @param windowHandle window handle whose window is to be refreshed.
     */
    public static void actionRefreshBrowser(WebDriver driver, String windowHandle) {

        driver.switchTo().window(windowHandle).navigate().refresh();

    }

    /**
     * Move forward a single "item"/ 1 step" in the browser history for a tab.
     */
    public static void actionTabFoward() {

        String preStr = classname + "actionTabFoward()" + logseparator;

        try {

            driver.navigate().forward();

        } catch (Exception | Error e) {

            log.error(preStr + "Error, message:-" + e);
        }
    }

    /**
     * Move back a single "item"/"1 step" in the browser history for a tab.
     */
    public static void actionTabBack() {
        String preStr = classname + "actionTabBack()" + logseparator;
        try {

            driver.navigate().back();

        } catch (Exception | Error e) {

            log.error(preStr + "Error, message:-" + e);
        }
    }

    /**
     * Load a new web page in the current browser window. This is done using
     * HTTP GET operation, and the method will block until the load is
     * complete.This will follow redirects issued either by the server or as a
     * meta-redirect from within the returned HTML.Should a meta-redirect "rest"
     * for any duration of time, it is best to wait until this timeout is over,
     * since should the underlying page change whilst your test is executing the
     * results of future calls against this interface will be against the
     * freshly loaded page.
     *
     * @param url - The URL to load. It is best to use a fully qualified URL.
     */
    public void actionNavigateToURL(String url) {

        String preStr = classname + "actionNavigateToURL()" + logseparator;

        try {

            driver.navigate().to(url);

        } catch (Exception | Error e) {

            log.error(preStr + "Error, details:-" + e);
        }
    }

    /**
     * Load a new web page(for the supplied url) in the current browser window.
     *
     * @param url - The URL to load. It is best to use a fully qualified URL.
     */
    public void actionNavigateToURL(URL url) {
        String preStr = classname + "actionNavigateToURL()" + logseparator;
        try {

            driver.navigate().to(url);

        } catch (Exception | Error e) {

            log.error(preStr + ", details:- " + e.getMessage());
        }
    }

    /**
     * Closes the window for supplied window handle.
     *
     * @param windowHandle identifies the window to switch to.
     */
    public static void actionCloseWindow(String windowHandle) {

        String preStr = classname + "actionCloseWindow()" + logseparator;

        try {

            driver.close();

        } catch (Exception | Error e) {

            log.error(preStr + ", details :- " + e.getMessage());

        }
    }

    /**
     * switches the driver to the window for supplied window handle.
     *
     * @param windowHandle identifies the window to switch to.
     */
    public static void actionSwitchToWindow(String windowHandle) {

        String preStr = classname + "actionSwitchToWindow()" + logseparator;

        boolean status = false;

        try {

            driver.switchTo().window(windowHandle);
            status = true;

        } catch (Exception | Error e) {

            log.error(preStr + "Error, message:-" + e.getMessage());

        }
    }

    /**
     * switches the specified driver to the window for supplied window handle.
     *
     * @param webdriver Web Driver to use
     * @param windowHandle identifies the window to switch to.
     */
    public static void actionSwitchToWindow(WebDriver webdriver, String windowHandle) {

        String preStr = classname + "actionSwitchToWindow()" + logseparator;

        boolean status = false;

        try {

            webdriver.switchTo().window(windowHandle);
            status = true;

        } catch (Exception | Error e) {

            log.error(preStr + "Error, message:-" + e.getMessage());

        }
    }

    /**
     * gets window handle from list of window handles for the supplied driver.
     *
     * @param webDriver driver for the currently opened window.
     * @param windowNumber Position of the window in the list of opened windows.
     * @return window handle from list of window handles opened by the driver.
     * handle is used to focus on a window in the focusOnWindow() method.
     */
    public static String getWindowHandle(WebDriver webDriver, int windowNumber) {

        String preStr = classname + "getWindowHandle()" + logseparator;
        String windowHandle = null;

        try {
            ArrayList<String> windowHandles = getWindowHandleList(webDriver);

            windowHandle = windowHandles.get(windowNumber);

            log.info(preStr + "window handle from the list:-" + windowHandle);

        } catch (Exception | Error e) {

            log.info(preStr + "Error encountered, message:-" + e);

        }
        return windowHandle;
    }

    /**
     * gets list of window handles for the supplied driver.
     *
     * @param webDriver web driver
     * @return window handles list .
     */
    public static ArrayList getWindowHandleList(WebDriver webDriver) {

        String preStr = classname + "getWindowHandleList()" + logseparator;

        ArrayList<String> windowHandles = null;
        try {

            windowHandles = (ArrayList) webDriver.getWindowHandles();

            log.info(preStr + "# of window handles for the driver:-"
                    + windowHandles.size());

        } catch (Exception | Error e) {

            log.error(preStr + "Error, message:-" + e);
        }
        return windowHandles;
    }

    /**
     * gets a handle from list of handles for the current driver, based on the
     * supplied window number.
     *
     * @param windowNumber position of the window in list of opened windows.
     * @return window handle for the specified windowNumber from list of window
     * handles opened by current driver.Window handle is used to focus on a
     * window in the focusOnWindow() method.
     */
    public static String getWindowHandle(int windowNumber) {

        String preStr = classname + "getWindowHandle()" + logseparator;

        ArrayList<String> windowHandles = null;

        String windowHandle = null;

        try {
            windowHandles = getWindowHandleList();

            windowHandle = windowHandles.get(windowNumber);

            log.info(preStr + "current driver window from list:-" + windowHandle);

        } catch (Exception | Error e) {
            log.error(preStr + "Error, message:-" + e);
        }
        return windowHandle;
    }

    /**
     * Gets list of window handles for current driver.
     *
     * @return window handles list .
     */
    public static ArrayList getWindowHandleList() {

        String preStr = classname + "getWindowHandleList()" + logseparator;

        ArrayList<String> windowHandles = null;
        try {

            windowHandles = (ArrayList) driver.getWindowHandles();
            log.info(preStr + "# of window handle for current driver:-"
                    + windowHandles.size());
        } catch (Exception | Error e) {
            log.error(preStr + "Error, message:-" + e);
        }

        return windowHandles;
    }

    /**
     * performs a drag and drop action
     *
     * @param From element from where to drag.
     * @param To element to drag to.
     * @return status true if drag and drop is successful.
     */
    public static boolean actionDragAndDrop(WebElement From, WebElement To) {

        String preStr = classname + "actionDragAndDrop()" + logseparator;

        boolean status = false;

        try {
            Actions builder = new Actions(driver);

            Action dragAndDrop = builder.clickAndHold(From).
                    moveToElement(To).release(To).build();

            dragAndDrop.perform();

            log.info(preStr + "complete.");

            status = true;
        } catch (Exception | Error e) {
            log.error(preStr + "Error, message:-" + e);
        }
        return status;
    }

    /**
     * gets test data from file for Parameterized tests in JUnit/testNG.
     *
     * @param sheet file data excel sheet number counting left-right from 0.
     * @param fromRow row number counting top-down from 0.
     * @param records number of records to fetch.
     * @param columns number of columns per record.
     * @return Multi-dimensional array holding one or more test data sets.
     */
    public static Object[][] getFileDataForParameterizedTests(int sheet,
            int fromRow, int records, int columns) {

        String logPreStr = classname + "getFileDataForParameterizedTests()"
                + logseparator;

        Object[][] dataSet = null;

        try {

            ArrayList<String> columndata = new ArrayList<String>();

            log.info(logPreStr + "about to fetch parametized test data.");

            HashMap<Integer, ArrayList<String>> testDataRecords = getExcelRowsData(
                    test_data_file, sheet, fromRow, records, 0, columns);

            dataSet = new Object[records][columns];

            for (int r = 0; r < records; ++r) {

                for (int c = 0; c < columns; ++c) {

                    dataSet[r][c] = testDataRecords.get(r).get(c);
                }
            }

            log.info(logPreStr + "Data Records fetched are" + dataSet.length);

        } catch (Exception | Error e) {

            log.error(logPreStr + "Error on fetching, details:-" + e);
        }

        return dataSet;
    }

}
