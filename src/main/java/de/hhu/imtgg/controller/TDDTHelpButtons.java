package de.hhu.imtgg.controller;

import java.io.IOException;

import de.hhu.imtgg.TDDTMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TDDTHelpButtons {
	
	private static Stage methodHelpStage = new Stage();
	
	public static void howToWriteAMethod() {
		BorderPane pane = new BorderPane();
		
		Image javaFunktion1 = new Image("de/hhu/imtgg/layout/javafunktion1.png");
		Image javaFunktion2 = new Image("de//hhu/imtgg/layout/javafunktion2.png");
		ImageView helpImage = new ImageView();
		helpImage.setImage(javaFunktion1);
		helpImage.setFitHeight(350);
		helpImage.setFitWidth(600);
		
		Button forAndBackButton = new Button(">");									
		forAndBackButton.setBackground(null);
		forAndBackButton.setStyle("-fx-border-color: black;");
		
		AnchorPane bottom = new AnchorPane();
		bottom.getChildren().add(forAndBackButton);
		bottom.setRightAnchor(forAndBackButton,0.0);
		
		forAndBackButton.setOnAction(e -> { if(forAndBackButton.getText().equals(">")) {
			helpImage.setImage(javaFunktion2);
			forAndBackButton.setText("<"); 
			bottom.setLeftAnchor(forAndBackButton, 0.0);
			bottom.setRightAnchor(forAndBackButton, null);
		}
		else { 
			helpImage.setImage(javaFunktion1); 
			forAndBackButton.setText(">");
			bottom.setRightAnchor(forAndBackButton, 0.0);
			bottom.setLeftAnchor(forAndBackButton, null);
		}});
		
		pane.setCenter(helpImage);
		pane.setBottom(bottom);
		Scene scene = new Scene(pane,600,400);
		methodHelpStage.setScene(scene);
		methodHelpStage.setTitle("TDD Trainer by ImmortalsGG - How To: Eine Methode schreiben");
		methodHelpStage.show();
	}
	
	public static void closeAllHelpWindows() {
		methodHelpStage.close();
	}
	
	
}
