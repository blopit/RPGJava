package namegenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NameGenerator {

	public Random random;
	protected List<String> vocals = new ArrayList<String>();
	protected List<String> startConsonants = new ArrayList<String>();
	protected List<String> endConsonants = new ArrayList<String>();
	protected List<String> nameInstructions = new ArrayList<String>();
	protected List<String> femaleEnd = new ArrayList<String>();

	public NameGenerator() {
		random = new Random();
	}

	public NameGenerator(String[] vocals, String[] startConsonants,
			String[] endConsonants) {
		this.vocals.addAll(Arrays.asList(vocals));
		this.startConsonants.addAll(Arrays.asList(startConsonants));
		this.endConsonants.addAll(Arrays.asList(endConsonants));
	}

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
		return (int) (min + (random.nextDouble() * (max + 1 - min)));
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