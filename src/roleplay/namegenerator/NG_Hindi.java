package roleplay.namegenerator;
import java.util.Arrays;

public class NG_Hindi extends NameGenerator {
	public NG_Hindi() {
		String demoVocals[] = { "a", "e", "i", "o", "ei", "ie", "ee", "a", "e",
				"i", "o" };

		String demoStartConsonants[] = { "b", "ch", "d", "f", "g", "h", "j",
				"l", "m", "n", "p", "r", "t", "w", "ch", "bl", "br", "th" };

		String demoEndConsonants[] = { "b", "d", "f", "g", "h", "k", "l", "m",
				"n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
				"sh", "th", "tt", "ss", "pf", "nt" };

		String demofemaleEnd[] = { "a", "ey", "ay", "i", "ah" };

		String nameInstructions[] = { "cvd", "cvdf" };
						
		this.vocals.addAll(Arrays.asList(demoVocals));
		this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
		this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
		this.nameInstructions.addAll(Arrays.asList(nameInstructions));
		this.femaleEnd.addAll(Arrays.asList(demofemaleEnd));
	}
}
