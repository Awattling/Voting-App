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

import javax.swing.JLabel;

import server.Poll;

/**
 * @author Austin Wattling
 *
 */
public class SocketHandler {
	//protected DataOutputStream out;
	//protected BufferedReader in;
	protected ObjectInputStream objIn;
	protected ObjectOutputStream objOut;
	protected final String host = "localhost"; 
	protected final int port = 4000; 
	protected Socket clientSocket; 
	protected Views window; 
	
	
	public void setup(){
		try{
			establishConnection();
			Poll aPoll = getPolls();
			if(aPoll == null){
				window.loading_view(false);
				JLabel lbl = (JLabel)window.panel.getComponent(0);
				lbl.setText("No Active Polls at this time. Please Try again Later");
			}else{
				window.registration_view();
			}
		}catch(IOException e){
			window.loading_view(false);
			JLabel lbl = (JLabel)window.panel.getComponent(0);
			lbl.setText("Error: Server Connection Could not be Established. Try again later");
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Poll getPolls() throws IOException, ClassNotFoundException {
		window.loading_view(true);
		JLabel lbl = (JLabel)window.panel.getComponent(0);
		objOut.writeObject((getTime().toString()));
		lbl.setText("Askng for open polls and registrations");
		Poll apoll = (Poll)objIn.readObject(); 
		return apoll;
	}

	// Starting method needed to get the connection to server up and running // 
	private void establishConnection() throws UnknownHostException, IOException, ClassNotFoundException{
		// Showing loading screen view // 
		window.loading_view(true);
		JLabel lbl = (JLabel)window.panel.getComponent(0);
		lbl.setText("Attempting Connection to Server");
		
		// Attempting connection to client // 
		clientSocket = new Socket(host , port);
		//out = new DataOutputStream(clientSocket.getOutputStream());
		//in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		objIn = new ObjectInputStream(clientSocket.getInputStream());
		objOut = new ObjectOutputStream(clientSocket.getOutputStream());
		// Sharing connection Successful with User // 	
		lbl.setText("Server Connection Established");		
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void terminateConnection(){
		try {
			//in.close();
			//out.close();
			objIn.close();
			objOut.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Something went wrong shutting down the connection");
		}
	}
}
