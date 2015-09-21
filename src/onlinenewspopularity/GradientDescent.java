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
    
    private double alpha = 0.5;

    @Override
    public Matrix doLinearRegression() {
        Matrix res = x.times(theta);
        //res.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        //System.out.println(res.getRowDimension() + "x" + res.getColumnDimension());
        
        for(int i = 0; i<10; i++) {
            updateTheta();
            //System.out.println("theta:" );
            //theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        }
        return theta;
    }
    
    private void updateTheta() {
        Matrix der = derivative();
        //System.out.println("der:" );
        //der.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        //System.out.println("der times alpha");
        //der.times((double)alpha).print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        theta = theta.minus(der.times((double)alpha));
    }
    
    private Matrix derivative() {
        Matrix der = new Matrix(x.getColumnDimension(), 1, 0.0);
        
        
        for(int i = 0; i<Constants.SIZE; i++) {
            der.set(0, 0, der.get(0, 0) + (thetaX(i)-y.get(i, 0)));
        }
        
        for(int j = 1; j<x.getColumnDimension(); j++) {
            for(int i = 0; i<Constants.SIZE; i++) {
                der.set(j, 0, der.get(j, 0) + (thetaX(i)-y.get(i, 0)) * x.get(i, j));
            }
        }
        der = der.times((double)1/Constants.SIZE);
        
        return der;
    }
    
    private double thetaX(int i) {
        Matrix sample = x.getMatrix(i, i, 0, x.getColumnDimension()-1);
        return sample.times(theta).get(0, 0);
    }
}
