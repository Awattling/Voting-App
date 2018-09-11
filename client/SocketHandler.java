/**
 * 
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.time.LocalDateTime;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.sun.net.ssl.internal.ssl.Provider;

import server.Poll;

/**
 * @author Austin Wattling
 *
 */
public class SocketHandler {
	protected ObjectInputStream objIn;
	protected ObjectOutputStream objOut;
	protected final String host = "localhost"; 
	protected final int port = 4000; 
	protected Socket clientSocket; 
	protected Views window; 
	
	public void setup(){
		try{
			// Establishing Connection to Server // 
			establishConnection();
			// Getting Active polls from the Server // 
			getPolls();
			
		}catch(IOException e){
			e.printStackTrace();
			window.loading_view(false, "Error: Server Connection Could not be Established. Try again later", true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// Starting method needed to get the connection to server up and running // 
	private void establishConnection() throws UnknownHostException, IOException, ClassNotFoundException, KeyManagementException, NoSuchAlgorithmException{
		// Showing loading screen view // 
		window.loading_view(true, "Attempting Connection to Server", false);
		
		// Attempting connection to client // 
		
		// Adding JSSE provider for client encryption functionality.// 
		Security.addProvider(new Provider());
		// Specifying the trust store file which contains the certificate of the public server. // 
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
		// Specifying the password required to access the trust store // 
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		// Setting property to output SSL information --optional // 
		//System.setProperty("javax.net.debug", "all");
		
		
		SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault(); 
		SSLSocket sslClientSocket = (SSLSocket)factory.createSocket(host, port); 
		//sslClientSocket.startHandshake();
		
		objIn = new ObjectInputStream(sslClientSocket.getInputStream());
		objOut = new ObjectOutputStream(sslClientSocket.getOutputStream());		
		
		// Sharing connection Successful with User // 	
		window.loading_view(true, "Server Connection Established", false);	
	}
	private void getPolls() throws IOException, ClassNotFoundException {
		// Using window to notify user // 
		window.loading_view(true, "Askng for open polls and registrations", false);
		// Writing time to connection // 
		objOut.writeObject((getTime().toString()));
		Poll poll = (Poll)objIn.readObject();
		if(poll == null){
			window.loading_view(false, "No Active Polls at this time. Please Try again Later", true);
		}else{
			window.setPoll(poll); 
			window.registration_view();
		}
	}
	private LocalDateTime getTime(){
		LocalDateTime now = LocalDateTime.now();  
		return now;  
	}
	
	public void setWindow(Views window) {
		this.window = window; 
	}

	public void submitRegistrationToServer(Person person) {
		try {
			// Writing person to connection // 
			objOut.writeObject(person);
			boolean valid = (boolean)objIn.readObject();
			if(valid){
				window.voting_view();
			}else{
				System.out.println("Registration Invalid");
			}
		} catch (IOException e) {
			window.loading_view(false, "Server Connection could not be Established. Please Restart Application", true);	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void terminateConnection(){
		try {
			objIn.close();
			objOut.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Something went wrong shutting down the connection");
		}
	}

	public void collectVote(String vote) {
		try {
			objOut.writeObject(vote);
			boolean valid = (boolean)objIn.readObject();
			if(valid){
				window.loading_view(false, "Vote Sucessfully Cast", true );
			}else{
				window.loading_view(false, "Vote could not be cast at this time. Try again Later", true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			window.loading_view(false, "Server Connection could not be Established. Please Restart Application", true);	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
