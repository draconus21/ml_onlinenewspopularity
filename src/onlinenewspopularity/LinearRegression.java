/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.*;

/**
 *
 * @author neeth
 */
public class LinearRegression {
   
    private Matrix theta;
    private Matrix x;
    
    public void init (Matrix x, Matrix theta) {
        this.x = x;
        this.theta = theta;
    }
    
    
    public void test() {
        System.out.println("test");
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
    }
}
