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
	private Poll poll;
	boolean debug = false;
	private DatabaseManager dbma; 
	
	public ClientHandler(Socket sock, Boolean debug, DatabaseManager dbma) {
		clientSock = sock; 
		this.debug = debug; 
		this.dbma = dbma;
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
		
		debug(person.getFname() + " cast a vote for " + vote);
		return dbma.castVote(vote, person);
	}
	private boolean validatePerson() {
		debug("validating Person");
		return dbma.validatePerson(person,poll);
	}
	private Poll activePolls(String time) {
		poll = dbma.getPoll();
		// Check to see if Database Manager found anything for us // 
		if(poll == null){
			debug("Database Manager did not return a poll");
			return null;
		}
		LocalDateTime currentTime = LocalDateTime.parse(time);
		if(poll.getActiveTime().isBefore(currentTime) && poll.getInActiveTime().isAfter(currentTime)){
			debug("Active Poll found");
			return poll;
		}else{
			debug("Poll Found but is not active at this time");
			return null;	
		}
	}
	private void debug(String msg){
		if(debug){
			System.out.println("[ClientHandler] " + msg);
		}
	}
	
}
