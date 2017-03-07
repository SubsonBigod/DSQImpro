package edu.utils;

import java.util.List;

public class StrUtil {

	public static void spliter(String line, String regex, List<String> list) {
		String[] strTok = line.split(regex);
		for (String str : strTok) {
			list.add(str);
		}
	}
	
	public static boolean isLetter(String str) {
		String regex = "^[a-zA-Z]$";
		return str.matches(regex);
	}
	
	public static boolean isLetters(String str) {
		String regex = "^[a-zA-Z]+$";
		return str.matches(regex);
	}
	
	public static boolean isLetterDigitOr(String str) {
		String regex = "^[a-z0-9A-Z]+$";
		return str.matches(regex);
	}
	
	public static boolean isLetterDigitOrChinese(String str) {
		String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
		return str.matches(regex);
	}

}
