package server;


import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

import client.Person;

public class ClientHandler extends Thread {
	private Socket clientSock; 
	private ObjectOutputStream outObj;
	private ObjectInputStream inObj;
	private Person person; 
	boolean debug = false;
	
	public ClientHandler(Socket sock, Boolean debug) {
		clientSock = sock; 
		this.debug = debug; 
	}
	public void run(){
		debug("Accepted Client. Address: " + clientSock.getInetAddress().getHostName());
		try{
			// Starting up connection // 
			outObj = new ObjectOutputStream(clientSock.getOutputStream()); 
			inObj = new ObjectInputStream(clientSock.getInputStream()); 
			
			// Receiving time from client // 
			String time = (String) inObj.readObject();
			debug("Client Says: " +  time);
			
			//Responding with active polls to client // 
			outObj.writeObject(activePolls(time));
			
			// Receiving Person from client and responding with validation results // 
			person = (Person) inObj.readObject(); 
			outObj.writeObject(validatePerson());
			
			// Receiving vote from client and responding with validation that the vote was cast // 
			String vote = (String) inObj.readObject();
			outObj.writeObject(castVote(vote));
			
		}catch(Exception e){
			debug("Client closed connection unexpectedly");
		}finally{
			debug("Closing Server Connection");
			try{
				outObj.close();
				clientSock.close();
			}catch(Exception e){
				debug("Failed to close socket");
			}
		}
	}
	private boolean castVote(String vote) {
		//TODO Record vote into database // 
		debug(person.getFname() + " cast a vote for " + vote);
		return true; 
	}
	private boolean validatePerson() {
		// TODO Validate person against database // 
		debug("Person Validated");
		return true;
	}
	private Poll activePolls(String time) {
		// TODO Get polls from Database // 
		LocalDateTime currentTime = LocalDateTime.parse(time);
		Poll aPoll = new Poll();
		if(aPoll.activeTime.isBefore(currentTime) && aPoll.inactiveTime.isAfter(currentTime)){
			debug("Active Poll found");
			return aPoll;
		}else{
			debug("No Active Polls at this time");
			return null;	
		}
	}
	private void debug(String msg){
		if(debug){
			System.out.println("[ClientHandler] " + msg);
		}
	}
	
}
