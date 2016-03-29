package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;

public class Hitbox extends Entity {
	Shape shape;
	int life;
	Entity owner;
	ArrayList<Entity> hitlist;
	double forceX, forceY;
	int damage;

	public Hitbox(Entity owner, Shape shape, int damage, int life) {
		this.shape = shape;
		this.life = life;
		this.owner = owner;
		this.damage = damage;
		this.hitlist = new ArrayList<Entity>();
		forceX = forceY = 0;
	}

	public void step() {
		life--;
		if (life < 0) {
			this.room.removeEntity(this);
		}
		ArrayList<Entity> arru = checkClassList(Unit.class, x, y);
		for (Entity e : arru) {
			Unit u = (Unit) e;
			if (u != null && u != owner && !hitlist.contains(u)) {
				u.hp -= damage;
				hitlist.add(u);
				u.hsp += forceX;
				u.vsp += forceY;
				double a = Game.anglePoints(owner.getEntityPoint(),
						u.getEntityPoint());
				for (int i = 0; i < 10; i++)
					Game.addEntity(new Particle(u.x + 8 * Math.cos(a+Math.random()-0.5), u.y
							- 24 + 8 * Math.sin(a+Math.random()-0.5), 8 * Math.random()
							* Math.cos(a), 8 * Math.random() * Math.sin(a), (int) (8+5*Math.random()),
							Color.RED));
			}
		}

	}

	public void draw(Graphics2D g2, Object drawer) {
		// g2.setColor(Color.red);
		// g2.draw(bounds);
	}

	public Shape getBounds() {
		return shape;
	}

}
