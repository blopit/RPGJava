package Roleplaying;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import core.Action;
import core.DialogBox;
import core.Game;
import core.Unit;

public class Roleplay {
	public enum Tag {
		HUMANOID, BEAST, ROYALTY, DEMON, PLANT, UNDEAD, MACHINE, ELEMENTAL, PRIDE, LUST /* FLIRT_PRONE */, ENVY, GREED /* BRIBE_PRONE */, WRATH, SLOTH, GLUTTONY, HUMILITY /* SHAME_PRONE */, CHASITY, KINDNESS, CHARITY, PATIENCE, DILIGENCE, TEMPERANCE, RACIST, SJW, INTIMIDATE_PRONE, TAUNT_PRONE, EXCITE_PRONE, HUMOR_PRONE, PLEAD_PRONE, GAIN_PITY_PRONE, FEIGN_INNOCENCE_PRONE, NAIVE /* TRICK_PRONE */, CONFUSE_PRONE, DIVERT_PRONE, REASON_PRONE, BANDWAGON_PRONE, RATIONALIZE_PRONE, DEPENDANT, IMMATURE, IMPRESSIONABLE, TRUSTING, LONELY, ALTRUISTIC, PSYCHOPATH, FRUGAL, MASOCHISTIC, DOESNT_LIKE_TO_BE_TOUCHED,

		ORPHAN
	}

	public enum Gender {
		MALE, FEMALE
	}

	ArrayList<Tag> tags = new ArrayList<Tag>();

	public enum Response {
		NEUTRAL, HAPPY_THANKFUL, ANGRY_INTIMIDATE, CONFUSION_DECEPTION, DISGUST_SARCASM, FEARFUL_SURPRISED_SAD
	}

	public enum Emote {
		NULL, INTIMIDATE, TAUNT /* make angry */, SHAME, EXCITE, FLIRT /* charm */, HUMOR, PLEAD, GAIN_PITY, FEIGN_INNOCENCE, TRICK /* lie */, CONFUSE /* rambling */, DIVERT, REASON, BANDWAGON, RATIONALIZE, BRIBE, GIFT
	}

	Map<Emote, String> success = new HashMap<Emote, String>();
	Map<Emote, String> failure = new HashMap<Emote, String>();
	String action = "okey do action";
	String ask = "okey what is question?";

	int age;
	short trait_Openness; // inventive/curious vs. consistent/cautious
	short trait_Conscientiousness; // efficient/organized vs.easy-going/careless
	short trait_Extraversion; // outgoing/energetic vs. solitary/reserved
	short trait_Agreeableness; // friendly/compassionate vs. analytical/detached
	short trait_Neuroticism; // sensitive/nervous vs. secure/confident

	byte init_like;
	String init = null;
	ArrayList<RelEdge> rel_edges;
	ArrayList<Action> actions;
	String name, pro, prs;
	String[] opener;
	double txtspd = 0.2;
	double patience = 10.0;
	Node root = null;
	Unit owner;

	private class RelEdge {
		Unit a;
		Unit b;
		byte aff_a;
		byte aff_b;
		long last_meeting;

		public RelEdge(Unit _a, Unit _b) {
			a = _a;
			b = _b;
			aff_a = _a.role.init_like;
			aff_b = _b.role.init_like;
			last_meeting = 0;
		}
	}

	public class Edge {
		Node dest;
		String name;
		String desc;

		public Edge(Node _dest, String _name, String _desc) {
			dest = _dest;
			desc = _desc;
			name = _name;
		}
		
		public void activate(Unit target){
			if (name == "Perform"){
				dest.edges.clear();
				for (Action a : target.role.actions){
					dest.edges.add(new EdgeAct(null, a));
				}
			}
			if (dest != null)
				Game.addEvent(dest.createDialog(target, txtspd));
		}
	}

	private class EdgeRes extends Edge {
		Response type;

		public EdgeRes(Node _dest, Response t, String d) {
			super(_dest, d, "ERROR NAMING RES");
			type = t;
			switch (type) {
			case NEUTRAL:
				name = ":|";
				break;
			case HAPPY_THANKFUL:
				name = ":)";
				break;
			case ANGRY_INTIMIDATE:
				name = ">:(";
				break;
			case CONFUSION_DECEPTION:
				name = ":S";
				break;
			case DISGUST_SARCASM:
				name = ":/";
				break;
			case FEARFUL_SURPRISED_SAD:
				name = ":(";
				break;
			default:
				name = "ERROR NAMING RES";
				break;
			}
		}
	}

	private class EdgeAct extends Edge {
		Action type;

		public EdgeAct(Node _dest, Action t) {
			super(_dest, t.getName(), "ERROR NAMING ACT");
			type = t;
			
			Random random = new Random();
			desc = type.phrases.get(random.nextInt(type.phrases.size()));
		}
	}

	private class Node {
		ArrayList<Edge> edges;
		String desc;

		public Node(String d) {
			desc = d;
			edges = new ArrayList<Edge>();
		}

		public DialogBox createDialog(Unit target, double spd) {
			DialogBox dia = new DialogBox(this.desc, owner, target, spd);
			for (Edge e : edges) {
				dia.buttons.add(new DialogBox.Button(e.name, e.desc, e));
			}
			return dia;
		}
	}
	
	public DialogBox startDialog(Unit target){
		return root.createDialog(target, 999);
	}

	private class HUB extends Node {
		public HUB() {
			super(name);
			Node act = new Node(action);
			Node que = new Node(ask);
			edges.add(new Edge(act, "Perform", "Choose from ACTIONS to Perform"));
			edges.add(new Edge(que, "Ask", "Choose from QUESTIONS to Ask"));
		}
	}

	public void performAction(Action a) {
		int chance = a.chance(new short[] { this.trait_Openness,
				this.trait_Conscientiousness, this.trait_Extraversion,
				this.trait_Agreeableness, this.trait_Neuroticism },
				this.tags);
		System.out.println("Good chance: " + chance);

		int roll = (int) (Math.random() * 100);
		if (roll <= chance) {
			System.out.println("SUCCESS!");
		}
	}

	public String stringRep(String str) {
		str.replaceAll("{name}", name);
		str.replaceAll("{NAME}", name.toUpperCase());
		str.replaceAll("{plural_name}", Game.possessiveForm(name));
		str.replaceAll("{pro}", pro);
		str.replaceAll("{prs}", prs);
		return str;
	}

	public enum affinity {
		HATE, DISLIKE, NEUTRAL, LIKE, LOVE
	}

	public Roleplay(Unit u) {
		owner = u;
		actions = new ArrayList<Action>();
	}

	public static class rpBarbarian_Goltar extends Roleplay {
		public rpBarbarian_Goltar(Unit u) {
			super(u);
			name = "Goltar";
			pro = "he";
			prs = "his";
			age = 25;
			root = new HUB();
			
			actions.add(new Action.Pet());
			actions.add(new Action.Insult());
			
			opener = new String[] {
					"\"NO TIME FOR TALK! ONLY KILL!\"",
					"\"GO! Goltar does not wish to speak to the likes of you!\"",
					"\"What you want?\"", "\"What do you wish from Goltar?\"",
					"\"You know, you are Goltar favourite\"",
					"\"Goltar missed you friend!\"",
					"\"Goltar have bad day, you come back later ok?\"" };
			failure.put(Emote.NULL, "...");
			success.put(Emote.NULL, "...");

			trait_Openness = -80;
			trait_Conscientiousness = -35;
			trait_Extraversion = -60;
			trait_Agreeableness = -50;
			trait_Neuroticism = -80;

			success.put(Emote.EXCITE,
					"\"You amuse {name}\", {name} insists you continue");
			success.put(
					Emote.FLIRT,
					"\"...\", {name} blushes, {pro} has never felt this way before, {pro} is at a loss for words");
			success.put(Emote.INTIMIDATE,
					"\"I will let you have this one but, mark my words, I will return\"");
			success.put(Emote.TRICK, "\"You bore {name}, begone!\"");
			success.put(Emote.GAIN_PITY, "\"You bore {name}, begone!\"");
			success.put(Emote.REASON,
					"\"{name} understand your point of view\"");
			success.put(Emote.BRIBE, "\"This offering pleases {name}\"");

			failure.put(Emote.EXCITE,
					"{name} flares {prs} nostrils. {name} is not amused");
			failure.put(Emote.INTIMIDATE, "\"YOU DARE CHALLENGE {NAME}?\"");
			failure.put(Emote.TRICK, "\"You try to trick {NAME}!?\"");
			failure.put(Emote.GAIN_PITY,
					"\"You weak, {name} will kill you quickly!\"");
			failure.put(Emote.REASON,
					"\"{name} not understand, {NAME} ANGRY!\"");
			failure.put(Emote.BRIBE, "{name} spits at your offer");

			rel_edges = new ArrayList<RelEdge>();
			tags.add(Tag.HUMANOID);
			tags.add(Tag.WRATH);
			tags.add(Tag.BEAST);
		}
	}
}
