package de.hhu.imtgg.controller;

public class TDDTDarkModeController {

	private static boolean darkMode = false;
	
	/**
	 * 
	 * @return gibt den aktuellen stand von darkmode zurück (true / false)(an oder aus)
	 */
	public static boolean getDarkMode() {
		return darkMode;
	}
	
	/**
	 * setzt den darkmodus auf true oder false
	 * @param modus
	 */
	public static void setDarkMode(boolean modus) {
		darkMode = modus;
	}
	
}
