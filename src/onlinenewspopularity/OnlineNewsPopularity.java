/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
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
            Matrix data = (Matrix)testSet.get(2);
            Matrix y    = (Matrix)testSet.get(3);
        
            //print data that has been read
            System.out.println("predict: " + predictColName);
            System.out.print("features: ");
            for(int i = 0; i<features.size(); i++) {
                System.out.print(features.get(i) + " | ");
            }
            System.out.println("Data set:");
            data.print(new DecimalFormat(Constants.NUMBER_FORMAT), 5);
            System.out.println("y:");
            y.print(new DecimalFormat("### ###.###"), 5);
            
            //Perform Linear Regression
            LinearRegression lr = new ClosedFormSolution();
            lr.init(data, y);
            Matrix res = lr.doLinearRegression();
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
        }
    }
    
}
