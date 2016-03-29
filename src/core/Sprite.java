package core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Sprite extends JFrame implements ImageObserver {
	private static final long serialVersionUID = -6010514685070784777L;
	
	String spriteloc;
	Image sprite;
	double offx, offy, w, h = 0;
	double xscale, yscale;
	Entity ma = null;

	public Sprite(String loc, double ox, double oy) {
		spriteloc = loc;
		offx = ox;
		offy = oy;
		xscale = yscale = 1;

		if (spriteloc != null) {
			System.out.println(spriteloc);
			sprite = new ImageIcon(this.getClass().getResource(spriteloc))
					.getImage();
			w = sprite.getWidth(this);
			h = sprite.getHeight(this);
		} else {
			sprite = null;
		}

	}

	public void draw(Graphics2D g2d, double x, double y, double rot) {
		AffineTransform backup = g2d.getTransform();
		AffineTransform trans = new AffineTransform();
		int xx = (int) (x - offx * xscale);
		int yy = (int) (y - offy * yscale);

		trans.rotate(rot, x, y);

		g2d.transform(trans);
		g2d.drawImage(sprite, xx, yy, (int) (w * xscale), (int) (h * yscale),
				this);

		g2d.setTransform(backup);
	}
	
	public void draw(Graphics2D g2d, double x, double y, double rot, int frame) {
		this.draw(g2d, x, y, rot);
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		w = width;
		h = height;
		return false;
	}
}
