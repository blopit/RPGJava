package core.battle;

import core.battle.Battle.Unit;

import java.awt.Point;
import java.util.concurrent.Callable;

import core.Game;

public class BasicHit implements AnimInfo {
	Unit o;
	Callable<Void> func;

	public void update(int frame) {

		if (frame == 5) {
			try {
				func.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Point.Double v = null;

		if (frame <= 5) {
			double d = frame * 1.0 / 5;
			v = Game.smoothSlope(o.startX, o.startY, o.ABtarget.startX,
					o.ABtarget.startY, d);
			o.x = (int) v.x;
			o.y = (int) v.y;
		} else if (frame >= 10) {
			double d = (frame - 10) * 1.0 / 10;
			v = Game.smoothSlope(o.ABtarget.startX, o.ABtarget.startY,
					o.startX, o.startY, d);
			o.x = (int) v.x;
			o.y = (int) v.y;
		}

	}

}
