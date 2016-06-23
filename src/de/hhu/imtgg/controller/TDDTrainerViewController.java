package de.hhu.imtgg.controller;

import de.hhu.imtgg.TDDTMain;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TDDTrainerViewController {
	
	@FXML private TextArea testCode;
	@FXML private TextArea sourceCode;
	
	@FXML
	private void pressedBackButton() { // man kehrt ins hauptmenu zurück
		TDDTMain.initTDDTViewLayout();
	}
	

	
}
