package core;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Roleplaying.Roleplay;
import composite.BlendComposite;
import core.DialogBox.Button;

public class Unit extends Entity {

	boolean controlled = false;
	double hsp, vsp, zsp, angle, max_mvspd, mvspd;
	double ground_fric;
	ArrayList<Unit> viscinity;

	Sprite wep = null;
	AnimSprite weapon = null;
	short ammo = 0;
	double wep_angle = 0;
	short attspd = 0;
	short attstun = 0;
	boolean wep_flip = false;

	double maxhp, hp, dhp;
	double weight = 0;
	boolean talking = false;
	boolean inrange = false;

	public Roleplay role;

	private Shape sight;
	private double sight_radius = 128;

	public Unit(int x_, int y_, int z_) {
		super(x_, y_, null);
		w = 24;
		h = 24;
		zlen = 32;
		ground_fric = 1;
		max_mvspd = 3000;
		maxhp = hp = dhp = 100;
		attspd = 10;
		weight = 1;

		role = new Roleplay.rpBarbarian_Goltar(this);

		mvspd = max_mvspd;

		sprite = new Sprite("/images/barb.png", 40, 80);
		sprite.yscale = sprite.xscale = 0.5;

		wep = new Sprite("/images/axe.png", -30, 100);
		wep.yscale = wep.xscale = 0.5;

		int fdata[] = { 0, 1, 1, 1, 1, 2 };
		weapon = new AnimSprite("/images/axe_anim.png", 24, 29, 101, 108, 6,
				fdata);
	}

	private void control() {
		int vecX = 0;
		int vecY = 0;

		if (Game.keyCheck(KeyEvent.VK_UP)) {
			vecY -= 1;
		}
		if (Game.keyCheck(KeyEvent.VK_DOWN)) {
			vecY += 1;
		}
		if (Game.keyCheck(KeyEvent.VK_LEFT)) {
			vecX -= 1;
		}
		if (Game.keyCheck(KeyEvent.VK_RIGHT)) {
			vecX += 1;
		}

		if (vecX == 1) {
			sprite.xscale = 0.5;
		} else if (vecX == -1) {
			sprite.xscale = -0.5;
		}

		if (vecX != 0 || vecY != 0) {
			double ang = Game.anglePoints(Game.origin, new Point.Double(vecX,
					vecY));
			this.hsp += Math.cos(ang) * 2 * ground_fric;
			this.vsp += Math.sin(ang) * 2 * ground_fric;
			this.angle = Game.anglePoints(Game.origin, new Point.Double(hsp,
					vsp));
		}

		double d = Game.distPoints(Game.origin, new Point.Double(hsp, vsp));

		if (d > mvspd / 600) {
			d = mvspd / 600;
		} else if (vecX == 0 && vecY == 0) {
			// d = Game.fric(d, ground_fric);
		}

		hsp = Math.cos(angle) * d;
		vsp = Math.sin(angle) * d;
	}

	public void step() {
		depth = (int) -y;
		if (ammo > 0)
			ammo--;
		if (ammo < 0)
			ammo = 0;

		if (attstun > 0)
			attstun--;

		if (talking == false && controlled) {
			ArrayList<Unit> sights = new ArrayList<Unit>();
			ArrayList<Entity> copy = new ArrayList<Entity>(Game.getObjects());
			for (Entity y : copy) {
				if (sight != null && y.bounds != null
						&& y.getClass().equals(Unit.class) && y != this) {
					((Unit) y).inrange = false;
					if (Game.testIntersection(sight, y.bounds) && this != y) {
						sights.add((Unit) y);
					}
				}
			}

			double max = Integer.MAX_VALUE;
			Unit q = null;
			for (Unit u : sights) {
				double m = Game.distPoints(new Point.Double(this.x, this.y),
						new Point.Double(u.x, u.y));
				if (m < max) {
					max = m;
					q = u;
				}
			}
			if (q != null)
				q.inrange = true;

		}
		// ALL
		if (controlled && attstun == 0 && talking == false) {
			control();

			if (Game.keyCheckPressed(KeyEvent.VK_Z) && ammo <= 0) {
				ammo = attspd;
				attstun = 13;
				hsp = vsp = 0;
			}
			if (Game.keyCheckPressed(KeyEvent.VK_A)) {

				ArrayList<Entity> copy = new ArrayList<Entity>(Game.getObjects());
				for (Entity y : copy) {
					if (sight != null && y.bounds != null
							&& y.getClass().equals(Unit.class)) {
						if (Game.testIntersection(sight, y.bounds) && this != y
								&& ((Unit) y).inrange) {
							hsp = 0;
							vsp = 0;
							Game.addEvent(((Unit) y).role.startDialog(this));
						}
					}
				}

			}

		} else if (!controlled) {
			this.angle = Game.anglePoints(Game.origin, new Point.Double(hsp,
					vsp));
			if (hp <= 0) {
				hp = dhp = 100;
			}
		}

		double d = Game.distPoints(Game.origin, new Point.Double(hsp, vsp));
		d = Game.fric(d, ground_fric);
		hsp = Math.cos(angle) * d;
		vsp = Math.sin(angle) * d;

		nudge();
		stepMove();

	}

	public void collision(Entity e) {
		if (e.getClass().equals(Unit.class)) {
			/*
			 * double iw = 0; if (weight != 0) { iw = 1 / weight; } Point.Double
			 * me = new Point.Double(this.x, this.y); Point.Double other = new
			 * Point.Double(e.x, e.y); double angle = Game.anglePoints(other,
			 * me); hsp += Math.sin(angle) * iw; vsp += Math.cos(angle) * iw;
			 */
		}
	}

	public void nudge() {
		Shape bnd = getBounds();
		for (Entity q : Game.getObjects()) {
			if (q.bounds != null && q.getClass().equals(Unit.class)) {
				Unit y = (Unit) q;
				if (Game.testIntersection(bnd, y.bounds) && this != y) {
					double iw = 0;
					if (y.weight != 0) {
						iw = y.weight;
					}
					Point.Double me = new Point.Double(this.x, this.y);
					Point.Double other = new Point.Double(y.x, y.y);
					double angle = Game.anglePoints(other, me);
					iw = Math.min(iw, Game.distPoints(me, other));
					hsp += Math.cos(angle) * iw;
					vsp += Math.sin(angle) * iw;
				}
			}
		}
	}

	public void stepMove() {
		double ivsp = vsp * Game.spdf;
		double ihsp = hsp * Game.spdf;

		double val = ihsp / 10;
		for (int i = 0; i < 10; i++) {
			if (checkClass(Block.class, x + val, y) == null) {
				x += val;
			} else {
				hsp = 0;
				break;
			}
		}
		val = ivsp / 10;
		for (int i = 0; i < 10; i++) {
			if (checkClass(Block.class, x, y + val) == null) {
				y += val;
			} else {
				vsp = 0;
				break;
			}
		}
	}

	public void draw(Graphics2D g2, Object drawer) {
		g2.setColor(Color.white);
		g2.draw(bounds);
		g2.drawString("test", 16, 16);

		dhp += (this.hp - this.dhp) * 0.03;

		g2.drawRect((int) (x - 16), (int) (y - 48), 32, 4);
		g2.setColor(Color.RED);
		g2.fillRect((int) (x - 16), (int) (y - 48), (int) (32 * dhp / maxhp), 4);
		g2.setColor(Color.GREEN);
		g2.fillRect((int) (x - 16), (int) (y - 48), (int) (32 * hp / maxhp), 4);

		// Composite normal = g2.getComposite();
		// g2.setComposite(BlendComposite.Add);
		sprite.draw(g2, x, y, 0);
		// g2.setComposite(normal);
		weapon.xscale = 2 * sprite.xscale;

		if (inrange)
			g2.drawString("?", (int) x, (int) y - 52);

		int k = 1;
		if (weapon.xscale < 0)
			k = -1;

		sight = new Arc2D.Double();
		((Arc2D) sight).setArcByCenter(x, y, sight_radius, -60 - angle * 180
				/ Math.PI, 120, Arc2D.PIE);

		if (ammo == attspd - 1) {
			wep_flip = !wep_flip;
			Arc2D.Double hit = new Arc2D.Double();
			hit.setArcByCenter(x, y, 70, 290 - angle * 180 / Math.PI, 150,
					Arc2D.PIE);
			Hitbox h = new Hitbox(this, hit, 20, 5);
			h.forceX = 12 * Math.cos(angle);
			h.forceY = 12 * Math.sin(angle);
			Game.addEntity(h);
		}
		if (ammo != 0) {
			if (!wep_flip) {
				weapon.yscale = -1;
				weapon.draw(g2, x, y, angle + (k == -1 ? Math.PI : 0) + k
						* Math.PI / 4, (ammo == 0) ? 0 : attspd - ammo);
			} else {
				weapon.yscale = 1;
				weapon.draw(g2, x, y, angle + (k == -1 ? Math.PI : 0) - k
						* Math.PI / 4, (ammo == 0) ? 0 : attspd - ammo);
			}

		}

	}

	public Shape getBounds() {
		return new Ellipse2D.Double(x - w / 2, y - h / 2, w, h);
	}

}
