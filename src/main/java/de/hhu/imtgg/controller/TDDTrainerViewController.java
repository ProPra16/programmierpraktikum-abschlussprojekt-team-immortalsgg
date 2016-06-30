package de.hhu.imtgg.controller;

import java.util.Optional;

import de.hhu.imtgg.compiler.TDDCompiler;
import de.hhu.imtgg.objects.TDDAlert;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class TDDTrainerViewController {

	@FXML private TextArea testCode;
	@FXML private TextArea sourceCode;
	@FXML private Button leftSaveButton;
	@FXML private Button rightSaveButton;
	@FXML private Button refactorButton;
	@FXML private Text bottomStatusText;
	
	private static Thread sourceCodeTimerThread = new Thread(() -> {}); // damit man alles beendet wenn man hauptprogramm schließt
	private static Thread testCodeTimerThread = new Thread(() -> {}); //thread für rechten timer , der drüber für den linken
	@FXML private Text testCodeTimer; // die javafx Texte wo der timer tickt
	@FXML private Text sourceCodeTimer;
	private boolean sourceCodeTimerStop = false;	// an / aus für timer
	private boolean testCodeTimerStop = true; 
	private int testCodeCounter = TDDTViewController.getBbyMinute();
	private int sourceCodeCounter = TDDTViewController.getBbyMinute();
	
	private int farbcounter = TDDTViewController.getBbyMinute();
	private int farbe1 = farbcounter - farbcounter / 5;
	private int farbe2 = farbcounter - (2*(farbcounter / 5));
	private int farbe3 = farbcounter - (3*(farbcounter / 5));
	private int farbe4 = farbcounter - (4*(farbcounter / 5));
	
	private static boolean writeafailtest = true; // booleans fuer den status der gerade ist
	private static boolean makethetestpass = false;
	private static boolean refactor = false;
	
	private String sourceCodeSave;
	private String testCodeSave;
	
	@FXML
	private void pressedBackButton() { // man kehrt ins hauptmenu zurueck
		writeafailtest = true;
		makethetestpass = false;
		refactor = false;
		new TDDAlert().areUSureMessage();
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
		bottomStatusText.setText("Du befindest dich im Modus WriteAFailTest");
		sourceCode.setText(sourceCodeSave);
		
		sourceCodeCounter = TDDTViewController.getBbyMinute();
		testCodeCounter = TDDTViewController.getBbyMinute();
		sourceCodeTimerStop = false;
		testCodeTimerStop = true;
		
		if(TDDTViewController.getBabyStepsMode())
			startTestCodeTimer();
		
		new TDDAlert("WriteAFailTest",true,false,false).switchedModeAlert();

		
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
		
		testCodeSave = TDDTViewController.getTestCodevorlage(); //Babymode
		sourceCodeTimer.setText(String.valueOf(TDDTViewController.getBbyMinute()));
		if(TDDTViewController.getBabyStepsMode())
			startTestCodeTimer();
		else {
			testCodeTimer.setVisible(false);
			sourceCodeTimer.setVisible(false);
		}
	}
	
	@FXML
	private void testCodeSaveButtonPressed() { // der rechte savebutton 
		if(!writeafailtest) return;
		
		
		TDDCompiler.setTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.setSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess() || !TDDCompiler.checkTests1Failed())
				new TDDAlert("In dem Modus WriteAFailTest darf nur 1 Test fehlschlagen!").showTestResults();
				
						
			if(TDDCompiler.checkTests1Failed()) {				
				testCodeTimerStop = false;
				sourceCodeTimerStop = true;
				sourceCodeCounter = TDDTViewController.getBbyMinute();
				if(TDDTViewController.getBabyStepsMode())
					startSourceCodeTimer();
			
				switchToMakeTheTestPass();
				try { // damit man sieht auf wie viel sek man auf der anderen seite war , 1sek warten damit der timer durchläuft und nichts dazwischen kommt
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				testCodeTimer.setOpacity(0.25);
				testCodeTimer.setStyle("-fx-fill: black;");
			}
		
		}
		
		else {
			TDDAlert alert = new TDDAlert();
			boolean clickYes = alert.compileErrorModeSwitchAlert(1);
			if (clickYes) {
				switchToMakeTheTestPass();
			}
		}		
	}

	private void switchToMakeTheTestPass() {
		new TDDAlert("MakeTheTestPass",false,true,false).switchedModeAlert();
		sourceCode.setEditable(true);
		testCode.setEditable(false);
		leftSaveButton.setStyle("-fx-border-color: green;");
		rightSaveButton.setStyle("-fx-border-color: red;");
		sourceCode.setStyle("-fx-border-color: green;");
		testCode.setStyle("-fx-border-color: red");
		bottomStatusText.setText("Du befindest dich im Modus MakeTheTestPass");
		sourceCodeSave = sourceCode.getText();
	}
	
	@FXML
	private void sourceCodeSaveButtonPressed() { // linker savebutton
		if(!makethetestpass) return;
		
		
		TDDCompiler.setTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.setSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess()) {
				leftSaveButton.setStyle("-fx-border-color: red;");
				refactorButton.setStyle("-fx-border-color: green;");
				sourceCode.setStyle("-fx-border-color: black;");
				bottomStatusText.setText("Du befindest dich im Modus Refactor");
				testCodeSave = testCode.getText(); //babymode
				
				sourceCodeTimerStop = false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				testCodeTimer.setOpacity(0.25);
				testCodeTimer.setStyle("-fx-fill: black;");
				
				new TDDAlert("Refactor",false,false,true).switchedModeAlert();

			}
			else new TDDAlert("Du musst alle Tests bestehen um in den Modus: Refactor zugelangen!").showTestResults();
			
		}
		else new TDDAlert("Source").compileError(2);
		
	}
	
	@FXML
	private void refactorButtonPressed() { // refactor button linke seite
		if(!refactor) return;
		
		
		TDDCompiler.setTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.setSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
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
		alert.setContentText("Sind sie sicher, dass sie genug refactored haben?\nBitte waehle aus:");

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
			
			testCodeTimerStop = true;
			testCodeCounter = TDDTViewController.getBbyMinute();
			startTestCodeTimer();
		}
	}
	
	
	
	private void startTestCodeTimer() { // babymode
		testCodeTimerThread = new Thread(() -> {
			while(testCodeTimerStop) {
				testCodeTimer.setText(String.valueOf(testCodeCounter));
				testCodeCounter--;
				if(testCodeCounter == -1) {
					testCodeCounter = TDDTViewController.getBbyMinute();
					testCode.setText(testCodeSave);
				}
				
				if(testCodeCounter > farbe1) testCodeTimer.setStyle("-fx-fill: lime;"); // feature lässt farben grün gelb oder rot erscheinen jenach wie viel zeit noch da ist
				else if(testCodeCounter > farbe2) testCodeTimer.setStyle("-fx-fill: gold;");
				else if(testCodeCounter > farbe3) testCodeTimer.setStyle("-fx-fill: orange;");
				else if(testCodeCounter > farbe4) testCodeTimer.setStyle("-fx-fill: orangered;");
				else if(testCodeCounter > 0) testCodeTimer.setStyle("-fx-fill: red;");

				
				for(int i = 40; i > 0; i--) { // feature lääst den counter verblassen
					try {
						Thread.sleep(25);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					testCodeTimer.setOpacity(i*0.025);
				}
			}
		});
		testCodeTimerThread.start();
	}
	
	private void startSourceCodeTimer() {
		sourceCodeTimerThread = new Thread(() -> {
			while(sourceCodeTimerStop) {
				sourceCodeTimer.setText(String.valueOf(sourceCodeCounter));
				sourceCodeCounter--;
				if(sourceCodeCounter == -1) {
					sourceCodeCounter = TDDTViewController.getBbyMinute();
					sourceCode.setText(sourceCodeSave);
				}
				if(sourceCodeCounter > farbe1) sourceCodeTimer.setStyle("-fx-fill: lime;"); // feature lässt farben grün gelb oder rot erscheinen jenach wie viel zeit noch da ist
				else if(sourceCodeCounter > farbe2) sourceCodeTimer.setStyle("-fx-fill: gold;");
				else if(sourceCodeCounter > farbe3) sourceCodeTimer.setStyle("-fx-fill: orange;");
				else if(sourceCodeCounter > farbe4) sourceCodeTimer.setStyle("-fx-fill: orangered;");
				else if(sourceCodeCounter > 0) sourceCodeTimer.setStyle("-fx-fill: red;");

				
				for(int i = 40; i > 0; i--) { // feature lääst den counter verblassen
					try {
						Thread.sleep(25);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sourceCodeTimer.setOpacity(i*0.025);
				}
			}
		});
		sourceCodeTimerThread.start();
	}
	

	public static void interruptTimer() { //babymode
		testCodeTimerThread.stop();
		sourceCodeTimerThread.stop();
	}
	
}
