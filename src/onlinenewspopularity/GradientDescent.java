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
    
    private int trainLen;
    private int crossLen;
    private int m;
    
    private double alpha = 100;
    private double[] mean;
    private double[] std;
    private Matrix cvX;
    private Matrix cvY;
    
    @Override
    public void init (Matrix x, Matrix y) throws Exception {
        super.init(x, y);
        m = x.getColumnDimension();
        mean = new double[m];
        std = new double[m];
        setData(Constants.CROSS_VALIDATOIN_RATIO);
        normalize(this.x);
        LOGGER.log(Level.INFO, "Training dataset size: {0}", trainLen);
        LOGGER.log(Level.INFO, "Crossvalidation dataset size: {0}", crossLen);
    }
    
    @Override
    public Matrix predict(Matrix data) {
        for(int i = 0; i<data.getRowDimension(); i++) {
            for(int j = 1; j<m; j++) {
                data.set(i, j, (data.get(i, j) - mean[j])/std[j]);
            }
        }
        
        return data.times(theta);
    }
    
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
            System.out.println();
            System.out.println("Iterations: " + itr);
            double crossErr = crossValidate();
            System.out.println("CrossValidation Error: " + crossErr);
            return theta;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    
    private double crossValidate() {
        for(int i = 0; i<crossLen; i++) {
            for(int j = 1; j<m; j++) {
                cvX.set(i, j, (cvX.get(i, j) - mean[j])/std[j]);
            }
        }
        return getError(cvX, cvY, theta);
    }
    
    private double updateTheta() {
        Matrix der = derivative();
        
        double err = getError(x, y, theta);
        double nextErr = getError(x, y, theta.minus(der.times((double)alpha)));
        
        while (nextErr > err) {
            alpha = alpha/3;
            nextErr = getError(x, y, theta.minus(der.times((double)alpha)));
        }
        
        System.out.println("Error: " + err);
        theta = theta.minus(der.times((double)alpha));
        return err;
    }
    
    private double getError(Matrix x, Matrix y, Matrix theta) {
        Matrix check = x.times(theta).minus(y);
        Matrix cost  = check.transpose().times(check);
                
        return 0.5 * cost.get(0, 0)/trainLen;
    }
    
    private Matrix derivative() {
        Matrix err = thetaX().minus(y);
        Matrix der = x.transpose().times(err);
        der = der.times((double)1/trainLen);
        return der;
    }
    
    private Matrix thetaX() {
        Matrix res = x.times(theta);
        return res;
    }
    
    private void setData(double crossValidationRatio) {
        try {
            if(crossValidationRatio >= 1) {
                LOGGER.log(Level.WARNING, "Skipping crossvalidataion. Invalid "
                        + "CrossValidation ratio: {0}", crossValidationRatio);
                trainLen = x.getRowDimension();
                crossLen = 0;
                return;
            }
            
            int n = x.getRowDimension();
            
            crossLen = (int) (n * crossValidationRatio);
            trainLen = n-crossLen;
            
            Matrix xCopy = x;
            Matrix yCopy = y;
            
            x = new Matrix(trainLen, m);
            y = new Matrix(trainLen, 1);
            cvX = new Matrix(crossLen, m);
            cvY = new Matrix(crossLen, 1);
            
            x.setMatrix  (0, trainLen-1, 0, m-1, xCopy.getMatrix(0, trainLen-1, 0, m-1));
            y.setMatrix  (0, trainLen-1, 0, 0,   yCopy.getMatrix(0, trainLen-1, 0, 0));
            cvX.setMatrix(0, crossLen-1, 0, m-1, xCopy.getMatrix(trainLen, n-1, 0, m-1));
            cvY.setMatrix(0, crossLen-1, 0, 0,   yCopy.getMatrix(trainLen, n-1, 0, 0));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }
    
    private void normalize(Matrix x) {
        try {
            int n = x.getRowDimension();
            int mLocal = x.getColumnDimension();
            
            for(int j = 0; j<mLocal; j++) {
                mean[j] = 0;
                for(int i = 0; i<n; i++) {
                    mean[j] = mean[j] + x.get(i, j);
                }
                mean[j] = mean[j]/n;
            }
            LOGGER.log(Level.INFO, "Calulated mean");
            
            for(int j = 0; j<mLocal; j++) {
                std[j] = 0;
                for(int i = 0; i<n; i++) {
                    std[j] = std[j] + Math.pow((mean[j] - x.get(i, j)), 2);
                }
                std[j] = Math.sqrt(std[j]/n);
            }
            LOGGER.log(Level.INFO, "Calculated Standard Deviation");
            
            for(int i = 0; i<n; i++) {
                for(int j = 1; j<mLocal; j++) {
                    x.set(i, j, (x.get(i, j)-mean[j])/std[j]);
                }
            }
            LOGGER.info("Data normalized");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }
}
