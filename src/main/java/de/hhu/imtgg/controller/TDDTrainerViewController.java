package de.hhu.imtgg.controller;

import java.util.LinkedList;
import java.util.Optional;

import de.hhu.imtgg.compiler.TDDCompiler;
import de.hhu.imtgg.objects.TDDAlert;
import de.hhu.imtgg.objects.TDDTuple;
import de.hhu.imtgg.objects.TrackingHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class TDDTrainerViewController {

	@FXML private TextArea testCode;
	@FXML private TextArea sourceCode;
	@FXML private Button leftSaveButton;
	@FXML private Button rightSaveButton;
	@FXML private Button refactorButton;
	@FXML private Text bottomStatusText;
	
	private static Thread sourceCodeTimerThread = new Thread(() -> {}); // damit man alles beendet wenn man hauptprogramm schlie�t
	private static Thread testCodeTimerThread = new Thread(() -> {}); //thread f�r rechten timer , der dr�ber f�r den linken
	@FXML private Text testCodeTimer; // die javafx Texte wo der timer tickt
	@FXML private Text sourceCodeTimer;

	@FXML private Button showTrackingButton;
	@FXML private Button showATDDButton;

	private static boolean sourceCodeTimerStop = false;	// an / aus f�r timer
	private static boolean testCodeTimerStop = true; 
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

	private int phaseCounter = 1;
	
	private String sourceCodeSave;
	private String testCodeSave;

	private LinkedList<TDDTuple> log;

	/**
	 * ist der zur�ckinshauptmenu button gedr�ckt sowird abgefragt ob man dies wirklich m�chte
	 * und die werte die wichtig f�r einen neustart sind wieder auf den standard wert zur�ckgesetzt
	 */
	@FXML
	private void pressedBackButton() { // man kehrt ins hauptmenu zurueck
		writeafailtest = true;
		makethetestpass = false;
		refactor = false;
		new TDDAlert().areUSureMessage();
	}
	
	/**
	 * ist dieser button gedr�ckt wird man gefragt ob man wirklich zur�ck in den testmodus m�chte
	 * ja oder nein -> ja man kommt in den testmode alle werte die daf�r n�tig sind �ndern sich
	 * nein -> es passiert nichts
	 */
	@FXML
	private void changeBackToTestMode() {
		if(writeafailtest) return;
		testCode.setEditable(true);
		sourceCode.setEditable(false);
		refactorButton.setStyle("-fx-border-color: red;");
		rightSaveButton.setStyle("-fx-border-color: green;");
		sourceCode.setStyle("-fx-border-color: red;");
		testCode.setStyle("-fx-border-color: green;");
		bottomStatusText.setText("Du befindest dich im Modus 'schreib einen fehlschlagenden Test'");
		sourceCode.setText(sourceCodeSave);
		

		
		if(TDDTViewController.getBabyStepsMode())
			sourceCodeCounter = TDDTViewController.getBbyMinute();
			testCodeCounter = TDDTViewController.getBbyMinute();
			sourceCodeTimerStop = false;
			testCodeTimerStop = true;
			startTestCodeTimer();
		new TDDAlert("'schreib einen fehlschlagenden Test'",true,false,false).switchedModeAlert();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sourceCodeTimer.setStyle("-fx-fill: black;");
		sourceCodeTimer.setOpacity(0.25);
		
	}
	
	/**
	 * initialisiert die fxml datei(stage) mit den standard werten
	 */
	@FXML
	private void initialize() {
		sourceCode.setStyle("-fx-border-color: red;");
		testCode.setStyle("-fx-border-color: green;");
		leftSaveButton.setStyle("-fx-border-color: red;");
		rightSaveButton.setStyle("-fx-border-color: green;");
		refactorButton.setStyle("-fx-border-color: red;");
		sourceCode.setEditable(false);
		bottomStatusText.setText("Du befindest dich im Modus 'schreib einen fehlschlagenden Test'");
		
		testCodeSave = TDDTViewController.getTestCodevorlage(); //Babymode
		log = new LinkedList<TDDTuple>(); //Tracking
		TrackingHelper.startTrackingCounter();
		if(TDDTViewController.getBabyStepsMode()) {
			startTestCodeTimer();
			sourceCodeTimer.setText(String.valueOf(TDDTViewController.getBbyMinute()));
		}  else {
			testCodeTimer.setVisible(false);
			sourceCodeTimer.setVisible(false);
		}
		if(!TDDTViewController.getTrackingMode()) {
			showTrackingButton.setVisible(false);
			showTrackingButton.setDisable(true);
		}
		if(!TDDTViewController.getATDDMode()) {
			showATDDButton.setVisible(false);
			showATDDButton.setDisable(true);
		}
	}
	
	/**
	 * TDD Aktion: es wird �berpr�ft ob ein test fehlschl�gt ,dann darf man weiter
	 * wenn es aber nicht compiliert wird man trotzdem gefragt ob man weiter m�chte oder nicht
	 */
	@FXML
	private void testCodeSaveButtonPressed() { // der rechte savebutton 
		if(!writeafailtest) return;
		
		
		TDDCompiler.setTestClass(TDDTViewController.getTestCodeClassName(), testCode.getText());
		TDDCompiler.setSourceClass(TDDTViewController.getSourceCodeClassName(), sourceCode.getText());
		
		boolean checkCompile = TDDCompiler.checkCompile();
		if(checkCompile) {
			if(TDDCompiler.checkTestsAllSuccess() || !TDDCompiler.checkTests1Failed())
				new TDDAlert("In dem Modus 'schreib einen fehlschlagenden Test' darf nur 1 Test fehlschlagen!").showTestResults();
				
						
			if(TDDCompiler.checkTests1Failed()) {				
				testCodeTimerStop = false;
				sourceCodeTimerStop = true;

				if(TDDTViewController.getBabyStepsMode())
					sourceCodeCounter = TDDTViewController.getBbyMinute();

				if(TDDTViewController.getTrackingMode()){
					TrackingHelper.stopTrackingCounter();
					log.addLast(new TDDTuple(TrackingHelper.difference(), "Test" + String.valueOf(phaseCounter)));
					TrackingHelper.startTrackingCounter();
				}

				switchToMakeTheTestPass();
				try { // damit man sieht auf wie viel sek man auf der anderen seite war , 1sek warten damit der timer durchl�uft und nichts dazwischen kommt
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
				TrackingHelper.stopTrackingCounter();
				log.addLast(new TDDTuple(TrackingHelper.difference(), "Test" + String.valueOf(phaseCounter)));
				switchToMakeTheTestPass();
				TrackingHelper.startTrackingCounter();
			}
		}		
	}


	@FXML
	private void dataButtonPressed(){
		new TDDAlert(log).showTrackingData();
	}
	
	/**
	 * werte die sich nach einer phase �ndern in einer methode refactored
	 */
	private void switchToMakeTheTestPass() {
		new TDDAlert("'Mach dass der Test gelingt'",false,true,false).switchedModeAlert();
		sourceCode.setEditable(true);
		testCode.setEditable(false);
		leftSaveButton.setStyle("-fx-border-color: green;");
		rightSaveButton.setStyle("-fx-border-color: red;");
		sourceCode.setStyle("-fx-border-color: green;");
		testCode.setStyle("-fx-border-color: red");
		bottomStatusText.setText("Du befindest dich im Modus Mach dass der Test gelingt");
		sourceCodeSave = sourceCode.getText();
		
		testCodeTimerStop = false;
		sourceCodeTimerStop = true;
		sourceCodeCounter = TDDTViewController.getBbyMinute();
		if(TDDTViewController.getBabyStepsMode())
			startSourceCodeTimer();
		
		try { // damit man sieht auf wie viel sek man auf der anderen seite war , 1sek warten damit der timer durchl�uft und nichts dazwischen kommt
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		testCodeTimer.setOpacity(0.25);
		testCodeTimer.setStyle("-fx-fill: black;");
	}
	/**
	 * tdd aktion:
	 * sind alle tests bestanden darf man in den refactor modus
	 */
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
				new TDDAlert("Refactor",false,false,true).switchedModeAlert();
				sourceCodeTimerStop = false;
				TrackingHelper.stopTrackingCounter();
				log.addLast(new TDDTuple(TrackingHelper.difference(), "SourceCode" + String.valueOf(phaseCounter)));
				TrackingHelper.startTrackingCounter();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				sourceCodeTimer.setOpacity(0.25);
				sourceCodeTimer.setStyle("-fx-fill: black;");

			}
			else new TDDAlert("Du musst alle Tests bestehen um in den Modus: Refactor zugelangen!").showTestResults();
			
		} else new TDDAlert("Source").compileError(2);
		
	}
	
	/**
	 * tdd aktion:
	 * sind immernoch alle tests bestanden so darf man zur�ck zu den tests.
	 * sind compilier fehler oder tests die fehlschlagen muss man diese erst nochbeheben
	 */
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

			else new TDDAlert("Du musst alle Tests bestehen um in den Modus: 'schreib einen fehlschlagenden Test' zu gelangen!").showTestResults();

			
		}
		else new TDDAlert("Source").compileError(2);
		
	}
	
	/**
	 * setzt den {@link refactor} mode auf true oder false
	 * @param mode
	 */
	public static void setRefactorMode(boolean mode) {
		refactor = mode;
	}
	
	/**
	 * setzt den {@link makethetestpass} mode auf true oder false
	 * @param mode
	 */
	public static void setSourceCodeMode(boolean mode) {
		makethetestpass = mode;
	}
	
	/**
	 * setzt den {@link writeafailtest} mode auf true oder false
	 * @param mode
	 */
	public static void setTestMode(boolean mode) {
		writeafailtest = mode;
	}
	
	/**
	 * erzeugt einen alert , welcher ein neues fenster aufruft in dem man die wahl hat
	 * weiter zu refactoren oder zur�ck zu den tests zugehen
	 */
	public void refactorAlert() { // von http://code.makery.ch/blog/javafx-dialogs-official/
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("TDDTrainer by Team ImmortalsGG");
		alert.setHeaderText("Refactor Optionen");
		alert.setContentText("Sind sie sicher, dass sie genug refactored haben?\nBitte waehle aus:");

		ButtonType continueRef = new ButtonType("Weiter Refactoren");
		ButtonType newtestButton = new ButtonType("Neue Tests schreiben");

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
			bottomStatusText.setText("Du befindest dich im Modus 'schreib einen fehlschlagenden Test'");
			
			testCodeTimerStop = true;
			if (TDDTViewController.getBabyStepsMode()) {
				testCodeCounter = TDDTViewController.getBbyMinute();
				startTestCodeTimer();
			}

			if (TDDTViewController.getTrackingMode()) {
				TrackingHelper.stopTrackingCounter();
				log.addLast(new TDDTuple(TrackingHelper.difference(), "Refactoren " + String.valueOf(phaseCounter)));
				TrackingHelper.startTrackingCounter();
			}
			phaseCounter++;

		}
	}
	
	
	/**
	 * startet einen visuell sichtbaren timer der von einer gegebenen anzahl an sekunden runterl�uft
	 * 
	 */
	private void startTestCodeTimer() { // babymode
		testCodeTimerThread = new Thread(() -> {
			while(testCodeTimerStop) {
				testCodeTimer.setText(String.valueOf(testCodeCounter));
				testCodeCounter--;
				
				if(testCodeCounter > farbe1) testCodeTimer.setStyle("-fx-fill: lime;"); // feature l�sst farben gr�n gelb oder rot erscheinen jenach wie viel zeit noch da ist
				else if(testCodeCounter > farbe2) testCodeTimer.setStyle("-fx-fill: gold;");
				else if(testCodeCounter > farbe3) testCodeTimer.setStyle("-fx-fill: orange;");
				else if(testCodeCounter > farbe4) testCodeTimer.setStyle("-fx-fill: orangered;");
				else if(testCodeCounter >= 0) testCodeTimer.setStyle("-fx-fill: red;");

				
				for(int i = 40; i > 0; i--) { // feature l��st den counter verblassen
					try {
						Thread.sleep(25);
					} catch (Exception e) {
						e.printStackTrace();
					}
					testCodeTimer.setOpacity(i*0.025);
				}
				if(testCodeCounter == -1) {
					testCodeCounter = TDDTViewController.getBbyMinute();
					testCode.setText(testCodeSave);
				}
			}
		});
		testCodeTimerThread.start();
	}
	
	/**
	 * startet einen visuell sichtbaren timer der von einer gegebenen anzahl an sekunden runterl�uft
	 * 
	 */
	private void startSourceCodeTimer() {
		sourceCodeTimerThread = new Thread(() -> {
			while(sourceCodeTimerStop) {
				sourceCodeTimer.setText(String.valueOf(sourceCodeCounter));
				sourceCodeCounter--;
				
				if(sourceCodeCounter > farbe1) sourceCodeTimer.setStyle("-fx-fill: lime;"); // feature l�sst farben gr�n gelb oder rot erscheinen jenach wie viel zeit noch da ist
				else if(sourceCodeCounter > farbe2) sourceCodeTimer.setStyle("-fx-fill: gold;");
				else if(sourceCodeCounter > farbe3) sourceCodeTimer.setStyle("-fx-fill: orange;");
				else if(sourceCodeCounter > farbe4) sourceCodeTimer.setStyle("-fx-fill: orangered;");
				else if(sourceCodeCounter >= 0) sourceCodeTimer.setStyle("-fx-fill: red;");


				for(int i = 40; i > 0; i--) { // feature l��st den counter verblassen
					try {
						Thread.sleep(25);
					} catch (Exception e) {
						e.printStackTrace();
					}
					sourceCodeTimer.setOpacity(i*0.025);
				}
				if(sourceCodeCounter == -1) {
					sourceCodeCounter = TDDTViewController.getBbyMinute();

					sourceCode.setText(sourceCodeSave);
				}
			}
		});
		sourceCodeTimerThread.start();
	}
	
	/**
	 * unterbricht die timer
	 */
	public static void interruptTimer() { //babymode
	
		testCodeTimerStop = false;
		sourceCodeTimerStop = false;
	}
	
	
	//Helpbuttons
	
	/**
	 * ruft {@link TDDTHelpButtons.howToWriteAMethod} auf wenn der button gedr�ckt worden ist
	 */
	@FXML
	private void pressedHelpButtonMethod() {
		TDDTHelpButtons.howToWriteAMethod();
	}
	
	
}
