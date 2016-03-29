package core;

import java.awt.Image;

import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

public abstract class Entity {
	double x, y, z, w, h, zlen;
	int depth;
	Sprite sprite;
	Shape bounds;
	boolean visible = true;
	boolean active = true;
	Room room = null;
	
	public Entity() {
		x = y = w = h = z = zlen = 0.0;
		depth = 0;
		sprite = null;
		sprite = null;
		this.bounds = getBounds();
	}
	
	public Entity(int _x, int _y, int _w, int _h) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		z = zlen = 0.0;
		depth = 0;
		sprite = null;
		sprite = null;
		this.bounds = getBounds();
	}

	public Entity(int x, int y, Sprite spr) {
		w = h = z = zlen = 0.0;
		this.x = x;
		this.y = y;
		this.z = 0;
		this.sprite = spr;
		this.bounds = getBounds();
	}

	public abstract void step();

	public void destroy() {
		Game.removeEntity(this);
	}

	public void collision(Entity x) {
	}
	
	public abstract void draw(Graphics2D g2, Object drawer);
	public abstract Shape getBounds();


	public final static void sort(ArrayList<Entity> arraylist) {
		for (int i = 0; i < arraylist.size() - 1; i++) {
			for (int j = i + 1; j < arraylist.size(); j++) {
				if (arraylist.get(i).depth < arraylist.get(j).depth) {
					Entity temp = arraylist.get(i);
					arraylist.set(i, arraylist.get(j));
					arraylist.set(j, temp);
				}
			}
		}
	}

	public final void setImage(Sprite spr) {
		if (spr != null) {
			sprite = spr;
		}
	}

	public final void instanceDestroy() {
		for (int i = 0; i < Game.getObjects().size(); i++) {
			if (Game.getObjects().get(i) == this)
				Game.getObjects().remove(i);
		}
		destroy();
	}

	public final static void destroyAll() {
		//Game.getObjects() = null;
	}

	public Entity checkClass(Class<?> cls, double xx, double yy) {
		double tempx = this.x;
		double tempy = this.y;
		this.x = xx;
		this.y = yy;
		
		Shape bnd = getBounds();

		if (bnd != null) {
			for (Entity y : Game.getObjects()) {
				if (y.bounds != null && y.getClass().equals(cls)) {
					if (Game.testIntersection(bnd, y.bounds) && this != y) {
						this.x = tempx;
						this.y = tempy;
						return y;
					}
				}
			}
		}

		this.x = tempx;
		this.y = tempy;
		return null;
	}	
	
	public ArrayList<Entity> checkClassList(Class<?> cls, double xx, double yy) {
		double tempx = this.x;
		double tempy = this.y;
		this.x = xx;
		this.y = yy;
		ArrayList<Entity> ret = new ArrayList<Entity>();
		Shape bnd = getBounds();

		if (bnd != null) {
			for (Entity y : Game.getObjects()) {
				if (y.bounds != null && y.getClass().equals(cls)) {
					if (Game.testIntersection(bnd, y.bounds) && this != y) {
						this.x = tempx;
						this.y = tempy;
						ret.add(y);
					}
				}
			}
		}

		this.x = tempx;
		this.y = tempy;
		return ret;
	}
	
	public Point.Double getEntityPoint(){
		return new Point.Double(this.x,this.y);
	}
	
	public boolean checkEntity(Entity e, double xx, double yy) {
		double tempx = this.x;
		double tempy = this.y;
		this.x = xx;
		this.y = yy;
		
		Shape bnd = getBounds();

		if (bnd != null && e.bounds!= null) {
			if (Game.testIntersection(bnd, e.bounds) && this != e) {
				this.x = tempx;
				this.y = tempy;
				return true;
			}
		}

		this.x = tempx;
		this.y = tempy;
		return false;
	}
}
