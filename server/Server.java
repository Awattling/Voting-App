/* Author: Austin Wattling
 * Date: August 24, 2018
 */
package server;

import java.util.Scanner;

public class Server {
	protected static int portNumber = 4000;
	protected static boolean running = true;  
	protected static boolean quit = false;
	static ClientAcceptor clientAcceptor; 
	public static void main(String[] args){
		startServer();
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
				databaseToggle();
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
		// TODO Auto-generated method stub
		
	}
	private static void databaseToggle() {
		// TODO Auto-generated method stub
		
	}
	public static void startServer(){
		// Starting the main Client Acceptor thread // 
		clientAcceptor = new ClientAcceptor(portNumber);
		Thread t1 = new Thread(clientAcceptor);
		t1.start();
	}
}
