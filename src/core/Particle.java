package core;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import composite.BlendComposite;

public class Particle extends Entity {
	double xsp, ysp;
	protected double gravity;
	protected int life;
	protected int maxlife;
	protected boolean destroy;
	protected Color col;

	public Particle(double sx, double sy, double hsp, double vsp, int li,
			Color c) {
		this.x = sx;
		this.y = sy;
		this.xsp = hsp;
		this.ysp = vsp;
		this.life = (int) (li / Game.spdf);
		this.destroy = false;
		this.maxlife = this.life;
		this.gravity = 0.1 * Game.spdf;
		this.col = c;
		this.depth = -5000;
	}

	public void step() {
		this.life--;
		if (this.life <= 0) {
			this.destroy();
		}
		this.ysp += this.gravity;
		this.x += this.xsp * Game.spdf;
		this.y += this.ysp * Game.spdf;
	}

	public void draw(Graphics2D g2d, Object drawer) {
		

		double sp = Game.distPoints(Game.origin, new Point.Double(this.xsp,
				this.ysp));
		if (sp > 4) {
			g2d.setStroke(new BasicStroke(2));
		} else if (sp > 2) {
			g2d.setStroke(new BasicStroke(3));
		} else {
			g2d.setStroke(new BasicStroke(4));
		}

		g2d.setColor(new Color(this.col.getRed(), this.col.getGreen(), this.col
				.getBlue(), Math.min(
				(int) (1000.0 * ((float) (this.life) / (float) (this.maxlife))),
				255)));
		
		 g2d.drawLine((int) this.x, (int) this.y, (int) (this.x - this.xsp),
		 (int) (this.y - this.ysp));
		 

		g2d.setStroke(new BasicStroke(1));
	}

	public Shape getBounds() {
		return null;
	}
}