package Roleplaying;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Game;
import Roleplaying.Roleplay.Tag;

public class Background {
	ArrayList<Tag> tags = new ArrayList<Tag>();

	public enum ParentAliveStatus {
		BOTH_ALIVE, FATHER_ONLY, MOTHER_ONLY, BOTH_DEAD
	}

	private ParentAliveStatus chooseParentAliveStatus() {
		int d = 1 + (int) (Math.random() * 100);
		if ((d -= 50) < 0)
			return ParentAliveStatus.BOTH_ALIVE;
		if ((d -= 70) < 0)
			return ParentAliveStatus.FATHER_ONLY;
		if ((d -= 90) < 0)
			return ParentAliveStatus.MOTHER_ONLY;
		return ParentAliveStatus.BOTH_DEAD;
	}

	public static class BirthCircumstance {
		String description;
		byte maxProf = 100;

		public BirthCircumstance() {
			description = "...";
		}

		public static class LowerClass extends BirthCircumstance {
			public LowerClass() {
				maxProf = 40;
				description = fix("[I was born[| among peasants] in[| the slums of]|"
						+ "[We grew|I grew|Grew] up working the land around] {hometown}. "
						+ "[My [pa|ma] taught us how to trade[ and such|]|[We begged|Begged] for our daily bread]. "
						+ "[|It was [|pretty ]unpleasant, but I survived|Not an experience I recommend.]");
			}
		}

		public static class MiddleClass extends BirthCircumstance {
			public MiddleClass() {
				maxProf = 40;
				description = fix("I grew up in {hometown}. "
						+ "It's a [|real |really ]["
						+ "[mediocre|boring] place[|, nothing special]. |"
						+ "[nice|decent] place[|, you should [|go ]visit[| sometime]"
						+ "]. [|There's this[| really nice| lovely] [restaurant|bakery|garden|tavern] [there|in {hometown}] that [I highly recommend|"
						+ "I've been dying to go back to|I used to go to as a child]. It's one of my favourite places[|, really]. ]]"
						+ "[|[As a kid, everyone|Everyone] [I knew |]in {hometown} was [in a guild|in trade|in a guild or trade|in a guild or a trade organization]. ]");
						//+ "[||I never [|really ][got|felt] the [hardship|struggle|struggles] [of|like] some [other people|folkes|people] here[| [nor|but neither] the privledges of [nobility|royalty]]. ]");
			}
		}
	}

	List<String> places = Arrays.asList("Havengard", "Cosmodan", "Smickles");

	byte biological_siblings;
	byte adopted_siblings;
	String hometown;

	public Background() {
		String str = new BirthCircumstance.MiddleClass().description;
		System.out.println(str);
	}

	public static String fix(String str) {
		int passes = 1;
		Matcher m;
		StringBuffer s = null;
		while (passes > 0) {
			passes = 0;
			m = Pattern
					.compile(
							"\\[([^\\[\\]\\|]*?)\\|([^\\[\\]\\|]*?)(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?(?:\\|([^\\[\\]\\|]*?))?\\]")
					.matcher(str);
			s = new StringBuffer();
			while (m.find()) {
				int i;
				for (i = 3; i < 11; i++) {
					if (m.group(i) == null)
						break;
				}
				//System.out.println(m.group(0));
				m.appendReplacement(s, m.group(Game.randInt(1, i - 1)));
				passes++;
			}
			m.appendTail(s);
			str = s.toString();
		}
		return s.toString();
	}

}
