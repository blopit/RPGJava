package core.battle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;

import core.Entity;
import core.Game;
import core.battle.Passive.LOC;

public class Battle extends Entity {

	static boolean normal;

	ArrayList<Unit> units = new ArrayList<Unit>();
	static ArrayList<Animation> anim = new ArrayList<Animation>();

	boolean stopped = false;
	final int tfps = 60;
	Unit ready_unit = null;

	public static class Animation {
		int MAXFRAME;
		int FRAME;
		Unit unit;
		AnimInfo animinfo;

		public Animation(Unit u, int max, AnimInfo a) {
			FRAME = 0;
			MAXFRAME = max;
			unit = u;
			animinfo = a;
		}

		public void step() {
			animinfo.update(FRAME);
			FRAME++;
			if (FRAME >= MAXFRAME) {
				anim.remove(this);
				FRAME = 0;
				unit.x = unit.startX;
				unit.y = unit.startY;
			}
		}
	}

	public Battle() {
		super(0, 0, 0, 0);
		normal = true;
		Ally a = new Ally();
		Enemy e = new Enemy();
		a.ABtarget = e;
		e.ABtarget = a;

		units.add(a);
		units.add(e);
	}

	public enum Phase {
		WAIT,
		READY,
		CAST
	}

	public enum DamageType {
		PHYS,
		MAG,
		MIXED,
		PURE,
		NONE
	}

	public abstract class Unit {
		int HP, MAXHP, RES, MAXRES;

		int SPD, CDR, ATTSPD, CRIT;

		int STR, INT;
		int DEF, MDEF;
		int x, y, startX, startY;

		int anchorX;
		int anchorY;
		int dx;
		int dy;

		double dhp;
		double wait_time;
		double cast_time;
		double max_wait_time;
		double max_cast_time;
		Phase phase;

		Ability ABselected = null;
		Unit ABtarget;
		ArrayList<Ability> abilties = new ArrayList<Ability>();
		ArrayList<Passive> passives = new ArrayList<Passive>();

		public Unit(int _x, int _y, int hp, int spd) {
			startX = x = _x;
			startY = y = _y;
			dhp = HP = MAXHP = hp;
			SPD = spd;
			phase = Phase.WAIT;
			cast_time = max_cast_time = 0.0;
			wait_time = max_wait_time = 100.0 * basicMit(SPD);
		}

		public void step() {
			// conc passive update
			switch (phase) {
			case CAST:
				cast_time--;
				if (cast_time <= 0) {
					// use ability
					phase = Phase.WAIT;
					anim.add(this.ABselected.animation);
				}
				break;
			case WAIT:
				wait_time--;
				if (wait_time <= 0) {
					phase = Phase.READY;
					stopped = true;
					ready_unit = this;
				}
				break;
			default:
				break;
			}

			Passive.tickLOC(LOC.constant, this, this, null);
		}

		public void drawBar(Graphics2D g2, Color col, int x, int y, int w,
				int h, double per) {
			g2.setColor(col);
			g2.fillRect(x, y, (int) (w * per), h);
			g2.setColor(Color.BLACK);
			int xx = (int) (w * per) + x;
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(xx, y, xx, y + h);
		}

		public void draw(Graphics2D g2) {
			double c = 1 - (cast_time * 1.0 / max_cast_time);
			double w = 1 - (wait_time * 1.0 / max_wait_time);
			double h = HP * 1.0 / MAXHP;
			dhp += (HP - dhp) * 0.03;

			g2.setColor(Color.GREEN);

			int xxx = x + 32;
			int yyy = y + 32;

			g2.fillRect(xxx - 16, yyy - 16, 32, 32);

			int W = 128;
			int H = 12;

			g2.setColor(Color.WHITE);
			g2.fillRect(x, y, W, H);

			if (w < 1.0) {
				drawBar(g2, Color.CYAN, x, y, W, H, w);
				g2.setColor(new Color(0, 0, 0, 64));
				for (int i = 0; i < max_wait_time / (tfps / 2); i++) {
					int xx = (int) (x + W * i * (tfps / max_wait_time));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(xx, y, xx, y + H);
				}
				g2.setColor(new Color(0, 0, 0, 32));
				for (int i = 1; i < max_wait_time / (tfps / 4); i += 2) {
					int xx = (int) (x + W * i * ((tfps / 2) / max_wait_time));
					g2.setStroke(new BasicStroke(1));
					g2.drawLine(xx, y + 3, xx, y + H - 4);
				}
				g2.setColor(Color.BLACK);
			}

			if (c < 1.0) {
				drawBar(g2, Color.red, x, y, W, H, c);
				g2.setColor(new Color(0, 0, 0, 64));
				for (int i = 0; i < max_cast_time / tfps; i++) {
					int xx = (int) (x + W * i * (tfps / max_cast_time));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(xx, y, xx, y + H);
				}
				g2.setColor(new Color(0, 0, 0, 32));
				for (int i = 0; i < max_cast_time / (tfps / 2); i++) {
					int xx = (int) (x + W * i * ((tfps / 2) / max_cast_time));
					g2.setStroke(new BasicStroke(1));
					g2.drawLine(xx, y + 3, xx, y + H - 4);
				}
				g2.setColor(Color.BLACK);
			}

			if (ready_unit == this) {
				g2.setColor(Game.hex2Rgb("#FFD700"));
				g2.fillRect(x, y, W, H);
			}
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(2));
			g2.drawRect(x, y, W, H);

			drawBar(g2, Color.red, x, y - 16, W, H, dhp / MAXHP);
			drawBar(g2, Color.green, x, y - 16, W, H, h);
		}

		public void useSelected() {
			ABselected.beginCast(this, ABtarget);
		}

		public void beginCast(double ct, double wt, int cost) {
			RES -= cost;
			cast_time = max_cast_time = ct * tfps * basicMit(SPD);
			wait_time = max_wait_time = wt * tfps * basicMit(SPD);
			phase = Phase.CAST;
			stopped = false;
		}

	}

	public class Ally extends Unit {
		public Ally() {
			super(200, 256, 500, 50);
			this.abilties.add(new Ability.Chop(this));
			this.abilties.add(new Ability.Bolt(this));
			this.abilties.add(new Ability.Bolt(this));
			this.abilties.add(new Ability.Chop(this));

			this.passives.add(new Passive.p_HoT(30, -2, 30));
		}
	}

	public class Enemy extends Unit {
		public Enemy() {
			super(400, 256, 300, 100);
			this.abilties.add(new Ability.Bolt(this));
			this.abilties.add(new Ability.Bolt(this));
			this.abilties.add(new Ability.Bolt(this));
			this.abilties.add(new Ability.Chop(this));
		}
	}

	public static void deal_damage(Unit source, Unit target, double damage,
			DamageType type) {
		target.HP -= damage;
	}

	public static void heal(Unit source, Unit target, double heal) {
		target.HP += heal;
		if (target.HP > target.MAXHP) {target.HP = target.MAXHP; }
	}

	public double basicMit(double def) {
		double c = 100;
		return c / (def + c);
	}

	public void step() {
		if (anim.isEmpty()) {

			for (Unit u : units) {
				if (ready_unit == u) {
					if (u.phase == Phase.READY) {
						if (Game.keyCheckPressed('Z')) {
							u.ABselected = u.abilties.get(0);
							u.useSelected();
							stopped = false;
							ready_unit = null;
						}
					}
				}
				if (!stopped) {
					u.step();
				} else {

				}
			}

		} else {
			anim.get(0).step();
		}

	}

	public void draw(Graphics2D g2, Object drawer) {
		for (Unit u : units) {
			u.draw(g2);
		}

		if (ready_unit != null) {
			g2.setColor(Color.WHITE);
			g2.drawOval(ready_unit.x, ready_unit.y, 16, 16);
		}

	}

	public Shape getBounds() {
		return null;
	}

}
