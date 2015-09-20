/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinenewspopularity.logging.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neeth
 */
public class MlLogger {
    private static final Logger logger = Logger.getLogger("onlinenewspopularity");

    private Level getLevel(String level) {
        if("SEVERE".equalsIgnoreCase(level)) {
            return Level.SEVERE;
        } else if ("WARNING".equalsIgnoreCase(level)) {
            return Level.WARNING;
        } else if ("INFO".equalsIgnoreCase(level)) {
            return Level.INFO;
        } else if ("FINE".equalsIgnoreCase(level)) {
            return Level.FINE;
        } else if ("FINER".equalsIgnoreCase(level)) {
            return Level.FINER;
        } else if ("FINEST".equalsIgnoreCase(level)) {
            return Level.FINEST;
        } else if ("CONFIG".equalsIgnoreCase(level)) {
            return Level.CONFIG;
        } else if ("ALL".equalsIgnoreCase(level)) {
            return Level.ALL;
        } else if ("OFF".equalsIgnoreCase(level)) {
            return Level.OFF;
        } else {
            logger.log(Level.INFO, "Invalid level: {0} Returning Level.INFO", level);
            return Level.INFO;
        }
    }
    
    public void log(String msg) {
        log(msg, Level.INFO);
    }
    
    public void log(String msg, String level) {
        log(msg, getLevel(level));
    }
    public void log(String msg, Level level) {
        logger.log(level, msg);
    }
}
