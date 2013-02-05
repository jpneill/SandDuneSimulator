package com.jneill.SandDuneSimulator;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class SandDuneSimulator extends Application{
	private Button run;
	private GridPane rootGrid, windGrid;
	private VBox imagesVBox;
	private TextField wind00, wind01, wind02, wind10, wind12, wind20, wind21, wind22;
	private TextField Dinp, betaInp, timeInp, stepsInp;
	private Label D, beta, time, steps;
	private ScrollPane sp;
	private List<Image> images;
	private ImageView[] pictures;
	private double[] windInput;
	private SandDunes dunes;
	int i;
	
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
		
		wind00 = new TextField("0");
		windGrid.add(wind00, 0, 0);
		wind01 = new TextField("0");
		windGrid.add(wind01, 1, 0);
		wind02 = new TextField("0");
		windGrid.add(wind02, 2, 0);
		wind10 = new TextField("0");
		windGrid.add(wind10, 0, 1);
		wind12 = new TextField("0");
		windGrid.add(wind12, 2, 1);
		wind20 = new TextField("0");
		windGrid.add(wind20, 0, 2);
		wind21 = new TextField("0");
		windGrid.add(wind21, 1, 2);
		wind22 = new TextField("0");
		windGrid.add(wind22, 2, 2);
		
		Dinp = new TextField("0");
		betaInp = new TextField("0");
		timeInp = new TextField("100");
		stepsInp = new TextField("10");
		D = new Label("D");
		beta = new Label("beta");
		time = new Label("time");
		steps = new Label("steps");
				
		sp = new ScrollPane();
		sp.setPrefSize(200, 200);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		imagesVBox = new VBox();
		imagesVBox.setSpacing(10);
		
		//set up the textfields, buttons and scrollpane for calculation and display 
		run = new Button();
		run.setText("Run Simulation");
		run.setOnAction(new EventHandler<ActionEvent>(){			
			public void handle(ActionEvent arg0) {
				images = new ArrayList<>();
				imagesVBox.getChildren().clear();
				sp.setContent(null);
				
				/*
				 * TODO: THERE IS AN ISSUE WITH WIND DIRECTION BEING ROTATED 90 DEGREES. DOUBLE CHECK INPUT AND LOGIC.
				 * (band-aid fix might be to just auto change the input values so we get the right image)
				 */
				
				//create the wind input array
				windInput = new double[8];
				windInput[0] = Double.parseDouble(wind00.getText());
				windInput[1] = Double.parseDouble(wind01.getText());
				windInput[2] = Double.parseDouble(wind02.getText());
				windInput[3] = Double.parseDouble(wind10.getText());
				windInput[4] = Double.parseDouble(wind12.getText());
				windInput[5] = Double.parseDouble(wind20.getText());
				windInput[6] = Double.parseDouble(wind21.getText());
				windInput[7] = Double.parseDouble(wind22.getText());
				
				//if WindArrayCheck returns false then switch to default array
				if(!WindArrayCheck(windInput)){
					windInput[3] = 0.359;
					windInput[0] = windInput[5] = 0.143;
					windInput[1] = windInput[2] = windInput[4] = windInput[6] = windInput[7] = 0.071;
					wind10.setText("0.359");
					wind00.setText("0.143");
					wind20.setText("0.143");
					wind01.setText("0.071");
					wind02.setText("0.071");
					wind12.setText("0.071");
					wind21.setText("0.071");
					wind22.setText("0.071");
					Dinp.setText("0.2");
					betaInp.setText("1.1");
				}
				
				//run simulation and create images
				dunes = new SandDunes(150, 150, Double.parseDouble(Dinp.getText()), Double.parseDouble(betaInp.getText()), Integer.parseInt(timeInp.getText()), Integer.parseInt(stepsInp.getText()), windInput);
				dunes.Initiate();
				try {
					images.add(convertImage(dunes.Draw(dunes.gethmap())));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//create the images
				for(i = 0; i <= Integer.parseInt(timeInp.getText()); i++){
					//simulate the formation of the dunes over time
					dunes.Iterate(i);
					//draw image if the time = one of the desired timesteps
					if(i % Integer.parseInt(stepsInp.getText()) == 0 || i == Integer.parseInt(timeInp.getText())){
						try {
							images.add(convertImage(dunes.Draw(dunes.getupdatedMap())));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				//add images to imageview array to be displayed in scrollframe
				pictures = new ImageView[images.size()];
				for(i = 0; i < images.size(); i++){
					pictures[i] = new ImageView(images.get(i));
					imagesVBox.getChildren().add(pictures[i]);
				}
				
				sp.setContent(imagesVBox);
			}
		});

		rootGrid.add(windGrid, 0, 0, 3, 1);
		rootGrid.add(sp, 4, 0, 2, 6);
		rootGrid.add(D, 0, 1);
		rootGrid.add(Dinp, 1, 1);
		rootGrid.add(beta, 0, 2);
		rootGrid.add(betaInp, 1, 2);
		rootGrid.add(time, 0, 3);
		rootGrid.add(timeInp, 1, 3);
		rootGrid.add(steps, 0, 4);
		rootGrid.add(stepsInp, 1, 4);		
		rootGrid.add(run, 0, 5);
		
		primaryStage.setScene(new Scene(rootGrid, 800, 300));
		primaryStage.show();
	}
	
	private boolean WindArrayCheck(double[] wArray){
		/* sum the values in the wind array
		 * if the values != 1 return false causing a switch to default array 
		 */
		double summation = 0;
		for(i = 0; i < 8; i++){
			summation += wArray[i];
		}
		if(summation != 1){
			return false;
		}else{
			return true;
		}
	}
	
	//need to convert an awt.image to a jfx.image for display
	public static Image convertImage(java.awt.Image image) throws IOException{
		if (!(image instanceof RenderedImage)) {
		      BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
		              image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		      Graphics g = bufferedImage.createGraphics();
		      g.drawImage(image, 0, 0, null);
		      g.dispose();
		 
		      image = bufferedImage;
		    }
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ImageIO.write((RenderedImage) image, "png", out);
		    out.flush();
		    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		    return new javafx.scene.image.Image(in);
	}

	public static void main(String[] args){
		launch(args);
	}
}
