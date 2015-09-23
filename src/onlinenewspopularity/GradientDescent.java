/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neeth
 */
public class GradientDescent extends LinearRegression {
    private static final Logger LOGGER = Logger.getLogger(GradientDescent.class.getName());
    
    private double alpha = 0.4;
    private Matrix prevTheta;
    
    @Override
    public Matrix doLinearRegression() {
        try {
            System.out.println("==============START LINEAR REGRESSION==============");
            Matrix res = x.times(theta);
            double err = 0;
            while(true) {
            //for(int i = 0; i<500; i++) {
                Matrix check = thetaX().minus(y);
                Matrix cost  = check.transpose().times(check);
                
                err = 0.5 * cost.get(0, 0)/Constants.SIZE;
                System.out.println("====ERR: " + err);
                
                if(err < 0.0000001) {
                    break;
                }
                updateTheta();
                
            }
            return theta;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    
    private void updateTheta() {
        Matrix der = derivative();
        prevTheta = theta;
        theta = theta.minus(der.times((double)alpha));
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
