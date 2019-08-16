package ru.will0376.xBlocker.common;

public class BaseUtils {
	public static String lat2cyr(String s) {
		StringBuilder sb = new StringBuilder(s.length());

		for (int i = 0; i < s.length(); ++i) {
			char ch = s.charAt(i);
			boolean lc = Character.isLowerCase(ch);
			ch = Character.toUpperCase(ch);
			if (ch == 'J') {
				++i;
				ch = Character.toUpperCase(s.charAt(i));
				switch (ch) {
					case 'A':
						sb.append(ch('Я', lc));
						break;
					case 'H':
						if (i + 1 < s.length() && Character.toUpperCase(s.charAt(i + 1)) == 'H') {
							sb.append(ch('Ъ', lc));
							++i;
						} else {
							sb.append(ch('Ь', lc));
						}
						break;
					case 'O':
						sb.append(ch('Ё', lc));
						break;
					case 'U':
						sb.append(ch('Ю', lc));
						break;
					case '_':
						sb.append(ch(' ', lc));
						break;
					default:
						throw new IllegalArgumentException("Illegal transliterated symbol '" + ch + "' at position " + i);
				}
			} else if (i + 1 < s.length() && Character.toUpperCase(s.charAt(i + 1)) == 'H') {
				switch (ch) {
					case 'C':
						sb.append(ch('Ч', lc));
						break;
					case 'E':
						sb.append(ch('Э', lc));
						break;
					case 'I':
						sb.append(ch('Ы', lc));
						break;
					case 'K':
						sb.append(ch('Х', lc));
						break;
					case 'S':
						if (i + 2 < s.length() && Character.toUpperCase(s.charAt(i + 2)) == 'H') {
							sb.append(ch('Щ', lc));
							++i;
						} else {
							sb.append(ch('Ш', lc));
						}
						break;
					case 'Z':
						sb.append(ch('Ж', lc));
						break;
					case '_':
						sb.append(ch(' ', lc));
						break;


					default:
						throw new IllegalArgumentException("Illegal transliterated symbol '" + ch + "' at position " + i);
				}

				++i;
			} else {
				switch (ch) {
					case 'A':
						sb.append(ch('А', lc));
						break;
					case 'B':
						sb.append(ch('Б', lc));
						break;
					case 'C':
						sb.append(ch('Ц', lc));
						break;
					case 'D':
						sb.append(ch('Д', lc));
						break;
					case 'E':
						sb.append(ch('Е', lc));
						break;
					case 'F':
						sb.append(ch('Ф', lc));
						break;
					case 'G':
						sb.append(ch('Г', lc));
						break;
					case 'H':
					case 'J':
					case 'Q':
					case 'W':
					case 'X':
					default:
						sb.append(ch(ch, lc));
						break;
					case 'I':
						sb.append(ch('И', lc));
						break;
					case 'K':
						sb.append(ch('К', lc));
						break;
					case 'L':
						sb.append(ch('Л', lc));
						break;
					case 'M':
						sb.append(ch('М', lc));
						break;
					case 'N':
						sb.append(ch('Н', lc));
						break;
					case 'O':
						sb.append(ch('О', lc));
						break;
					case 'P':
						sb.append(ch('П', lc));
						break;
					case 'R':
						sb.append(ch('Р', lc));
						break;
					case 'S':
						sb.append(ch('С', lc));
						break;
					case 'T':
						sb.append(ch('Т', lc));
						break;
					case 'U':
						sb.append(ch('У', lc));
						break;
					case 'V':
						sb.append(ch('В', lc));
						break;
					case 'Y':
						sb.append(ch('Й', lc));
						break;
					case 'Z':
						sb.append(ch('З', lc));
				}
			}
		}

		return sb.toString();
	}

	public static String cyr2lat(char ch) {
		switch (ch) {
			case ' ':
				return "_";
			case 'Ё':
				return "JO";
			case 'А':
				return "A";
			case 'Б':
				return "B";
			case 'В':
				return "V";
			case 'Г':
				return "G";
			case 'Д':
				return "D";
			case 'Е':
				return "E";
			case 'Ж':
				return "ZH";
			case 'З':
				return "Z";
			case 'И':
				return "I";
			case 'Й':
				return "Y";
			case 'К':
				return "K";
			case 'Л':
				return "L";
			case 'М':
				return "M";
			case 'Н':
				return "N";
			case 'О':
				return "O";
			case 'П':
				return "P";
			case 'Р':
				return "R";
			case 'С':
				return "S";
			case 'Т':
				return "T";
			case 'У':
				return "U";
			case 'Ф':
				return "F";
			case 'Х':
				return "KH";
			case 'Ц':
				return "C";
			case 'Ч':
				return "CH";
			case 'Ш':
				return "SH";
			case 'Щ':
				return "SHH";
			case 'Ъ':
				return "JHH";
			case 'Ы':
				return "IH";
			case 'Ь':
				return "JH";
			case 'Э':
				return "EH";
			case 'Ю':
				return "JU";
			case 'Я':
				return "JA";
			default:
				return String.valueOf(ch);
		}
	}

	public static String cyr2lat(String s) {
		StringBuilder sb = new StringBuilder(s.length() * 2);
		char[] var2 = s.toCharArray();
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			char ch = var2[var4];
			char upCh = Character.toUpperCase(ch);
			String lat = cyr2lat(upCh);
			if (ch != upCh) {
				lat = lat.toLowerCase();
			}

			sb.append(lat);
		}

		return sb.toString();
	}

	private static char ch(char ch, boolean toLowerCase) {
		return toLowerCase ? Character.toLowerCase(ch) : ch;
	}
}
