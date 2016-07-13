package de.hhu.imtgg.controller;


import java.util.ArrayList;

import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.objects.TDDTest;
import de.hhu.imtgg.objects.TDDUebungTests;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class TDDTViewController {
	
	@FXML private MenuButton uebungsButton;
	@FXML private Label darkModeStatus;
	
	private ArrayList<String> uebungsnamen = TDDUebungTests.getUebungsnamen();
	private ArrayList<String> uebungsdescr = TDDUebungTests.getUebungsDescr();
	private ArrayList<TDDTest> uebungtests = TDDUebungTests.getUebungsCode();
	private static String sourceCodeClassName = "";
	
	@FXML private Spinner bbyStepsMinute;
	private static int bbyMinutes = 1;
	private static String testCodevorlage;
	
	private static boolean babySteps = false;
	private boolean tracking = false;
	private boolean atdd = false;
	
	/**
	 * initialisiert dass hauptmenu layout mit wichtigen werten
	 * die �bungen werden geladen und einstellungen konfiguriert(eingestellt)
	 */
	@FXML
	private void initialize() { // haut alle uebungen aus dem ordner Uebungen in den Menubutton
		int uebungsanzahl = uebungsnamen.size();
		babySteps = false; // reset 
		setSpinnerConfig();
		
		for(int i = 0; i < uebungsanzahl; i++) {
			MenuItem menuitem = new MenuItem();
			String testcode = uebungtests.get(i).getTestCode();
			String uebungsclassname = uebungsnamen.get(i);
			menuitem.setText(uebungsclassname);
			int currentuebung = i;
			menuitem.setOnAction(e -> menuItemActions(testcode,getSourceCode(currentuebung),uebungsclassname));
			uebungsButton.getItems().add(menuitem);
		}	
	}
	
	/**
	 * setzt den spinner auf werte von 1 - 10 
	 */
	private void setSpinnerConfig() {
		SpinnerValueFactory svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
		bbyStepsMinute.setValueFactory(svf);
		bbyStepsMinute.setOnMouseClicked(e -> getSpinnerValue());
	}
	
	/**
	 * gibt den wert von dem spinner zur�ck
	 */
	private void getSpinnerValue() {
		String minutes = bbyStepsMinute.getValue().toString();
		bbyMinutes = Integer.parseInt(minutes);
	}
	
	/**
	 * setzt den spinner auf einen default wert
	 */
	public static void setBbyMinuteDefault() { //  falls man zur�ck ins hauptmenu geht , der wert zur�ckgesetzt wird
		bbyMinutes = 1;
	}
	
	/**
	 * hilfsfunktion
	 * @return
	 */
	public static int getBbyMinute() {
		return bbyMinutes*60;
	}
	
	/**
	 * erzeugt einen gestell f�r den sourcecode
	 * @param i
	 * @return einen String welches das ger�st f�+r den SourceCode beinhaltet
	 */
	private String getSourceCode(int i) { // kleines geruest fuer die ausgewaehlte uebung
		return uebungsdescr.get(i) + "\npublic class " + uebungsnamen.get(i) +" {\n\n"
				+ "}";
	}
	
	public static boolean getBabyStepsMode() {
		return babySteps;
	}
	
	private void menuItemActions(String testcode , String sourcecode,String uebungsnamen) {	
		testCodevorlage = testcode;
		TDDTMain.initTDDTrainerViewNormalMode(testcode,sourcecode);	
		sourceCodeClassName =  uebungsnamen;
	}
	
	public static String getSourceCodeClassName() {
		return sourceCodeClassName;
	}
	
	public static String getTestCodeClassName() {
		return sourceCodeClassName + "Test";
	}
	
	@FXML //darkmode button switch f�r hauptmenu
	private void darkModeButtonPressed() {
		if(TDDTDarkModeController.getDarkMode()) {
			TDDTMain.initTDDTViewLayoutNormalMode();
			TDDTDarkModeController.setDarkMode(false);
		}
		else {
			TDDTDarkModeController.setDarkMode(true);
			TDDTMain.initTDDTViewLayoutDarkMode();

		}
	}
	
	@FXML
	private void babyStepsActivated() {
		if(babySteps) babySteps = false;
		else babySteps = true;
	}
	@FXML
	private void trackingActivated() {
		if(tracking) tracking = false;
		else tracking = true;
	}
	@FXML
	private void atddActivated() {
		if(atdd) atdd = false;
		else atdd = true;
	}
	
	public static String getTestCodevorlage() {
		return testCodevorlage;
	}
	
}
