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
import java.text.DecimalFormat;
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
    
    private final String fileName;
    
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
                
                double[][] data = new double [Constants.SIZE][features.size()];
                double[][] res  = new double [Constants.SIZE][1];
                double[][] trainMinMax = new double[3][features.size()];
                boolean[] normalize = new boolean[features.size()];
                boolean[] validFeature = new boolean[features.size()];
                int featureCount = 0;
                
                for(int i = 0; i<trainMinMax[0].length; i++) {
                    trainMinMax[0][i] = Double.MAX_VALUE;   //Min Value of feature i
                    trainMinMax[1][i] = Double.MIN_VALUE;   //Max Value of feature i
                    trainMinMax[2][i] = 0;                  //Avg Value of feature i
                    normalize[i]      = Boolean.FALSE;      //No nomramlisation required by default
                    validFeature[i]   = Boolean.FALSE;      //Not a valid feature by default
                }
                
                int i=0;
                for(CSVRecord record : records) {
                    if(i<Constants.SIZE) {
                        for(int j = 0; j<=features.size(); j++) {
                            double value;
                            if(j == 0) {
                                data[i][j] = 1.0;
                                value = data [i][j];
                                if(validFeature[j] == Boolean.FALSE) {
                                    featureCount++;
                                }
                                validFeature[j] = Boolean.TRUE;
                            } else if(j == features.size()) {
                                res[i][0] = Double.parseDouble(record.get(record.size()-1));
                                value = res[i][0];
                            } else {
                                data[i][j] = Double.parseDouble(record.get(j+1));
                                value = data[i][j];
                                if(value != 0) {
                                    if(validFeature[j] == Boolean.FALSE) {
                                        featureCount++;
                                    }
                                    validFeature[j] = Boolean.TRUE;
                                    
                                }
                            }
                            
                            if(j != features.size()) {          //skip normalisation 
                                                                //check for predict column
                                    if(value < trainMinMax[0][j]) {
                                        trainMinMax[0][j] = value;
                                    }
                                    if(value > trainMinMax[1][j]) {
                                        trainMinMax[1][j] = value;
                                    }
                                    
                                    trainMinMax[2][j] = (trainMinMax[2][j] * (i) + value ) / (i+1);
                                    
                                }
                        }
                    } else {
                        break;
                    }
                    i++;
                }
                
                //Normalization check
                for(i = 0; i<features.size(); i++) {
                    if(trainMinMax[0][i] < (-1*Constants.SPREAD)|| //Perform Normalization if 
                       trainMinMax[1][i] > Constants.SPREAD) {      //the data is spread out
                        normalize[i] = Boolean.TRUE;
                    }
                }
                
                //Perform normalisation 
                for(i = 0; i<Constants.SIZE; i++) {
                    for(int j = 0; j<data[i].length; j++) {
                        if(normalize[j] == Boolean.TRUE) {
                            double range = Math.abs(trainMinMax[1][j] - trainMinMax[0][j]);
                            data[i][j] = (data[i][j]-trainMinMax[2][j])/range;
                        }
                    }
                }
                
                //Remove empty features
                if(featureCount < features.size()) {
                    List featuresCopy = new ArrayList<String>();
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
}
