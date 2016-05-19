/*Framework -Web Selenium Test Framework
 * Version - 0.1
 * Creation Date - May, 2016
 * Author - Tian Yu
 * This class reads the method from the property file
 */
package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ReadProperty {
	public static String PropertyValue;
	public static String ConfigPathLocation="./Config.properties";
	public static Logger logger= Logger.getLogger(ReadProperty.class);

	public static String readFile(String File,String PropertyName) {
		try {
			Properties pro = new Properties();
			FileInputStream in = new FileInputStream(File);
			pro.load(in);
			// getting values from property file
			PropertyValue = pro.getProperty(PropertyName);
			logger.info("Value is: " + PropertyValue );
			return PropertyValue;
		} catch (IOException e) {
			System.out.println("Error is:" + e.getMessage());
			e.printStackTrace();
		}
		return PropertyValue;
	}

	 public static String getConfigPropertyVal(final String key) {
		    String ConfigPropertyVal = readFile(ConfigPathLocation,key);
			return ConfigPropertyVal != null ? ConfigPropertyVal.trim() : ConfigPropertyVal;
	}
}
