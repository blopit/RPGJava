package art;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AnimSprite extends Sprite implements ImageObserver {
	private static final long serialVersionUID = -6010514685070784777L;

	int frames;
	int[] frame_data;

	public AnimSprite(String loc, double x, double y, double w, double h,
			int frames) {
		super(loc, x, y);
		setXscale(setYscale(1));
		this.frames = frames;
		this.w = w;
		this.h = h;
		frame_data = new int[frames];
		Arrays.fill(frame_data, 2);

		if (spriteloc != null) {
			System.out.println(spriteloc);
			sprite = new ImageIcon(this.getClass().getResource(spriteloc))
					.getImage();
		} else {
			sprite = null;
		}
	}

	public AnimSprite(String loc, double x, double y, double w, double h,
			int frames, int[] data) {
		this(loc, x, y, w, h, frames);
		assert(data.length == frames);
		frame_data = data;
	}
	
	private int getFrame(int index){
		int total = frame_data[0];
		int idx = 1;
		while(total < index && idx < frames){
			total += frame_data[idx++];
		}
		return idx-1;
	}
	
	private int getFrameLoop(int index){
		int total = frame_data[0];
		int idx = 0;
		
		while(total < index){
			total += frame_data[idx++];
			idx = idx % frames;
		}
		return idx;
	}
	
	public void draw(Graphics2D g2d, double x, double y, double rot, int frame, boolean loop) {
		AffineTransform backup = g2d.getTransform();
		AffineTransform trans = new AffineTransform();
		int xx = (int) (x - offx * getXscale());
		int yy = (int) (y - offy * getYscale());
		int xx2 = (int) (w * getFrame(frame));
				//((loop)?(frame % frames):Math.max(frame, frames-1)));

		int ww = (int) (w * getXscale());
		int hh = (int) (h * getYscale());

		trans.rotate(rot, x, y);
		g2d.transform(trans);

		g2d.drawImage(sprite, xx, yy, (int) (xx + ww), (int) (yy + hh), xx2, 0,
				(int) (xx2 + w), (int) (h), this);

		g2d.setTransform(backup);
	}
	public void draw(Graphics2D g2d, double x, double y, double rot, int frame) {
		this.draw(g2d, x, y, rot, frame, true);
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		w = width;
		h = height;
		return false;
	}
}
