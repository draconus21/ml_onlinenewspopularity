/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import onlinenewspopularity.logging.logger.MlLogger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author neeth
 */
public class DataFormatter {
    
    private static final MlLogger logger = new MlLogger();
    
    private String fileName;
    
    public DataFormatter(String fileName) {
        this.fileName = fileName;
    }
    
    public void readData(Matrix x, Matrix y) {
        try {
            try (Reader br = new FileReader(new File(fileName))) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(br);
                
                List features = new ArrayList<String>();
                Iterator<CSVRecord> itr = records.iterator();
                CSVRecord header = itr.next();
                for(int i=2; i<header.size(); i++) {
                    //System.out.print(i + ": " + header.get(i).trim() + " | ");
                    
                    features.add(header.get(i).trim());
                }
                
                double[][] data = new double [39644][header.size()-2];
                double[][] res  = new double [39644][1];
                
                int i=0;
                for(CSVRecord record : records) {
                    for(int j=0; j<features.size(); j++) {
                        //logger.log((String)features.get(i));
                        try {
                            if(j == features.size() - 1) {
                                res[i][0] = Double.parseDouble(record.get(j));
                            }
                            data[i][j] = Double.parseDouble(record.get(j));
                        } catch (Exception e) {
                            logger.log("fail: " +(String) features.get(j) + ": " + record.get(j));
                            data[i][j] = 0;
                        }
                        //System.out.print(data[i][j] + " | ");
                    }
                    //System.out.println();
                    i++;
                }
                
                Matrix tmpx = new Matrix(data);
                Matrix tmpy = new Matrix(res);
                for(i=2; i<features.size(); i++) {
                    System.out.print((String)features.get(i) + "|");
                }
                tmpx.print(new DecimalFormat("### ###.###"), 5);
                //tmpy.print(10, 1);
            }
        } catch (IOException iEx) {
            logger.log("IOException in readData");
        }
    }
}
