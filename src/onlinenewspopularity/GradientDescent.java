/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neeth
 */
public class GradientDescent extends LinearRegression {
    private static final Logger LOGGER = Logger.getLogger(GradientDescent.class.getName());
    
    private double alpha = 100;
    
    @Override
    public Matrix doLinearRegression() {
        try {
            System.out.println("==============START LINEAR REGRESSION==============");
            int itr = 0;
            
            //while(true) {
            for(int i = 0; i<500; i++) {
                if(updateTheta() < 0.0000001) {
                    break;
                }
                itr++;
            }
            System.out.println("Iterations: " + itr);
            return theta;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    
    private double updateTheta() {
        Matrix der = derivative();
        
        double err = getError(theta);
        double nextErr = getError(theta.minus(der.times((double)alpha)));
        
        while (nextErr > err) {
            alpha = alpha/3;
            nextErr = getError(theta.minus(der.times((double)alpha)));
        }
        
        System.out.println("Error: " + err);
        theta = theta.minus(der.times((double)alpha));
        return err;
    }
    
    private double getError(Matrix theta) {
        Matrix check = x.times(theta).minus(y);
        Matrix cost  = check.transpose().times(check);
                
        return 0.5 * cost.get(0, 0)/Constants.SIZE;
    }
    
    private Matrix derivative() {
        Matrix err = thetaX().minus(y);
        Matrix der = x.transpose().times(err);
        der = der.times((double)1/Constants.SIZE);
        return der;
    }
    
    private Matrix thetaX() {
        Matrix res = x.times(theta);
        return res;
    }
}
