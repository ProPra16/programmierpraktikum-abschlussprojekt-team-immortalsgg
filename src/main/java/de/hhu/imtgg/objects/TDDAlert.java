package de.hhu.imtgg.objects;

import de.hhu.imtgg.compiler.TDDCompiler;
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
		
	public TDDAlert(String modus,boolean test,boolean source,boolean refactor) {
		this.message = modus;
		this.test = test;
		this.source = source;
		this.refactor = refactor;
	}
	
	public TDDAlert(String message) {
		this.message = message;
	}
	
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
	
	public void showTestResults() {
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
