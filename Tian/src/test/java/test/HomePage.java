package test;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Page.WelcomePage;
import common.CommonMethods;





public class HomePage {
	private WebDriver driver;
	private CommonMethods CF;
	//private ConnectDataBase DB;
	@Parameters({ "remoteBrowserType"})
	
	@BeforeClass(alwaysRun=true)
	public void setup(String remoteBrowserType) throws Exception {
		CF = new CommonMethods();
		driver = CF.openBrowser(remoteBrowserType);
	}
	
	@Parameters({ "remoteBrowserType"})
	
	@BeforeMethod(alwaysRun=true)
	public void navigate(String remoteBrowserType) throws Exception {
		CF.openDefault(remoteBrowserType);
		driver = CF.getDriver();
	
	}
	@AfterMethod(alwaysRun=true) 
	public void after(ITestResult it) throws Exception{
		if(it.getStatus()!=1){
		it.setThrowable(new Throwable("Error capture: blah blah. \n" + "System error: " + it.getThrowable().getMessage()));
		}
		CF.postResults(it);
	    driver.manage().deleteAllCookies(); 
		CF.closeBroswer();
	}	
	@Test(groups = {"Regression"},description="Open Home Page")
	public void OpenHomePage(){
		WelcomePage WelcomePage = new WelcomePage(driver);
		
	}
	@Test(groups = {"Regression"},description="Click Login Link")
	public void ClickLogIn(){
		WelcomePage WelcomePage = new WelcomePage(driver);
		WelcomePage.ClickLogYourSelfLink();
	}
	
}
