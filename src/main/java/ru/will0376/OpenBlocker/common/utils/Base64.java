package ru.will0376.OpenBlocker.common.utils;

public class Base64 {
	public static String encode(String text) {
		return java.util.Base64.getEncoder().encodeToString(text.getBytes());
	}

	public static String decode(String b64) {
		try {
			return new String(java.util.Base64.getDecoder().decode(b64));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Decode error";
	}
}
