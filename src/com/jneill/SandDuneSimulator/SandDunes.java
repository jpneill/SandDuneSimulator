package com.jneill.SandDuneSimulator;

import java.awt.image.*;
import java.util.Random;

public class SandDunes {
	
	//paramters/fields
	private int x, i, j, k, l, t, dsteps;
	private double h, D, beta, sum;
	private double[][] hmap, delta1, delta2, wind, I, delta, updatedMap;
	private double[] windInput;
	
	public int getT(){
		return this.t;
	}
	
	public void setT(int H){
		this.t = H;
	}
	
	public double getD(){
		return this.D;
	}
	
	public void setD(double d){
		this.D = d;
	}
	
	public double getbeta(){
		return this.beta;
	}
	
	public void setbeta(double Beta){
		this.beta = Beta;
	}
	
	public int getdsteps(){
		return this.dsteps;
	}
	
	public void setdsteps(int Dsteps){
		this.dsteps = Dsteps;
	}
	
	public double[][] gethmap(){
		return this.hmap;
	}
	
	public double[][] getupdatedMap(){
		return this.updatedMap;
	}
	
	//Constructors
	public SandDunes(int rows, int cols, double d, double Beta, int T, int Dsteps, double[] wInp){
		wind = new double[3][3];
		hmap = new double[rows][cols];
		delta1 = new double[rows][cols];
		delta2 = new double[rows][cols];
		delta = new double[rows][cols];
		I = new double[rows][cols];
		updatedMap = new double[rows][cols];
		windInput = new double[8];
		this.t = T;
		this.D = d;
		this.beta = Beta;
		this.dsteps = Dsteps;
		this.windInput = wInp;
	}
	
	//methods
	public void Initiate(){
		//generate the initial heightmap
		Random rand = new Random();
		for(i = 0; i < hmap[0].length; i++){
			for(j = 0; j < hmap.length; j++){
				//generate a random double between -0.05 and 0.05
				x = rand.nextInt();
				if(x % 2 == 0){
					x = rand.nextInt();
				} else {
					x = rand.nextInt()*-1;
				}
				if(x != 0){
					x /= Math.abs(x);
				}
				h = rand.nextDouble() / 20;
				hmap[i][j] = h * (double)x;
			}
		}
		wind[0][0] = windInput[0];
		wind[0][1] = windInput[1];
		wind[0][2] = windInput[2];
		wind[1][0] = windInput[3];
		wind[1][2] = windInput[4];
		wind[2][0] = windInput[5];
		wind[2][1] = windInput[6];
		wind[2][2] = windInput[7];
		wind[1][1] = 0;
	}
	
	public void IncrementHeight(double[][] update){
		/*this method calculates delta(i,j)
		 * first calculate delta1(i,j) and delta2(i,j)
		 * then I(i,j) = delta1(i,j) + delta2(i,j) 
		 */
		 for (i = 0; i < update[0].length; i++)
             for (j = 0; j < update.length; j++)
             {
                 sum = 0;
                 if (i == 0 && j == 0)//Find sum for the upper left corner element
                 {
                     sum = wind[0][0] * update[update[0].length - 1][update.length - 1] + wind[0][1] * 
                    		 update[update[0].length - 1][j] + wind[0][2] * update[update[0].length - 1][j + 1] + 
                    		 wind[1][0] * update[i][update.length - 1] + wind[1][2] * update[i][j + 1] + 
                    		 wind[2][0] * update[i + 1][update.length - 1] + wind[2][1] * update[i + 1][j] + 
                    		 wind[2][2] * update[i + 1][j + 1];
                 }
                 else if (i == update[0].length - 1 && j == update.length - 1)//Find sum for the bottom right corner element
                 {
                     sum = wind[0][0] * update[i - 1][j - 1] + wind[0][1] * update[i - 1][update.length - 1] + 
                    		 wind[0][2] * update[i - 1][0] + wind[1][0] * update[i][j - 1] + wind[1][2] * update[i][0] + 
                    		 wind[2][0] * update[0][j - 1] + wind[2][1] * update[0][j] + wind[2][2] * update[0][0];
                 }
                 else if (i == update[0].length - 1 && j == 0)//Find sum for the bottom left corner element
                 {
                     sum = wind[0][0] * update[i - 1][update.length - 1] + wind[0][1] * update[i - 1][j] + 
                    		 wind[0][2] * update[i - 1][j + 1] + wind[1][0] * update[i][update.length - 1] + 
                    		 wind[1][2] * update[i][j + 1] + wind[2][0] * update[0][update.length - 1] + wind[2][1] * 
                    		 update[0][0] + wind[2][2] * update[0][1];
                 }
                 else if (i == 0 && j == update.length - 1)//Find sum for the top right corner element
                 {
                     sum = wind[0][0] * update[update.length - 1][j - 1] + wind[0][1] * 
                    		 update[update[0].length - 1][update.length - 1] + wind[0][2] * 
                    		 update[update[0].length - 1][0] + wind[1][0] * update[i][j - 1] + wind[1][2] * 
                    		 update[0][0] + wind[2][0] * update[i + 1][j - 1] + wind[2][1] * update[i + 1][j] + wind[2][2] * 
                    		 update[i + 1][0];
                 }
                 else if (i == 0 && j > 0 && j < update.length - 1)//Find sum for the first row excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (k == 0)
                                 sum += wind[k][l] * update[update.length - 1][j + (l - 1)];
                             else
                                 sum += wind[k][l] * update[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else if (i == update[0].length - 1 && j > 0 && j < update.length - 1)//Find sum for the last row excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (k == 2)
                                 sum += wind[k][l] * update[0][j + (l - 1)];
                             else
                                 sum += wind[k][l] * update[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else if (j == 0 && i > 0 && i < update[0].length - 1)//Find sum for the first column excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (l == 0)
                                 sum += wind[k][l] * update[i + (k - 1)][update.length - 1];
                             else
                                 sum += wind[k][l] * update[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else if (j == update.length - 1 && i > 0 && i < update[0].length - 1)//Find sum for the last column excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (l == 2)
                                 sum += wind[k][l] * update[i + (k - 1)][0];
                             else
                                 sum += wind[k][l] * update[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else//Find sum for every element inside the edges where a(i,j) does not take into account the boundaries
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                             sum += (wind[k][l] * update[i + (k - 1)][j + (l - 1)]);
                 }
                 delta1[i][j] = D * (sum - update[i][j]); //This line calculates the lattice element delta1(i,j) = D((sum of a(k,l)*h(i+k,j+l))-h(i,j))
                 delta2[i][j] = beta * Math.tanh(update[i][j]) - update[i][j];//This line calculates the lattice element delta2(i,j) = beta*tanh(h(i,j))-h(i,j)
                 I[i][j] = delta1[i][j] + delta2[i][j];//This line calculates the lattice element I(i,j) = delta1(i,j) + delta2(i,j)
                 sum = 0;//finally calculate the lattice delta(i,j)
                 //Again we have the exact same if ladder as above, but with the new matrix I
                 if (i == 0 && j == 0)//Find sum for the upper left corner element
                 {
                     sum = wind[0][0] * I[I[0].length - 1][I.length - 1] + wind[0][1] * 
                    		 I[I[0].length - 1][j] + wind[0][2] * I[I[0].length - 1][j + 1] + 
                    		 wind[1][0] * I[i][I.length - 1] + wind[1][2] * I[i][j + 1] + 
                    		 wind[2][0] * I[i + 1][I.length - 1] + wind[2][1] * I[i + 1][j] + 
                    		 wind[2][2] * I[i + 1][j + 1];
                 }
                 else if (i == I[0].length - 1 && j == I.length - 1)//Find sum for the bottom right corner element
                 {
                     sum = wind[0][0] * I[i - 1][j - 1] + wind[0][1] * I[i - 1][I.length - 1] + 
                    		 wind[0][2] * I[i - 1][0] + wind[1][0] * I[i][j - 1] + wind[1][2] * I[i][0] + 
                    		 wind[2][0] * I[0][j - 1] + wind[2][1] * I[0][j] + wind[2][2] * I[0][0];
                 }
                 else if (i == I[0].length - 1 && j == 0)//Find sum for the bottom left corner element
                 {
                     sum = wind[0][0] * I[i - 1][I.length - 1] + wind[0][1] * I[i - 1][j] + 
                    		 wind[0][2] * I[i - 1][j + 1] + wind[1][0] * I[i][I.length - 1] + 
                    		 wind[1][2] * I[i][j + 1] + wind[2][0] * I[0][I.length - 1] + wind[2][1] * 
                    		 I[0][0] + wind[2][2] * I[0][1];
                 }
                 else if (i == 0 && j == I.length - 1)//Find sum for the top right corner element
                 {
                     sum = wind[0][0] * I[I.length - 1][j - 1] + wind[0][1] * 
                    		 I[I[0].length - 1][I.length - 1] + wind[0][2] * 
                    		 I[I[0].length - 1][0] + wind[1][0] * I[i][j - 1] + wind[1][2] * 
                    		 I[0][0] + wind[2][0] * I[i + 1][j - 1] + wind[2][1] * I[i + 1][j] + wind[2][2] * 
                    		 I[i + 1][0];
                 }
                 else if (i == 0 && j > 0 && j < I.length - 1)//Find sum for the first row excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (k == 0)
                                 sum += wind[k][l] * I[I.length - 1][j + (l - 1)];
                             else
                                 sum += wind[k][l] * I[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else if (i == I[0].length - 1 && j > 0 && j < I.length - 1)//Find sum for the last row excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (k == 2)
                                 sum += wind[k][l] * I[0][j + (l - 1)];
                             else
                                 sum += wind[k][l] * I[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else if (j == 0 && i > 0 && i < I[0].length - 1)//Find sum for the first column excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (l == 0)
                                 sum += wind[k][l] * I[i + (k - 1)][I.length - 1];
                             else
                                 sum += wind[k][l] * I[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else if (j == I.length - 1 && i > 0 && i < I[0].length - 1)//Find sum for the last column excluding the corner elements
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                         {
                             if (l == 2)
                                 sum += wind[k][l] * I[i + (k - 1)][0];
                             else
                                 sum += wind[k][l] * I[i + (k - 1)][j + (l - 1)];
                         }
                 }
                 else//Find sum for every element inside the edges where a(i,j) does not take into account the boundaries
                 {
                     for (k = 0; k < wind[0].length; k++)
                         for (l = 0; l < wind.length; l++)
                             sum += (wind[k][l] * I[i + (k - 1)][j + (l - 1)]);
                 }
                 
                 delta[i][j] = I[i][j] - sum;                 
             }
    }
	
	public BufferedImage Draw(double[][] sandGrid){
		BufferedImage image = new BufferedImage(sandGrid[0].length, sandGrid.length, BufferedImage.TYPE_INT_RGB);
		for(i = 0; i < sandGrid[0].length; i++){
			for(j = 0; j < sandGrid.length; j++){
				if(sandGrid[i][j] < 0.005){
					image.setRGB(i, j, 0x000000);//set pixel to white if it is below the threshold
				} else {
					image.setRGB(i, j, 0xFFFFFF);//set pixel to black
				}
			}
		}
		return image;
	}
	
	public void Iterate(int x){
		if(x == 0){
			IncrementHeight(hmap);
			for(i = 0; i < updatedMap.length; i++){
				for(j = 0; j < updatedMap.length; j++){
					updatedMap[i][j] = hmap[i][j] + delta[i][j];//create a new map after one timestep
				}
			}
		}else{
			IncrementHeight(updatedMap);
			for(i = 0; i < updatedMap.length; i++){
				for(j = 0; j < updatedMap.length; j++){
					updatedMap[i][j] += delta[i][j];//update the map for each time step
				}
			}
		}
	}
}

