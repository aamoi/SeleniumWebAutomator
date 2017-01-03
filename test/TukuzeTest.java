
/**
 *
 * @author amoi
 */
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import static seleniumwebautomator.utils.CommonJavaUtilities.setSleepTime;
import static seleniumwebautomator.utils.SeleniumUtilities.*;
import static seleniumwebautomator.utils.FileUtilities.*;
import seleniumwebautomator.utils.Logging;

public class TukuzeTest {
    
  private final String classname="TukuzeTest | ";
  
  String test_file="/Users/amoi/Desktop/testFile.xlsx";
  
  String locator_file="/Users/amoi/Desktop/locatorsFile.xlsx";
  
  HashMap<Integer,ArrayList<String>> locator_data=null;
  
  WebDriver driver=null;
  
  Logging log;
 
  @Before
  public void setUp()throws Exception  {
      
    log= new Logging();
      
    setTestDataFile(test_file);     
    setLocatorsDataFile(locator_file);
    locator_data=getExcelRowsData(locator_file,0,0,4,0,4);
    driver=selectBrowser("chrome", "http", "/usr/local/bin/chromedriver");
    
  } 
  
/**
 * Tests login with valid credentials.
 */
//@Test
  public void testLoginWithValidCreds() {

      selectURL("http:localhost:8080/2kuze");  
      
      actionTypeText("admin","//*[@id=\"j_username\"]",3);
            
      actionTypeText("Nyama@16",locator_data.get(2).get(3), 3);
      
      actionClick("submit", 3);
      
      actionClick("Collections", 3);

      actionTypeText("bananas","textfield-1131-inputEl", 30);
      
      String preStr="/html/body/div[2]/div[2]/div/div/div[2]/div/div/div/div[4]/"
              + "div[1]/div[2]/table[%S]/tbody/tr[1]/td[%S]/div";
       
      getColumnsPaginatedRecords(preStr, "1,2,3", 2, 1, 20, 2, 
        "/html/body/div[2]/div[2]/div/div/div[2]/div/div/div/div[5]/div/");
     
      actionPressEnterKey("textfield-1131-inputEl", 0);
      
      actionPassTestCase("",0,0,0);
      
  }
  
  /**
   * 
  Tests login with valid credentials.
 */
  
@Test
  public void testAddBuyerOrder() throws InterruptedException {
      
      selectURL("http:localhost:8080/2kuze"); 
      
      setSleepTime(10);
      
      actionTabFoward();
      
      actionTabFoward();

      
      actionTypeText("admin",locator_data.get(1).get(3),3);
      actionTypeText("Nyama@16",locator_data.get(2).get(3), 3);
      
      actionClick("submit", 2);
      
      actionClick("Buy Orders", 2);
      
      actionClick("Post Buy Order", 2);
      
      actionClick("combo-1045-inputEl", 1);
      actionClick("//ul[@id='boundlist-1537-listEl']/li[6]",2); 
        
      actionTypeText("10","txtQuantity-inputEl", 1);
      
      setSleepTime(1);
      
      actionClick("combo-1044-inputEl", 1);
         
      actionClick("//ul[@id='boundlist-1539-listEl']/li[4]", 1);
      
      actionTypeText("10/Aug/2016","datefield-1046-inputEl",2);
      
      actionClick("button-1047-btnEl",3);
            
      
//      actionSetDateByDatePicker("datefield-1046-trigger-picker","10/08/2016","",
//              "datepicker-1537-nextEl","button-1544-btnInnerEl",2);
           
  }
  
  @After
  public void tearDown() throws Exception {
      //quitBrowser();  
  }
}