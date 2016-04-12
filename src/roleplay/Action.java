package roleplay;

import java.util.ArrayList;

import roleplay.Roleplay.Emote;
import roleplay.Roleplay.Tag;

public abstract class Action {
	protected class ActionTag {
		Tag tag;
		short value;

		public ActionTag(Tag t, int val) {
			tag = t;
			value = (short) val;
		}
	}

	protected ArrayList<ActionTag> tags = new ArrayList<ActionTag>();
	public ArrayList<String> phrases = new ArrayList<String>();
	protected final String name;
	protected final short[] mod;
	protected final byte expected_rel;
	protected final Emote category;

	public Action(String name_, short[] mod_, int erel, Emote cat) {
		name = name_;
		expected_rel = (byte) erel;
		mod = mod_;
		category = cat;

	}

	public int chance(short[] m, ArrayList<Tag> t) {
		int ret = 500;

		for (int i = 0; i < 5; i++) {
			// System.out.println(((double) mod[i]) / 100 * (int) m[i]);
			ret += ((double) mod[i]) / 50 * (int) m[i];
		}
		if (t != null) {
			for (ActionTag at : tags) {
				if (t.contains(at.tag)) {
					ret += at.value;
				}
			}
		}
		return ret / 10;
	};

	public String getName() {
		return name;
	}

	//0: // inventive/curious vs. consistent/cautious 
	//1: // efficient/organized vs.easy-going/careless 
	//2; // outgoing/energetic vs. solitary/reserved 
	//3: // friendly/compassionate vs. analytical/detached 
	//4; // sensitive/nervous vs. secure/confident

	public static class Pet extends Action {
		public Pet() {
			super("pet", new short[] { 150, -30, 80, 80, 0 }, 10, Emote.EXCITE);
			tags.add(new ActionTag(Tag.BEAST, 200));
			tags.add(new ActionTag(Tag.DOESNT_LIKE_TO_BE_TOUCHED, -80));
			phrases.add("You pet {name}");
			phrases.add("You gentle caress {plural_name} mane");
		}
	}

	public static class Insult extends Action {
		public Insult() {
			super("insult", new short[] { 150, -30, 80, 80, 0 }, 10,
					Emote.TAUNT);
			// tags.add(new ActionTag(Tag.BEAST, 200));
			// tags.add(new ActionTag(Tag.DOESNT_LIKE_TO_BE_TOUCHED, -80));
			phrases.add("\"Your mom smells bad\"");
			// phrases.add("You gentle caress {plural_name} mane");
		}
	}

}
