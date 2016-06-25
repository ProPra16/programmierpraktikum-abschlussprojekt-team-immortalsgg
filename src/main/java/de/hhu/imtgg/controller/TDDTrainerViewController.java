package de.hhu.imtgg.controller;

import java.util.Optional;

import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.compiler.TDDCompiler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

public class TDDTrainerViewController {
	
	@FXML private TextArea testCode;
	@FXML private TextArea sourceCode;
	
	private boolean writeafailtest = true; // booleans fuer den status der gerade ist
	private boolean makethetestpass = false;
	private boolean refactor = false;
	
	@FXML
	private void pressedBackButton() { // man kehrt ins hauptmenu zurück
		TDDTMain.initTDDTViewLayout();
	}
	
	@FXML
	private void initialize() {
		sourceCode.setStyle("-fx-border-color: red;");
		testCode.setStyle("-fx-border-color: green");
		sourceCode.setEditable(false);
	}
	
	@FXML
	private void testCodeSaveButtonPressed() { // der rechte savebutton 
		if(!writeafailtest) {
			System.out.println("your not in the mode writeafailtest");
			return;
		}
		
		TDDCompiler.getTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.getSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess()) { 
				System.out.println("es sollte ein test failen");
				return; 
				}
						
			if(TDDCompiler.checkTests1Failed()) {
				System.out.println("du bist jetzt im modus make the test pass");
				makethetestpass = true;
				writeafailtest = false;
				sourceCode.setEditable(true);
				testCode.setEditable(false);
				sourceCode.setStyle("-fx-border-color: green;");
				testCode.setStyle("-fx-border-color: red");
			}
			
			else System.out.println("mehreretestsfail");
		}
		
		else System.out.println("da ist ein fehler im Test");
	}
	
	@FXML
	private void sourceCodeSaveButtonPressed() { // linker savebutton
		if(!makethetestpass) {
			System.out.println("Your not in the mode makethetestpass");
			return;
		}
		
		TDDCompiler.getTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.getSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess()) {
				refactor = true;
				makethetestpass = false;
				sourceCode.setStyle("-fx-border-color: black;");
				System.out.println("your now in mode refactor");
			}
			else {
				System.out.println("du musst alle tests bestehen um in den modus refactor zuwechseln");
				return;
			}
		}
		else System.out.println("dein sourcecode ist falsch");	
		
	}
	
	@FXML
	private void refactorButtonPressed() { // refactor button linke seite
		if(!refactor) {
			System.out.println("you not in the mode refactor");
			return;
		}
		
		TDDCompiler.getTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.getSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess()) {
				refactorOptions();
			}
			else {
				System.out.println("die tests schlagen fehl");
				return;
			}
		}
		else System.out.println("dein sourcecode ist falsch");
		
	}
	
	public void refactorOptions() { // optionen beim refactoring
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("TDDTrainer by ImmortalsGG");
		alert.setHeaderText("Refactor Options");
		alert.setContentText("Bitte waehle aus:");

		ButtonType refactorButton = new ButtonType("ContinueRefactoring");
		ButtonType newtestButton = new ButtonType("WriteNewTests");

		alert.getButtonTypes().setAll(refactorButton, newtestButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == refactorButton) {
			return;
		}
		else if (result.get() == newtestButton) {
			refactor = false;
			writeafailtest = true;
			testCode.setEditable(true);
			sourceCode.setEditable(false);
			sourceCode.setStyle("-fx-border-color: red;");
			testCode.setStyle("-fx-border-color: green;");
			System.out.println("du bist jetzt in mode writeafailtest");
		}

	}
	
}
	

	

