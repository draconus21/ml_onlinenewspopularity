/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.text.DecimalFormat;
import java.util.logging.Logger;

/**
 *
 * @author neeth
 */
public class GradientDescent extends LinearRegression {
    private static final Logger LOGGER = Logger.getLogger(GradientDescent.class.getName());
    
    private double alpha = 0.05;

    @Override
    public Matrix doLinearRegression() {
        System.out.println("==============START LINEAR REGRESSION==============");
        Matrix res = x.times(theta);
        //res.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        //System.out.println(res.getRowDimension() + "x" + res.getColumnDimension());
        
        //derivative();
        
        for(int i = 0; i<10; i++) {
            updateTheta();
            Matrix check = thetaX().minus(y);
            double err = 0;
            for(int j = 0; j<check.getRowDimension(); j++) {
                err = err + check.get(j, 0);
            }
            System.out.println("====ERR: " + err);
            if(Math.abs(err) < 0.001) {
                break;
            }
            //System.out.println("theta:" );
            //theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        }
        return theta;
    }
    
    private void updateTheta() {
        Matrix der = derivative();
        //System.out.println("der:" );
        //der.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        System.out.println("der times alpha");
        der.times((double)alpha).print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        theta = theta.minus(der.times((double)alpha));
        System.out.println("theta");
        theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
    }
    
    private Matrix derivative() {
        Matrix err = thetaX().minus(y);
        
        Matrix der = err.transpose().times(x).transpose();
        /*for(int i = 0; i<Constants.SIZE; i++) {
            der.set(0, 0, der.get(0, 0) + (thetaX(i)-y.get(i, 0)));
        }
        
        for(int j = 1; j<x.getColumnDimension(); j++) {
            for(int i = 0; i<Constants.SIZE; i++) {
                der.set(j, 0, der.get(j, 0) + (thetaX(i)-y.get(i, 0)) * x.get(i, j));
            }
        }
        
        System.out.println("old der");
        der.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        der = der.times((double)1/Constants.SIZE);
        System.out.println("new der");
        der.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        */
        return der;
    }
    
    private Matrix thetaX() {
        Matrix res = x.times(theta);
        return res;
        
    }
}
