
package Page;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import common.CommonMethods;

import org.apache.log4j.*;

public class WelcomePage{

	private Logger logger= Logger.getLogger(WelcomePage.class);

	private WebDriver driver;
	private CommonMethods CF;

	@FindBy(how = How.XPATH, using ="//*[@id='bodyContent']/div/div[1]/a[1]/u")
	private WebElement LogYourSelfLink;

	@FindBy(how = How.XPATH, using ="//*[@id='bodyContent']/h1")
	private WebElement HeaderText;


	public WelcomePage(WebDriver driver) {
		this.driver = driver;
		CF = new CommonMethods();
		
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		logger.info("Loading WelcomePage page..."); 
		PageFactory.initElements(this.driver, this);
		/*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String ActualText=HeaderText.getText();
		logger.info(ActualText+" header found.");
		String ExpectedText="Welcome to iBusiness";
		Assert.assertEquals(ActualText,ExpectedText);
		//CF.verifyText("New Products For March");
		logger.info(ExpectedText+"header verified.");

	}

	public void ClickLogYourSelfLink(){
		CF.click(LogYourSelfLink);
		PageFactory.initElements(this.driver, this);
		String ActualText=HeaderText.getText();
		logger.info(ActualText+" header found.");
		String ExpectedText="Welcome, Please Sign In";
		Assert.assertEquals(ActualText,ExpectedText);
		logger.info(ExpectedText+"header verified.");
	}
}