package client;

import java.awt.*;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.Border;

import server.Poll;

public class Views implements Runnable {
	JFrame frame = new JFrame("Voting App");
	JPanel panel = new JPanel();
	SocketHandler handle; 
	Poll poll; 
	
	public void run(){
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(panel);
	}
	
	// Method to clear the panel before repainting it // 
	public void resetPanel(){
		panel.removeAll();
		panel.revalidate();
		panel.setCursor(Cursor.getDefaultCursor());
	}
	
	// Method to display a loading type of view. 
		// Boolean to show active loading // 
		// String to show message to user // 
		// Boolean to add in a restart button for some use cases // 
	public void loading_view(boolean active, String message, boolean restart){
		resetPanel();
		// Creating label element for this view // 
		JLabel status = new JLabel(message);
		
		// Adding elements in gridbagLayout. // 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(status, gbc);
		
		// Elements to show active loading// 
		if(active){
			JProgressBar progress = new JProgressBar();
			progress.setIndeterminate(true);
			gbc.gridx = 0;
			gbc.gridy = 1;
			panel.add(progress, gbc);
			// Setting cursor to imply loading // 
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		if(restart){
			// Submit Button //
			JButton restartBtn = new JButton("Restart");
			
			// Since Java 8 we can use a lambda closure to make this much prettier//
			restartBtn.addActionListener(e -> Client.initilizing());
			
			restartBtn.setBackground(Color.green);
			restartBtn.setOpaque(true);
			gbc.gridwidth = 2;
			gbc.gridx = 0;
			gbc.gridy = 2;
			panel.add(restartBtn, gbc);
		}
		// Refreshing the display of the panel with added elements//
		panel.revalidate();
		panel.repaint();
	}
	
	// Registration View // 
	public void registration_view(){
		resetPanel();
		
		// Setting up gridbag layout// 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// First name // 
		JLabel fnamelbl = new JLabel("Legal First Name:");
		JTextField fname = new JTextField(30);
		fname.setName("fname");
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(fnamelbl, gbc);
		gbc.gridx = 1;
		panel.add(fname, gbc);
		
		// Last name // 
		JLabel lnamelbl = new JLabel("Legal Last Name:");
		JTextField lname = new JTextField(30);
		lname.setName("lname");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(lnamelbl, gbc);
		gbc.gridx = 1;
		panel.add(lname, gbc);
		
		// Address line 1 //
		JLabel add1lbl = new JLabel("Address Line 1:");
		JTextField add1 = new JTextField(30);
		add1.setName("add1");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(add1lbl, gbc);
		gbc.gridx = 1;
		panel.add(add1, gbc);
		
		// Address Line 2 // 
		JLabel add2lbl = new JLabel("Address Line 2:");
		JTextField add2 = new JTextField(30);
		add2.setName("add2");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(add2lbl, gbc);
		gbc.gridx = 1;
		panel.add(add2, gbc);
		
		// Postal Code // 
		JLabel postlbl = new JLabel("Postal Code:");
		JTextField post = new JTextField(30);
		post.setName("post");
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(postlbl, gbc);
		gbc.gridx = 1;
		panel.add(post, gbc);
		
		// City // 
		JLabel citylbl = new JLabel("City:");
		JTextField city = new JTextField(30);
		city.setName("city");
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(citylbl, gbc);
		gbc.gridx = 1;
		panel.add(city, gbc);
		
		// Province or state // 
		JLabel provlbl = new JLabel("Province / State:");
		JTextField prov = new JTextField(30);
		prov.setName("prov");
		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(provlbl, gbc);
		gbc.gridx = 1;
		panel.add(prov, gbc);
		
		// Country // 
		JLabel countlbl = new JLabel("Country:");
		JTextField count = new JTextField(30);
		count.setName("count");
		gbc.gridx = 0;
		gbc.gridy = 7;
		panel.add(countlbl, gbc);
		gbc.gridx = 1;
		panel.add(count, gbc);
		
		// ID // 
		JLabel IDlbl = new JLabel("ID Number:");
		JTextField ID = new JTextField(30);
		ID.setName("ID");
		gbc.gridx = 0;
		gbc.gridy = 8;
		panel.add(IDlbl, gbc);
		gbc.gridx = 1;
		panel.add(ID, gbc);
		
		// Submit Button //
		JButton submit = new JButton("Submit");
		
		// Since Java 8 we can use a lambda closure to make this much prettier//
		submit.addActionListener(e -> validateRegistration());
		
		submit.setBackground(Color.green);
		submit.setOpaque(true);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 9;
		panel.add(submit, gbc);
		
		// Refreshing the display of the panel with added elements//
		panel.revalidate();
		panel.repaint();
	}
	
	// Method to ensure all the fields in the registration view are filled out// 
	// If they are not the field that is not filled out will be highlighted for the user // 
	public void validateRegistration(){
		boolean allFull = true;
		// Checks to see if forms have an input before submitting to server to see if valid // 
		for(Component comp : panel.getComponents()){
			if(comp instanceof JTextField){
				if (((JTextField)comp).getText().isEmpty()){
					Border border = BorderFactory.createLineBorder(Color.red);
					((JTextField) comp).setBorder(border);
					allFull = false;
				}else{
					Border border = BorderFactory.createLineBorder(Color.gray);
					((JTextField) comp).setBorder(border);
				}
			}
		}
		if(allFull == true){
			Person person = new Person();
			for(Component comp : panel.getComponents()){
				if(comp instanceof JTextField){
					switch(comp.getName()){
						case "fname" :
							person.setFname(((JTextField) comp).getText());
							break; 
						case "lname" :
							person.setLname(((JTextField) comp).getText());
							break; 
						case "add1" :
							person.setAdd1(((JTextField) comp).getText());
							break; 
						case "add2" :
							person.setAdd2(((JTextField) comp).getText());
							break; 
						case "post" :
							person.setPost(((JTextField) comp).getText());
							break; 
						case "city" :
							person.setCity(((JTextField) comp).getText());
							break; 
						case "prov" :
							person.setProv(((JTextField) comp).getText());
							break; 
						case "count" :
							person.setCount(((JTextField) comp).getText());
							break; 
						case "ID" :
							person.setId(((JTextField) comp).getText());
							break; 
						default:
							System.out.println("Form field type not found in person object switch cases");
							break;
					}
				}
			}
			handle.submitRegistrationToServer(person);
		}
	}

	public void setSocketHandler(SocketHandler handle) {
		this.handle = handle;
	}

	// View that uses the poll given to the view to make a poll view // 
	public void voting_view() {
		resetPanel();
		
		// Setting up gridbag layout// 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// Question // 
		JLabel question = new JLabel(poll.getQuestion());
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(question, gbc);
		
		String[] candidates = poll.getCandidates(); 
		ButtonGroup bg = new ButtonGroup();
		for(int x = 0; x < candidates.length; x++){
			
			//Create the radio buttons.
	        JRadioButton radio = new JRadioButton(candidates[x]);
	        bg.add(radio);
			gbc.gridx = 0;
			gbc.gridy = x+1;
			panel.add(radio, gbc);
		}
		
		// Submit Button //
		JButton submit = new JButton("Submit");
		
		// Since Java 8 we can use a lambda closure to make this much prettier//
		submit.addActionListener(e -> confirm_vote_helper(bg));
		
		submit.setBackground(Color.green);
		submit.setOpaque(true);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = candidates.length + 2;
		panel.add(submit, gbc);
		
		// Refreshing the display of the panel with added elements//
		panel.revalidate();
		panel.repaint();
	}

	// Method used to pull the value for the selected candidate in voting view // 
	private void confirm_vote_helper(ButtonGroup bg) {
		boolean voted = false; 
		for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
	       AbstractButton button = (AbstractButton) buttons.nextElement();
	       if (button.isSelected()) {
	       		voted = true;
	       		confirmation_view (button.getText());
	        }
	     }
		 if(voted == false){
			 confirmation_view(null);
		 }
	}
	
	// Asks the user to confirm their vote before submitting // 
	public void confirmation_view(String vote){
		resetPanel();
		System.out.println(vote);
		
		// Setting up gridbag layout // 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// Creating label elements for this view // 
		JLabel message; 
		if(vote == null){
			message = new JLabel("You have chosen to throw away your ballot");
		}else{
			message = new JLabel("You have chosen to cast a vote for " + vote);
		}
		JLabel question = new JLabel("Are you sure you want to do this?"); 
		
		// Adding labels to view // 
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(message, gbc);
		gbc.gridy = 1;
		panel.add(question, gbc);
		
		// Yes Button //
		JButton yes = new JButton("Yes");
		
		// Since Java 8 we can use a lambda closure to make this much prettier//
		yes.addActionListener(e -> handle.collectVote(vote));
		
		yes.setBackground(Color.green);
		yes.setOpaque(true);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(yes, gbc);

		// NO Button //
		JButton no = new JButton("No");
		
		// Since Java 8 we can use a lambda closure to make this much prettier//
		no.addActionListener(e -> voting_view());
		
		no.setBackground(Color.red);
		no.setOpaque(true);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(no, gbc);

		// Refreshing the display of the panel with added elements//
		panel.revalidate();
		panel.repaint();
	}
	
	public void setPoll(Poll poll) {
		this.poll = poll;		
	}
}
