package de.hhu.imtgg;

import java.io.IOException;
import java.net.URL;

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
	private static BorderPane tddtviewlayout;
	private static BorderPane tddtrainerview;
	private static Scene scene;
	
	@Override
	public void start(Stage stage) {
		TDDTMain.primarystage = stage;
		TDDTMain.primarystage.setTitle("TDD Trainer by Team ImmortalsGG");
		
		initTDDTViewLayout();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void initTDDTViewLayout() {			// laedt layout fuer startbild aus FXML -> siehe TDDTView.fmxl
		 try {
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(TDDTMain.class.getResource("layout/TDDTView.fxml"));
			 tddtviewlayout = (BorderPane) loader.load(); 
			 scene = new Scene(tddtviewlayout);			 
			 primarystage.setScene(scene);
			 primarystage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void initTDDTrainerView(String testcode,String sourcecode) { //komisch gemacht doch die textarea laesst sich veraendern beim start
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
			scene = new Scene(tddtrainerview);
						
			primarystage.setScene(scene);
			primarystage.show();
		} catch(IOException e) {
			e.printStackTrace();
		
		}
	}
}

