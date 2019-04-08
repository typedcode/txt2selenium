/*
 * MIT License
 * 
 * Copyright (c) 2018 Markus Fischer (www.typedcode.de)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.typedcode.txt2Selenium.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnitLogger {

    private static Logger INSTANCE;

    static {
        // Logging will only be done to the stdout
        INSTANCE = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );
    }

    /**
     * No Instantiation allowed.
     */
    private UnitLogger() {

    }

    /**
     * Sets the Log level
     *
     * @param level Level to log.
     */
    public static void setLogLevel( Level level ) {
        INSTANCE.setLevel( level );
    }

    /**
     * Logs a message with the given level to the logger.
     * 
     * @param logLevel
     *            Level to log the Message with
     * @param logMessage
     *            Message to Log
     * @param throwable
     *            The throwable to log. Can be null if no throwable is to log.
     */
    private static void log( Level logLevel, String logMessage, Throwable throwable ) {
        Logger localInstance = INSTANCE;

        if( localInstance == null ) {
            throw new RuntimeException( "Logging not Possible. Instance is null." );
        }

        if( throwable == null ) {
            localInstance.log( logLevel, logMessage );
        } else {
            localInstance.log( logLevel, logMessage, throwable );
        }
    }

    /**
     * Logs a Severe message.
     * 
     * @param logMessage
     *            Message to log.
     */
    public static void logSevere( String logMessage ) {
        log( Level.SEVERE, logMessage, null );
    }

    /**
     * Logs a Severe message and the given Throwable
     * 
     * @param logMessage
     *            Message to log.
     * @param exception
     *            Throwable to log.
     */
    public static void logSevere( String logMessage, Throwable exception ) {
        log( Level.SEVERE, logMessage, exception );
    }

    /**
     * Logs an Info message.
     * 
     * @param logMessage
     *            Message to log.
     */
    public static void logInfo( String logMessage ) {
        log( Level.INFO, logMessage, null );
    }

    /**
     * Logs a Fine message.
     *
     * @param logMessage
     *            Message to log.
     */
    public static void logFine( String logMessage ) {
        log( Level.FINE, logMessage, null );
    }

    /**
     * Adding a handler to the Logger.
     *
     * @param handler Handler to add.
     */
    public static void addHandler( Handler handler ) {
        INSTANCE.addHandler( handler );
    }

    /**
     * Removes a handler from the logger.
     *
     * @param handler Handler to remove.
     */
    public static void removeHandler( Handler handler ) {
        INSTANCE.removeHandler( handler );
    }
}
