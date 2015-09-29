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
import java.io.PrintWriter;
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
            Matrix data  = (Matrix)testSet.get(2);
            Matrix y     = (Matrix)testSet.get(3);
            Matrix testx = (Matrix)testSet.get(4);
            Matrix testy = (Matrix)testSet.get(5);
            System.out.println("data: " + data.getRowDimension() + " | " + y.getRowDimension());
            
            //print data that has been read
            System.out.println("predict: " + predictColName);
            System.out.print("features: ");
            for(int i = 0; i<features.size(); i++) {
                System.out.print(features.get(i) + " | ");
            }
            System.out.println();
            System.out.println("Data set:");
            //data.print(new DecimalFormat(Constants.NUMBER_FORMAT), 8);
            //testx.print(new DecimalFormat(Constants.NUMBER_FORMAT), 8);
            System.out.println("y:");
            //y.print(new DecimalFormat(Constants.NUMBER_FORMAT), 8);
            //testy.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
            
            //Perform Linear Regression
            //LinearRegression lr = new ClosedFormSolution();
            LinearRegression lr = new GradientDescent();
            lr.init(data, y);
            
            Matrix res = lr.doLinearRegression();
            
            System.out.println();
            System.out.println("FINAL THETA:");
            res.print(new DecimalFormat(), 5);
            
            System.out.println("\n prediction:");
            //data.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
            if(testx.getRowDimension() > 0) {
                Matrix prediction = lr.predict(testx);
                //prediction.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
                System.out.println("Error in prediction: " + (prediction.minus(testy).transpose().times(prediction.minus(testy))).get(0, 0)/prediction.getRowDimension());
            } else {
                LOGGER.log(Level.INFO, "No test set available");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
            e.printStackTrace();
        }
    }
    
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
            //System.out.println(value);
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
}
