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

    public static final int SIZE = 5;
    
    public static void main(String[] args) {
        DataFormatter df = new DataFormatter("data\\OnlineNewsPopularity.csv");
        Matrix data = new Matrix(SIZE, 58);
        Matrix y    = new Matrix(SIZE, 1);
        
        df.readData(data, y);
        
        data.print(2, 61);
    }
    
}
