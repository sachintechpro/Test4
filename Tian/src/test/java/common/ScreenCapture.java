/**
 * Framework -Ecalix Test Framework
 * Version - 0.1
 * Creation Date - Feb, 2014
 * Author - Ramesh 
 * Description: Capture Screenshot
 *  **/
package common;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestNGMethod;
import org.testng.Reporter;
import org.apache.log4j.Logger;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ScreenCapture {

	private WebDriver driver;
	private ReadProperty propFile=new ReadProperty();
	public  Logger logger= Logger.getLogger(ScreenCapture.class);	//private static final String SOLUTIONDELIVERY_AUTOMATION = "\\\\04vfile002\\adm\\Solution Delivery\\Automation\\TestExecution";
	private static final String SCREENSHOT_FOLDER = "./screenshots";
	
	public ScreenCapture(WebDriver driver){
		this.driver = driver;
	}
	
	public void CaptureScreen (String sFileName) throws Exception
	{
		BufferedImage PrtScn = null;
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Robot robot = new Robot();
		
		// captures whole screen
		Rectangle rect = new Rectangle(0,0, ScreenSize.width, ScreenSize.height);
		PrtScn = robot.createScreenCapture(rect);
		
		
		FileOutputStream out = new FileOutputStream(sFileName);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(PrtScn);
		out.flush();
		out.close();		
	}
	
 public String  takeScreenShoot(ITestNGMethod testMethod) throws Exception {
        	String browser =ReadProperty.getConfigPropertyVal("BrowserType");
        	File screenshot;
        		screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    		String nameScreenshot = testMethod.getTestClass().getRealClass().getSimpleName() + "_" + testMethod.getMethodName();
    	    logger.info("Screen Name:=" + nameScreenshot);
    		String path = getPath(nameScreenshot);
         
    		 FileUtils.copyFile(screenshot, new File(path));
    		 System.setProperty("org.uncommons.reportng.escape-output", "false");
    		 Reporter.log(testMethod.getMethodName() + " Failed. Link to Screenshot: ");

    		 logger.info(("<a href= " + path + " target='_blank' >" + getFileName(nameScreenshot) + "</a>"));

    		 Reporter.log("<a href=\""+ path +"\">" + getFileName(nameScreenshot) + "</a>");

    	 	 Reporter.log("<br>");   
    
    	 	 String serverPath = path.replaceAll("\\\\", "/");
   
    	 	 serverPath = serverPath.substring(13); //replace machine name with "" is not working
    
    	 	 return serverPath; 
        }


    	 private String getFileName(String nametest) throws IOException {
     
    		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");

    		 Date date = new Date();
     
    		return dateFormat.format(date) + "_" + nametest + ".jpg";

    	 }
 
    	private String getPath(String nameTest) throws IOException {
    		String app =SCREENSHOT_FOLDER + "\\" + propFile.getConfigPropertyVal("Application"); 
    		String cycle = app + "\\" +propFile.getConfigPropertyVal("Cycle");
    		
    		new File(app).mkdirs();
    		new File(cycle).mkdirs();

    		File directory = new File(cycle);
     
    		String newFileNamePath = directory.getCanonicalPath() + "\\screenshots\\" + getFileName(nameTest);

    		return newFileNamePath;
     
    	}

    	
   	 public static String encodeImage(byte[] imageByArray){
		 return Base64.encodeBase64URLSafeString(imageByArray);
	 }
	 
	 public static byte[]decodeImage(String imageDataString){
		 return Base64.decodeBase64(imageDataString);
	 }
	
	

}

