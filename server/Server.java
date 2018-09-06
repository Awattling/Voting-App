/* Author: Austin Wattling
 * Date: August 24, 2018
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	protected static int portNumber = 4000;
	protected static boolean running = true; 
	protected static ServerSocket serveSocket; 
	
	public static void main(String[] args){
		try {
			serveSocket = new ServerSocket(portNumber);
			System.out.println("Server started on port " + portNumber);
		} catch (IOException e) {
			System.out.println("Server unable to start on port " + portNumber + ". Quitting");
			System.exit(-1);
		}
		
		while(running){
			try{
				// Starts a new ClientServer thread to handle each client and continues to listen for new clients. // 
				Socket clientSocket = serveSocket.accept();
				ClientServerThread helper = new ClientServerThread(clientSocket);
				helper.start();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}

}
