/**
 * 
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

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
			establishConnection();
			getPolls();
			
		}catch(IOException e){
			window.loading_view(false, "Error: Server Connection Could not be Established. Try again later");
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void getPolls() throws IOException, ClassNotFoundException {
		window.loading_view(true, "Askng for open polls and registrations");
		objOut.writeObject((getTime().toString()));
		Poll poll = (Poll)objIn.readObject();
		if(poll == null){
			window.loading_view(false, "No Active Polls at this time. Please Try again Later");
		}else{
			window.setPoll(poll); 
			window.registration_view();
		}
	}

	// Starting method needed to get the connection to server up and running // 
	private void establishConnection() throws UnknownHostException, IOException, ClassNotFoundException{
		// Showing loading screen view // 
		window.loading_view(true, "Attempting Connection to Server");
		// Attempting connection to client // 
		clientSocket = new Socket(host , port);
		objIn = new ObjectInputStream(clientSocket.getInputStream());
		objOut = new ObjectOutputStream(clientSocket.getOutputStream());
		// Sharing connection Successful with User // 	
		window.loading_view(true, "Server Connection Established");	
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
			objOut.writeObject(person);
			boolean valid = (boolean)objIn.readObject();
			if(valid){
				window.voting_view();
			}else{
				System.out.println("Registartion Invalid");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			window.loading_view(false, "Server Connection could not be Established. Please Restart Application");	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
				window.loading_view(false, "Vote Sucessfully Cast" );
			}else{
				window.loading_view(false, "Vote could not be cast at this time. Try again Later" );
			}
			Thread.sleep(5000);
			setup();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			window.loading_view(false, "Server Connection could not be Established. Please Restart Application");	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
