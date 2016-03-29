package namegenerator;
import java.util.Arrays;

public class NG_Muslim extends NameGenerator {
	public NG_Muslim() {
		// V
		String demoVocals[] = { "a", "i", "o", "aa", "ei", "ai", "ou", "oo",
				 "y", "a", "u", "ey" };

		// C
		String demoStartConsonants[] = { "ab'", "ak'", "akh'", "am", "fa'",
				"hus", "m", "moham", "moj", "n", "om", "sh", "sin", "a'",
				"zan'", "a", "az", "el", "f", "kh", "k", "m", "n", "r", "s",
				"sh", "t", "z", "kh", "k", "gh", "qu", "c", "ch" };

		// D
		String demoEndConsonants[] = { "r", "bar", "bil", "der", "dul", "r",
				"kir", "med", "nir", "sien", "soud", "hin", "hn", "feliz",
				"han", "heh", "h", "ch", "jjan", "khtar", "nit", "rib", "shim",
				"srin", "tim", "zit", "dh", "nan", "nv", "radh", "n", "nik",
				"yr", "rn", "dit", "han", "l", "roh", "rish", "ng", "kshar",
				"ny", "soud" };

		// F
		String demofemaleEnd[] = { "i", "a", "a", "a", "aya", "ai", "ali",
				"ah", "a", "ey", "ay", "i", "ah", "ah", "ya", "iya", };

		String nameInstructions[] = { "cvd", "cvdf" };

		this.vocals.addAll(Arrays.asList(demoVocals));
		this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
		this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
		this.nameInstructions.addAll(Arrays.asList(nameInstructions));
		this.femaleEnd.addAll(Arrays.asList(demofemaleEnd));
	}
}
