/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements
		FacePamphletConstants {

	// Instance variables
	private String name = "";
	private double oldx = 0;
	private double oldy = 0;

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	/**
	 * This method displays a message string near the bottom of the canvas.
	 * Every time this method is called, the previously displayed message (if
	 * any) is replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		GLabel message = new GLabel(msg);
		double x = getWidth() / 2 - message.getWidth() / 2;
		double y = getHeight() - BOTTOM_MESSAGE_MARGIN - message.getHeight();
		if (getElementAt(oldx, oldy) != null) {
			remove(getElementAt(oldx, oldy));
		}
		oldx = x;
		oldy = y;
		message.setFont(MESSAGE_FONT);
		add(message, x, y);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the
	 * bottom of the screen) and then the given profile is displayed. The
	 * profile display includes the name of the user from the profile, the
	 * corresponding image (or an indication that an image does not exist), the
	 * status of the user, and a list of the user's friends in the social
	 * network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		name(profile.getName());
		image(profile.getImage());
		status(profile.getStatus());
		// friends(profile.getFriends());
	}

	public void name(String name) {
		GLabel pname = new GLabel(name);
		add(pname, LEFT_MARGIN, TOP_MARGIN + pname.getHeight());
		pname.setFont(PROFILE_NAME_FONT);

	}

	public void image(GImage image) {
		if (image != null) {
			image.setBounds(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN,
					IMAGE_WIDTH, IMAGE_HEIGHT);
			image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(image);
		} else {
			GRect imageRect = new GRect(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN,
					IMAGE_WIDTH, IMAGE_HEIGHT);
			add(imageRect);
			GLabel noImage = new GLabel("No Image");
			noImage.setFont(PROFILE_IMAGE_FONT);
			double labelWidth = LEFT_MARGIN + IMAGE_WIDTH / 2
					- noImage.getWidth() / 2;
			double labelHeight = TOP_MARGIN + IMAGE_MARGIN + IMAGE_HEIGHT / 2;
			add(noImage, labelWidth, labelHeight);
		}
	}

	public void status(String status) {
		if (!status.equals("")) {
			double x = LEFT_MARGIN;
			double y = TOP_MARGIN + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN;
			GLabel pstatus = new GLabel(status);
			pstatus.setFont(PROFILE_STATUS_FONT);
			if (getElementAt(x, y) != null) {
				remove(getElementAt(x, y));
			}
			add(pstatus, x, y);
		}
		// public void friends(){

		// }
	}
}
