package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import game.SnakeArena.Cell;

public class Snake implements SnakeArenaComponent {

	int x;
	int y;
	
	int lastDir = 2; //0 is up, 1 is left, 2 is down, 3 is right
	int length = 3;
	
	public ArrayList<Character> keys;
	
	public LinkedList<Point> body;
	
	public Snake (int x, int y) {
		this.x = x;
		this.y = y;
		keys = new ArrayList<Character> ();
		body = new LinkedList<Point> ();
		body.add (new Point (x, y));
	}
	
	@Override
	public void refresh(SnakeArena arena) {
		Iterator<Point> iter = body.iterator ();
		while (iter.hasNext ()) {
			Point pt = iter.next ();
			arena.setCell (SnakeArena.Cell.CELL_SNAKE, pt.x, pt.y);
		}
	}
	
	@Override
	public void frameEvent () {
		keys.addAll (MainLoop.getWindow ().getKeys ());
	}
	
	@Override
	public void tick () {
		
		int xPrev = x;
		int yPrev = y;
		
		//Do movement
		boolean moved = false;
		if (keys.size () != 0) {
			char last = keys.get (keys.size () - 1);
			switch (last) {
				case 'w':
					moved = goUp ();
					break;
				case 'a':
					moved = goLeft ();
					break;
				case 's':
					moved = goDown ();
					break;
				case 'd':
					moved = goRight ();
					break;
				default:
					break;
			}
		} if (!moved) {
			switch (lastDir) {
				case 0:
					goUp ();
					break;
				case 1:
					goLeft ();
					break;
				case 2:
					goDown ();
					break;
				case 3:
					goRight ();
					break;
			}
		}
		
		//Death for side of the map
		if (x < 0 || y < 0 || x >= MainLoop.getArena ().getWidth () || y >= MainLoop.getArena ().getHeight ()) {
			x = xPrev;
			y = yPrev;
			MainLoop.gameOver ();
			return;
		}
		
		//Make the move
		doMove ();
		
		keys.clear ();
	}
	
	public boolean goUp () {
		if (lastDir != 2) {
			y--;
			lastDir = 0;
			return true;
		}
		return false;
	}
	
	public boolean goLeft () {
		if (lastDir != 3) {
			x--;
			lastDir = 1;
			return true;
		}
		return false;
	}
	
	public boolean goDown () {
		if (lastDir != 0) {
			y++;
			lastDir = 2;
			return true;
		}
		return false;
	}
	
	public boolean goRight () {
		if (lastDir != 1) {
			x++;
			lastDir = 3;
			return true;
		}
		return false;
	}
	
	public void doMove () {
		
		//Check for collision with obstacles
		if (MainLoop.getArena ().getCell (x, y).getContents() == Cell.CELL_OBSTACLE) {
			MainLoop.gameOver ();
			return;
		}
		
		//Move the snake
		body.addLast (new Point (x, y));
		if (body.size () > length) {
			body.remove ();
		}
		
		//Check for collision with the snake
		HashMap<Point, Point> ptsMap = new HashMap<Point, Point> ();
		Iterator<Point> iter = body.iterator ();
		while (iter.hasNext ()) {
			Point pt = iter.next ();
			if (ptsMap.containsKey (pt)) {
				MainLoop.gameOver ();
			} else {
				ptsMap.put (pt, pt);
			}
		}
		
	}
	
	public void extend () {
		length++;
	}
	
	public Point getHeadPos () {
		return new Point (x, y);
	}

}
