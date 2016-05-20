package org.test;
import  java.sql.Connection;        
import  java.sql.Statement;     
import  java.sql.ResultSet;     
import  java.sql.DriverManager;     
import  java.sql.SQLException; 
import org.testng.annotations.Test;

public class DBConnectionTest {
  
	 @Test
	  public void testDBConnection() throws  ClassNotFoundException, SQLException {
		  
		  
		  String dbUrl = "jdbc:mysql://localhost:3306/itech";                   
		  String username = "qatest";   
		  String password = "qatest";             
		  String query = "select customers_email_address from customers where customer_id=1;";  
		  Class.forName("com.mysql.jdbc.Driver");     
		  
		  //SshClient sshClient = new SshClient();
	      //List<String> actual = sshClient.listFiles(userName, password, host, path);
	      

		  
		  Connection con = DriverManager.getConnection(dbUrl,username,password);
		  
		  Statement stmt = con.createStatement();                  
		          
		 ResultSet rs= stmt.executeQuery(query);                         
		            
		 System. out.println(rs.getString(1));     
		 con.close();           
		}
		  
		  
	  
	  
	  
  }