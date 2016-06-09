package core.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import core.Game;
import core.battle.Battle.DamageType;
import core.battle.Battle.Unit;

public abstract class Passive {

	public class PassiveComparator implements Comparator<Passive> {
		@Override
		public int compare(Passive o1, Passive o2) {
			return o2.prio.val - o1.prio.val;
		}
	}

	public class PassiveContComparator implements Comparator<PassiveContainer> {
		@Override
		public int compare(PassiveContainer o1, PassiveContainer o2) {
			return o2.passive.prio.val - o1.passive.prio.val;
		}
	}

	public enum LOC {
		none,
		constant,

		att_damage,
		def_damage,

	}

	public enum PRIO {
		SYSTEM(0),
		SPECIAL(1),
		HIGH_ULT(2),
		ULTIMATE(3),
		LOW_ULT(4),
		RARE(5),
		NORMAL(7),
		LOW(9),
		NULL(10);
		private final int val;

		private PRIO(int levelCode) {
			this.val = levelCode;
		}
	}

	public enum BUFFTYPE {
		NONE,
		NEUTRAL,
		BUFF,
		DEBUFF,
		CROWD_CONTROL
	}

	int id;
	ArrayList<LOC> locs = new ArrayList<LOC>(Arrays.asList(LOC.none));
	PRIO prio = PRIO.NORMAL;
	int tick = (int) (Game.TARGET_FPS / 2);
	int dur = -1;
	Unit source;
	BUFFTYPE buff = BUFFTYPE.NONE;
	String name;
	int timer;

	public void init(Unit src, Unit targ) {
		System.out.println(" null INITIALIZED");
	}

	public void expire() {
		System.out.println(" null EXPIRED");
	}

	public void purge() {
		this.expire();
	}

	public void stack() {
		System.out.println(" null STACKED");
	}

	public abstract Map<String, Object> process(Unit src, Unit targ,
			Map<String, Object> list);

	public Passive(String _name, ArrayList<LOC> _locs, BUFFTYPE _buff) {
		name = _name;
		locs = _locs;
		buff = _buff;
	}

	public Passive(String _name, ArrayList<LOC> _locs, BUFFTYPE _buff, int _dur) {
		name = _name;
		locs = _locs;
		buff = _buff;
		dur = (int) (dur * Game.TARGET_FPS);
	}

	public Passive(String _name, ArrayList<LOC> _locs, BUFFTYPE _buff,
			int _dur, int _tick) {
		name = _name;
		locs = _locs;
		buff = _buff;
		dur = (int) (dur * Game.TARGET_FPS);
		tick = _tick;
	}

	public void applyPassive(Unit src, Unit targ, Passive p) {
		Battle.normal = true;

		if (Battle.normal) {
			targ.passives.add(p);
			p.source = src;
		}

		Collections.sort(targ.passives, new PassiveComparator());
	}

	public static void removePassive(Unit src, Unit targ, Passive p) {
		Battle.normal = true;

		if (Battle.normal) {
			p.expire();
			targ.passives.remove(p);
		}
	}

	public static Map<String, Object> tickLOC(LOC loc, Unit src, Unit targ,
			Map<String, Object> list) {
		for (Passive p : src.passives) {
			p.timer++;
			if (p.dur != -1 && p.timer > p.dur) {
				removePassive(src, src, p);
			} else if (p.locs.contains(loc) && p.timer % p.tick == 0) {
				list = p.process(src, targ, list);
			}
		}
		return list;
	}

	public Map<String, Object> executeLOC(LOC loc, Unit src, Unit targ,
			Map<String, Object> list) {
		for (Passive p : src.passives) {
			if (p.locs.contains(loc)) {
				list = p.process(src, targ, list);
			}
		}
		return list;
	}

	private class PassiveContainer {
		Passive passive;
		Unit src;
		Unit targ;
		LOC loc;

		public PassiveContainer(Passive _passive, Unit _src, Unit _targ,
				LOC _loc) {
			passive = _passive;
			src = _src;
			targ = _targ;
			loc = _loc;
		}
	}

	public Map<String, Object> doubleExecuteLOC(LOC locSrc, LOC locTar,
			Unit src, Unit targ, Map<String, Object> list) {
		ArrayList<PassiveContainer> plist = new ArrayList<PassiveContainer>();

		for (Passive p : src.passives) {
			if (p.locs.contains(locSrc)) {
				plist.add(new PassiveContainer(p, src, targ, locSrc));
			}
		}

		for (Passive p : targ.passives) {
			if (p.locs.contains(locTar)) {
				plist.add(new PassiveContainer(p, targ, src, locTar));
			}
		}

		Collections.sort(plist, new PassiveContComparator());

		for (PassiveContainer pc : plist) {
			list = pc.passive.process(pc.src, pc.targ, list);
		}

		return list;
	}

	// /////////////////////////////////////////////////////////

	// MAKES USER INVINCIBLE
	// ---------------------------------------------------------------------
	public class p_Invincible extends Passive {
		public p_Invincible() {
			super("Invincible", new ArrayList<LOC>(
					Arrays.asList(LOC.def_damage)), BUFFTYPE.BUFF);
			this.prio = PRIO.HIGH_ULT;
		}

		@Override
		public Map<String, Object> process(Unit src, Unit targ,
				final Map<String, Object> list) {
			return new HashMap<String, Object>() {
				{
					put("dmg", 0);
					put("mit", 0);
					put("type", DamageType.NONE);
				}
			};
		}
	}

	// PROTECT PERCENT
	// ---------------------------------------------------------------------
	public class p_ProtectionPCT extends Passive {
		double val;

		public p_ProtectionPCT(double reduction) {
			super("Test Protection", new ArrayList<LOC>(
					Arrays.asList(LOC.def_damage)), BUFFTYPE.BUFF);
			this.val = 1 - reduction;
		}

		@Override
		public Map<String, Object> process(Unit src, Unit targ,
				final Map<String, Object> list) {
			return new HashMap<String, Object>() {
				{
					put("dmg", (double) list.get("dmg") * val);
					put("mit", (double) list.get("mit") * val);
					put("type", list.get("type"));
				}
			};
		}
	}

	// Damage over time
	// ---------------------------------------------------------------------
	public static class p_DoT extends Passive {
		double damage;
		DamageType type;

		public p_DoT(double _damage, int _dur, int _tick, DamageType _type) {
			super("Test DoT", new ArrayList<LOC>(Arrays.asList(LOC.constant)),
					BUFFTYPE.DEBUFF);
			this.damage = _damage;
			this.type = _type;
		}

		@Override
		public Map<String, Object> process(Unit src, Unit targ,
				final Map<String, Object> list) {
			Battle.deal_damage(this.source, targ, damage, type);
			return new HashMap<String, Object>() {
			};
		}
	}

	// Heal over time
	// ---------------------------------------------------------------------
	public static class p_HoT extends Passive {
		double heal;

		public p_HoT(double _damage, int _dur, int _tick) {
			super("Test DoT", new ArrayList<LOC>(Arrays.asList(LOC.constant)),
					BUFFTYPE.DEBUFF);
			this.heal = _damage;
		}

		@Override
		public Map<String, Object> process(Unit src, Unit targ,
				final Map<String, Object> list) {
			Battle.heal(this.source, targ, heal);
			return new HashMap<String, Object>() {
			};
		}
	}

}
