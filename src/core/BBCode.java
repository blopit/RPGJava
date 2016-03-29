package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Field;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The BBCode class provides methods to parse a BBCode formatted string into an
 * {@link AttributedString}.
 * <p>
 * BBCode is a simple mark-up language used by many forums and comment systems.
 * Sections of text may be enclosed in various tags to modify properties such as
 * font and color. For example, the string "Hello [b]world![/b]" would appear as
 * "Hello <strong>world!</strong>".
 *
 * <p>
 * Supported tags include:
 * <li>[b][/b]: bold</li>
 * <li>[i][/i]: italic</li>
 * <li>[u][/u]: underline</li>
 * <li>[s][/s]: strike through</li>
 * <li>[color=x][/color]: color where x can be the name of the color or the hex
 * formated string #rrggbb</li>
 * <li>[[]: escape sequence for '['</li>
 * <li>[]]: escape sequence for ']'</li>
 * 
 * <p>
 * Tags may be nested, but must not overlap (i.e. "[b][u]Hello[/u][/b] is legal,
 * but "[b][u]Hello[/b][/u]" is not). Failing to provide properly formatted text
 * will result in undefined behavior.
 * 
 * @author Eric
 *
 */
public class BBCode {
	private AttributedCharacterIterator.Attribute attribute;
	private Object value;
	private int start;
	private int end = -1;
	private String closingTag;

	/**
	 * Create a BBCode object to store tag information.
	 * 
	 * @param attribute
	 *            the attribute this tag represents.
	 * @param value
	 *            any additional value information about the attribute.
	 * @param start
	 *            the first character in the result string affected by this tag.
	 * @param closingTag
	 *            the string representing the closing tag.
	 */
	private BBCode(AttributedCharacterIterator.Attribute attribute,
			Object value, int start, String closingTag) {
		this.attribute = attribute;
		this.value = value;
		this.start = start;
		this.closingTag = closingTag;
	}

	public static String fix(String text) {
		StringBuilder plainText = new StringBuilder(text.length());
		Matcher tagMatcher = Pattern.compile("\\[(.+?)\\]").matcher(text);

		Stack<BBCode> openTags = new Stack<BBCode>();
		ArrayList<BBCode> closedTags = new ArrayList<BBCode>();

		// index of the next character after the last found tag
		int nextCharIndex = 0;
		while (tagMatcher.find()) {
			MatchResult result = tagMatcher.toMatchResult();
			// append all the text between the the last tag and the found one to
			// the plain text
			plainText.append(text.substring(nextCharIndex, result.start(0)));
			// update the next character to just after the end of the tag
			nextCharIndex = result.end(0);

			// the tag string only includes the text (not the outer brackets)
			String tag = result.group(1);

			// special case for escape sequences
			if (tag.equals("[") || tag.equals("]")) {
				plainText.append(tag);
			} else {
				// if the tag is a closing tag
				if (tag.startsWith("/")) {
					// check if the top tag matches the closing tag
					if (!openTags.isEmpty()
							&& openTags.peek().isClosingTag(tag)) {
						openTags.peek().closeTag(plainText.length());
						closedTags.add(openTags.pop());
					} else {
						// if it doesn't match, just add it as plain text
						plainText.append(result.group());
					}
				} else // if the tag is an opening tag
				{
					// create a new opening tag
					BBCode openingTag = BBCode.openTag(tag, plainText.length());
					// if null is returned, the tag failed to parse
					if (openingTag == null) {
						plainText.append(result.group());
					} else {
						// add the opening tag to the top of the tag stack
						openTags.push(openingTag);
					}
				}
			}
		}

		// add any remaining text after the last tag
		if (nextCharIndex < text.length()) {
			plainText.append(text.substring(nextCharIndex));
		}

		// construct the attributed string and add each tag to it
		for (BBCode tag : openTags) {
			text += '[' + tag.closingTag + ']';
		}
		return text;
	}

	/**
	 * Parse a BBCode formatted string into an AttributedString.
	 * 
	 * @param text
	 *            the BBCode formated plain text.
	 * @return an AttributedString corresponding the the input text.
	 */
	public static AttributedString parse(Graphics2D g2, String text) {
		StringBuilder plainText = new StringBuilder(text.length());
		Matcher tagMatcher = Pattern.compile("\\[(.+?)\\]").matcher(text);

		Stack<BBCode> openTags = new Stack<BBCode>();
		ArrayList<BBCode> closedTags = new ArrayList<BBCode>();

		// index of the next character after the last found tag
		int nextCharIndex = 0;
		while (tagMatcher.find()) {
			MatchResult result = tagMatcher.toMatchResult();
			// append all the text between the the last tag and the found one to
			// the plain text
			plainText.append(text.substring(nextCharIndex, result.start(0)));
			// update the next character to just after the end of the tag
			nextCharIndex = result.end(0);

			// the tag string only includes the text (not the outer brackets)
			String tag = result.group(1);

			// special case for escape sequences
			if (tag.equals("[") || tag.equals("]")) {
				plainText.append(tag);
			} else {
				// if the tag is a closing tag
				if (tag.startsWith("/")) {
					// check if the top tag matches the closing tag
					if (!openTags.isEmpty()
							&& openTags.peek().isClosingTag(tag)) {
						openTags.peek().closeTag(plainText.length());
						closedTags.add(openTags.pop());
					} else {
						// if it doesn't match, just add it as plain text
						plainText.append(result.group());
					}
				} else // if the tag is an opening tag
				{
					// create a new opening tag
					BBCode openingTag = BBCode.openTag(tag, plainText.length());
					// if null is returned, the tag failed to parse
					if (openingTag == null) {
						plainText.append(result.group());
					} else {
						// add the opening tag to the top of the tag stack
						openTags.push(openingTag);
					}
				}
			}
		}

		// add any remaining text after the last tag
		if (nextCharIndex < text.length()) {
			plainText.append(text.substring(nextCharIndex));
		}

		AttributedString aString = new AttributedString(plainText.toString());
		if (plainText.toString().length() > 0) {
			for (BBCode tag : closedTags) {
				tag.addToAttributedString(aString, plainText.toString()
						.length());
			}
			aString.addAttribute(TextAttribute.SIZE, g2.getFont().getSize());
			aString.addAttribute(TextAttribute.FAMILY, g2.getFont().getFamily());
		}

		return aString;
	}

	/**
	 * Constructs a new BBCodeTag.
	 * 
	 * @param tag
	 *            the string representing the tag (the part inside the
	 *            brackets).
	 * @param start
	 *            index of the first character after the tag (after the
	 *            brackets).
	 * @return a new BBCodeTag
	 */
	private static BBCode openTag(String tag, int start) {
		if (tag.equals("b")) // bold
		{
			return new BBCode(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD,
					start, "/b");
		}
		else if (tag.equals("i")) // italics
		{
			return new BBCode(TextAttribute.POSTURE,
					TextAttribute.POSTURE_OBLIQUE, start, "/i");
		}
		else if (tag.equals("u")) // underline
		{
			return new BBCode(TextAttribute.UNDERLINE,
					TextAttribute.UNDERLINE_ON, start, "/u");
		}
		else if (tag.equals("s")) {
			return new BBCode(TextAttribute.STRIKETHROUGH,
					TextAttribute.STRIKETHROUGH_ON, start, "/s");
		}
		else if (tag.startsWith("color=")) {
			return new BBCode(TextAttribute.FOREGROUND,
					parseColor(tag.substring(6)), start, "/color");
		}
		else if (tag.startsWith("B")) {
			return new BBCode(TextAttribute.WEIGHT,
					TextAttribute.WEIGHT_ULTRABOLD, start, "/B");
		}
		else if (tag.startsWith("sub")) {
			return new BBCode(TextAttribute.SUPERSCRIPT,
					TextAttribute.SUPERSCRIPT_SUB, start, "/sub");
		}
		else if (tag.startsWith("sup")) {
			return new BBCode(TextAttribute.SUPERSCRIPT,
					TextAttribute.SUPERSCRIPT_SUPER, start, "/sup");
		}
		else if (tag.startsWith("w0")) {
			return new BBCode(TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,
							8 * Math.sin(Math.toRadians(16 * Game.time))),
					start, "/w0");
		}
		else if (tag.startsWith("w1")) {
			return new BBCode(TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,
							8 * Math.sin(Math.toRadians(60 + 10 * Game.time))),
					start, "/w1");
		}
		else if (tag.startsWith("w2")) {
			return new BBCode(
					TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,
							8 * Math.sin(Math.toRadians(120 + 10 * Game.time))),
					start, "/w2");
		}
		else if (tag.startsWith("w3")) {
			return new BBCode(
					TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,
							8 * Math.sin(Math.toRadians(180 + 10 * Game.time))),
					start, "/w3");
		}
		else if (tag.startsWith("w4")) {
			return new BBCode(
					TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,
							8 * Math.sin(Math.toRadians(240 + 10 * Game.time))),
					start, "/w4");
		}
		else if (tag.startsWith("w5")) {
			return new BBCode(
					TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,
							8 * Math.sin(Math.toRadians(300 + 10 * Game.time))),
					start, "/w5");
		}
		else if (tag.startsWith("!")) {
			return new BBCode(
					TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,0),
					start, "/!");
		}
		else if (tag.startsWith("#")) {
			return new BBCode(
					TextAttribute.TRANSFORM,
					AffineTransform.getTranslateInstance(0,0),
					start, "/#");
		}
		return null;
	}

	/**
	 * Returns true is the given string is the closing tag.
	 * 
	 * @param tag
	 * @return
	 */
	private boolean isClosingTag(String tag) {
		return closingTag.equals(tag);
	}

	/**
	 * Closes the tag.
	 * 
	 * @param end
	 */
	private void closeTag(int end) {
		this.end = end;
	}

	/**
	 * Adds this tag to an AttributedString.
	 * 
	 * @param string
	 * @param i
	 */
	private void addToAttributedString(AttributedString string, int i) {
		int x = (end == -1) ? i : end;
		if (start == x)
			return;
		string.addAttribute(attribute, value, start, x);
	}

	/**
	 * Parse a color name or hex value to a Color object.
	 * 
	 * @param colorCode
	 *            the color code string.
	 * @return the resulting color (or null on failure).
	 */
	private static Color parseColor(String colorCode) {
		if (colorCode.startsWith("#")) {
			return new Color(Integer.decode("0x" + colorCode.substring(1)));
		}
		try {
			// use reflection to find a predefined color.
			Field field = Color.class.getField(colorCode.toLowerCase());
			return (Color) field.get(null);
		} catch (Exception e) {
			return null;
		}
	}
}
