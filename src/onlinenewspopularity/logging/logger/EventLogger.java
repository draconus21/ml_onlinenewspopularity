/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity.logging.logger;

import org.apache.logging.log4j.Logger;

/**
 *
 * @author neeth
 */
public class EventLogger {
    private StringBuilder logger;
    
    public EventLogger() {
        logger = new StringBuilder();
    }
    
    public void log(String msg) {
        logger.append("\n").append(msg);
        System.out.println(msg);
    }
    
    public void flushLog() {
        System.out.println(logger);
    }
    
   
}
