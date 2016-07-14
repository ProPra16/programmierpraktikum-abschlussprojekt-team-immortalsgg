package de.hhu.imtgg.controller;


import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.objects.TDDTest;
import de.hhu.imtgg.objects.TDDUebungTests;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class TDDTViewController {
	
	@FXML private MenuButton uebungsButton;
	@FXML private Label darkModeStatus;
	private String[] uebungsnamen = TDDUebungTests.uebungsNamen();
	private TDDTest[] uebungtests = TDDUebungTests.getAllUebungen();
	private static String sourceCodeClassName = "";
	@FXML private Spinner BbyMinute;
	private static int bbyMinutes = 1;
	private static int trackingMinutes = 1;

	private static String testCodevorlage;
	
	private static boolean babySteps = false;
	private static boolean tracking = false;
	private boolean atdd = false;
	
	
	@FXML
	private void initialize() { // haut alle uebungen aus dem ordner Uebungen in den Menubutton
		int uebungsanzahl = uebungsnamen.length;
		babySteps = false; // reset
		tracking = false;
		setSpinnerConfig();
		
		for(int i = 0; i < uebungsanzahl; i++) {
			MenuItem menuitem = new MenuItem();
			String testcode = uebungtests[i].getTestCode();
			String uebungsclassname = uebungsnamen[i];
			menuitem.setText(uebungsclassname);
			int currentuebung = i;
			menuitem.setOnAction(e -> menuItemActions(testcode,getSourceCode(currentuebung),uebungsclassname));
			uebungsButton.getItems().add(menuitem);
		}	
	}
	
	private void setSpinnerConfig() {
		SpinnerValueFactory svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
		BbyMinute.setValueFactory(svf);
		BbyMinute.setOnMouseClicked(e -> getSpinnerValue());
	}
	
	private void getSpinnerValue() {
		String minutes = BbyMinute.getValue().toString();
		bbyMinutes = Integer.parseInt(minutes);
	}

	public static void setBbyMinuteDefault() { //  falls man zur�ck ins hauptmenu geht , der wert zur�ckgesetzt wird
		bbyMinutes = 1;
	}

	public static int getBbyMinute() {
		return bbyMinutes*60;
	}

	private String getSourceCode(int i) { // kleines geruest fuer die ausgewaehlte uebung
		return "public class " + uebungsnamen[i] +" {\n\n"
				+ "}";
	}
	
	public static boolean getBabyStepsMode() {
		return babySteps;
	}

	public static boolean getTrackingMode() {
		return tracking;
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
