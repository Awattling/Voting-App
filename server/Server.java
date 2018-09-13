/* Author: Austin Wattling
 * Date: August 24, 2018
 */
package server;

import java.util.Scanner;

public class Server {
	protected static int portNumber = 4000;
	protected static boolean running = true;  
	protected static boolean quit = false;
	protected static boolean debug = false; 
	protected static boolean SSLdebug = false;
	protected static ClientAcceptor clientAcceptor; 
	protected static DatabaseManager dbma; 
	

	public static void main(String[] args){
		if(args.length == 1){
			SSLdebug = Boolean.parseBoolean(args[0]); 
			System.out.println("SSLDebug set to " + SSLdebug);
		}
		dbma = new DatabaseManager();
		startServer(portNumber);
		mainMenu();
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
		dbma.toggle();
	}
	
	public static void startServer(int portNumber){
		// Starting the main Client Acceptor thread // 
		System.out.println("Starting Server on port " + portNumber);
		clientAcceptor = new ClientAcceptor(portNumber, SSLdebug); 
		Thread t1 = new Thread(clientAcceptor);
		t1.start();
	}
}
