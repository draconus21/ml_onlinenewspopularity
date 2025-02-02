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
 * This class extends LinearRegression to perform Gradient Descent
 * This function dynamically adjusts alpha to prevent the cost from diverging
 * @author neeth
 */
public class GradientDescent extends LinearRegression {
    private static final Logger LOGGER = Logger.getLogger(GradientDescent.class.getName());
    
    private int trainLen;
    private int crossLen;
    private int m;
    private int n;
    private int[][] crossMat;
    
    private double alpha = 100;
    private double lambda = 100;
    private double[] mean;
    private double[] std;
    private Matrix cvX;
    private Matrix cvY;
    private Matrix trainX;
    private Matrix trainY;
    
    /**
     * Does few preprocessing steps for gradient descent
     * 1. Decides blocks for cross validation
     * 2. Normalizes data (optional)
     * @param x is the data available for training. It is an nxm matrix.
     * NOTE: This does not include test data
     * @param y is the target values for training data. It is an nx1 matrix.
     * Here n is the number of training examples and m is the number of features
     * @throws Exception 
     */
    @Override
    public void init (Matrix x, Matrix y) throws Exception {
        super.init(x, y);
        m = x.getColumnDimension();
        n = x.getRowDimension();
        mean = new double[m];
        std  = new double[m];
        for(int i = 0; i<m; i++) {
            std[i]  = 1;
            mean[i] = 0;
        }
        setCrossValidationPartitions(Constants.CROSS_VALIDATOIN_RATIO);
        setData(0);
        normalize(this.x);
        LOGGER.log(Level.INFO, "Training dataset size: {0}", trainLen);
        LOGGER.log(Level.INFO, "Crossvalidation dataset size: {0}", crossLen);
    }
    
    /**
     * Predicts the target values for give data
     * @param data is an nxm matrix with data for prediction
     * @return an nx1 matrix with target values
     * NOTE: n is the number of data available for predictions and m is the
     * number of features.
     */
    @Override
    public Matrix predict(Matrix data) {
        for(int i = 0; i<data.getRowDimension(); i++) {
            for(int j = 1; j<m; j++) {
                data.set(i, j, (data.get(i, j) - mean[j])/std[j]);
            }
        }
        
        return data.times(theta);
    }
    
    /**
     * Performs Gradient Descent
     * First it tests the model by performing k-fold cross validation
     * Then, it learns using the entire training data available
     * @return mx1 matrix of trained weights
     * NOTE: m is the number of features in training data
     */
    @Override
    public Matrix doLinearRegression() {
        try {
            LOGGER.log(Level.INFO, "===START MODEL TESTING (K-FOLD CROSS VALIDATAION)===");
            if(this.crossLen > 0) {
                double crossErr = doKFoldCrossValidation();
                LOGGER.log(Level.INFO, "Cross validation error: {0}", crossErr);
            } else {
                LOGGER.log(Level.WARNING, "No cross validataion set available.");
            }
            LOGGER.log(Level.INFO, "Model testing completed.");
            
            theta = new Matrix(this.m, 1, 0.0);
            LOGGER.log(Level.INFO, "Theta reset");
            
            LOGGER.log(Level.INFO, "==============START LINEAR REGRESSION==============");
            
            int itr = 0;
            
            //while(true) {
            for(int i = 0; i<Constants.ITERLIMIT; i++) {
                if(updateTheta(this.x, this.y) < 0.0000001) {
                    break;
                }
                itr++;
            }
            LOGGER.log(Level.INFO, "Completed Linear Regression in {0} iterations", itr);
            
            LOGGER.log(Level.INFO, "Error: {0}", updateTheta(this.x, this.y));
            return theta;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    
    /**
     * Performs k-fold cross validation
     * @return average cross validation error
     */
    public double doKFoldCrossValidation() {
        try {
            double crossErr = 0;
            for(int i = 0; i<this.crossMat.length; i++) {
                setData(i);
                theta = new Matrix(this.m, 1, 0.0);
                int itr = 0;
                for(int j = 0; j<Constants.ITERLIMIT; j++) {
                    if(updateTheta(this.trainX, this.trainY) < 0.0000001) {
                    break;
                    }
                    itr++;
                }
                LOGGER.log(Level.INFO, "Cross validataion round: {0} | "
                        + "Iterations: {1}", new Object[]{i, itr});
                if(this.crossLen > 0) {
                    crossErr = crossErr + getError(cvX, cvY, theta);
                } else {
                    LOGGER.log(Level.WARNING, "No cross validataion set available.");
                }
            }
            return crossErr/this.crossMat.length;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "{0}: {1}", new Object[]{e.getClass().getSimpleName(), e.getMessage()});
            return -1;
        }
    }
    
    /**
     * Updates theta
     * UpdateTheta() adjusts alpha to prevent the error from diverging by using a
     * simple look-ahead logic
     * @param localX 
     * @param localY
     * @return Cost with old theta
     */
    private double updateTheta(Matrix localX, Matrix localY) {
        Matrix der = derivative(localX, localY);
        
        double err = getError(localX, localY, this.theta);
        double nextErr = getError(localX, localY, this.theta.minus(der.times((double)alpha)));
        
        while (nextErr > err) {
            alpha = alpha/3;
            nextErr = getError(localX, localY, this.theta.minus(der.times((double)alpha)));
        }
        
        this.theta = this.theta.minus(der.times((double)alpha));
        return err;
    }
    
    /**
     * @param localx
     * @param localy
     * @param localtheta
     * @return Cost
     */
    private double getError(Matrix localx, Matrix localy, Matrix localtheta) {
        Matrix check = localx.times(localtheta).minus(localy);
        Matrix cost  = check.transpose().times(check);
        Matrix reg   = localtheta.transpose().times(localtheta);
        
        return 0.5 * (cost.get(0, 0) + lambda * reg.get(0, 0))/localx.getRowDimension();
    }
    
    /**
     * @param localX
     * @param localY
     * @return mx1 matrix of derivatives for m features
     */
    private Matrix derivative(Matrix localX, Matrix localY) {
        Matrix err = thetaX(localX).minus(localY);
        Matrix der = localX.transpose().times(err);
        der = der.plus(theta.times(lambda));
        der = der.times((double)1/trainLen);
        
        return der;
    }
    
    /**
     * Computes prediction values for localX using current theta.
     * @param localX
     * @return nx1 matrix 
     * NOTE: This should only be used to predict values for data that 
     * was used for normalizing
     */
    private Matrix thetaX(Matrix localX) {
        Matrix res = localX.times(theta);
        return res;
    }
    
    /**
     * Sets the blocks for k-fold cross validation
     * @param crossValidationRatio
     * @throws Exception 
     */
    private void setCrossValidationPartitions(double crossValidationRatio) throws Exception{
        try {
            if(crossValidationRatio >= 1) {
                LOGGER.log(Level.WARNING, "Invalid "
                        + "CrossValidation ratio: {0}", crossValidationRatio);
                this.crossMat = new int[][]{{-1, -1}};
                return;
            }
            
            int blocks = (int)(1/crossValidationRatio);
            int blockSize = (int)(this.n * crossValidationRatio);
            LOGGER.log(Level.INFO, "blocks: {0} | block size: {1}",
                    new Object[]{blocks, blockSize});
            
            if(blockSize == 0) {
                LOGGER.log(Level.INFO, "Not enough data for required crossvalidation "
                        + "ratio. size: {0} | crossValidationRatio: {1}", 
                        new Object[]{this.n, crossValidationRatio});
                this.crossMat = new int[][]{{-1, -1}};
                return;
            }
            
            int surplus = 0;
            if(this.n % blocks != 0) {
                surplus = this.n % blocks;
            }
            this.crossMat = new int[blocks][2];
            
            int index = 0;
            for(int i = 0; i<this.n; ) {
                this.crossMat[index][0] = i;
                this.crossMat[index][1] = i+blockSize-1;
                i = i + blockSize;
                if(surplus > 0) {
                    this.crossMat[index][1]++ ;
                    surplus--;
                    i++;
                }
                LOGGER.log(Level.INFO, "Block {0}: {1} - {2}",
                        new Object[]{index, crossMat[index][0], crossMat[index][1]});
                index++;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Sets training data and cross validation data using crossMat
     * @param crossIndex cross validation block number
     */
    private void setData(int crossIndex) {
        try {
            if(this.crossMat[crossIndex][0] != -1 && this.crossMat[crossIndex][1] != -1) {
                int lower = this.crossMat[crossIndex][0];
                int upper = this.crossMat[crossIndex][1];
                
                this.crossLen = this.crossMat[crossIndex][1]-this.crossMat[crossIndex][0] + 1;
                this.trainLen = this.n - this.crossLen;
                
                this.trainX = new Matrix(trainLen, this.m);
                this.trainY = new Matrix(trainLen, 1);
                this.cvX    = new Matrix(crossLen, this.m);
                this.cvY    = new Matrix(crossLen, 1);
                
                this.cvX.setMatrix(0, crossLen-1, 0, m-1, this.x.getMatrix(this.crossMat[crossIndex][0], this.crossMat[crossIndex][1], 0, m-1));
                this.cvY.setMatrix(0, crossLen-1, 0, 0,   this.y.getMatrix(this.crossMat[crossIndex][0], this.crossMat[crossIndex][1], 0, 0));
                
                int lIndex = 0;
                if(lower-1 >= 0) {
                    this.trainX.setMatrix(0, lower-1, 0, m-1, this.x.getMatrix(0, lower-1, 0, m-1));
                    this.trainY.setMatrix(0, lower-1, 0, 0,   this.y.getMatrix(0, lower-1, 0, 0));
                    lIndex = lower;
                }
                if(upper+1 < this.n) {
                    this.trainX.setMatrix(lIndex, trainLen-1, 0, m-1, this.x.getMatrix(upper+1, this.n-1, 0, m-1));
                    this.trainY.setMatrix(lIndex, trainLen-1, 0, 0,   this.y.getMatrix(upper+1, this.n-1, 0, 0));
                }
            } else {
                this.trainLen = this.n;
                this.crossLen = 0;
                
                this.trainX = this.x;
                this.trainY = this.y;
                
                this.cvX = new Matrix(0, 0);
                this.cvY = new Matrix(0, 0);
                LOGGER.log(Level.INFO, "No Cross validation set created.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Normalizes matrix x
     * @param x nxm matrix 
     * NOTE: n is the number of data and m is the number of features
     */
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
