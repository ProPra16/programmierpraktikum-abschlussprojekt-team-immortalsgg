package de.hhu.imtgg.objects;

import java.util.Optional;

import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.compiler.TDDCompiler;
import de.hhu.imtgg.controller.TDDTDarkModeController;
import de.hhu.imtgg.controller.TDDTViewController;
import de.hhu.imtgg.controller.TDDTrainerViewController;
import de.hhu.imtgg.controller.TDDTrainerViewController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class TDDAlert {
	
	private String message;
	private boolean test;
	private boolean source;
	private boolean refactor;
		
	/**
	 * konstruktor für einen Alert , setzt werte auf verschiedene params , siehe params
	 * @param modus
	 * @param test
	 * @param source
	 * @param refactor
	 */
	public TDDAlert(String modus,boolean test,boolean source,boolean refactor) {
		this.message = modus;
		this.test = test;
		this.source = source;
		this.refactor = refactor;
	}
	
	/**
	 * konstruktor für einen alert setzt nur wert auf message siehe param
	 * @param message
	 */
	public TDDAlert(String message) {
		this.message = message;
	}
	
	/**
	 * default konstruktor 
	 */
	public TDDAlert() {
		
	}
	
	/**
	 * fragt ob man wirklich sicher ist , ja weiter oder nein abbrechen
	 */
	public void areUSureMessage() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("TDDTrainer by Team ImmortalsGG");
		alert.setHeaderText("Zurueck ins Hauptmenue?");
		alert.setContentText("Sind sie sicher, dass sie ins Hauptmenue wechseln wollen?\nIhr ganzer Fortschritt geht dabei verloren!");

		ButtonType yesButton = new ButtonType("Ja");
		ButtonType noButton = new ButtonType("Nein, abbrechen!");

		alert.getButtonTypes().setAll(yesButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yesButton) {
			if(TDDTDarkModeController.getDarkMode()) TDDTMain.initTDDTViewLayoutDarkMode();
			else TDDTMain.initTDDTViewLayoutNormalMode();
			
			TDDTViewController.setBbyMinuteDefault();
		}
		else if (result.get() == noButton) {
			return;
		}
	}
	
	/**
	 * der modus wird geswitcht , der modus wird hier auch direkt geändert und ein alert kommt als information
	 */
	public void switchedModeAlert() { // alert das modus geswitcht und switcht booleans in tddvcontroller
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("TDD Trainer by Team ImmortalsGG");
		alert.setHeaderText(null);
		alert.setContentText("Du befindest dich nun im Modus " + message);
		
		TDDTrainerViewController.setTestMode(test);
		TDDTrainerViewController.setSourceCodeMode(source);
		TDDTrainerViewController.setRefactorMode(refactor);
	
		alert.showAndWait();
	}
	
	/**
	 * gibt einen alert aus der sagt dass der test nicht compiliert aber fragt ob man dennoch weiter will
	 * @param klasse
	 * @return
	 */
	public boolean compileErrorModeSwitchAlert(int klasse) { // alert das der Test nicht kompiliert und ob man trotzdem switchen will
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("TDD Trainer by Team ImmortalsGG");
		alert.setHeaderText(null);
		alert.setContentText("Deine Tests kompillieren nicht!\nMÃ¶chtest du den Modus trotzdem wechseln?");
		
		ButtonType yesButton = new ButtonType("Ja");
		ButtonType noButton = new ButtonType("Nein, abbrechen!");

		alert.getButtonTypes().setAll(yesButton, noButton);
		
		String exceptionText = TDDCompiler.getCompileErrors(klasse);

		Label label = new Label("Fehlermeldung:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yesButton) {
			return true;
		}
		return false;
	}
	
	/**
	 * gibt einen alert aus und zeigt die test results
	 */
	public void showTestResults() {	// von http://code.makery.ch/blog/javafx-dialogs-official/
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("TDD Trainer by Team ImmortalsGG");
		alert.setHeaderText(null);
		alert.setContentText(message);
	
		String exceptionText = TDDCompiler.getTestFails();
		if(exceptionText.isEmpty()) exceptionText = "Alle Tests bestanden.";
		Label label = new Label("Testergebnisse:");
		
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		TDDCompiler.resetFails();
		alert.showAndWait();
	}
	
	/**
	 * gibt einen alert aus und zeigt an das der code nicht compiliert und dazu die compilierfehler
	 * @param klasse
	 */
	public void compileError(int klasse) {  // von http://code.makery.ch/blog/javafx-dialogs-official/
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("TDD Trainer by Team ImmortalsGG");
		alert.setHeaderText(null);
		alert.setContentText("Dein " + message +"code ist falsch!");
	
		String exceptionText = TDDCompiler.getCompileErrors(klasse);

		Label label = new Label("Fehlermeldung:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		
		alert.showAndWait();
	}
	
}
