package namegenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameGenerator {

	protected List<String> vocals = new ArrayList<String>();
	protected List<String> startConsonants = new ArrayList<String>();
	protected List<String> endConsonants = new ArrayList<String>();
	protected List<String> nameInstructions = new ArrayList<String>();
	protected List<String> femaleEnd = new ArrayList<String>();

	public NameGenerator() {
		/*
		 * String demoVocals[] = { "a", "e", "i", "o", "u", "ei", "ai", "ou",
		 * "oo", "y", "oi", "au" };
		 * 
		 * String demoStartConsonants[] = { "b", "c", "d", "f", "g", "h", "k",
		 * "l", "m", "n", "p", "qu", "r", "s", "t", "v", "w", "x", "z", "ch",
		 * "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh", "th" };
		 * 
		 * String demoEndConsonants[] = { "b", "d", "f", "g", "h", "k", "l",
		 * "m", "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
		 * "sh", "th", "tt", "ss", "pf", "nt" };
		 * 
		 * String demofemaleEnd[] = { "a", "ey", "ay", "i", "ah" };
		 * 
		 * String nameInstructions[] = { "cvdvd", "cvd", "vdvd", "cvdvdf",
		 * "cvdf", "vdvdf" };
		 */

		// ORC
		/*
		 * String demoVocals[] = { "a", "u", "o" };
		 * 
		 * String demoStartConsonants[] = { "at", "az", "bag", "bar", "bak",
		 * "baz", "bash", "bog", "bol", "bor", "both", "bug", "bum", "bur",
		 * "dul", "dum", "dur", "gar", "gash", "gham", "ghor", "gl", "kh",
		 * "khag", "lor", "mot", "mug", "nag", "nar", "nash", "shar", "og",
		 * "ug", "um", "ur", "yag", "yam", "yash", "b", "k", "g", "d", "m", "n"
		 * };
		 * 
		 * String demoEndConsonants[] = { "lg", "rz", "sh", "k", "zub", "gol",
		 * "tur", "l", "z", "kh", "rz", "th", "ph" };
		 * 
		 * String demofemaleEnd[] = { "a", "ob", "i", "ah", "ai", "sai", "ahk"
		 * };
		 * 
		 * String nameInstructions[] = {"cvd", "cvdf", "cvdvd" };
		 */

		/*
		 * //ROMANIC? String demoVocals[] = { "a", "e", "i", "o", "ei", "ie",
		 * "ee", "a", "e", "i", "o"};
		 * 
		 * String demoStartConsonants[] = { "b", "ch", "d", "f", "g", "h", "j",
		 * "l", "m", "n", "p", "r", "t", "w", "ch", "bl", "br", "th" };
		 * 
		 * String demoEndConsonants[] = { "b", "d", "f", "g", "h", "k", "l",
		 * "m", "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
		 * "sh", "th", "tt", "ss", "pf", "nt" };
		 * 
		 * 
		 * String demofemaleEnd[] = { "a", "ey", "ay", "i", "ah" };
		 * 
		 * String nameInstructions[] = { "cvdvd", "cvd", "vdvd", "cvdvdf",
		 * "cvdf", "vdvdf" };
		 */
	}

	/**
	 * 
	 * The names will look like this
	 * (v=vocal,c=startConsonsonant,d=endConsonants): vd, cvdvd, cvd, vdvd
	 * 
	 * @param vocals
	 *            pass something like {"a","e","ou",..}
	 * @param startConsonants
	 *            pass something like {"s","f","kl",..}
	 * @param endConsonants
	 *            pass something like {"th","sh","f",..}
	 */
	public NameGenerator(String[] vocals, String[] startConsonants,
			String[] endConsonants) {
		this.vocals.addAll(Arrays.asList(vocals));
		this.startConsonants.addAll(Arrays.asList(startConsonants));
		this.endConsonants.addAll(Arrays.asList(endConsonants));
	}

	/**
	 * see {@link NameGenerator#NameGenerator(String[], String[], String[])}
	 * 
	 * @param vocals
	 * @param startConsonants
	 * @param endConsonants
	 * @param nameInstructions
	 *            Use only the following letters:
	 *            (v=vocal,c=startConsonsonant,d=endConsonants)! Pass something
	 *            like {"vd", "cvdvd", "cvd", "vdvd"}
	 */
	public NameGenerator(String[] vocals, String[] startConsonants,
			String[] endConsonants, String[] nameInstructions) {
		this(vocals, startConsonants, endConsonants);
		this.nameInstructions.addAll(Arrays.asList(nameInstructions));
	}

	private boolean hasVowel(String str) {
		return (str.contains("a") || str.contains("e") || str.contains("i")
				|| str.contains("o") || str.contains("u"));

	}

	public String getName() {
		String ret = firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));
		while (ret.length() > 14 || !hasVowel(ret))
			ret = firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));
		return ret;
	}

	private int randomInt(int min, int max) {
		return (int) (min + (Math.random() * (max + 1 - min)));
	}

	private String getNameByInstructions(String nameInstructions) {
		String name = "";
		int l = nameInstructions.length();
		String o = nameInstructions;
		for (int i = 0; i < l; i++) {
			char x = nameInstructions.charAt(0);
			switch (x) {
			case 'v':
				name += getRandomElementFrom(vocals);
				break;
			case 'c':
				name += getRandomElementFrom(startConsonants);
				break;
			case 'd':
				name += getRandomElementFrom(endConsonants);
				break;
			case 'f':
				name += getRandomElementFrom(femaleEnd);
				break;
			}

			nameInstructions = nameInstructions.substring(1);
		}
		return name;
	}

	private String firstCharUppercase(String name) {
		return Character.toString(name.charAt(0)).toUpperCase()
				+ name.substring(1);
	}

	private String getRandomElementFrom(List v) {
		return (String) v.get(randomInt(0, v.size() - 1));
	}

}