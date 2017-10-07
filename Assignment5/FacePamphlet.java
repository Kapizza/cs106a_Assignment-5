/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	// Instance variables:
	private JLabel name;
	private JButton add;
	private JButton delete;
	private JButton lookUp;
	private JButton changestat;
	private JButton changepic;
	private JButton addFriend;
	private JTextField field;
	private JTextField field1;
	private JTextField field2;
	private JTextField field3;
	private FacePamphletDatabase dat = new FacePamphletDatabase();
	private FacePamphletProfile Profile = null;
	private FacePamphletCanvas canvas;

	/**
	 * This method has the responsibility for initializing the interactors in
	 * the application, and taking care of any other initialization that needs
	 * to be performed.
	 */
	public void init() {
		// Adding interactors
		southSide();
		westSide();
		field1.addActionListener(this);
		field2.addActionListener(this);
		field3.addActionListener(this);
		addActionListeners();
		canvas = new FacePamphletCanvas();
		add(canvas);

	}

	// This method adds interactors on south side of canvas.
	public void southSide() {
		// Label Name
		name = new JLabel("Name");
		add(name, NORTH);
		// Text field for add, delete and lookup buttons
		field = new JTextField(TEXT_FIELD_SIZE);
		add(field, NORTH);
		// Buttons
		add = new JButton("Add");
		add(add, NORTH);
		delete = new JButton("Delete");
		add(delete, NORTH);
		lookUp = new JButton("Lookup");
		add(lookUp, NORTH);
	}

	// This method adds interactors on weat side of canvas.
	public void westSide() {
		changestat = new JButton("Change Status");
		add(changestat, WEST);
		field1 = new JTextField(TEXT_FIELD_SIZE);
		add(field1, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		changepic = new JButton("Change Picture");
		add(changepic, WEST);
		field2 = new JTextField(TEXT_FIELD_SIZE);
		add(field2, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		addFriend = new JButton("Add Friend");
		add(addFriend, WEST);
		field3 = new JTextField(TEXT_FIELD_SIZE);
		add(field3, WEST);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == field1
				|| e.getActionCommand().equals("Change Status")) {
			String text = field1.getText();
			if (Profile != null) {
				Profile.setStatus(text);
				dat.getProfile(Profile.getName());
				Profile.setStatus(Profile.getName() + " is " + text);
				canvas.displayProfile(Profile);
				canvas.showMessage("Status updated to " + text);
			} else {
				canvas.showMessage("Please select a profile to change status");
			}

		}
		if (e.getSource() == field2
				|| e.getActionCommand().equals("Change Picture")) {
			String text = field2.getText();
			if (Profile != null) {
				GImage image = null;
				try {
					image = new GImage(text);
					Profile.setImage(image);
					canvas.displayProfile(Profile);
				} catch (ErrorException ex) {
					canvas.showMessage("Unable to open image file: " + text);
				}

			} else {
				canvas.showMessage("Please select a profile to change picture");
			}
		}

		if (e.getSource() == field3
				|| e.getActionCommand().equals("Add Friend")) {
			String text = field3.getText();
			if (Profile != null) {
				if (Profile.addFriend(text)) {
					// addfriend
				} else {
					canvas.showMessage(Profile.getName() + " already has "
							+ text + "as a friend.");
				}

			} else {
				canvas.showMessage("Please select a profile to add friend");
			}
		}

		if (!field.getText().equals("")) {
			if (e.getActionCommand().equals("Add")) {
				String text = field.getText();
				if (dat.containsProfile(text) == false) {
					FacePamphletProfile profile = new FacePamphletProfile(text);
					dat.addProfile(profile);
					Profile = profile;
					canvas.showMessage("New profile created");
				} else {

					canvas.showMessage("A profile with the name " + text
							+ " already exists");
				}
			}
		}
		if (!field.getText().equals("")) {
			if (e.getActionCommand().equals("Delete")) {
				String text = field.getText();
				if (dat.containsProfile(text)) {
					dat.deleteProfile(text);
					Profile = null;
					canvas.removeAll();
					canvas.showMessage("Profile of " + text + " deleted");
				} else {
					canvas.showMessage("A profile with the name " + text
							+ " already exists");
				}
			}
		}

		if (!field.getText().equals("")) {
			if (e.getActionCommand().equals("Lookup")) {
				String text = field.getText();
				if (dat.containsProfile(text)) {
					FacePamphletProfile profile = dat.getProfile(text);
					canvas.displayProfile(profile);
					canvas.showMessage("Displaying " + text);
					Profile = dat.getProfile(text);
					canvas.showMessage("Displaying " + text);
				} else {
					canvas.showMessage("A profile with the name " + text
							+ " already exists");
				}
			}
		}
	}

}
