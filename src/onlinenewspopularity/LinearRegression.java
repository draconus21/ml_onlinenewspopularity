/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * This abstract class defines the structure for Linear Regression classes
 * @author neeth
 */
public abstract class LinearRegression {
   
    protected Matrix theta;
    protected Matrix x;
    protected Matrix y;
    
    public void init (Matrix x, Matrix y) throws Exception {
        this.x = x;
        this.y = y;
        int features = x.getColumnDimension();
        if(y.getRowDimension() != x.getRowDimension()) {
            throw new Exception ("Invalid traning data. Row count mismatch. X: "
                    + x.getRowDimension() + "x" + x.getColumnDimension() +
                    " | Y: "  + y.getRowDimension() + "x" + y.getColumnDimension());
        }
        theta = new Matrix(features,1);
        init_theta(theta);
    }
    
    private void init_theta(Matrix theta) {
        Random randomNumGen = new Random();
        int rand_bound = 10;
        for(int i = 0; i < theta.getRowDimension(); i++) {
            double thetaVal = (double)(randomNumGen.nextInt(rand_bound)+1)/rand_bound;
            theta.set(i, 0, thetaVal);
        }
        System.out.println("theta: ");
        theta.set(0, 0, 0.5);
        theta.set(1, 0, 1);
        theta.set(2, 0, 0.5);
        theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
    }
    public abstract Matrix doLinearRegression(); 
    /*public void test() {
        LinearRegression l = new LinearRegression();
        double[][] test = new double[3][3];
        for(int i=0; i<3; i++) {
            for(int j = 0; j<3; j++) {
                test[i][j] = i;
            }
        }
        Matrix x = new Matrix(test);
        
        for(int i=0; i<3; i++) {
            for(int j = 0; j<3; j++) {
                test[i][j] = 1;
            }
        }
        test[0][0] = 1;
        test[0][1] = 2;
        test[0][2] = 3;
        test[1][0] = 0;
        test[1][1] = 1;
        test[1][2] = 4;
        test[2][0] = 5;
        test[2][1] = 6;
        test[2][2] = 0;
        
        Matrix theta = new Matrix(test);
        l.init(x, theta);
        theta.print(3, 3);
        System.out.println("x");
        Matrix inv = theta.inverse();
        inv.print(3, 3);
    }*/
}
