package game;

import java.util.ArrayList;

import ui.Window;

public class MainLoop {

	public static Window window;
	
	public static final int TARGET_FPS = 60;
	public static final long TARGET_NS = 1000000000L / TARGET_FPS;
	
	public static final int STATUS_RUNNING = 0;
	public static final int STATUS_GAME_OVER = 1;
	public static final int STATUS_WIN = 2;
	
	private static SnakeArena arena;
	private static Snake snake;
	private static ArrayList<Apple> apples;
	
	private static int score = 0;
	
	private static int status = STATUS_RUNNING;
	
	public static void main (String[] args) {
		
		//Initialize the window
		window = new Window ();
		
		//Game initialization code
		newGame ("map.txt");
		
		//Main game loop
		while (true) {
			
			//Get the initial time
			long startTime = System.nanoTime ();
			
			//New game logic (if needed)
			if (status != STATUS_RUNNING) {
				if (!window.getKeys ().isEmpty()) {
					newGame ("map.txt");
				}
			}
			
			//Game logic
			window.swapKeyBuffers ();
			//System.out.println (window.getKeys ());
			
			//Render the window
			arena.draw (window);
			window.doPaint (window.getGraphics ());
			window.updateScore (getScore ());
			
			//Wait for next frame
			long elapsedTime = System.nanoTime () - startTime;
			if (elapsedTime < TARGET_NS) {
				while (TARGET_NS - elapsedTime > 1000000) {
					//1000000 NS is one MS
					try {
						Thread.sleep (1);
					} catch (InterruptedException e) {
						//Do nothing, the timing of Thread.sleep is not imporant here
					}
					elapsedTime = System.nanoTime () - startTime;
				}
				while (elapsedTime < TARGET_NS) {
					//Wait with ns precision
					elapsedTime = System.nanoTime () - startTime;
				}
			}
			
		}
		
	}
	
	public static Window getWindow () {
		return window;
	}
	
	public static Snake getSnake () {
		return snake;
	}
	
	public static SnakeArena getArena () {
		return arena;
	}
	
	public static int getScore () {
		return score;
	}
	
	public static int getStatus () {
		return status;
	}
	
	public static void setScore (int newScore) {
		score = newScore;
	}
	
	public static void gameOver () {
		status = STATUS_GAME_OVER;
	}
	
	public static void win () {
		status = STATUS_WIN;
	}
	
	public static void addApple (int x, int y) {
		apples.add (new Apple (x, y));
	}
	
	public static void addSnake (int x, int y) {
		snake = new Snake (x, y);
	}
	
	public static void newGame (String filepath) {
		setScore (0);
		snake = null;
		arena = new SnakeArena (40, 30);
		apples = new ArrayList<Apple> ();
		if (filepath != null) {
			arena.loadMap (filepath);
		}
		if (apples.size () == 0) {
			Apple a = new Apple (0, 0);
			a.move ();
			apples.add (a);
		}
		if (snake == null) {
			snake = new Snake (0, 0);
		}
		arena.addComponent (snake);
		arena.getComponents ().addAll (apples);
		status = STATUS_RUNNING;
	}
	
}
