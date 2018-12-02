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

package de.typedcode.txt2Selenium.exceptions;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.typedcode.txt2Selenium.util.UnitLogger;

@NonNullByDefault
public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 6997876310350164158L;

    /**
     * Exception will be thrown when parsing a File was erroneous.
     * 
     * Will automatically log the given message to the UnitLogger.
     * 
     * @param message
     *            Message to define what went wrong.
     */
    public ParseException( String message ) {
        super( message );

        UnitLogger.logSevere( message, this );
    }

    /**
     * Exception will be thrown when parsing a File was erroneous.
     * 
     * Will automatically log the given message to the UnitLogger.
     * 
     * @param message
     *            Message to define what went wrong.
     * @param e
     *            Exception that was thrown.
     */
    public ParseException( String message, Exception e ) {
        super( message, e );

        UnitLogger.logSevere( message, this );
    }
}
