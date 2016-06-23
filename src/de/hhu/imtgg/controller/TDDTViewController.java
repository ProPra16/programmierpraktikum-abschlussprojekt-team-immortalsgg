package de.hhu.imtgg.controller;


import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.objects.TDDTest;
import de.hhu.imtgg.objects.TDDUebung;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class TDDTViewController {
	
	@FXML private MenuButton uebungsButton;
	private String[] uebungsnamen = TDDUebung.uebungsNamen();
	private TDDTest[] uebungtests = TDDUebung.getAllUebungen();
	
	@FXML
	private void initialize() { // haut alle uebungen aus dem ordner Uebungen in den Menubutton
		int uebungsanzahl = uebungsnamen.length;
		for(int i = 0; i < uebungsanzahl; i++) {
			MenuItem menuitem = new MenuItem();
			String testcode = uebungtests[i].getTestCode();
			menuitem.setText(uebungsnamen[i]);
			menuitem.setOnAction(e -> TDDTMain.initTDDTrainerView(testcode));
			uebungsButton.getItems().add(menuitem);
		}
	}
}
