package game;

import java.awt.Point;

public class Apple implements SnakeArenaComponent {

	private int x;
	private int y;
	
	public Apple (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void collect () {
		MainLoop.setScore (MainLoop.getScore () + 1);
		move ();
	}
	
	public void move () {
		
		//Find an empty x, y
		SnakeArena arena = MainLoop.getArena ();
		int randX = (int)(Math.random () * arena.getWidth ());
		int randY = (int)(Math.random () * arena.getHeight ());
		int attempts = 0;
		while (MainLoop.getArena ().getCell (randX, randY).getContents () != SnakeArena.Cell.CELL_EMPTY) {
			randX = (int)(Math.random () * arena.getWidth ());
			randY = (int)(Math.random () * arena.getHeight ());
			attempts++;
			if (attempts > 100000) {
				if (MainLoop.getArena ().checkWin ()) {
					MainLoop.win ();
					return;
				}
			}
		}
		
		//Move the apple
		x = randX;
		y = randY;
		
	}
	
	@Override
	public void refresh(SnakeArena arena) {
		arena.setCell (SnakeArena.Cell.CELL_APPLE, x, y);
	}

	@Override
	public void frameEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if (MainLoop.getSnake ().getHeadPos ().equals (new Point (x, y))) {
			System.out.println ("NOM");
			collect ();
			MainLoop.getSnake ().extend ();
		}
	}

}
