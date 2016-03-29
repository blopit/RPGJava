package namegenerator;
import java.util.Arrays;

public class NG_AboriginalMale extends NameGenerator {
	public NG_AboriginalMale() {
		String demoVocals[] = { "u", "e", "o", "a", "e", "i", "ia", "e", "i",
				"i", "o", "a", "oo", "ay", "ey", "i", "i", "i", "a", "a", "e",
				"e", "e", "i", "iy", "o", "o", "o", "y", "a", "e", "y", "o",
				"o", "e", "a", "a", "o", "a", "e", "e", "ia", "ia", "i", "o",
				"o", "o", "o", "o", "o", "a", "e", "e", "a", "o", "a", "a",
				"ey", "i", "i", "o", "o", "o", "u", "uy", "a", "a", "e", "ey",
				"i", "o", "o", "o", "oo", "a", "a", "a", "a", "a", "a", "o",
				"a", "a", "e", "a", "e", "i", "i", "i", "i", "o", "o", "o",
				"o", "o", "o", "u", "u", "a", "a", "ai", "a", "a", "a", "a",
				"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "ay", "a",
				"a", "e", "i", "i", "i", "i", "i", "i", "i", "a", "i", "o",
				"o", "a", "a", "i", "e", "o", "a", "a", "a", "a", "a", "a",
				"a", "ay", "ay", "i", "i", "ua", "a", "a", "a", "a", "u", "a",
				"e", "a", "a", "a", "a", "e", "o", "i", "i", "i", "o", "a",
				"a", "a", "a", "a", "ee", "e", "e", "o", "ooa", "u", "u", "u",
				"u", "u", "a", "a", "i", "a", "i", "a", "a", "oo", "a", "a",
				"a", "a", "a", "a", "a", "a", "a", "a", "e", "i", "e", "i",
				"e", "i", "i", "i", "i", "i", "u", "i", "u", "y", };
		String demoStartConsonants[] = { "ad", "aen", "ah", "ahm", "ah", "ak",
				"ak", "ak", "ak", "al", "alm", "al", "an", "anp", "ans", "an",
				"ap", "ap", "ap", "av", "aw", "b", "b", "b", "ch", "ch", "ch",
				"ch", "ch", "ch", "c", "d", "d", "d", "d", "d", "d", "d", "d",
				"d", "d", "ech", "e", "em", "en", "en", "ey", "f", "g", "g",
				"h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h",
				"h", "h", "h", "h", "h", "h", "h", "h", "ich", "ich", "ig",
				"ik", "im", "int", "is", "it", "it", "izt", "j", "j", "k", "k",
				"k", "k", "k", "k", "k", "k", "k", "k", "k", "kn", "k", "k",
				"k", "k", "k", "k", "l", "l", "l", "l", "l", "l", "l", "l",
				"l", "l", "l", "l", "l", "l", "m", "m", "m", "m", "m", "m",
				"m", "m", "m", "m", "m", "m", "m", "m", "m", "m", "m", "m",
				"m", "m", "m", "m", "m", "m", "m", "m", "m", "m", "m", "n",
				"n", "n", "n", "n", "n", "n", "n", "n", "n", "n", "n", "n",
				"n", "n", "n", "n", "n", "n", "n", "n", "n", "n", "n", "n",
				"n", "n", "n", "n", "n", "n", "n", "n", "od", "og", "og", "oh",
				"oh", "ol", "ot", "ot", "o", "oy", "oyam", "oy", "p", "p", "p",
				"p", "p", "p", "p", "p", "p", "q", "r", "r", "s", "s", "s",
				"s", "s", "sh", "sh", "sh", "sh", "sh", "s", "s", "sk", "sk",
				"s", "s", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t",
				"t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "uz",
				"v", "v", "v", "w", "w", "w", "w", "w", "w", "w", "w", "w",
				"w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
				"w", "w", "w", "w", "w", "w", "w", "y", "y", "yosk", "yot",
				"y", "y", "z", };
		String demoEndConsonants[] = { "s", "ch", "w", "l", "n", "m", "t", "d",
				"m", "n", "d", "nk", "w", "t", "m", "r", "q", "k", "s", "g",
				"ls", "m", "ch", "h", "s", "w", "h", "t", "ch", "sh", "v", "k",
				"nd", "w", "s", "l", "s", "m", "w", "n", "k", "n", "n", "t",
				"t", "w", "hk", "k", "n", "l", "lm", "n", "j", "n", "t", "h",
				"s", "r", "k", "ng", "sh", "w", "h", "n", "n", "t", "h", "hk",
				"n", "t", "sk", "z", "bl", "n", "sh", "t", "ch", "k", "l",
				"ng", "hk", "j", "l", "m", "t", "t", "r", "g", "h", "r", "k",
				"k", "k", "m", "m", "n", "nt", "p", "sh", "sh", "sh", "t", "w",
				"wk", "b", "g", "j", "k", "k", "k", "m", "k", "t", "d", "t",
				"k", "l", "t", "k", "w", "d", "m", "t", "k", "t", "m", "tw",
				"t", "l", "n", "n", "h", "k", "k", "r", "nd", "w", "n", "n",
				"hw", "nd", "p", "t", "k", "w", "r", "ng", "k", "s", "t", "w",
				"t", "l", "nn", "k", "nt", "k", "k", "p", "m", "b", "hch",
				"nks", "hch", "nt", "hk", "hk", "w", "j", "k", "l", "m", "mbl",
				"n", "g", "n", "ht", "n", "h", "n", "m", "l", "n", "c", "c",
				"ch", "c", "l", "l", "t", "n", "n", };
		String demofemaleEnd[] = { "ahy", "oh", "anu", "ik", "aka", "ando",
				"eta", "ocha", "ule", "am", "ika", "an", "oki", "ona", "ale",
				"unka", "ani", "on", "an", "ali", "an", "agi", "ak", "away",
				"ashtay", "ava", "on", "enne", "al", "apa", "ala", "ota", "an",
				"an", "in", "othi", "ali", "in", "an", "an", "ami", "ow",
				"isu", "al", "apay", "eto", "ota", "ik", "omda", "en", "an",
				"an", "an", "iin", "aku", "elki", "utu", "ovi", "atha", "into",
				"un", "ama", "on", "ovi", "osa", "ah", "oto", "an", "owi",
				"ute", "ante", "ik", "asho", "an", "asu", "us", "emu", "an",
				"ale", "ali", "acy", "on", "aga", "anu", "en", "angee", "ele",
				"an", "ibbe", "iche", "ika", "ika", "an", "on", "ana", "ola",
				"ono", "umi", "uk", "ani", "ota", "undo", "anu", "en", "enno",
				"aro", "ati", "ise", "anu", "an", "okni", "an", "ato", "ah",
				"away", "ah", "ahpee", "akya", "ipi", "ona", "one", "aska",
				"asou", "ato", "ah", "aska", "en", "ikla", "ante", "ika",
				"asi", "ap", "en", "isu", "ave", "ag", "imo", "uso", "ona",
				"ave", "ega", "ata", "aco", "an", "ele", "ahko", "ahma", "en",
				"in", "os", "ota", "id", "iid", "ose", "an", "ayshni", "uk",
				"oba", "aani", "at", "aw", "ati", "ylay", "yra", "eka", "osho",
				"aw", "an", "uga", "an", "iq", "iti", "in", "is", "in", "aku",
				"ota", "eesha", "ima", "anzee", "ah", "an", "an", "aktay",
				"uray", "am", "al", "ate", "oon", "at", "asu", "on", "in",
				"at", "ah", "an", "on", "ah", "ini", "axka", "ele", "ima",
				"uta", "iin", "ati", "ah", "ane", "iin", "en", "ek", "in",
				"ili", "ah", "iki", "an", "unkwa", "adi", "ima", "oda", "alli",
				"unke", "anka", "ate", "achi", "onka", "utci", "esy", "ika",
				"ilmu", "ala", "uh", "ari", "etu", "uli", "umu", "ac", "upi",
				"ati", "iho", "ohpe", "ohpey", "an", "apa", "onka", "an", "ah",
				"iwa", "iza", "uta", "ani", "amblee", "eeska", "eeska", "on",
				"ikiya", "api", "aseya", "ahpay", "at", "utu", "aka", "asa",
				"ado", "oni", "anu", "ilu", "on", "and", "ono", "ahto", "ochi",
				"olo", "imo", "uma", "utu", "ebi", };
		String nameInstructions[] = { "cf", "cf", "cf", "cf", "cvdf", "cf",
				"cvdf", "cf", "cf", "cf", "cf", "cvdf", "cf", "cf", "cf", "cf",
				"cvdf", "cvdvdf", "cvdf", "cf", "cf", "cvdf", "cvdvdf", "cvdf",
				"cvdvdf", "cf", "cvdf", "cvf", "cvdf", "cvdf", "cvdf", "cvdf",
				"cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvf", "cvdvdf",
				"cvdf", "cvf", "cvdf", "cf", "cvdf", "cf", "cvf", "cf", "cvdf",
				"cf", "cvdvdf", "cvdf", "cf", "cvdvdf", "cvdf", "cvdf", "cf",
				"cvdf", "cvdf", "cvdf", "cf", "cvdf", "cvdf", "cvdf", "cvdf",
				"cf", "cvdf", "cvdf", "cvdvdf", "cf", "cf", "cf", "cf", "cf",
				"cf", "cf", "cvf", "cvdf", "cf", "cvdf", "cf", "cf", "cvdf",
				"cf", "cvdf", "cvdf", "cf", "cf", "cvf", "cf", "cf", "cvdf",
				"cf", "cvdf", "cvdf", "cvdf", "cf", "cf", "cvdf", "cvdf",
				"cvf", "cvdf", "cvdf", "cf", "cf", "cf", "cvdf", "cvf", "cf",
				"cvdf", "cvdf", "cf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf",
				"cf", "cf", "cvdf", "cvf", "cvf", "cf", "cf", "cf", "cvdvdf",
				"cvdvdf", "cvdvdf", "cvdf", "cvdf", "cf", "cvdf", "cvdf",
				"cvdf", "cf", "cvdf", "cvdf", "cvdf", "cvdf", "cf", "cvdf",
				"cvdf", "cvf", "cvdf", "cvdf", "cvdf", "cf", "cf", "cvdf",
				"cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf",
				"cvdvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvf", "cvf", "cvf",
				"cf", "cvf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf",
				"cvdvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cf", "cf",
				"cvdvdf", "cvdf", "cvdf", "cf", "cf", "cf", "cf", "cf", "cvdf",
				"cf", "cvdvdf", "cvdvdf", "cvdf", "cvf", "cvdf", "cvdf",
				"cvdf", "cvdf", "cvf", "cf", "cvdf", "cvdf", "cvdvdf", "cvdf",
				"cvdf", "cvdf", "cvdvdf", "cvdf", "cvdf", "cvdf", "cvdf",
				"cvdf", "cf", "cvdf", "cvdf", "cf", "cf", "cvf", "cvdf", "cf",
				"cvdf", "cvdf", "cf", "cvdf", "cvdf", "cvdf", "cvdf", "cf",
				"cf", "cvdf", "cvdf", "cvf", "cvdf", "cvdf", "cf", "cvdf",
				"cf", "cvdf", "cf", "cf", "cf", "cvdf", "cvdvdf", "cvdvdf",
				"cvdf", "cvdvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cf", "cvdf",
				"cvdvdf", "cvdvdf", "cvdf", "cf", "cf", "cvdvdf", "cvdvdf",
				"cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cvdf", "cf", "cvdvdf",
				"cvdf", "cvdf", "cf", "cf", "cf", "cf", "cf", "cf", "cf", };

		this.vocals.addAll(Arrays.asList(demoVocals));
		this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
		this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
		this.nameInstructions.addAll(Arrays.asList(nameInstructions));
		this.femaleEnd.addAll(Arrays.asList(demofemaleEnd));
	}
}