package roleplay.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import roleplay.namegenerator.NG_OldEnglishMale;
import roleplay.namegenerator.NameGenerator;

public class language {

	Random random = new Random();
	List<Repl> replacements = new ArrayList<Repl>();
	double global_fct = 1.0;
	
	private class Repl {
		public final String orig;
		public final String repl;
		public final double chance;

		public Repl(String o, String r) {
			orig = o;
			repl = r;
			chance = 1.0;
		}
		
		public Repl(String o, String r, double d) {
			orig = o;
			repl = r;
			chance = d;
			System.out.println(d);
		}
	}

	public String getFromPool(StringBuilder s) {
		return getFromPoolNoNew(s) + "\n";
	}

	public String getFromPoolNoNew(StringBuilder s) {
		int index = (int) (random.nextDouble() * s.length());
		char c = s.charAt(index);
		s.deleteCharAt(index);
		return String.valueOf(c);
	}

	public String randomLanguage(long l) {
		random.setSeed(l);

		NameGenerator ng = new NG_OldEnglishMale();
		ng.random = random;
		System.out.println("Language: " + ng.getName());

		StringBuilder vowels = new StringBuilder("aeiouaeoy");
		StringBuilder common = new StringBuilder("bcdfghklmnprstw");
		StringBuilder commonS = new StringBuilder("hhllrrwwssg");
		StringBuilder rare = new StringBuilder("jqvxz");

		StringBuilder commonHard = new StringBuilder("bcdfgkmnpstlr");
		StringBuilder commonSoft = new StringBuilder(
				"hhhhlllrrrwwwsssggtaeiouy");

		return /*"\\ba\\b :0.5 $4\n" +*/ "[Aa] : " + getFromPool(vowels) + "sh|Sh|SH : "
				+ getFromPoolNoNew(commonHard) + getFromPool(commonSoft)
				+ "th|Th|TH : " + getFromPoolNoNew(commonHard)
				+ getFromPool(commonSoft) + "ch|ch|CH : "
				+ getFromPoolNoNew(commonHard) + getFromPool(commonSoft)
				+ "[Bb] : " + getFromPool(common) + "[Cc] : "
				+ getFromPool(common) + "[Dd] : " + getFromPool(common)
				+ "[Ee] : " + getFromPool(vowels) + "[Ff] : "
				+ getFromPool(common) + "[Gg] : " + getFromPool(commonS)
				+ "[Hh] : " + getFromPool(commonS) + "[Ii] : "
				+ getFromPool(vowels) + "[Jj] : " + getFromPool(rare)
				+ "[Kk] : " + getFromPool(common) + "[Ll] : "
				+ getFromPool(commonS) + "[Mm] : " + getFromPool(common)
				+ "[Nn] : " + getFromPool(common) + "[Oo] : "
				+ getFromPool(vowels) + "[Pp] : " + getFromPool(common)
				+ "qu|Qu|QU : " + getFromPoolNoNew(rare)
				+ getFromPool(commonSoft) + "[Rr] : " + getFromPool(commonS)
				+ "[Ss] : " + getFromPool(commonS) + "[Tt] : "
				+ getFromPool(common) + "[Uu] : " + getFromPool(vowels)
				+ "[Vv] : " + getFromPool(rare) + "[Ww] : "
				+ getFromPool(commonS) + "[Xx] : " + getFromPool(rare)
				+ "[Yy] : " + getFromPool(vowels) + "[Zz] : "
				+ getFromPool(rare);
	}

	public String translate(String input){
		String regexp = "";
		int i = 0;
		for (Repl r : replacements) {
			if (i == 0) {
				regexp = "(" + r.orig + ")";
			} else {
				if (r.orig.contains("("))
					regexp += "|(?:" + r.orig + ")";
				else
					regexp += "|(" + r.orig + ")";
			}
			i++;
		}
		
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(input);

		while (m.find()) {
			for (i = 1; i < m.groupCount() + 1; i++) {
				String s = m.group(i);
				
				if (s != "" && s != null && global_fct * replacements.get(i - 1).chance > random.nextDouble() ) {
					m.appendReplacement(sb, replacements.get(i - 1).repl);
					break;
				}
			}
		}
		m.appendTail(sb);
		return sb.toString().toLowerCase();
	}
	
	public void parseLanguage(String lang){
		Pattern p = Pattern.compile("^(.*?)\\s*:([\\d\\.]*)\\s*(.*?)$", Pattern.MULTILINE);
		Matcher m = p.matcher(lang);

		while (m.find()) {
			String s = m.group(2);
			if (s != null && !s.isEmpty() && s != "") {
				replacements.add(new Repl(m.group(1), m.group(3), Double.parseDouble(m.group(2))));
			} else {
				replacements.add(new Repl(m.group(1), m.group(3)));
			}
		}
	}
	
	//TODO make bad word dictionary
	public language() {
		String input = "I need you to slay a dragon for me";
		
		String lang = "[wW] : v\n"
				+ "[vV] :0.75 f\n"
				+ "qu|Qu|QU :0.15 kwo\n"
				+ "([\\S]*)[tT] :0.25 $4d\n"
				+ "[jJ] :0.25 y\n"
				+ "[sS] : z\n"
				//+ "ur|Ur|UR : yer\n"
				+ "sch|Sch|SCH :0.25 sh\n"
				+ "a|A|ar|Ar|AR|er|Er|ER :0.15 ah\n"
				+ "ch|Ch|CH :0.5 k\n"
				+ "[zZ] :0.25 ts\n"
				+ "and :0.5 und\n"
				+ "is :0.5 ist\n"
				+ "oo :0.15 ü\n";
		
		global_fct = 1.0;
		parseLanguage(randomLanguage(random.nextInt()*031365));
		
		//Talker talker = new Talker();
		//Converter c = new Converter();
		//String s = c.getPhoneWord("hello my name is green eye jim");
		
		//System.out.println(s);
        //talker.sayPhoneWord(s);
		
		System.out.println(translate(input));
	}

}
