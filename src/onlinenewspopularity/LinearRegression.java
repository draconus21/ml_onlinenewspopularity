/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;

/**
 * This abstract class defines the structure for Linear Regression classes
 * @author neeth
 */
public abstract class LinearRegression {
   
    protected Matrix theta;
    protected Matrix x;
    protected Matrix y;
    
    /**
     * Initializes the class.
     * @param x is an nxm matrix that contains training data
     * @param y is an nx1 matrix that contains the target values for training data
     * Note: n is the number of examples and m is the number of features
     * @throws Exception 
     */
    public void init (Matrix x, Matrix y) throws Exception {
        this.x = x;
        this.y = y;
        int features = x.getColumnDimension();
        if(y.getRowDimension() != x.getRowDimension()) {
            throw new Exception ("Invalid traning data. Row count mismatch. X: "
                    + x.getRowDimension() + "x" + x.getColumnDimension() +
                    " | Y: "  + y.getRowDimension() + "x" + y.getColumnDimension());
        }
        theta = new Matrix(features,1, 0.0);
    }
    
    /**
     * Performs linear regression.
     * It must be defined in all classes that extend LinearRegression
     * @return weights of linear regression model
     */
    public abstract Matrix doLinearRegression(); 
    
    /**
     * Performs prediction using weights determined by linear regression.
     * It must be defined in all classes that extend LinearRegression
     * The function takes data as an nxm matrix and returns a nx1 matrix 
     * where n is the number of data and m is the number of features
     * @return target value for data set.
     */
    public abstract Matrix predict(Matrix data);
    
}
