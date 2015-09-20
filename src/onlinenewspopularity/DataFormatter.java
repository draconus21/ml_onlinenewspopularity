/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import com.sun.istack.internal.logging.Logger;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * This class reads data from a file and creates feature list, prediction column,
 * and test data
 * @author neeth
 */
public class DataFormatter {
    
    private static final Logger LOGGER = Logger.getLogger(DataFormatter.class);
    
    private final String fileName;
    
    public DataFormatter(String fileName) {
        this.fileName = fileName;
    }
    
    public List<Matrix> readData() {
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
                
                int i=0;
                for(CSVRecord record : records) {
                    if(i<Constants.SIZE) {
                        for(int j=1; j<features.size(); j++) {
                            try {
                                if(j == features.size() - 1) {
                                    res[i][0] = Double.parseDouble(record.get(j));
                                } else if(j == 1) {
                                    data[i][j-1] = 1.0;
                                } else {
                                    data[i][j-1] = Double.parseDouble(record.get(j));
                                }
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "fail: " +(String) features.get(j) + ": " + record.get(j) + "\n" + e.getMessage());
                                data[i][j] = 0;
                            }
                        }
                        i++;
                    } else {
                        break;
                    }
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
        } catch (IOException iEx) {
            LOGGER.log(Level.WARNING, "IOException in readData: " + iEx.getMessage());
            return null;
        }
    }
}
