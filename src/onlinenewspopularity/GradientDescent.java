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
    
    private double alpha = 0.0001;
    private Matrix prevTheta;
    
    @Override
    public Matrix doLinearRegression() {
        try {
            System.out.println("==============START LINEAR REGRESSION==============");
            Matrix res = x.times(theta);
            double prevErr = 0;
            double err = 0;
            theta = new Matrix(new double[][]{{0.5}, {2}, {1}});
            boolean first = true;
            //while(true) {
            for(int i = 0; i<10; i++) {
                updateTheta();
                Matrix check = thetaX().minus(y);
                System.out.println("check matrix: " + check.getRowDimension() + "x" + check.getColumnDimension());
                check.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
                for(int j = 0; j<check.getRowDimension(); j++) {
                    err = err + check.get(j, 0);
                }
                System.out.println("====ERR: " + err);
                if(first) {
                    prevErr = err;
                    first = false;
                }
                
                if(Math.abs(err) < 0.0000001) {
                    break;
                }
                
                /*if((err>0 && prevErr<0) || (err<0 && prevErr>0)) {
                    alpha = alpha/10;
                    System.out.println(alpha);
                    LOGGER.info("prevError: " + prevErr + " | Error : " + err);
                    LOGGER.log(Level.INFO, "Reducing alpha: {0}", (double)alpha);
                    //theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
                    theta = prevTheta;
                    //theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
                    
                    //return doLinearRegression();
                }/* else if(((prevErr - err)/err < 0.00001)) {
                    alpha = alpha * 10;
                    LOGGER.info(prevErr - err + " | prevError: " + prevErr + " | Error : " + err);
                    LOGGER.log(Level.INFO, "Increasing alpha: {0}", (double)alpha);
                    //return doLinearRegression();
                }*/else {
                    prevErr = err;
                }
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
        System.out.println("der");
        der.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        
        theta = theta.minus(der.times((double)alpha));
        //System.out.println("new theta");
        //theta.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
    }
    
    private Matrix derivative() {
        Matrix err = thetaX().minus(y);
        Matrix der = x.transpose().times(err);
        der = der.times((double)1/Constants.SIZE);
        der.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
        return der;
    }
    
    private Matrix thetaX() {
        Matrix res = x.times(theta);
        return res;
        
    }
}
