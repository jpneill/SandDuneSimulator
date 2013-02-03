package com.jneill.SandDuneSimulator;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class SandDuneSimulator extends Application{
	private Button btn;
	private HBox rootBox;
	private ScrollPane sp;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Sand Dune Sim");
		
		btn = new Button();
		btn.setText("This is a button.");
		
		sp = new ScrollPane();
		sp.setPrefHeight(200);
		sp.setVmax(200);
		
		rootBox = new HBox(10);
		rootBox.setAlignment(Pos.CENTER);		
		rootBox.getChildren().addAll(btn, sp);
		
		primaryStage.setScene(new Scene(rootBox, 400, 400));
		primaryStage.show();
	}
		

	public static void main(String[] args){
		launch(args);
	}
}
