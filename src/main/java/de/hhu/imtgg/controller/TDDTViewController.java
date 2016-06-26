package de.hhu.imtgg.controller;


import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.objects.TDDTest;
import de.hhu.imtgg.objects.TDDUebungTests;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;

public class TDDTViewController {
	
	@FXML private MenuButton uebungsButton;
	@FXML private Label darkModeStatus;
	private String[] uebungsnamen = TDDUebungTests.uebungsNamen();
	private TDDTest[] uebungtests = TDDUebungTests.getAllUebungen();
	private static String sourceCodeClassName = "";
	
	
	@FXML
	private void initialize() { // haut alle uebungen aus dem ordner Uebungen in den Menubutton
		int uebungsanzahl = uebungsnamen.length;
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
	
	private String getSourceCode(int i) { // kleines geruest fuer die ausgewaehlte uebung
		return "public class " + uebungsnamen[i] +" {\n\n"
				+ "	public static int defaultMethod() {\n"
				+ "		return 0;\n"
				+ "	}\n"
				+ "}";
	}
	
	private void menuItemActions(String testcode , String sourcecode,String uebungsnamen) {		
		TDDTMain.initTDDTrainerView(testcode,sourcecode);
		sourceCodeClassName =  uebungsnamen;
	}
	
	public static String getSourceCodeClassName() {
		return sourceCodeClassName;
	}
	
	public static String getTestCodeClassName() {
		return sourceCodeClassName + "Test";
	}
	
	@FXML
	private void darkModeButtonPressed() {
		if(TDDTDarkModeController.getDarkMode()) {
			TDDTMain.initTDDTViewLayoutNormalMode();
			TDDTDarkModeController.setDarkMode(false);
			System.out.println("test");
		}
		else {
			TDDTDarkModeController.setDarkMode(true);
			TDDTMain.initTDDTViewLayoutDarkMode();
			System.out.println("test2");

		}
	}
}
