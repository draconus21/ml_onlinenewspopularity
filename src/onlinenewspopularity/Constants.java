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
     * 
     * MAX_DESIRED_ACCURACY  : Maximum accuracy that is desired (for gradient descent)
     * 
     * CROSS_VALIDATION_RATIO: Desired ratio of cross validation set vs total data set size
     * NOTE: This ratio determines the number of blocks for k-fold crossvalidation
     * 
     * TEST_SET_RATIO        : Desired ratio of test set vs total data set size
     * 
     * SIZE                  : Size of dataset read from file (inclusive of 
     *                         crossvalidation set and test set)
     * INPUT_FILE            : Path to input file from project directory
     * INITIAL_FEATURE_INDEX : Index of the first predictive feature.
     * NOTE: All features (except the last one) should be a predictive feature
     * The input file should contain the features and the target data value
     */
    
    public static final int    ITERLIMIT              = 100;
    public static final double MAX_DESIRED_ACCURACY   = 0.0000001;
    public static final double CROSS_VALIDATOIN_RATIO = 0.2;
    public static final double TEST_SET_RATIO         = 0.2;
    
    /**
     * Uncomment the set below to use the Entire Mashable dataset
     */
    /*
    public static final String INPUT_FILE             = "data\\OnlineNewsPopularity.csv";
    public static final int    SIZE                   = 39644;
    public static final int    INITIAL_FEATURE_INDEX  = 2;
    */
    /**
     * Uncomment the set below to use just the training data from Mashable data set
     */
    /*
    public static final int    SIZE                   = 31716;
    public static final String INPUT_FILE             = "data\\analysisData.csv";
    public static final int    INITIAL_FEATURE_INDEX  = 1;
    */
    
    /**
     * Uncomment the set below to use the complementary data set
     * This data set was obtained from Tech Crunch
     */
    
    public static final int    SIZE                   = 9456;
    public static final String INPUT_FILE             = "data\\data.csv";
    public static final int    INITIAL_FEATURE_INDEX  = 1;
    
    /**
     * Other constants.
     * NOTE: DO NOT CHANGE THESE VALUES
     */
    public static final String NUMBER_FORMAT     = "### ###.###;-#";
    public static final String FEATURE_COL1_NAME = "THETA_0";
}
