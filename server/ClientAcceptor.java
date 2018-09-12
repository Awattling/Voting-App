package server;

import java.io.IOException;
import java.net.Socket;
import java.security.Security;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import com.sun.net.ssl.internal.ssl.Provider;

public class ClientAcceptor implements Runnable {
	int portNumber;
	boolean running = true;
	SSLServerSocket serveSocket;
	
	public ClientAcceptor(int portnumber){
		this.portNumber = portnumber;
	}
	public void run() {
		
		try {
			// Adding JSSE provider for server encryption functionality.// 
			Security.addProvider(new Provider());
			// Specifying the key store file which contains our public key. //
			System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks"); 
			// Entering the password required to access our public key // 
			System.setProperty("javax.net.ssl.keyStorePassword", "password");
			// Showing the details for debug purposes -- Optional //
			//System.setProperty("javax.net.debug", "all");
			
			// Establishes the SSL context. // 
			SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault(); 
			// Creates SSL Socket // 
			serveSocket = (SSLServerSocket)factory.createServerSocket(portNumber);
			
			System.out.println("Server started on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server unable to start on port " + portNumber + ". Quitting");
			System.exit(-1);
		}
		while(running){
			try{
				// Starts a new ClientServer thread to handle each client and continues to listen for new clients. // 
				Socket clientSocket = serveSocket.accept();
				ClientHandler helper = new ClientHandler(clientSocket);
				helper.start();
			}catch(Exception e){
				// Probably means socket was closed // 
			}
			
		}
		
	}
	public void haltServer(){
		running = false;
		try {
			serveSocket.close();
		} catch (IOException e) {
			//Nothing to worry about // 
		}
	}
}
