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
 * This is a LinearRegression class.
 * It performs linear regression by using the Closed-form solution
 * @author neeth
 */
public class ClosedFormSolution extends LinearRegression {

    private static final Logger LOGGER = Logger.getLogger(ClosedFormSolution.class.getName());
    
    /**
     * Predicts the target values for give data
     * @param data is an nxm matrix with data for prediction
     * @return an nx1 matrix with target values
     * NOTE: n is the number of data available for predictions and m is the
     * number of features.
     */
    @Override
    public Matrix predict(Matrix data) {
        return data.times(theta);
    }
    
    /**
     * Gives the closed form solution for the data set
     * @return mx1 matrix of trained weights
     * NOTE: m is the number of features in training data
     */
    @Override
    public Matrix doLinearRegression() {
        try{
            Matrix transpose = x.transpose();

            Matrix prod = transpose.times(x);
            Matrix inv  = prod.inverse();
            Matrix temp = inv.times(x.transpose());
            theta  = temp.times(y);
            
            return theta;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
            return null;
        }
       
    }
    
}
