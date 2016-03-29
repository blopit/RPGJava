package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import java.util.ArrayList;

import Roleplaying.Roleplay;

public class DialogBox extends Entity {

	private String str;
	private double txttick;
	private double txtspd;
	private int p;
	Unit unit1;
	Unit unit2;
	public ArrayList<Button> buttons;
	public Button sel = null;
	private boolean done = false;

	public static class Button {
		// Button left, right, up, down;
		// Boolean selected = false;
		public final String type;
		public final String desc;
		public final Roleplay.Edge edge;

		public Button(String t, String d, Roleplay.Edge e) {
			// left = right = up = down = null;
			type = t;
			desc = d;
			edge = e;
		}
	}

	public DialogBox(String s, Unit u1, Unit u2, double spd) {
		super(16, 240 + 16, 280, 240 - 64);
		this.txttick = 0;
		this.txtspd = spd;
		this.p = 0;
		this.str = s;
		this.depth = Integer.MIN_VALUE + 10;
		unit1 = u1;
		unit2 = u2;
		buttons = new ArrayList<Button>();
	}

	public void step() {
		unit1.talking = true;
		unit2.talking = true;
		txttick += txtspd;
		int j = 0;
		p = 0;

		if (buttons.size() > 0) {
			if (sel == null) {
				sel = buttons.get(0);
				System.out.println((int) ((h / 2) / 16));
			}

			if (Game.keyCheckPressed(KeyEvent.VK_UP)) {
				int idx = buttons.indexOf(sel) - 1;
				if (idx < 0) {
					idx = buttons.size() - 1;
				}
				sel = buttons.get(idx);
			}
			if (Game.keyCheckPressed(KeyEvent.VK_DOWN)) {
				int idx = buttons.indexOf(sel) + 1;
				if (idx > buttons.size() - 1) {
					idx = 0;
				}
				sel = buttons.get(idx);
			}

			if (Game.keyCheckPressed(KeyEvent.VK_RIGHT)) {
				int m = (int) ((h / 2) / 16);
				int mod = buttons.indexOf(sel) % m;
				int idx = buttons.indexOf(sel) + m;
				if (idx > buttons.size() - 1) {
					idx = 0;
					while (idx % m != mod) {
						idx++;
					}
				}
				sel = buttons.get(idx);
			}
			if (Game.keyCheckPressed(KeyEvent.VK_LEFT)) {
				int m = (int) ((h / 2) / 16);
				int mod = buttons.indexOf(sel) % m;
				int idx = buttons.indexOf(sel) - m;
				if (idx < 0) {
					idx = buttons.size() - 1;
					while (idx % m != mod) {
						idx--;
					}
				}
				sel = buttons.get(idx);
			}
		}

		if (!done) {
			if (txttick >= str.length())
				done = true;
			for (int n = (int) txttick; j < n;) {
				if (p > str.length() - 1)
					break;
				while (str.charAt(p) == ' ') {
					p++;
				}
				if (str.charAt(p) == '[') {
					while (str.charAt(p) != ']') {
						if (str.charAt(p) == '#')
							txtspd = 0.05;
						p++;
					}
					p++;
				}
				if (p > str.length() - 1)
					break;
				if (str.charAt(p) == '[') {
					while (str.charAt(p) != ']') {
						if (str.charAt(p) == '#')
							txtspd = 0.05;
						p++;
					}
					p++;
				}

				p++;
				j++;
			}
		}else {
			p = str.length();
		}

		if (Game.keyCheckPressed(KeyEvent.VK_A)) {
			if (p < str.length()) {
				txttick = str.length();
				done = true;
			} else {
				this.destroy();
			}
		}
		// System.out.println(str.substring(0, p));
	}

	public void destroy() {
		if (sel != null) {
			sel.edge.activate(unit2);
		}

		Game.removeEntity(this);
		unit1.talking = false;
		unit2.talking = false;
		// TODO: make a key reset method
		Game.keys.clear();
		Game.keysPressed.clear();
		Game.keysReleased.clear();
		if (Game.currentEvent == this) {
			Game.currentEvent = null;
		}

	}

	public void draw(Graphics2D g2, Object drawer) {
		g2.setColor(Color.ORANGE);
		g2.fillRect((int) (x), (int) (y), (int) (w), (int) (h));
		g2.setColor(Color.BLACK);
		g2.drawRect((int) (x + 16), (int) (y + 16), (int) (w - 32),
				(int) (h - 32));
		g2.setFont(new Font("Comic Sans", Font.PLAIN, 14));
		AttributedString s = Game
				.getBBCode(g2, BBCode.fix(str.substring(0, p)));

		// System.out.println(str.substring(0, p));
		g2.drawString(s.getIterator(), (int) x + 24, (int) (y) + 32);

		if (buttons.size() > 0 && done) {
			double d = 280 / buttons.size();

			int ix = 0;
			int iy = 0;
			int textx, texty, textw, texth;
			textx = texty = textw = texth = 0;
			String textip = "";
			for (Button b : buttons) {
				String shortDesc = b.type;
				Font prev = g2.getFont();
				if (sel == b) {
					Font font = new Font("default", Font.BOLD, 14);
					FontRenderContext frc = new FontRenderContext(null, true,
							true);
					g2.setFont(font);
					g2.setColor(Color.BLACK);
					shortDesc += "◄";
					textx = (int) (font.getStringBounds(shortDesc, frc)
							.getWidth()) + (int) x + 24 + ix + 16;
					g2.drawString(shortDesc, (int) x + 24 + ix,
							(int) (y + h / 2) + iy);
					g2.setFont(prev);
					texty = (int) (y + h / 2) + iy;

					textw = (int) (font.getStringBounds(b.desc, frc).getWidth());
					texth = (int) (font.getStringBounds(b.desc, frc)
							.getHeight());
					textip = b.desc;

				} else {
					g2.setColor(Color.DARK_GRAY);
					g2.drawString(shortDesc, (int) x + 24 + ix,
							(int) (y + h / 2) + iy);
				}

				iy += 16;
				if (iy > (h / 2 - 16)) {
					iy = 0;
					ix += 80;
				}
			}

			g2.setColor(Color.WHITE);
			g2.fillRect(textx - 4, texty - 14 - 4, textw + 8, texth + 8);
			g2.setColor(Color.BLACK);
			g2.drawString(textip, textx, texty);

		}

	}

	public Shape getBounds() {
		return null;
	}
}
