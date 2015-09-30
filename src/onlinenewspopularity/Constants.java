/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

/**
 * This class defines various constants that are used throughout the project 
 * @author neeth
 */
public class Constants {
    
    /**
     * These values must be set by the user 
     * INTERLIMIT            : Maximum number of iterations for gradient descent
     * SIZE                  : Size of dataset read from file (inclusive of 
     *                         crossvalidation set and test set)
     * 
     * CROSS_VALIDATION_RATIO: Desired ratio of cross validation set vs total data set size
     * NOTE: This ratio determines the number of blocks for k-fold crossvalidation
     * 
     * TEST_SET_RATIO        : Desired ratio of test set vs total data set size
     * 
     * INPUT_FILE            : Path to input file from project directory
     * INITIAL_FEATURE_INDEX : Index of the first predictive feature.
     * NOTE: All features (except the last one) should be a predictive feature
     * The input file should contain the features and the target data value
     */
    
    public static final int    ITERLIMIT              = 1000;
    public static final int    SIZE                   = 39644;
    public static final double CROSS_VALIDATOIN_RATIO = 0.2;
    public static final double TEST_SET_RATIO         = 0.2;
    public static final String INPUT_FILE             = "data\\OnlineNewsPopularity.csv";
    //public static final String INPUT_FILE = "data\\ngData.csv";
    //public static final String INPUT_FILE = "data\\data.csv";
    //public static final String INPUT_FILE = "data\\test1.csv";
    public static final int    INITIAL_FEATURE_INDEX  = 2;
    
    /**
     * Other constants.
     * NOTE: DO NOT CHANGE THESE VALUES
     */
    //public static final String NUMBER_FORMAT     = "### ###.###;-#";
    public static final String NUMBER_FORMAT     = "";
    public static final String FEATURE_COL1_NAME = "THETA_0";
}
