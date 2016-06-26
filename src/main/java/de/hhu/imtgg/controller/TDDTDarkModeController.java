package de.hhu.imtgg.controller;

public class TDDTDarkModeController {

	private static boolean darkMode = false;
	
	public static boolean getDarkMode() {
		return darkMode;
	}
	
	public static void setDarkMode(boolean modus) {
		darkMode = modus;
	}
	
}
