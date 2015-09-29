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
import java.util.Random;
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
                
                for(int i = 0; i<validFeature.length; i++) {
                    validFeature[i]    = Boolean.FALSE;           //Not a valid feature by default
                }
                
                List indices = new ArrayList<>();
                int n = Constants.SIZE;
                for(int i=0; i<n; i++) {
                    indices.add(i);
                }
                Random randGen = new Random();
                
                validFeature[0] = Boolean.TRUE;     //theta_0 is a valid feature
                int i=0;
                for(CSVRecord record : records) {
                    if(i<Constants.SIZE && !indices.isEmpty()) {
                        int index = (int)indices.get(randGen.nextInt(indices.size()));
                        for(int j = 0; j<=features.size(); j++) {
                            if(j == 0) {
                                data[index][j] = 1.0;
                            } else if(j == features.size()) {
                                res[index][0] = Double.parseDouble(record.get(record.size()-1));
                            } else {
                                data[index][j] = Double.parseDouble(record.get(j+1));
                                if(data[index][j] != 0) {
                                    if(validFeature[j] == Boolean.FALSE) {
                                        featureCount++;
                                        validFeature[j] = Boolean.TRUE;
                                    }
                                }
                            }
                        }
                        indices.remove((Object)index);
                    } else {
                        break;
                    }
                    i++;
                }
                
                //Remove empty features
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
                
                int testLen = (int)(Constants.TEST_SET_RATIO * Constants.SIZE);
                int trainLen = Constants.SIZE - testLen;
                
                Matrix tmpx = new Matrix(data);
                Matrix tmpy = new Matrix(res);
                
                List temp = new ArrayList<>();
                temp.add(features);
                temp.add(predictColName);
                temp.add(tmpx.getMatrix(0,        trainLen-1,               0, tmpx.getColumnDimension()-1));
                temp.add(tmpy.getMatrix(0,        trainLen-1,               0, tmpy.getColumnDimension()-1));
                temp.add(tmpx.getMatrix(trainLen, tmpx.getRowDimension()-1, 0, tmpx.getColumnDimension()-1));
                temp.add(tmpy.getMatrix(trainLen, tmpy.getRowDimension()-1, 0, tmpy.getColumnDimension()-1));
                
                return temp;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{e.getClass().getName(), e.getMessage()});
            throw e;
        }
    }
    
    /*private void shuffle(Matrix x, Matrix y) {
        try {
            List indices = new ArrayList<>();
            int n = x.getRowDimension();
            for(int i=0; i<n; i++) {
                indices.add(i);
            }
            Matrix randX = new Matrix(x.getRowDimension(), x.getColumnDimension());
            Matrix randY = new Matrix(y.getRowDimension(), y.getColumnDimension());
            
            Random randGen = new Random();
            int i=0;
            while(!indices.isEmpty() && i<randX.getRowDimension()) {
                int index = (int)indices.get(randGen.nextInt(indices.size()));
                randX.setMatrix(i, i, 0, this.m-1, this.x.getMatrix(index, index, 0, this.m-1));
                randY.setMatrix(i, i, 0, 0,        this.y.getMatrix(index, index, 0, y.getColumnDimension()-1));
                indices.remove((Object)index);
                i++;
            }
            LOGGER.log(Level.INFO, "Randomized data set");
        }  catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }*/
    
    public double[][] getDataStat() {
        return trainStat;
    }
}
