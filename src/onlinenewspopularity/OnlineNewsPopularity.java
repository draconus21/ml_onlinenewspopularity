/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class. It perform ML by using appropriate classes
 * @author neeth
 */
public class OnlineNewsPopularity {

    private static final Logger LOGGER = Logger.getLogger(OnlineNewsPopularity.class.getName());
    
    public static void main(String[] args) {
        try {
            DataFormatter df = new DataFormatter(Constants.INPUT_FILE);

            List testSet = df.readData();

            List features = (ArrayList)testSet.get(0);
            
            String predictColName = (String)testSet.get(1);
            Matrix data           = (Matrix)testSet.get(2);
            Matrix y              = (Matrix)testSet.get(3);
            Matrix testx          = (Matrix)testSet.get(4);
            Matrix testy          = (Matrix)testSet.get(5);
            
            /**
             * Perform Linear Regression
             */
            //LinearRegression lr = new ClosedFormSolution();
            LinearRegression lr = new GradientDescent();
            lr.init(data, y);
            
            Matrix res = lr.doLinearRegression();

            printTheta(res, features);

            if(testx.getRowDimension() > 0) {
                System.out.println("Test set:");
                Matrix prediction = lr.predict(testx);
                prediction.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
                System.out.println("Error in prediction: " + getError(testx, testy, res));
            } else {
                LOGGER.log(Level.INFO, "No test set available");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
            e.printStackTrace();
        }
    }
    
    /**
     * Calculates the error while theta is used to predict.
     * @param localx is an nxm matrix for which the prediction is done
     * @param localy is an nx1 matrix with the target values
     * @param localtheta is an mx1 matrix with weights for features
     * @return  error in prediction
     * NOTE: m is the number of features and n is the number of examples
     */
    public static double getError(Matrix localx, Matrix localy, Matrix localtheta) {
        Matrix check = localx.times(localtheta).minus(localy);
        Matrix cost  = check.transpose().times(check);
        
        return 0.5 * (cost.get(0, 0))/localx.getRowDimension();
    }
    
    /**
     * Prints the weights for corresponding features
     * @param theta mx1 matrix of weights
     * @param features list of features
     * NOTE: m is the number of valid features
     * @throws Exception 
     */
    private static void printTheta(Matrix theta, List features) throws Exception {
        System.out.println("Weights: ");
        try {
            for(int i = 0; i<theta.getRowDimension(); i++) {
                System.out.println(features.get(i) + ": " + theta.get(i, 0));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
            throw e;
        }
    }
    /**
     * This is used to determine which features to use for part 2 of the assignment
     * @param res mx1 matrix of weights for m features
     * @param features list of m Strings of labels for m features
     * @throws IOException 
     */
    private void featureSelector(Matrix res, List features) throws IOException {
        FileWriter fw = new FileWriter(new File("featureList.csv"));
        
        fw.write("Feature Number,FeatureName,Weight\n");
        System.out.println("validFeatures:");
        int nF = 0;
        List<List> len = new ArrayList<>();
        for(int i = 0; i<11; i++) {
            len.add(new ArrayList<>());
        }

        for(int i=0; i<res.getRowDimension(); i++) {
            int value = (int)Math.abs(res.get(i, 0))/100;
            if(value>9) {
                len.get(10).add(i);
            } else {
                len.get(value).add(i);
            }
        }
        System.out.println("features: " );
        for(int i = len.size()-1; i>=0; i--) {
            if(!len.get(i).isEmpty()) {
                if(i == len.size()-1) {
                    fw.write("weight more than: " + (i)*100 + "\n");
                } else {
                    fw.write("weight less than: " + (i+1)*100 + "\n");
                }
                List temp = len.get(i);
                for(int j = 0; j<temp.size(); j++) {
                    fw.write((int)temp.get(j) + ", " + features.get((int)temp.get(j)) + ","  + res.get((int)temp.get(j), 0));
                    fw.write("\n");
                }
                fw.write("\n");
                fw.flush();
            }
        }
    }
    
    /**
     * Removes a feature at a time and performs k-fold cross validation.
     * It is used to determine the relationship between the features and
     * target.
     * @param fileName name of file with only training data
     * @throws Exception 
     * NOTE: Test ratio (in Constants.java) should be set to 0 in order to
     * perform cross validation on the entire file.
     */
    public static void featureAnalyzer(String fileName) throws Exception {
        DataFormatter df = new DataFormatter(fileName);
        FileWriter fw = new FileWriter(new File("analysis data\\featureAnalysis3.csv"));
        List temp = df.readData();
        Matrix data;
        Matrix y;
        Matrix testx;
        Matrix testy;
        
        List allFeatures = (List)temp.get(0);
        String predictColName = (String)temp.get(1);
        GradientDescent lr2 = new GradientDescent();
        lr2.init((Matrix)temp.get(2), (Matrix)temp.get(3));
        
        double normalError = lr2.doKFoldCrossValidation();//getError((Matrix)temp.get(4), (Matrix)temp.get(5), theta2);
        System.out.println("normalError: " + normalError);
        for(int i = 1; i<allFeatures.size(); i++) {
            GradientDescent lr = new GradientDescent();
            List featureIndices = new ArrayList<>();
            List features;
            for(int j = 0; j<allFeatures.size(); j++) {
                if(j != i) {
                    featureIndices.add(j);
                }
            }
            
            List temp2 = df.resetData((Matrix)temp.get(2), (Matrix)temp.get(3), featureIndices, allFeatures, predictColName);
            features = (List)temp2.get(0);
            data = (Matrix)temp2.get(2);
            y = (Matrix)temp2.get(3);
            
            List temp3 = df.resetData((Matrix)temp.get(4), (Matrix)temp.get(5), featureIndices, allFeatures, predictColName);
            testx = (Matrix)temp3.get(2);
            testy = (Matrix)temp3.get(3);
            
            lr.init(data, y);
            
            double predErr = lr.doKFoldCrossValidation();
            System.out.println("Error: " + predErr);
            StringBuilder sb = new StringBuilder();
            sb.append(i).append(",");
            sb.append(predErr).append(",");
            sb.append(normalError).append("\n");
            fw.write(sb.toString());
            fw.flush();
        }
    }
}
