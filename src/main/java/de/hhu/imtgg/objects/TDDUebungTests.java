package de.hhu.imtgg.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TDDUebungTests {
	
	/**
	 * filtert den uebungscode aus einer datei 
	 * @return speichert die codes in einer arraylist
	 */
	public static ArrayList<TDDTest> getUebungsCode() {
		ArrayList<TDDTest> uebungsCode = new ArrayList<TDDTest>();
		try {
			FileReader fileR = new FileReader("./src/main/java/de/hhu/imtgg/uebungen/Uebungen.txt");
			BufferedReader buffR = new BufferedReader(fileR);
			String check = buffR.readLine();
			String testcode = "";
			boolean code = false;
			while(!check.equals("end")) {
				check = buffR.readLine();
				if(check.equals("Code"))  { code = true; check = buffR.readLine(); }
				if(check.equals("next") || check.equals("end")) { code = false; uebungsCode.add(new TDDTest(testcode)); testcode = "";}
				if(code) {
					testcode = testcode + check + "\n";
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uebungsCode;
	}
	
	/**
	 * filtert die übungsbeschreibung aus einer datei
	 * @return speichert die beschreibungen in einer arraylist
	 */
	public static ArrayList<String> getUebungsDescr() {
		ArrayList<String> uebungsdescr = new ArrayList<String>();
		try {
			FileReader fileR = new FileReader("./src/main/java/de/hhu/imtgg/uebungen/Uebungen.txt");
			BufferedReader buffR = new BufferedReader(fileR);
			String check = buffR.readLine();
			String uebungdesc = "";
			boolean desc = false;
			while(!check.equals("end")) {
				check = buffR.readLine();
				if(check.equals("Beschreibung"))  { desc = true; check = buffR.readLine(); }
				if(check.equals("Code")) { desc = false;  uebungsdescr.add(uebungdesc); uebungdesc = ""; }
				if(desc) {
					uebungdesc = uebungdesc + check + "\n" ;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uebungsdescr;

	}
	
	/**
	 * filtert die übungsnamen aus einer datei
	 * @return speichert die namen in einer arraylist
	 */
	public static ArrayList<String> getUebungsnamen() { // filtert Uebungsnamen heraus
		ArrayList<String> uebungsnamen = new ArrayList<String>();
		try {
			FileReader fileR = new FileReader("./src/main/java/de/hhu/imtgg/uebungen/Uebungen.txt");
			BufferedReader buffR = new BufferedReader(fileR);
			String uebungsname = buffR.readLine();
			uebungsnamen.add(uebungsname);
			String check = "";
			while(!check.equals("end")) {
				check = buffR.readLine();
				if(check.equals("next")) {
					uebungsname = buffR.readLine();
					uebungsnamen.add(uebungsname);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uebungsnamen;
		
	}
		
}
