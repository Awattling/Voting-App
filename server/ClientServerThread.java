package server;


import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

import client.Person;

public class ClientServerThread extends Thread {
	private Socket clientSock; 
	//private BufferedReader in;
	//private DataOutputStream outData;
	private ObjectOutputStream outObj;
	private ObjectInputStream inObj;
	private boolean running = true;
	public ClientServerThread(Socket sock) {
		clientSock = sock; 
	}
	public void run(){
		System.out.println("Accepted Client. Address: " + clientSock.getInetAddress().getHostName());
		try{
			outObj = new ObjectOutputStream(clientSock.getOutputStream()); 
			inObj = new ObjectInputStream(clientSock.getInputStream()); 
			
			
			while(running){
				String time = (String) inObj.readObject();
				System.out.println("Client Says: " +  time);
				outObj.writeObject(activePolls(time));
				Person person = (Person) inObj.readObject(); 
				System.out.println("Client Says: " +  person.getFname());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("Closing Connection");
			try{
				outObj.close();
				clientSock.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	private Poll activePolls(String time) {
		// TODO Get polls from Database // 
		LocalDateTime currentTime = LocalDateTime.parse(time);
		Poll aPoll = new Poll();
		if(aPoll.activeTime.isBefore(currentTime) && aPoll.inactiveTime.isAfter(currentTime)){
			System.out.println("Active Poll found");
			return aPoll;
		}else{
			System.out.println("No Active Polls at this time");
			return null;	
		}
	}
	
}
