package server;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Poll implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id = 123323;
	LocalDateTime activeTime = LocalDateTime.parse("2018-08-28T09:00:00");
	LocalDateTime inactiveTime = LocalDateTime.parse("2018-09-29T21:00:00");
	private String question = "Who do you vote for President?"; 
	private String[] candidates = {"Donald Trump", "Hillary Clinton"};

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
}
