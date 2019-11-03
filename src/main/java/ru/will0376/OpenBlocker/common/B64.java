package ru.will0376.OpenBlocker.common;

import java.util.Base64;

public class B64 {
	public static String encode(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes());
	}

	public static String decode(String b64) {
		try {
			System.out.println(b64);
			return new String(Base64.getDecoder().decode(b64));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Decode error";
	}
}
