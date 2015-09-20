/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity;

import Jama.Matrix;

/**
 *
 * @author neeth
 */
public class OnlineNewsPopularity {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataFormatter df = new DataFormatter("data\\OnlineNewsPopularity.csv");
        Matrix data = new Matrix(39644, 58);
        Matrix y    = new Matrix(39644, 1);
        
        df.readData(data, y);
        
        data.print(2, 61);
    }
    
}
