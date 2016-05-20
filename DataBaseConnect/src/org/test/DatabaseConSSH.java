package org.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.testng.annotations.Test;

	public class DatabaseConSSH {
		
	    private static int doSshTunnel(String strSshUser, String strSshPassword, String strSshHost,int nLocalPort,String strRemoteHost,int nRemotePort) throws JSchException {
	        final JSch jsch = new JSch();
	        Session session = jsch.getSession(strSshUser, strSshHost, 22);
	        session.setPassword(strSshPassword);

	        final Properties config = new Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);

	        session.connect();
	        int assigned_port = session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
	        return assigned_port;
	    }
	    
	    
	    @Test
	    public static void testConnection() {
	        try {
	            String strSshUser = "qatester"; // SSH loging username
	            String strSshPassword = "qatester"; // SSH login password
	            String strSshHost = "107.170.213.234"; // hostname or ip or
	                                                            // SSH server
	            
	            String strRemoteHost = "127.0.0.1"; // hostname or
	            String query = "select * from customers where customers_id < 11;";                                                        // ip of
	                                                                    // your
	                                                                    // database
	                                                                    // server
	            int nLocalPort = 3366; // local port number use to bind SSH tunnel
	            int nRemotePort = 3306; // remote port number of your database
	            String strDbUser = "qatest"; // database loging username
	            String strDbPassword = "qatest"; // database login password

	            int assignedPort = DatabaseConSSH.doSshTunnel(strSshUser, strSshPassword, strSshHost,nLocalPort, strRemoteHost, nRemotePort);

	            StringBuilder url =
	                    new StringBuilder("jdbc:mysql://localhost:");
	             
	            // use assigned_port to establish database connection
	            url.append(assignedPort).append ("/").append("itech").append ("?user=").
	                    append(strDbUser).append ("&password=").
	                    append (strDbPassword);
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	           
	            Connection con = DriverManager.getConnection(url.toString());
	            Statement stmt = con.createStatement();                  
		          
	       	 	ResultSet rs= stmt.executeQuery(query);                         
	       	 	while (rs.next())
	       	 	{
	       	
	       	 		int id = rs.getInt(1);
	       	 		String lastName = rs.getString("customers_lastname");
	       	 		String firstName = rs.getString("customers_firstName");
	       	 		System.out.println(id+" "+lastName+","+firstName); 
	       	 	}        
	       	 	
	            con.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            System.exit(0);
	        }
	    }

}