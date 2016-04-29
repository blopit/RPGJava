package core.battle;

import core.battle.Battle.Animation;
import core.battle.Battle.Unit;

public abstract class Ability {
	double cast_time;
	double wait_time;
	int res_cost;
	Targeting targeting;
	double cooldown;
	Animation animation;
	Unit owner;

	public enum Targeting {
		DIRECT, ROW, COLUMN, ALL
	}

	public Ability(Unit o, double ct, double wt, int cost, double cd) {
		owner = o;
		cast_time = ct;
		wait_time = wt;
		res_cost = cost;
		animation = null;
	}

	public abstract void activate(Unit source, Unit target);

	public void beginCast(Unit source, Unit target) {
		if (source.RES >= res_cost) {
			//activate(source, target);
			source.beginCast(cast_time, wait_time, res_cost);
		}
	}

	public void doneCasting(Unit source, Unit target){
		activate(source, target);
	}
	
	public static class Bolt extends Ability {
		public Bolt(final Unit o) {
			super(o, 1.0, 4.0, 0, 0);
			animation = new Animation(o, 30, new AnimInfo() {
				public void update(int frame) {
					if (frame == 15)
						activate(o, o.ABtarget);
				}
			});
		}

		public void activate(Unit source, Unit target) {
			Battle.deal_damage(source, target, 100, Battle.DamageType.PHYS);
		}
	}

	public static class Chop extends Ability {
		public Chop(final Unit o) {
			super(o, 0, 2.65, 0, 0);
			animation = new Animation(o, 30, new AnimInfo() {
				public void update(int frame) {
					if (frame == 15 || frame == 20)
						activate(o, o.ABtarget);
				}
			});
		}

		public void activate(Unit source, Unit target) {
			Battle.deal_damage(source, target, 50, Battle.DamageType.PHYS);
		}
	}
}
