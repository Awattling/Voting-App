package server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import client.Person;

public class DatabaseManager {
	protected static String databaseLocal = "jdbc:mysql://localhost:3306/voting_app"; 
	protected static String databaseUser = "root"; 
	protected static String databasePass = "password"; 
	protected static Connection con = null; 
	protected boolean databaseToggle = true; 
	
	public DatabaseManager(){
		initilizeDatabase();
		populateDatabase();
	}
	
	public void initilizeDatabase(){
		try {
			 // Registering the SQL driver // 
		    Class.forName("com.mysql.jdbc.Driver");
		    // Connecting to database // 
		    con = DriverManager.getConnection(databaseLocal, databaseUser, databasePass);
			System.out.println("[DatabaseManager] Database connected");
			// Getting database meta data for table checks // 
			DatabaseMetaData dbmd = con.getMetaData();
			
			
			// Checking if polls table exists and creating if it does not // 
			ResultSet tables = dbmd.getTables(null, null, "polls", null); 
			if(tables.next()){
				System.out.println("[DatabaseManager] polls table found in database");
			}else{
				System.out.println("[DatabaseManager] polls table was not found in database. Creating Table");
				String sql = "CREATE TABLE POLLS " +
	                   "(id INTEGER not NULL, " +
	                   " active DATETIME, " + 
	                   " inActive DATETIME, " + 
	                   " question CHAR(255), " + 
	                   " PRIMARY KEY ( id ))";
				Statement stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql);
			}
			// Checking if options table exists and creating if it does not // 
			tables = dbmd.getTables(null, null, "options", null); 
			if(tables.next()){
				System.out.println("[DatabaseManager] options table found in database");
			}else{
				System.out.println("[DatabaseManager] options table was not found in database. Creating Table");
				String sql = "CREATE TABLE options " +
	                   "(id INTEGER not NULL, " +
	                   " name CHAR(255), " + 
	                   " votes INTEGER DEFAULT 0, " + 
	                   " pollID INTEGER not Null, " +
	                   " FOREIGN KEY (pollID) REFERENCES polls(id),"+
	                   " PRIMARY KEY ( id ))";
				Statement stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql);
			}
			// Checking if people table exists and creating if it does not // 
			tables = dbmd.getTables(null, null, "people", null); 
			if(tables.next()){
				System.out.println("[DatabaseManager] people table found in database");
			}else{
				System.out.println("[DatabaseManager] people table was not found in database. Creating Table");
				String sql = "CREATE TABLE people " +
	                   "(id INTEGER not NULL, " +
	                   " fname CHAR(255), " +
	                   " lname CHAR(255), " +
	                   " address1 CHAR(255), " +
	                   " address2 CHAR(255), " +
	                   " postal CHAR(255), " +
	                   " city CHAR(255), " +
	                   " state CHAR(255), " +
	                   " country CHAR(255), " +
	                   " pollID INTEGER not Null, " +
	                   " FOREIGN KEY (pollID) REFERENCES polls(id),"+
	                   " PRIMARY KEY ( id ))";
				Statement stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql);
			}
		}catch(Exception e){
			System.out.println("[DatabaseManager] Something went wrong initilizing database");
			System.out.println("[DatabaseManager] Running without database enabled and default poll information");
			try {
				con.close();
			} catch (SQLException e1) {
				// Nothing to worry about. Error closing database connector// 
			}
			con = null;
			databaseToggle = !databaseToggle; 
		}
		
	}
	
	public void populateDatabase(){
		ResultSet rs;
		Statement stmt;
		String sql;
		System.out.println("[DatabaseManager] Ensuring Database entries exist");
		try {
			// Checking to see if a poll exists in database // 
			sql = "SELECT * from polls";
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery(sql); 
			if(rs.next() == false){
				System.out.println("[DatabaseManager] Adding Default 2016 Election poll to database");
				// If nothing Exists fill in with default 2016 Poll // 
				sql = "INSERT INTO polls (id, active, inactive, question)" +
				"VALUES ('2016', '2016-01-01T10:00:00', '2032-01-01T10:00:00', 'Who do you vote to be the next president of the United States?')";
				stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql); 
			}
			// Checking to see if candidate options exist in database // 
			sql = "SELECT * from options";
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery(sql); 
			if(rs.next() == false){
				System.out.println("[DatabaseManager] Adding Default 2016 Election poll options to database");
				// If nothing Exists fill in with default 2016 Poll // 
				sql = "INSERT INTO options (id, name, pollID)" +
				"VALUES ('1', 'Donald Trump', '2016'), ('2', 'Hillary Clinton', '2016'), ('3', 'Austin Wattling', '2016')";
				stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql); 
			}
		} catch (Exception e) {
			System.out.println("[DatabaseManager] Something failed adding default poll to database");
		}
		
	}

	public void toggle() {
		databaseToggle = !databaseToggle;
		System.out.println("Setting Database enabled to " + databaseToggle);
		if(databaseToggle){
			initilizeDatabase();
		}
		
	}

	public Poll getPoll() {
		ResultSet rs;
		Statement stmt;
		String sql;
		Poll poll = new Poll();
		// Make Sure database operation is enabled // 
		if(databaseToggle){
			try {
				sql = "SELECT * from polls";
				stmt = (Statement) con.createStatement();
				rs = stmt.executeQuery(sql); 
				// Check to make sure we got something
				if(!rs.next()){
					return null;
				}
				//Right now take the first thing we get and cast it into the poll object //
				// TODO build a link list of polls so we can utilize multiple polls at once //
				poll.setId(rs.getInt("id"));
				poll.setQuestion(rs.getString("question"));
				poll.setActiveTime(rs.getTimestamp("active").toLocalDateTime());
				poll.setInActiveTime(rs.getTimestamp("inActive").toLocalDateTime());
				// Get corresponding candidates // 
				sql = "SELECT * from options WHERE pollID=" + rs.getInt("id");
				stmt = (Statement) con.createStatement();
				rs = stmt.executeQuery(sql); 
				// Put them into array and give to the poll for later use // 
				ArrayList<String> candidates = new ArrayList<String>();
				while(rs.next()){
					candidates.add(rs.getString("name"));
				}
				String[] candidatesArr = new String[candidates.size()];
				candidatesArr = candidates.toArray(candidatesArr);
				poll.setCandidates(candidatesArr);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poll;
	}

	public boolean validatePerson(Person person, Poll poll) {
		ResultSet rs;
		Statement stmt;
		String sql;
		
		// Check to see if database operation is enabled // 
		if(databaseToggle){
			try {
				// Check to make sure we got something // 
				if(person == null){
					return false;
				}
				sql = "SELECT * from PEOPLE WHERE id=" + person.getId();
				stmt = (Statement) con.createStatement();
				rs = stmt.executeQuery(sql); 
				// Check to see if something with the same ID lives in database //
				// For now if its a unique ID accept it and add it to the database// 
				if(!rs.next()){
					sql = "INSERT INTO people (id, fname, lname, address1, address2, postal, city, state, country, pollID)" +
					"VALUES (' "+ person.getId() +"', '"+ person.getFname() +"', '"+ person.getLname() +"', '"+ person.getAdd1() +"', '"+ person.getAdd2() + "', '"+ person.getPost() +"', '"+ person.getCity() +"', '"+ person.getProv() +"', '"+ person.getCount()  +"', '"+ poll.getID() + "')" ;
					stmt = (Statement) con.createStatement();
					stmt.executeUpdate(sql); 
					return true;
				}
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Something went wrong entering a person into the database. Make sure all fields filled out match whats expected in the database");
			}
		return false;
		}
		return true; 
	}

	public boolean castVote(String vote, Person person) {
		Statement stmt;
		String sql;
		// Do something only if database is enabled // 
		if(databaseToggle){
			try {
				// Cast Vote //
				sql = "UPDATE options SET votes = votes + 1 WHERE name='" + vote + "'";
				stmt = (Statement) con.createStatement();
				stmt.executeUpdate(sql); 
				return true;
			} catch (SQLException e) {
				try{
					// In the event the vote did not work remove person from db so they can vote later // 
					sql = "DELETE FROM people WHERE id=" + person.getId();
					stmt = (Statement) con.createStatement();
					stmt.executeUpdate(sql);
					System.out.println("Somethng went wrong adding votes to database Try again later");
				}catch(Exception e1){
					System.out.println("Something went very wrong adding votes to database. Contact advisor");
				}
				return false; 
			}
		}else{
			return true;
		}
	}

	public void viewVotes() {
		if(databaseToggle){
			try{
				ResultSet rs;
				Statement stmt;
				String sql;
				sql = "SELECT * from polls";
				stmt = (Statement) con.createStatement();
				rs = stmt.executeQuery(sql); 
				// Check to make sure we got something
				if(!rs.next()){
					System.out.println("[DatabaseManager] No polls exist at this time");
				}else{
					do{
						// Make poll // 
						Poll poll = new Poll();
						poll.setId(rs.getInt("id"));
						poll.setQuestion(rs.getString("question"));
						// Get corresponding candidates // 
						sql = "SELECT * from options WHERE pollID=" + rs.getInt("id");
						stmt = (Statement) con.createStatement();
						ResultSet rs2 = stmt.executeQuery(sql); 
						
						// Put candidates into array and vote numbers into array // 
						ArrayList<String> candidates = new ArrayList<String>();
						ArrayList<Integer> votes = new ArrayList<Integer>();
						while(rs2.next()){
							candidates.add(rs2.getString("name"));
							votes.add(rs2.getInt("votes"));
						}
						// Make array of candidates //
						String[] candidatesArr = new String[candidates.size()];
						candidatesArr = candidates.toArray(candidatesArr);
						poll.setCandidates(candidatesArr);
						// Make array of votes corresponding to candidates // 
						Integer[] votesArr = new Integer[votes.size()];
						votesArr = votes.toArray(votesArr);
						printPollHelper(poll, votesArr);
					}while(rs.next());
				}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("[DatabaseManager] Somethng went wrong getting votes from database");
			}
		}else{
			System.out.println("[DatabaseManager] Database is not enabled so no votes are being recorded");
		}
		
	}

	private void printPollHelper(Poll poll, Integer[] votesArr) {
		System.out.println(poll.getQuestion());
		for(int x = 0; x < poll.getCandidates().length; x++){
			System.out.println("-- " + poll.getCandidates()[x] + " Votes: " + votesArr[x]);
		}
		System.out.println("\n");
	}
}
