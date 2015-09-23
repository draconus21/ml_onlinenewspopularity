/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.io.FileReader;
import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * This class reads data from a file and creates feature list, prediction column,
 * and test data
 * @author neeth
 */
public class DataFormatter {
    
    private static final Logger LOGGER = Logger.getLogger(DataFormatter.class.getName());
    
    private final String     fileName;
    private       double[][] trainStat;
    
    public DataFormatter(String fileName) {
        this.fileName = fileName;
    }
    
    public List<Matrix> readData() throws Exception {
        try {
            try (Reader br = new FileReader(new File(fileName))) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(br);
                
                List features = new ArrayList<>();
                String predictColName;
                
                Iterator<CSVRecord> itr = records.iterator();
                CSVRecord header = itr.next();
                
                features.add(Constants.FEATURE_COL1_NAME);
                for(int i=2; i<header.size()-1; i++) {
                    features.add(header.get(i).trim());
                }
                predictColName = header.get((header.size()-1)).trim();
                
                trainStat = new double [2][features.size()];
                
                double [][] data         = new double [Constants.SIZE][features.size()];
                double [][] res          = new double [Constants.SIZE][1];
                boolean[]   validFeature = new boolean[features.size()];
                int         featureCount = 1;
                
                for(int i = 0; i<trainStat[0].length; i++) {
                    trainStat   [0][i] = 0;                       //Avg Value of feature i
                    trainStat   [1][i] = 0;                       //Std Deviation of feature i
                    validFeature[i]    = Boolean.FALSE;           //Not a valid feature by default
                }
                
                validFeature[0] = Boolean.TRUE;     //theta_0 is a valid feature
                int i=0;
                for(CSVRecord record : records) {
                    if(i<Constants.SIZE) {
                        for(int j = 0; j<=features.size(); j++) {
                            double value;
                            if(j == 0) {
                                data[i][j] = 1.0;
                                value = data [i][j];
                            } else if(j == features.size()) {
                                res[i][0] = Double.parseDouble(record.get(record.size()-1));
                                value = res[i][0];
                            } else {
                                data[i][j] = Double.parseDouble(record.get(j+1));
                                value = data[i][j];
                                if(value != 0) {
                                    if(validFeature[j] == Boolean.FALSE) {
                                        featureCount++;
                                        validFeature[j] = Boolean.TRUE;
                                    }
                                }
                            }
                            
                            if(j != features.size()) {
                                trainStat[0][j] = (trainStat[0][j] * (i) + value ) / (i+1);
                            }
                        }
                    } else {
                        break;
                    }
                    i++;
                }
                
                //Calculate Standard Deviation for the features (excluding theta_0)
                for(int j = 1; j<features.size(); j++) {
                    double var = 0.0;
                    for(i = 0; i<data.length; i++) {
                        var = var + (data[i][j] - trainStat[0][j]) * (data[i][j]-trainStat[0][j]);
                    }
                    trainStat[1][j] = Math.sqrt(var/Constants.SIZE);
                }
                
                //Perform normalisation 
                LOGGER.log(Level.INFO, "Normalizing training data");
                for(i = 0; i<Constants.SIZE; i++) {
                    for(int j = 1; j<data[i].length; j++) {
                            data[i][j] = (data[i][j]-trainStat[0][j])/trainStat[1][j];
                    }
                }
                
                //Remove empty features
                LOGGER.log(Level.INFO, "Removing empty features columns");
                if(featureCount < features.size()) {
                    List featuresCopy = new ArrayList<>();
                    featuresCopy.addAll(features);
                    double[][] newData = new double[Constants.SIZE][featureCount];
                    int k = 0;
                    
                    for(int j = 0; j<featuresCopy.size(); j++) {
                        if(validFeature[j] == Boolean.TRUE) {
                            for(i = 0; i<Constants.SIZE; i++) {
                                newData[i][k] = data[i][j];
                            } 
                            k++;
                        } else {
                            LOGGER.log(Level.INFO, "Removing empty feature: {0}", features.get(j));
                            features.remove(j);
                        }
                    }
                    
                    data = newData;
                }
                
                Matrix tmpx = new Matrix(data);
                Matrix tmpy = new Matrix(res);
               
                List temp = new ArrayList<>();
                temp.add(features);
                temp.add(predictColName);
                temp.add(tmpx);
                temp.add(tmpy);
                
                return temp;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
            throw e;
        }
    }
    
    public double[][] getDataStat() {
        return trainStat;
    }
}
