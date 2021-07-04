package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WindowKeyListener implements KeyListener {
	
	private Window window;
	
	public void setWindow (Window w) {
		window = w;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Do nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		window.doKeyPress (e.getKeyChar ());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Do nothing
	}

}
