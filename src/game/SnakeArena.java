package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import ui.Window;

public class SnakeArena {
	
	public static final int TICK_DURATION = 5;
	
	private Cell[][] arena; //Ordered by x then y
	private int width;
	private int height;
	
	private int tickTime;
	
	private ArrayList<SnakeArenaComponent> components;
	
	public SnakeArena (int width, int height) {
		
		//Init the arena
		initArena (width, height);
		
		//Initialize the components list
		components = new ArrayList<SnakeArenaComponent> ();
		
		//Initialize the tick time
		tickTime = 0;
		
	}

	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
	
	public Cell getCell (int x, int y) {
		return arena [x][y];
	}
	
	public ArrayList<SnakeArenaComponent> getComponents () {
		return components;
	}
	
	//Just for convenience
	public void setCell (int contents, int x, int y) {
		arena [x][y].setContents (contents);
	}
	
	//Just for convenience
	public void clearCell (int x, int y) {
		setCell (Cell.CELL_EMPTY, x, y);
	}
	
	public void refresh () {
		
		//Clear cells for refreshing
		for (int wx = 0; wx < width; wx++) {
			for (int wy = 0; wy < height; wy++) {
				if (getCell (wx, wy).clearOnRefresh ()) {
					clearCell (wx, wy);
				}
			}
		}
		
		//Refresh the arena components
		for (int i = 0; i < components.size (); i++) {
			components.get (i).refresh (this);
		}
		
	}
	
	public void clear () {
		for (int wx = 0; wx < width; wx++) {
			for (int wy = 0; wy < height; wy++) {
				clearCell (wx, wy);
			}
		}
	}
	
	public boolean checkWin () {
		boolean won = true;
		for (int wx = 0; wx < width; wx++) {
			for (int wy = 0; wy < height; wy++) {
				if (getCell (wx, wy).getContents () == Cell.CELL_EMPTY) {
					won = false;
				}
			}
		}
		return won;
	}
	
	public void addComponent (SnakeArenaComponent component) {
		components.add (component);
	}
	
	public void loadMap (String path) {
		File f = new File (path);
		try {
			ArrayList<String> lines = new ArrayList<String> ();
			Scanner s = new Scanner (f);
			while (s.hasNextLine ()) {
				lines.add (s.nextLine ());
			}
			int width = Integer.parseInt (lines.get (0).split("x")[0]);
			int height = Integer.parseInt (lines.get (0).split("x")[1]);
			initArena (width, height);
			for (int wy = 0; wy < height; wy++) {
				Scanner lineScanner = new Scanner (lines.get (wy + 1));
				for (int wx = 0; wx < width; wx++) {
					char c = lineScanner.next ().charAt (0);
					int cell = 0;
					switch (c) {
						case 'A':
							cell = Cell.CELL_EMPTY;
							MainLoop.addApple (wx, wy);
							break;
						case 'S':
							cell = Cell.CELL_EMPTY;
							MainLoop.addSnake (wx, wy);
							break;
						case 'X':
							cell = Cell.CELL_OBSTACLE;
							break;
						default:
							cell = Cell.CELL_EMPTY;
							break;
					}
					MainLoop.getArena ().setCell (cell, wx, wy);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initArena (int width, int height) {
		
		//Set the width and height
		this.width = width;
		this.height = height;
		
		//Make the arena
		arena = new Cell[width][height];
		for (int wx = 0; wx < width; wx++) {
			for (int wy = 0; wy < height; wy++) {
				arena [wx][wy] = new Cell ();
			}
		}
		
	}
	
	public void draw (Window w) {
		
		//Do frame events and ticks
		if (MainLoop.getStatus() == MainLoop.STATUS_RUNNING) {
			tickTime++;
			boolean doTick = false;
			if (tickTime > TICK_DURATION) {
				tickTime = 0;
				doTick = true;
			}
			for (int i = 0; i < components.size (); i++) {
				components.get (i).frameEvent ();
				if (doTick) {
					components.get (i).tick ();
				}
			}
		}
		
		refresh ();
		Graphics g = w.getBufferGraphics();
		Dimension resolution = w.getResolution ();
		for (int wx = 0; wx < width; wx++) {
			for (int wy = 0; wy < height; wy++) {
				double hscale = (double)resolution.getWidth () / width;
				double vscale = (double)resolution.getHeight () / height;
				int xFrom = (int)(wx * hscale);
				int yFrom = (int)(wy * vscale);
				int xTo = (int)((wx + 1) * hscale);
				int yTo = (int)((wy + 1) * vscale);
				g.setColor (getCell (wx, wy).getColor ());
				g.fillRect (xFrom, yFrom, xTo - xFrom, yTo - yFrom);
				//System.out.println (xFrom + ", " + xTo + "; " + yFrom + ", " + yTo);
			}
		}
	}
	
	public void randomize () {
		for (int wx = 0; wx < width; wx++) {
			for (int wy = 0; wy < height; wy++) {
				int rand = (int)(Math.random () * 4);
				setCell (rand, wx, wy);
			}
		}
	}
	
	public static class Cell {
		
		public static final int CELL_EMPTY = 0;
		public static final int CELL_OBSTACLE = 1;
		public static final int CELL_SNAKE = 2;
		public static final int CELL_APPLE = 3;
		
		private int contents;
		
		public Cell () {
			contents = CELL_EMPTY;
		}
		
		public Color getColor () {
			switch (contents) {
				case CELL_EMPTY:
					return Color.DARK_GRAY;
				case CELL_OBSTACLE:
					return Color.BLUE;
				case CELL_SNAKE:
					return Color.WHITE;
				case CELL_APPLE:
					return Color.RED;
				default:
					return Color.BLACK;
			}
		}
		
		public int getContents () {
			return contents;
		}
		
		public boolean clearOnRefresh () {
			return contents != CELL_OBSTACLE;
		}
		
		public void clear () {
			setContents (CELL_EMPTY);
		}
		
		public void setContents (int contents) {
			this.contents = contents;
		}
		
	}

}
