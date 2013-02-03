package com.jneill.SandDuneSimulator;

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class SandDuneSimulator extends Application{
	public static void main(String[] args){
		launch(args);
	}	

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Sand Dune Sim");
		Button btn = new Button();
		btn.setText("This is a button.");
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, 400, 400));
		primaryStage.show();
	}
		
}
