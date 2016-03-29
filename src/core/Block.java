package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

public class Block extends Entity{

	public Block(int xx, int yy, int ww, int hh){
		super(xx, yy, null);
		this.x = xx;
		this.y = yy;
		this.w = ww;
		this.h = hh;
		bounds = getBounds();
	}

	public void step() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics2D g2, Object drawer) {
		g2.setColor(Color.white);
		g2.draw(bounds);
	}

	public Shape getBounds() {
		return new Rectangle((int)x, (int)y, (int)this.w, (int)this.h);
	}

}
