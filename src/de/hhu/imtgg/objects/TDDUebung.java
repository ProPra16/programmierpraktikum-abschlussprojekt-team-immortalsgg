package de.hhu.imtgg.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TDDUebung {
	
	private static FileReader fr;
	private static File ordner = new File("./src/de/hhu/imtgg/uebungen");
	private static BufferedReader br;
	
	public static TDDTest[] getAllUebungen() { // macht die Uebungen für textarea lesbar
		File[] uebungen = scan();
		int uebungenanzahl = uebungen.length;
		TDDTest[] uebungstest = new TDDTest[uebungenanzahl];
		for(int i = 0; i < uebungenanzahl; i++ ) {
			try {
				fr = new FileReader(uebungen[i]);			
				br = new BufferedReader(fr);
				String testcode = br.readLine();
				String tmp = testcode;
				while(!tmp.equals("end")) {
					tmp = br.readLine();
					if(!tmp.equals("end"))
						testcode = testcode + "\n" + tmp;
				}
				TDDTest tddtest = new TDDTest(testcode);
				uebungstest[i] = tddtest;
			
		} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				}
		}
		return uebungstest;
	}
	
	public static String[] uebungsNamen() {  // filtert die namen der Uebungen heraus
		File[] uebungen = scan();
		int uebungsanzahl = uebungen.length;
		String[] uebungsnamen = new String[uebungsanzahl];
		for(int i = 0; i < uebungsanzahl; i++) {
			int start = 28;
			int ende = uebungen[i].toString().length() - 4;
			uebungsnamen[i] = uebungen[i].toString().substring(start,ende);
		}
		return uebungsnamen;
	}
	
	private static File[] scan() {
		 return ordner.listFiles();
	}
}
