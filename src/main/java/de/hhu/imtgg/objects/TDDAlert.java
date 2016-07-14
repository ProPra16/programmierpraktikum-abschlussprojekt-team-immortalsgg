package de.hhu.imtgg.objects;

import java.util.LinkedList;
import java.util.Optional;

import de.hhu.imtgg.TDDTMain;
import de.hhu.imtgg.compiler.TDDCompiler;
import de.hhu.imtgg.controller.TDDTDarkModeController;
import de.hhu.imtgg.controller.TDDTViewController;
import de.hhu.imtgg.controller.TDDTrainerViewController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TDDAlert {
	
	private String message;
	private boolean test;
	private boolean source;
	private boolean refactor;
	private LinkedList<TDDTuple> log;
		
	public TDDAlert(String modus,boolean test,boolean source,boolean refactor) {
		this.message = modus;
		this.test = test;
		this.source = source;
		this.refactor = refactor;
	}
	
	public TDDAlert(String message) {
		this.message = message;
	}
	public TDDAlert(LinkedList<TDDTuple> log){
		this.log = log;
	}
	
	public TDDAlert() {
		
	}
	
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
	
	public boolean compileErrorModeSwitchAlert(int klasse) { // alert das der Test nicht kompiliert und ob man trotzdem switchen will
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("TDD Trainer by Team ImmortalsGG");
		alert.setHeaderText(null);
		alert.setContentText("Deine Tests kompillieren nicht!\nMöchtest du den Modus trotzdem wechseln?");
		
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

	public void showTrackingData(){

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("TDD Trainer by Team ImmortalsGG");
		alert.setHeaderText("click 'show details' for chart of Spent Time");
		CategoryAxis tasks = new CategoryAxis();
		NumberAxis time = new NumberAxis();
		BarChart<String,Number> barChart = new BarChart<>(tasks,time);

		if (log.size()==1){
			barChart.setTitle("Tracking Information: One task so far");
		}else {
			barChart.setTitle("Tracking Information: " + log.size() + " tasks so far");
		}

		tasks.setLabel("Task");
		time.setLabel("Time Spent");

		XYChart.Series series1 = new XYChart.Series();
		for(TDDTuple date : log){
			series1.getData().add(new XYChart.Data(date.getTask(), date.getSeconds()));
		}

		barChart.getData().addAll(series1);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(barChart,0,0);
		alert.getDialogPane().setExpandableContent(expContent);

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
