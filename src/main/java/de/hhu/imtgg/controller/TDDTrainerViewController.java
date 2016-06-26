package de.hhu.imtgg.controller;

import java.util.Optional;

import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.compiler.TDDCompiler;
import de.hhu.imtgg.objects.TDDAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class TDDTrainerViewController {
	
	@FXML private TextArea testCode;
	@FXML private TextArea sourceCode;
	@FXML private Button leftSaveButton;
	@FXML private Button rightSaveButton;
	@FXML private Button refactorButton;
	@FXML private Text bottomStatusText;
	
	private static boolean writeafailtest = true; // booleans fuer den status der gerade ist
	private static boolean makethetestpass = false;
	private static boolean refactor = false;
	
	@FXML
	private void pressedBackButton() { // man kehrt ins hauptmenu zurueck
		TDDTMain.initTDDTViewLayout();
		writeafailtest = true;
		makethetestpass = false;
		refactor = false;
	}
	
	@FXML
	private void changeBackToTestMode() {
		if(writeafailtest) return;
		testCode.setEditable(true);
		sourceCode.setEditable(false);
		refactorButton.setStyle("-fx-border-color: red;");
		rightSaveButton.setStyle("-fx-border-color: green;");
		sourceCode.setStyle("-fx-border-color: red;");
		testCode.setStyle("-fx-border-color: green;");
		new TDDAlert("WriteAFailTest",true,false,false).switchedModeAlert();
		bottomStatusText.setText("Du befindest dich im Modus WriteAFailTest");
	}
	
	@FXML
	private void initialize() {
		sourceCode.setStyle("-fx-border-color: red;");
		testCode.setStyle("-fx-border-color: green;");
		leftSaveButton.setStyle("-fx-border-color: red;");
		rightSaveButton.setStyle("-fx-border-color: green;");
		refactorButton.setStyle("-fx-border-color: red;");
		sourceCode.setEditable(false);
		bottomStatusText.setText("Du befindest dich im Modus WriteAFailTest");
	}
	
	@FXML
	private void testCodeSaveButtonPressed() { // der rechte savebutton 
		if(!writeafailtest) return;
		
		
		TDDCompiler.getTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.getSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess() || !TDDCompiler.checkTests1Failed())
				new TDDAlert("In dem Modus WriteAFailTest darf nur 1 Test fehlschlagen!").showTestResults();
				
						
			if(TDDCompiler.checkTests1Failed()) {
				new TDDAlert("MakeTheTestPass",false,true,false).switchedModeAlert();
				sourceCode.setEditable(true);
				testCode.setEditable(false);
				leftSaveButton.setStyle("-fx-border-color: green;");
				rightSaveButton.setStyle("-fx-border-color: red;");
				sourceCode.setStyle("-fx-border-color: green;");
				testCode.setStyle("-fx-border-color: red");
				bottomStatusText.setText("Du befindest dich im Modus MakeTheTestPass");

			}
		
		}
		
		else new TDDAlert("Test").compileError(1);
			
	}
	
	@FXML
	private void sourceCodeSaveButtonPressed() { // linker savebutton
		if(!makethetestpass) return;
		
		
		TDDCompiler.getTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.getSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess()) {
				new TDDAlert("Refactor",false,false,true).switchedModeAlert();
				leftSaveButton.setStyle("-fx-border-color: red;");
				refactorButton.setStyle("-fx-border-color: green;");
				sourceCode.setStyle("-fx-border-color: black;");
				bottomStatusText.setText("Du befindest dich im Modus Refactor");

			}
			else new TDDAlert("Du musst alle Tests bestehen um in den Modus: Refactor zugelangen!").showTestResults();
			
		}
		else new TDDAlert("Source").compileError(2);
		
	}
	
	@FXML
	private void refactorButtonPressed() { // refactor button linke seite
		if(!refactor) return;
		
		
		TDDCompiler.getTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.getSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess()) {
				refactorAlert();				
			}
			else new TDDAlert("Du musst alle Tests bestehen um in den Modus: WriteAFailTest zu gelangen!").showTestResults();

			
		}
		else new TDDAlert("Source").compileError(2);
		
	}
	
	
	public static void setRefactorMode(boolean mode) {
		refactor = mode;
	}
	
	public static void setSourceCodeMode(boolean mode) {
		makethetestpass = mode;
	}
	
	public static void setTestMode(boolean mode) {
		writeafailtest = mode;
	}
	
	public void refactorAlert() { // von http://code.makery.ch/blog/javafx-dialogs-official/
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("TDDTrainer by Team ImmortalsGG");
		alert.setHeaderText("Refactor Options");
		alert.setContentText("Bitte waehle aus:");

		ButtonType continueRef = new ButtonType("ContinueRefactoring");
		ButtonType newtestButton = new ButtonType("WriteNewTests");

		alert.getButtonTypes().setAll(continueRef, newtestButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == continueRef) {
			return;
		}
		else if (result.get() == newtestButton) {
			testCode.setEditable(true);
			sourceCode.setEditable(false);
			refactorButton.setStyle("-fx-border-color: red;");
			rightSaveButton.setStyle("-fx-border-color: green;");
			sourceCode.setStyle("-fx-border-color: red;");
			testCode.setStyle("-fx-border-color: green;");
			new TDDAlert("WriteAFailTest",true,false,false).switchedModeAlert();
			bottomStatusText.setText("Du befindest dich im Modus WriteAFailTest");
		}
	}
	
}
	

	

