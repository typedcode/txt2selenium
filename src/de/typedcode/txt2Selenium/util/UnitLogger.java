/**
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

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class UnitLogger {

    private static Logger INSTANCE;

    static {
        INSTANCE = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

        INSTANCE.setLevel( Level.INFO );
        try {
            FileHandler fileTxt = new FileHandler( "Logging.txt" );
            SimpleFormatter formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter( formatterTxt );
            INSTANCE.addHandler( fileTxt );
        } catch( Exception e ) {
            throw new RuntimeException( "Can't create log.", e );
        }
    }

    private UnitLogger() {

    }

    private static void log( Level logLevel, String logMessage, @Nullable Throwable throwable ) {
        if( throwable == null ) {
            INSTANCE.log( logLevel, logMessage );
        } else {
            INSTANCE.log( logLevel, logMessage, throwable );
        }
    }

    public static void logSevere( String logMessage ) {
        log( Level.SEVERE, logMessage, null );
    }

    public static void logSevere( String logMessage, Throwable exception ) {
        log( Level.SEVERE, logMessage, exception );
    }

    public static void logInfo( String logMessage ) {
        log( Level.INFO, logMessage, null );
    }

}
