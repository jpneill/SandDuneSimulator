package com.jneill.SandDuneSimulator;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.stage.*;

public class SandDuneSimulator extends Application{
	private Button run;
	private GridPane rootGrid;
	private GridPane windGrid;
	private TextField wind00, wind01, wind02, wind10, wind12, wind20, wind21, wind22;
	private TextField D, beta, time, steps;
	private ScrollPane sp;
	
	//public int t, stps;
	//public double d, b;
		
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Sand Dune Sim");
		
		rootGrid = new GridPane();
		rootGrid.setAlignment(Pos.CENTER);
		rootGrid.setHgap(10);
		rootGrid.setVgap(10);
		rootGrid.setPadding(new Insets(10, 10, 10, 10));
		
		//set up the grid for wind input
		windGrid = new GridPane();
		windGrid.setHgap(10);
		windGrid.setVgap(10);
		
		wind00 = new TextField();
		windGrid.add(wind00, 0, 0);
		wind01 = new TextField();
		windGrid.add(wind01, 1, 0);
		wind02 = new TextField();
		windGrid.add(wind02, 2, 0);
		wind10 = new TextField();
		windGrid.add(wind10, 0, 1);
		wind12 = new TextField();
		windGrid.add(wind12, 2, 1);
		wind20 = new TextField();
		windGrid.add(wind20, 0, 2);
		wind21 = new TextField();
		windGrid.add(wind21, 1, 2);
		wind22 = new TextField();
		windGrid.add(wind22, 2, 2);
		
		//set up the textfields, buttons and scrollpane for calculation and display 
		run = new Button();
		run.setText("Run Simulation");
		
		D = new TextField();
		beta = new TextField();
		time = new TextField();
		steps = new TextField();
		
		sp = new ScrollPane();
		sp.setPrefSize(300, 400);
		//sp.setVmax(200);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		rootGrid.add(windGrid, 0, 0, 3, 1);
		rootGrid.add(sp, 4, 0, 2, 5);
		rootGrid.add(D, 1, 1);
		rootGrid.add(beta, 1, 2);
		rootGrid.add(time, 1, 3);
		rootGrid.add(steps, 1, 4);
		rootGrid.add(run, 0, 5);
		
		primaryStage.setScene(new Scene(rootGrid, 800, 800));
		primaryStage.show();
	}
		

	public static void main(String[] args){
		launch(args);
	}
}
