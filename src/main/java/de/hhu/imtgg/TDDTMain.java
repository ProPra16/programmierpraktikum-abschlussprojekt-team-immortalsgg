package de.hhu.imtgg;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TDDTMain extends Application {

	private static Stage primarystage;
	private static AnchorPane tddtviewlayout;
	private static BorderPane tddtrainerview;
	
	@Override
	public void start(Stage stage) {
		this.primarystage = stage;
		this.primarystage.setTitle("TDD Trainer by Team ImmortalsGG");
		
		initTDDTViewLayout();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static  void initTDDTViewLayout() {			// l�dt layout f�r startbild aus FXML -> siehe TDDTView.fmxl
		 try {
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(TDDTMain.class.getResource("layout/TDDTView.fxml"));
			 tddtviewlayout = (AnchorPane) loader.load(); 
			 Scene scene = new Scene(tddtviewlayout);
			 primarystage.setScene(scene);
			 primarystage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initTDDTrainerView(String testcode,String sourcecode) { //komisch gemacht doch die textarea l�sst sich ver�ndern beim start
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(TDDTMain.class.getResource("layout/TDDTrainerView.fxml"));
			tddtrainerview = (BorderPane) loader.load();
			
			SplitPane splitpanewithtextareas = (SplitPane) tddtrainerview.getChildren().get(2);
			AnchorPane anchorpanewithsourcecodearea = (AnchorPane) splitpanewithtextareas.getItems().get(0);
			AnchorPane anchorpanewithtestcodearea = (AnchorPane) splitpanewithtextareas.getItems().get(1);
			TextArea textareasourcecode = (TextArea) anchorpanewithsourcecodearea.getChildren().get(0); 
			TextArea textareatestcode = (TextArea) anchorpanewithtestcodearea.getChildren().get(0); 
			textareatestcode.setText(testcode);
			textareasourcecode.setText(sourcecode);
			Scene scene = new Scene(tddtrainerview);
			primarystage.setScene(scene);
			primarystage.show();
		} catch(IOException e) {
			e.printStackTrace();
		
		}
	}
}

