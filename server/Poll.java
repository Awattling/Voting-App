package server;


import java.io.Serializable;
import java.time.LocalDateTime;

public class Poll implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id = 123323;
	private LocalDateTime activeTime = LocalDateTime.parse("2015-08-28T09:00:00");
	private LocalDateTime inActiveTime = LocalDateTime.parse("2030-09-29T21:00:00");
	private String question = "Default Question"; 
	private String[] candidates = {"Default Poll Option 1", "Default Poll Option 2"};

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String[] getCandidates() {
		return candidates;
	}

	public void setCandidates(String[] candidates) {
		this.candidates = candidates;
	}

	public void setActiveTime(LocalDateTime activeTime) {
		this.activeTime = activeTime;
	}

	public void setInActiveTime(LocalDateTime inActiveTime) {
		this.inActiveTime = inActiveTime;
	}
	public LocalDateTime getActiveTime() {
		return activeTime;
	}

	public LocalDateTime getInActiveTime() {
		return inActiveTime;
	}
	
	public void setId(int id){
		this.id = id; 
	}
}
