package com.jneill.SandDuneSimulator;

import java.util.Random;

public class SandDunes {
	
	//paramters/fields
	private int x, i, j, k, l, m, t, dsteps;
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
}
