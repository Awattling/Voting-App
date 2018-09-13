/* Author: Austin Wattling
 * Date: August 24, 2018
 */
package server;

import java.sql.*;
import java.util.Scanner;

import com.mysql.jdbc.Statement;

public class Server {
	protected static int portNumber = 4000;
	protected static String databaseLocal = "jdbc:mysql://localhost:3306/voting_app"; 
	protected static String databaseUser = "root"; 
	protected static String databasePass = "password"; 
	protected static boolean running = true;  
	protected static boolean quit = false;
	protected static boolean debug = false; 
	protected static boolean SSLdebug = false;
	protected static boolean databaseToggle = false; 
	static ClientAcceptor clientAcceptor; 
	
	
	
	public static void main(String[] args){
		if(args.length == 1){
			SSLdebug = Boolean.parseBoolean(args[0]); 
			System.out.println("SSLDebug set to " + SSLdebug);
		}
		initilizeDatabase();
		startServer(portNumber);
		mainMenu();
	}
	private static void initilizeDatabase() {
		try {
			 // Registering the SQL driver // 
		    Class.forName("com.mysql.jdbc.Driver");
		    // Connecting to database // 
			Connection con = DriverManager.getConnection(databaseLocal, databaseUser, databasePass);
			System.out.println("Database connected");
			// Getting database metadata for table checks // 
			DatabaseMetaData dbmd = con.getMetaData();
			
			
			// Checking if polls table exists and creating if it does not // 
			ResultSet tables = dbmd.getTables(null, null, "polls", null); 
			if(tables.next()){
				System.out.println("polls table found in database");
			}else{
				System.out.println("polls table was not found in database. Creating Table");
				String sql = "CREATE TABLE POLLS " +
	                   "(id INTEGER not NULL, " +
	                   " active DATETIME, " + 
	                   " inActive DATETIME, " + 
	                   " question CHAR(255), " + 
	                   " PRIMARY KEY ( id ))";
				Statement stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql);
			}
			// Checking if options table exists and creating if it does not // 
			tables = dbmd.getTables(null, null, "options", null); 
			if(tables.next()){
				System.out.println("options table found in database");
			}else{
				System.out.println("options table was not found in database. Creating Table");
				String sql = "CREATE TABLE options " +
	                   "(id INTEGER not NULL, " +
	                   " name CHAR(255), " + 
	                   " votes INTEGER DEFAULT 0, " + 
	                   " pollID INTEGER not Null, " +
	                   " FOREIGN KEY (pollID) REFERENCES polls(id),"+
	                   " PRIMARY KEY ( id ))";
				Statement stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql);
			}
			// Checking if people table exists and creating if it does not // 
			tables = dbmd.getTables(null, null, "people", null); 
			if(tables.next()){
				System.out.println("people table found in database");
			}else{
				System.out.println("people table was not found in database. Creating Table");
				String sql = "CREATE TABLE people " +
	                   "(id INTEGER not NULL, " +
	                   " fname CHAR(255), " +
	                   " lname CHAR(255), " +
	                   " address1 CHAR(255), " +
	                   " address2 CHAR(255), " +
	                   " postal CHAR(255), " +
	                   " city CHAR(255), " +
	                   " state CHAR(255), " +
	                   " country CHAR(255), " +
	                   " idLegal INTEGER, " +
	                   " pollID INTEGER not Null, " +
	                   " FOREIGN KEY (pollID) REFERENCES polls(id),"+
	                   " PRIMARY KEY ( id ))";
				Statement stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql);
			}
		}catch(Exception e){
			System.out.println("Something went wrong initilizing database");
			System.out.println("Running without database enabled and default poll information");
			toggleDatabase();
		}
		
	}
	private static void mainMenu() {
		Scanner in = new Scanner(System.in);
		do{
			System.out.println("Welcome to Voting App Server Side");
			System.out.println("- To disable/enable Database access [db]");
			System.out.println("- To toggle debug messages [m]");
			System.out.println("- To view total Votes in database [t]");
			System.out.println("- To Stop Server [s]");
			String answer = in.nextLine();
			
			switch(answer){
			case "db":
				toggleDatabase();
				break;
			case "m":
				toggleDebug();
				break;
			case "t":
				viewVotes();
				break;
			case "s":
				stopServer();
				break; 
			default:
				System.out.println("Invalid Selection");
				break;
			}
		}while(!quit);
		in.close();
	}

	private static void stopServer() {
		System.out.println("Stopping Server");
		clientAcceptor.haltServer();
		quit = true;
	}
	private static void viewVotes() {
		// TODO Auto-generated method stub
		
	}
	private static void toggleDebug() {
		System.out.println("Debug messages set to " + !debug);
		if(debug){
			clientAcceptor.setDebug(!debug);
		}else{
			clientAcceptor.setDebug(!debug);
		}
		debug = !debug;
		
	}
	private static void toggleDatabase() {
		databaseToggle = !databaseToggle;
		

	}
	public static void startServer(int portNumber){
		// Starting the main Client Acceptor thread // 
		System.out.println("Starting Server on port " + portNumber);
		clientAcceptor = new ClientAcceptor(portNumber, SSLdebug); 
		Thread t1 = new Thread(clientAcceptor);
		t1.start();
	}
}
