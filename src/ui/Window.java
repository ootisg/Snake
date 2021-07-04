package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import game.MainLoop;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1092418710020581973L;
	
	public static final int RESOLUTION_WIDTH = 640;
	public static final int RESOLUTION_HEIGHT = 480;
	
	public static BufferedImage bufferImg;
	public static BufferedImage renderImg;
	
	public static ArrayList<Character> keysPressed;
	public static ArrayList<Character> currentKeys = new ArrayList<Character> ();
	
	public Window () {
		
		//Setup window parameters
		this.setDefaultCloseOperation (EXIT_ON_CLOSE);
		
		//Setup input listeners
		WindowKeyListener keyListener = new WindowKeyListener ();
		keyListener.setWindow (this);
		addKeyListener (keyListener);
		
		//Size the window
		this.getContentPane ().setPreferredSize (new Dimension (RESOLUTION_WIDTH, RESOLUTION_HEIGHT));
		this.pack ();
		this.setVisible (true);
		
		//Make the buffers
		bufferImg = new BufferedImage (RESOLUTION_WIDTH, RESOLUTION_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		renderImg = new BufferedImage (RESOLUTION_WIDTH, RESOLUTION_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	
	}
	
	public void doPaint (Graphics g) {
		//Resize the render img if needed
		if (renderImg == null || getContentPane ().getWidth () != renderImg.getWidth () || getContentPane ().getHeight () != renderImg.getHeight ()) {
			renderImg = new BufferedImage (getContentPane ().getWidth (), getContentPane ().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		}
		
		//Find the proper scale factors
		int contentHeight = getContentPane ().getHeight ();
		int contentWidth = (int)(contentHeight * ((double)RESOLUTION_WIDTH / RESOLUTION_HEIGHT));
		
		//Render the buffer to the render image
		if (contentWidth > getContentPane ().getWidth ()) {
			renderImg.getGraphics ().drawImage (bufferImg, getContentPane ().getWidth (), getContentPane ().getHeight (), 0, 0, bufferImg.getWidth (), bufferImg.getHeight (), 0, 0, null);
		} else {
			int startX = renderImg.getWidth () / 2 - contentWidth / 2;
			Graphics rg = renderImg.getGraphics();
			rg.setColor (Color.BLACK);
			rg.fillRect (0, 0, renderImg.getWidth (), renderImg.getHeight ());
			rg.drawImage (bufferImg, startX, 0, startX + contentWidth, contentHeight, 0, 0, bufferImg.getWidth (), bufferImg.getHeight (), null);
		}
		
		//Draw the render to the screen
		g.drawImage (renderImg, getInsets ().left, getInsets ().top, null);
		
		//Clear the buffer
		Graphics bg = bufferImg.getGraphics();
		bg.setColor (Color.DARK_GRAY);
		bg.fillRect (0, 0, bufferImg.getWidth (), bufferImg.getHeight ());
		
	}
	
	public Graphics getBufferGraphics () {
		return bufferImg.getGraphics ();
	}
	
	public void doKeyPress (char c) {
		currentKeys.add (c);
	}
	
	public ArrayList<Character> getKeys () {
		return keysPressed;
	}
	
	public void swapKeyBuffers () {
		keysPressed = currentKeys;
		currentKeys = new ArrayList<Character> ();
	}
	
	public Dimension getResolution () {
		return new Dimension (bufferImg.getWidth (), bufferImg.getHeight ());
	}
	
	public void updateScore (int score) {
		if (MainLoop.getStatus () == MainLoop.STATUS_RUNNING) {
			setTitle ("Score: " + score);
		} else if (MainLoop.getStatus () == MainLoop.STATUS_GAME_OVER) {
			setTitle ("Game Over! Score: " + score + " (press any key to retry)");
		} else if (MainLoop.getStatus () == MainLoop.STATUS_WIN) {
			setTitle ("You Win! Score: " + score + " (press any key to retry)");
		}
	}

}
