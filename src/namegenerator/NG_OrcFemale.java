package namegenerator;
import java.util.Arrays;

public class NG_OrcFemale extends NameGenerator {
	public NG_OrcFemale() {
		String demoVocals[] = { "a", "u", "o" };

		String demoStartConsonants[] = { "at", "az", "bag", "bar", "bak",
				"baz", "bash", "bog", "bol", "bor", "both", "bug", "bum",
				"bur", "dul", "dum", "dur", "gar", "gash", "gham", "ghor",
				"gl", "kh", "khag", "lor", "mot", "mug", "nag", "nar", "nash",
				"shar", "og", "ug", "um", "ur", "yag", "yam", "yash", "b", "k",
				"g", "d", "m", "n" };

		String demoEndConsonants[] = { "lg", "rz", "sh", "k", "zub", "gol",
				"tur", "l", "z", "kh", "rz", "th", "ph" };

		String demofemaleEnd[] = { "a", "ob", "i", "ah", "ai", "sai", "ahk" };

		String nameInstructions[] = { "cvdf", "cf"};

		this.vocals.addAll(Arrays.asList(demoVocals));
		this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
		this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
		this.nameInstructions.addAll(Arrays.asList(nameInstructions));
		this.femaleEnd.addAll(Arrays.asList(demofemaleEnd));
	}
}
