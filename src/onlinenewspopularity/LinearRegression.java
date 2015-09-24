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
    
    public abstract Matrix doLinearRegression(); 
    public abstract Matrix predict(Matrix data);
    
}
