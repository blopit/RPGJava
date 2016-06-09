package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.Statement;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;

import roleplay.DialogBox;
import core.battle.Battle;

public class Game extends JPanel implements Runnable, MouseListener,
		MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private Thread animator;
	public static double TARGET_FPS = 60.0;
	private final int FPS = (int) (1000 / TARGET_FPS);
	public static double spdf = 1.0;

	public static long time = 0;

	public static Room current_room = null;

	public static ArrayList<Room> rooms = new ArrayList<Room>();
	public static Set<Integer> keys = new HashSet<Integer>();
	public static Set<Integer> keysPressed = new HashSet<Integer>();
	public static Set<Integer> keysReleased = new HashSet<Integer>();
	public static Queue<DialogBox> eventQueue = new ArrayDeque<DialogBox>();
	public static DialogBox currentEvent = null;

	public static Point.Double mouse = new Point.Double(0, 0);
	public static final Point.Double origin = new Point.Double(0, 0);

	public Game() {
		addKeyListener(new KL());
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.BLACK);
		setFocusable(true);
		setDoubleBuffered(true);

		Connection c = null;
		Statement stmt = null;
		/*
		 * try { Class.forName("org.sqlite.JDBC"); c =
		 * DriverManager.getConnection("jdbc:sqlite:test.db");
		 * System.out.println("Opened database successfully");
		 * 
		 * stmt = c.createStatement(); String sql = "CREATE TABLE COMPANY " +
		 * "(ID INT PRIMARY KEY     NOT NULL," +
		 * " NAME           TEXT    NOT NULL, " +
		 * " AGE            INT     NOT NULL, " + " ADDRESS        CHAR(50), " +
		 * " SALARY         REAL)"; stmt.executeUpdate(sql); stmt.close();
		 * c.close(); } catch ( Exception e ) { System.err.println(
		 * e.getClass().getName() + ": " + e.getMessage() ); System.exit(0); }
		 * System.out.println("Table created successfully");
		 */

		current_room = new Room();
		/*
		 * Unit u1 = new Unit(128, 128, 0); u1.controlled = true; u1.weight = 1;
		 * addEntity(u1); addEntity(new Unit(128, 156, 0)); addEntity(new
		 * Block(256, 0, 32, 256));
		 */

		addEntity(new Battle());
	}

	public static void addEvent(DialogBox e) {
		eventQueue.add(e);
	}

	public static void insertEvent(DialogBox e) {
		if (currentEvent != null)
			currentEvent.destroy();
		currentEvent = e;
	}

	public static ArrayList<Entity> getObjects() {
		return current_room.objects;
	}

	public static void addEntity(Entity e) {
		current_room.addEntity(e);
	}

	public static void removeEntity(Entity e) {
		current_room.removeEntity(e);
	}

	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Entity.sort(getObjects());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		BufferedImage off_Image = new BufferedImage(640, 480,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = off_Image.createGraphics();

		ArrayList<Entity> copy = new ArrayList<Entity>(getObjects());
		for (Entity x : copy) {
			if (x.visible)
				x.draw(g2, this);
		}

		g2d.drawImage(off_Image, 0, 0, null);
		g2d.dispose();
		off_Image.flush();
		g2.dispose();
	}

	public Entity[] arrayConversion(ArrayList<Entity> x) {
		Entity[] arr = new Entity[x.size()];

		for (int i = 0; i < x.size(); i++) {
			arr[i] = x.get(i);
		}
		return arr;
	}

	public static boolean testIntersection(Shape shapeA, Shape shapeB) {
		Area areaA = new Area(shapeA);
		areaA.intersect(new Area(shapeB));
		return !areaA.isEmpty();
	}

	public void run() {

		while (true) {

			ArrayList<Entity> copy = new ArrayList<Entity>(getObjects());
			for (Entity x : copy) {
				x.step();
				x.bounds = x.getBounds();
			}

			if (currentEvent == null) {
				currentEvent = eventQueue.poll();
				if (currentEvent != null) {
					Game.addEntity(currentEvent);
				}
			}

			/*
			 * for (Entity x : copy) { if (x.active) {
			 * 
			 * // COLLISION if (x.bounds != null) { for (Entity y :
			 * getObjects()) { if (y.bounds != null) { if
			 * (testIntersection(x.bounds, y.bounds) && x != y) {
			 * x.collision(y); } } } }
			 * 
			 * } }
			 */

			keysPressed.clear();
			keysReleased.clear();

			repaint();
			time++;
			try {
				Thread.sleep(FPS);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

		for (Entity xx : getObjects()) {
			// xx.mouseReleased(e);
		}
	}

	public void mousePressed(MouseEvent e) {

		for (Entity xx : getObjects()) {
			// xx.mousePressed(e);
		}

	}

	public void mouseMoved(MouseEvent e) {
		mouse = new Point.Double(e.getX(), e.getY());
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public class KL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();

			if (!keys.contains(code)) {
				keys.add(code);
				keysPressed.add(code);
				System.out.println("Key pressed code=" + e.getKeyCode()
						+ ", char=" + e.getKeyChar());
			}
			keysReleased.remove(code);
		}

		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			keys.remove(code);
			keysPressed.remove(code);
			keysReleased.add(code);
			System.out.println("Key released code=" + e.getKeyCode()
					+ ", char=" + e.getKeyChar());
		}
	}

	public static boolean keyCheck(int e) {
		return keys.contains(e);
	}

	public static boolean keyCheckPressed(int e) {
		return keysPressed.contains(e);
	}

	public static boolean keyCheckReleased(int e) {
		return keysReleased.contains(e);
	}

	public static double anglePoints(Point.Double p1, Point.Double p2) {
		double xDiff = p2.x - p1.x;
		double yDiff = p2.y - p1.y;
		return Math.atan2(yDiff, xDiff);
	}

	public static double distPoints(Point.Double p1, Point.Double p2) {
		return Math.hypot(p2.x - p1.x, p2.y - p1.y);
	}

	public static Point.Double smoothSlope(double sx, double sy, double dx,
			double dy, double amount) {
		Point.Double s1 = new Point.Double(sx, sy);
		Point.Double d1 = new Point.Double(dx, dy);
		double dist = Game.distPoints(s1, d1);
		double angle = Game.anglePoints(s1, d1);

		double vx = s1.x + Math.cos(angle) * dist * amount;
		double vy = s1.y + Math.sin(angle) * dist * amount;

		return new Point.Double(vx, vy);
	}

	public static double fric(double q, double fr) {
		if (Math.abs(q) > fr) {
			return q - fr * Math.signum(q);
		}
		return 0;
	}

	public static String possessiveForm(String input) {
		if (input.isEmpty())
			return "";

		if (input.endsWith("s")) {
			return input + "'";
		} else {
			return input + "'s";
		}
	}

	public static String getStringAttr(AttributedString s) {
		AttributedCharacterIterator x = s.getIterator();
		String a = "";

		a += x.current();
		while (x.getIndex() < x.getEndIndex())
			a += x.next();
		a = a.substring(0, a.length() - 1);
		return a;
	}

	public static AttributedString getBBCode(Graphics2D g2, String str) {
		return BBCode.parse(g2, str);
	}

	public static void drawBBCode(Graphics2D g2, String str, int x, int y) {
		AttributedString a = getBBCode(g2, str);
		TextLayout textLayout = new TextLayout(a.getIterator(),
				g2.getFontRenderContext());
		textLayout.draw(g2, x, y);
	}

	public static class RandomEnum<E extends Enum> {

		private static final Random RND = new Random();
		private final E[] values;

		public RandomEnum(Class<E> token) {
			values = token.getEnumConstants();
		}

		public E random() {
			return values[RND.nextInt(values.length)];
		}
	}

	public static long stringToSeed(String strSeed) {
		long seed = 0;
		for (int i = 0; i < strSeed.length(); i++) {
			seed += (int) strSeed.charAt(i);
		}
		return seed;
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16),
				Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(
						colorStr.substring(5, 7), 16));
	}
}
