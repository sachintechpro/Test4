/*Framework -Web Selenium Test Framework
 * Version - 0.1
 * Creation Date - May, 2016
 * Author - Tian Yu
 * This is collection of common method to naviagate the browser such as:
 * OpenBroswer
 * CloseBroswer
 * getDriver
 * GoToURL
 * GoBack
 * GoForward
 * VerifyTextExist
 * InputText
 * Click
 * GetURL
 */

package common;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;

import common.ReadProperty;

public class CommonMethods {
	public  WebDriver driver;
	public  ReadProperty ReadPropertyFile;
	public  WebDriverWait wait;
	public  String OSName=System.getProperty("os.name");
	public  boolean bExeResult=false;
	public  Logger logger= Logger.getLogger(CommonMethods.class);	
	public  static Set<String> windowHandles;
	public  static String rootWindow = null;
	private static int pass = 0;
	private static int fail = 0;
	private static int waittime=15;
	private static String startTime;

	public WebDriver openBrowser(String remoteBrowserType) throws Exception
	{	
		
		ReadPropertyFile =new ReadProperty();
		DesiredCapabilities Capabilities = new DesiredCapabilities();
		String browser =ReadProperty.getConfigPropertyVal("BrowserType");
		logger.info("Current OS = " + OSName);

		//Operation System of Windows

		logger.info("Browser = " + browser);

		//set driver to Firefox
		if (browser.equalsIgnoreCase("FireFox")) {
			driver = new FirefoxDriver();
			driver.manage().deleteAllCookies();
		}

		//set driver to Chrome
		else if(browser.equalsIgnoreCase("Chrome")) {
			File chromedriver = new File("S:\\Selenium\\chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", chromedriver.getAbsolutePath());
			Capabilities = DesiredCapabilities.chrome();
			Capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
			driver = new ChromeDriver(Capabilities);  
		}

		//set driver to Safari
		else if (browser.equalsIgnoreCase("Safari")) {
			driver = new SafariDriver();
		}

		//set driver to IE
		else if(browser.equalsIgnoreCase("IE")) {	
			File IEfile = new File("S:\\Selenium\\IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", IEfile.getAbsolutePath());		
			Capabilities = DesiredCapabilities.internetExplorer();
			Capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(Capabilities); 
		} 

		//this is for remote testing
		else if(browser.equalsIgnoreCase("Remote")) {
			logger.info("Browser is=" + remoteBrowserType );

			if (remoteBrowserType.equalsIgnoreCase("FireFox"))
				driver= new RemoteWebDriver(DesiredCapabilities.firefox());			
			else if(remoteBrowserType.equalsIgnoreCase("IE")){
				Capabilities = DesiredCapabilities.internetExplorer();
				Capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				driver= new RemoteWebDriver(Capabilities);
			}
			else if(remoteBrowserType.equalsIgnoreCase("Chrome")) 
				driver= new RemoteWebDriver(DesiredCapabilities.chrome());
			//driver = new Augmenter().augment(driver);  
			else if(remoteBrowserType.equalsIgnoreCase("Safari")) 
				//Assert.assertTrue(isSupportedPlatform());
				driver= new RemoteWebDriver(DesiredCapabilities.safari());
			driver = new Augmenter().augment(driver);  
		}

		//Operation System of MAC
		if(OSName.toLowerCase().contains("mac")){
			if(browser.equalsIgnoreCase("Remote")){ 
				//Capabilities = DesiredCapabilities.safari();
				driver = new RemoteWebDriver(DesiredCapabilities.safari());
			}else{
				Capabilities = DesiredCapabilities.safari();
				driver = new SafariDriver(Capabilities);
			}
		}
		System.out.println("browser"+ browser + "has openned");
		//Browser Cleaning
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(waittime, TimeUnit.SECONDS);	
		//driver.manage().window().maximize(); 
		logger.info("Broswer has openned Successfully");

		//Record Broswer information in Log
		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		System.out.println("version: " + caps.getVersion());
		String browserName=caps.getBrowserName();
		String browserVersion=caps.getVersion();
		logger.info("\n"+"Browser="+browserName+"\n"+"BrowserVersion="+browserVersion+"\n"+"OS="+System.getProperty("os.name")+"\n"+"OSVersion="+System.getProperty("os.version"));
		return driver;
	}

	public void closeBroswer() {
		try{
			logger.info("Closing browser.");
			driver.quit();
			logger.info("Browser closed.");
		}
		catch(WebDriverException e){
			logger.info("Browser is already closed.");
		}
		rootWindow = null;
		windowHandles.clear();
		windowHandles = null;
	}

	public WebDriver getDriver(){
		return driver;
	}

	public void goToURL(String URL){
		if(rootWindow != null){
			driver.switchTo().window(rootWindow);
			driver.get(URL);
			logger.info("URL navigating to ="+URL);
		}
		else{
			driver.get(URL);
			logger.info("URL navigating to ="+URL);
			rootWindow = driver.getWindowHandle();
			windowHandles = driver.getWindowHandles();
		}
	}

	public void navigateBrowserback(){
		driver.navigate().back();
	}


	public void navigateBrowserFW(){
		driver.navigate().forward();
	}

	public void verifyText(String expected){
		try{
			driver.findElement(By.xpath("//*[contains(text(),'"+ expected.trim() +"')]"));
			logger.info("On page " + driver.getTitle() + ". Expected Text \""+ expected +"\" verified");
			// return true;
		}
		catch(NoSuchElementException e){
			logger.info("On page " + driver.getTitle() + ". Expected Text \""+ expected +"\" not found");
			Assert.fail("On page " + driver.getTitle() + ". Expected Text \""+ expected +"\" not found");
		}

	}

	public void inputText(WebElement slocator,String sValue){
		String Element=slocator.getText();
		try {	
			logger.info(Element + "is found.");
			slocator.clear();
			slocator.sendKeys(sValue);
			logger.info(sValue + " entered.");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(Element + " element is not found.");
		}	
	}

	public void click(WebElement slocator){
		try {
			String Element=slocator.getText();
			if ((Element.isEmpty()) || (Element==null)){
				Element=slocator.getAttribute("value");
			}
			logger.info("Click on "+Element);
			slocator.click();
			logger.info(Element + " clicked ");
			acceptPopup();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(slocator + " is not clicked ");
		}
	}

	public void acceptPopup() {
		try {
			Alert alert = driver.switchTo().alert();
			//Thread.sleep(10000);
			alert.accept();
			driver.switchTo().defaultContent();
			logger.info("Alert Accepted");
		} catch (Exception e) {
			// Sometimes the text exist, but not the accept button.
			logger.info("Alert not found");
		}
	}

	public void selectDropdown(WebElement slocator, String value){
		List<WebElement> getDropDownValues=slocator.findElements(By.tagName("option"));
		boolean match = false;
		logger.info("Total no. of dropdown values:"+ getDropDownValues.size());
		for(int i = 0; i< getDropDownValues.size();i++){
			logger.info(getDropDownValues.get(i).getText());
			if (getDropDownValues.get(i).getText().equalsIgnoreCase(value)){
				getDropDownValues.get(i).click();
				match = true;
				break;}

		}
		if(!match){
			logger.info("No Selection Found");
			Assert.fail(value + "Not found in the dropdown " + slocator);

		}
	}

	public void openDefault(String removeBroswerType) throws Exception{

		String defaultUrl  = "www.google.com";;

		try {
			defaultUrl=ReadPropertyFile.getConfigPropertyVal("URL"); //ReadPropertyFile.ReadFile(PropertyFilePath.ConfigPathLocation, URL);
			//logger.info("defaultURL = " + defaultUrl);
			//driver.get(defaultUrl);
		}catch (Exception ex) {
			
			logger.info("URL not found in Config.properties file. So opening default site = " + defaultUrl );
		}

		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			logger.info("URL Navigation");
			driver.get(defaultUrl);
		}catch(Exception e){
			logger.info("No browser found. New Browser Opening");	
			openBrowser(removeBroswerType);
			driver.get(defaultUrl);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			logger.info("URL Navigation");
		}

	}
	@SuppressWarnings("static-access")
	public void postResults( ITestResult it) throws SQLException{
		logger.info("Test description: " + it.getMethod().getDescription());
		logger.info("getMethod name:" +it.getMethod());
		logger.info("getName name:" +it.getName()); //tcID
		logger.info("getTestClass name:" +it.getTestClass()); //null
		logger.info("getThrow name:" +it.getThrowable());
		String HostID=System.getenv().get("COMPUTERNAME");
		logger.info(HostID);
		ReadPropertyFile =new ReadProperty();
		String className = it.getTestClass().toString().replace("[TestClass name=class", "");
		className = className.replace("]", "");

		if((pass+fail) == 0){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			startTime = dateFormat.format(date);	
			logger.info("Start time: " + startTime);
		}

		if (it.isSuccess()){
			pass++;
			try {	
				logger.info("Pass");

			}catch(Exception ex){
				System.out.print(ex.getMessage());
			}

		}else{
			fail++;
			try
			{
				if(it.getStatus() == ITestResult.SKIP){
					logger.info("Skipped");
				}
				else{
					logger.info("Fail");
					ScreenCapture screenCapture=new ScreenCapture(driver);
					String imgPath = screenCapture.takeScreenShoot(it.getMethod());
					logger.info("screenshot captured for: " +it.getMethod()+ " Failed TestCase");
					logger.info("screenshot captured is saved at" + imgPath);
				}
				//closeBrowser();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}	
}

