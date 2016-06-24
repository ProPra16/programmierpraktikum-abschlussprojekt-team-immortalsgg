package de.hhu.imtgg.controller;


import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.objects.TDDTest;
import de.hhu.imtgg.objects.TDDUebungTests;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class TDDTViewController {
	
	@FXML private MenuButton uebungsButton;
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
	
	private String getSourceCode(int i) { // kleines gerüst für die ausgewählte übung
		return "public class " + uebungsnamen[i] +" {\n\n}";
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
}
