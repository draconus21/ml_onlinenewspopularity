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
    
    @Override
    public Matrix predict(Matrix data) {
        return data.times(theta);
    }
    
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
