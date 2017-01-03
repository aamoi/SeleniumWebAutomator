/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumwebautomator.utils;

/**
 *
 * @author amoi
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Loads system properties from a file.
 *
 * @author <a href="brian.ngure@cellulant.com">Brian Ngure</a>
 */
@SuppressWarnings({"FinalClass", "ClassWithoutLogger"})
public final class Props {

    /**
     * A list of any errors that occurred while loading the properties.
     */
    private transient List<String> loadErrors;
    /**
     * relative path to config file
     */
    
    private static  String PROPS_FILE = "Config/HubclientuitestsConfigs.xml";
    /**
     * Info log level. Default = INFO.
     */
    private transient String infoLogLevel = "INFO";
    /**
     * Info log file name. 
     */
    private static transient String infoLogFile;
    /**
     * Error log level. Default = FATAL.
     */
    private static  transient String errorLogLevel = "ERROR";
    /**
     * Error log file name.
     */
    private static transient String errorLogFile;
    
    /**
     * Maximum allowed size(MBs) of log file
     */
    private transient String logfilesize;
    /**
     * Maximum allowed log files
     */
    private transient int logfiles;
    /**
     * The browser from which the application will run 
     */
    private transient String WebDriverBrowser;
    /**
     * Location of the web driver on the local system .
     */
    private transient String WebDriverLocation;
    /**
     * web driver's timeout on a web element 
     */
    private transient int webelementtimeout;
    /**
     * The url for the application under test
     */
    private transient String applicationURL;
    /**
     * test data file type
     */
    private transient String datafiletype;
    /**
     * 
    full path to the test data file
    */
    private transient String datafilepath;
    /**
     * Constructor.
     */
    /**
     * Excel type of web elements locators file.
     */
    private transient String locatorsFileType;
    /**
     * File containing web element locators attributes/information.
     */
    private transient String locatorsFilePath;
    /**
     * maximum number of records that can be displayed per page, in pagination.
     * This can be after search resulting into paginated list of results .
     */
    private transient int PaginationRecordSize;
    /**
     * average Sleep time waiting for pages to load, especially
     * search large data like 1 Million +.
     */
     private transient int AvarageSleepTimeInMils;
     
     /**
      * Setting for SSL, 1 indicates that application  is SSL enabled(HTTPS)
      */
     private transient int isSSLEnabled;
      
    public Props() {
       // if (errorLogFile.isEmpty()&&infoLogFile.isEmpty())
        setLogPath("");
        //loadErrors = new ArrayList<String>(0);
        //loadProperties(PROPS_FILE);
    }

    /**
     * Load system properties.
     *
     * @param propsFile the system properties xml file
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private void loadProperties(final String propsFile) {
        FileInputStream propsStream = null;
        Properties props;

        try {
            props = new Properties();
            propsStream = new FileInputStream(propsFile);
            props.loadFromXML(propsStream);

            String error1 = "ERROR: %s is <= 0 or may not have been set";
            String error2 = "ERROR: %s may not have been set";


            infoLogFile = props.getProperty("InfoLogFile");
            if (getInfoLogFile().isEmpty()) {
                getLoadErrors().add(String.format(error2, "InfoLogFile"));
            }

            errorLogFile = props.getProperty("ErrorLogFile");
            if (getErrorLogFile().isEmpty()) {
                getLoadErrors().add(String.format(error2, "ErrorLogFile"));
            }
            
            WebDriverBrowser=props.getProperty("WebDriverBrowser");
            if(getBrowser().isEmpty()){
               getLoadErrors().add(String.format(error1,"WebDriverBrowser"));  
            }
            
            WebDriverLocation=props.getProperty("WebDriverLocation");
    
            
            applicationURL=props.getProperty("ApplicationURL");
            if(getApplicationURL().isEmpty()){
                 getLoadErrors().add(String.format(error1,"ApplicationURL"));
            }
            
             datafiletype=props.getProperty("TestDataFileType");
            if(getDatafiletype().isEmpty()){
                getLoadErrors().add(String.format(error1,"TestDataFileType"));
            }
        
            datafilepath=props.getProperty("TestDataFilePath");
            if(getDatafilepath().isEmpty()){
                getLoadErrors().add(String.format(error1,"TestDataFilePath"));
            }
            
            
            locatorsFileType=props.getProperty("LocatorsFileType");
            if(getLocatorsFileType().isEmpty()){
                getLoadErrors().add(String.format(error1,"LocatorsFileType"));
            }
            
            locatorsFilePath=props.getProperty("LocatorsFilePath");
            if(getLocatorsFilePath().isEmpty()){
                getLoadErrors().add(String.format(error1,"LocatorsFilePath"));
            }
            
            
            String numberofLogFiles=props.getProperty("max_num_logfile");
            if(numberofLogFiles.isEmpty()){
                getLoadErrors().add(String.format(error1, "max_num_logfile"));
            }
            else{
                logfiles=Integer.parseInt(numberofLogFiles);
                if (logfiles<=0){
                getLoadErrors().add(String.format(error1, "max_num_logfile"));
            }
            }
            String PaginationSizeString=props.getProperty("MaxDisplayedRecordsPerPage");
             if(PaginationSizeString.isEmpty()){
                getLoadErrors().add(String.format(error1, "MaxDisplayedRecordsPerPage"));
            }
            else{
                PaginationRecordSize=Integer.parseInt(PaginationSizeString);
                if (PaginationRecordSize<=0){
                getLoadErrors().add(String.format(error1, "MaxDisplayedRecordsPerPage"));
            }
            }
               String AvarageSleepTimeInMilsString=props.getProperty(
                       "AvarageSleepTimeInMils");
             if(AvarageSleepTimeInMilsString.isEmpty()){
                getLoadErrors().add(String.format(error1, "AvarageSleepTimeInMils"));
            }
            else{
                AvarageSleepTimeInMils=Integer.parseInt(AvarageSleepTimeInMilsString);
                if (AvarageSleepTimeInMils<=0){
                getLoadErrors().add(String.format(error1, "AvarageSleepTimeInMils"));
            }
            }
            String isSSLEnabledString=props.getProperty("isSSLEnabled");
              if(AvarageSleepTimeInMilsString.isEmpty()){
                getLoadErrors().add(String.format(error1, "isSSLEnabled"));
            }
            else{
                isSSLEnabled=Integer.parseInt(isSSLEnabledString);
                if (isSSLEnabled<=0){
                getLoadErrors().add(String.format(error1, "isSSLEnabled"));
            }
            }
             
            logfilesize=props.getProperty("MaxLogFileSize");
            if(getLogfilesize().isEmpty()){
                getLoadErrors().add(String.format(error1, "MaxLogFileSize"));
            }
      
            String drivertimeouts=props.getProperty("TimeOutInSeconds");
            if(drivertimeouts.isEmpty()){
                getLoadErrors().add(String.format(error1,"TimeOutInSeconds"));
            }
            else{
                webelementtimeout=Integer.parseInt(drivertimeouts);
                if (webelementtimeout<=0){
                getLoadErrors().add(String.format(error1, "TimeOutInSeconds"));
            }
            }
            
            propsStream.close();
        } catch (NumberFormatException ne) {
            System.err.println("Exiting. String value found, Integer is "
                    + "required: " + ne.getMessage());

            try {
                propsStream.close();
            } catch (IOException ex) {
                System.err.println("Failed to close the properties file: "
                        + ex.getMessage());
            }

            System.exit(1);
        } catch (FileNotFoundException ne) {
            System.err.println("Exiting. Could not find the properties file: "
                    + ne.getMessage());

            try {
                propsStream.close();
            } catch (IOException ex) {
                System.err.println("Failed to close the properties file: "
                        + ex.getMessage());
            }

            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("Exiting. Failed to load system properties: "
                    + ioe.getMessage());

            try {
                propsStream.close();
            } catch (IOException ex) {
                System.err.println("Failed to close the properties file");
            }

            System.exit(1);
        }
    }

    private String loadJsonConfig(String path) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    System.err.println("Exception: " + ex.getMessage());
                }
            }
        }
        return sb.toString();
    }

    /**
     * Info log level. Default = INFO.
     *
     * @return the infoLogLevel
     */
    public String getInfoLogLevel() {
        return infoLogLevel;
    }

    /**
     * Error log level. Default = FATAL.
     *
     * @return the errorLogLevel
     */
    public String getErrorLogLevel() {
        return errorLogLevel;
    }

    /**
     * Info log file name.
     *
     * @return the infoLogFile
     */
    public String getInfoLogFile() {
        return infoLogFile;
    }

    /**
     * Error log file name.
     *
     * @return the errorLogFile
     */
    public String getErrorLogFile() {
        return errorLogFile;
    }
    /**
     * 
     * @return the browser  for selecting web driver. 
     */
    public String getBrowser() {
        return WebDriverBrowser;
    }
    /**
     * gets location to web driver on the local system.
     * @return web driver location
     */
    public String getWebDriverLocation() {
        return WebDriverLocation;
    }
    /**
     * 
     * @return application url
     */

    public String getApplicationURL() {
        return applicationURL;
    }
   /**
    * 
    * @return test data file path
    */

    public String getDatafilepath() {
        return datafilepath;
    }
    /**
     * 
     * @return test data file type
     */
    public String getDatafiletype() {
        return datafiletype;
    }
    /**
     * 
     * @return driver's time outs on web elements  
     */

    public int getWebelementtimeout() {
        return webelementtimeout;
    }
    /**
     * maximum number of log files used
     * @return 
     */
    public int getLogfiles() {
        return logfiles;
    }
    /**
     * 
     * @return maximum size of any log file
     */
    public String getLogfilesize() {
        return logfilesize;
    }
    /**
     * Web locators file type.It can be XLS, XLSX, XML etc.
     * @return file type for locators file
     */
    public String getLocatorsFileType() {
        return locatorsFileType;
    }
    /**
     * Gets web elements locators file.
     * @return full path to web element locators file.
     */
    public String getLocatorsFilePath() {
        return locatorsFilePath;
    }
  /**
   * Get maximum number of records per list/display page or result page
   * in case of a search that is resulting into many records.
   * @return 
   */
    public int getPaginationRecordSize() {
        return PaginationRecordSize;
    }
 /**
  * Get sleep time for allowing heavy pages  with huge to load.
  * @return wait time on Page/element to load.
  */
    public int getAvarageSleepTimeInMils() {
        return AvarageSleepTimeInMils;
    }
   /**
    * get configurations  for SSL(HTTPS), 1 indicates that its enabled.
    * @return configs for SSL on the application.
    */
    public int getIsSSLEnabled() {
        return isSSLEnabled;
    }
    /**
     * A list of any errors that occurred while loading the properties.
     *
     * @return the loadErrors
     */
    public List<String> getLoadErrors() {
        return Collections.unmodifiableList(loadErrors);
    }
    /**
     * Set log files path
     * @param logPath 
     */
     public static void setLogPath(String logPath){
     String workingDirectory =logPath.isEmpty()? System.getProperty("user.dir"):logPath;
     infoLogFile=workingDirectory.concat("SeleniumWebAutomator_Info.log");
     errorLogFile=workingDirectory.concat("SeleniumWebAutomator_Error.log");
     
        
    }
}
