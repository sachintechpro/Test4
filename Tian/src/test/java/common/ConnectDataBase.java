package common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ConnectDataBase {

	private static Connection connection = null;
	private static Session session = null;
	private Channel channel;
	private static final int SSH_PORT = 22;

	public final String  sshHost = "107.170.213.234",
			sshuser = "qatester",
			sshpassword="qatester",
			dbuserName = "qatest",
			dbpassword = "qatest",
			localSSHUrl = "localhost",
			remoteHost = "";

	public void ConnectDataBase() throws JSchException, SQLException{
	String host = "107.170.213.234";
	String servUser = "qatester";
	String servPwd = "qatester";
	int port = 22;

	String rhost = "localhost";
	int rport = 3306;
	int lport = 3307;

	String driverName = "com.mysql.jdbc.Driver";
	String db2Url = "jdbc:mysql://localhost:3306"  + "/itech";
	String dbUsr = "qatest";
	String dbPwd = "qatest";

	try {
		JSch jsch = new JSch();
		// Get SSH session
		session = jsch.getSession(servUser, host, port);
		session.setPassword(servPwd);
		java.util.Properties config = new java.util.Properties();
		// Never automatically add new host keys to the host file
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		// Connect to remote server
		session.connect();
		// Apply the port forwarding
		session.setPortForwardingL(lport, rhost, rport);
		// Connect to remote database
		//Class.forName(driverName);
		connection = DriverManager.getConnection(db2Url, dbUsr, dbPwd);
		System.out.println ("Connection to database established!");
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
		if(session !=null && session.isConnected()){
			session.disconnect();
		}
	}
}
/*
	private static int doSshTunnel(String strSshUser, String strSshPassword, String strSshHost,int nLocalPort,String strRemoteHost,int nRemotePort) throws JSchException {
		final JSch jsch = new JSch();
		Session session = jsch.getSession(strSshUser, strSshHost, 22);
		session.setPassword(strSshPassword);

		//final Properties config = new Properties();
		//config.put("StrictHostKeyChecking", "no");
		session.setConfig();

		session.connect();
		//int assigned_port = session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
		return assigned_port;
	}

	public void ConnectDataBase(String query) throws JSchException{
		String sshHost = "107.170.213.234";
		String sshuser = "qatester";
		String sshpassword="qatester";
		String dbuserName = "qatest";
		String dbpassword = "qatest";
		String localSSHUrl = "localhost";
		int remotePort = 3306; 
		int localPort = 5555;
		String remoteHost = "";
		String driverName = "com.mysql.jdbc.Driver";
		System.out.println("SSH will attemp to connect.");

		int assignedPort = doSshTunnel(sshuser, sshpassword, sshHost,localPort, remoteHost, remotePort);

		try{
			java.util.Properties config = new java.util.Properties();
			JSch jsch = new JSch();
			session = jsch.getSession(sshuser, sshHost, 22);
			session.setPassword(sshpassword);
			//jsch.addIdentity(SshKeyFilepath);
			config.put("StrictHostKeyChecking", "no");
			config.put("ConnectionAttempts", "3");
			session.setConfig(config);
			session.connect();
			System.out.println("SSH Connected");


			Class.forName(driverName).newInstance();

			//int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);

			//System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
			System.out.println("Port Forwarded");
			StringBuilder url = new StringBuilder("jdbc:mysql://localhost:");

			// use assigned_port to establish database connection
			url.append(assignedPort).append ("/").append("itech").append ("?user=").
			append(dbuserName).append ("&password=").
			append (dbpassword);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url.toString());


			Statement stmt = con.createStatement();                  

			ResultSet rs= stmt.executeQuery("select * from customers");                         
			while (rs.next())
			{

				int id = rs.getInt(1);
				String lastName = rs.getString("customers_lastname");
				String firstName = rs.getString("customers_firstName");
				System.out.println(id+" "+lastName+","+firstName); 
			}        

			con.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}

