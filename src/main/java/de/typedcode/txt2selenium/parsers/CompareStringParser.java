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

package de.typedcode.txt2selenium.parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import de.typedcode.txt2selenium.exceptions.ParseException;

public class CompareStringParser {

    /**
     * Parses the given File as Key-Value pairs.
     * 
     * Each line can provide a key-value-pair. The line starts with the key. The
     * delimiter to determine the end of the key is the space. Everything after the
     * space is the value-part of the pair.
     * 
     * @param stringFile
     *            File to parse as Key-Value pairs
     * @return key-value-Map of strings.
     * @throws ParseException
     *             Throws the Exception if the key is duplicated in the given File
     *             or if an IOException is thrown.
     */
    public static Map< String, String > parse( Path stringFile ) throws ParseException {
        Map< String, String > result = new HashMap<>();

        try( Stream< ? > stream = Files.lines( stringFile ) ) {
            String line;
            String[] split;

            String identifier;
            String value;
            int lineNumber = 0;

            Iterator< ? > lineIterator = stream.iterator();
            while( lineIterator.hasNext() ) {
                line = ( String ) lineIterator.next();

                lineNumber++;

                if( line == null ) {
                    continue;
                }

                // Skip lines that start with a space
                if( line.startsWith( " " ) ) {
                    continue;
                }

                // Skip Empty lines
                if( line.isEmpty() ) {
                    continue;
                }

                // Split the Line in two parts. Everything before the first space is the part
                // for the identifier
                split = line.split( " ", 2 );
                identifier = split[ 0 ];
                value = split[ 1 ];

                if( identifier != null ) {
                    if( value == null ) {
                        value = "";
                    }

                    // Check that the identifier is not already taken.
                    if( result.containsKey( identifier ) ) {
                        throw new ParseException( "Comparestrings contain a doubled key entry at line " + lineNumber );
                    }

                    result.put( identifier, value );
                }
            }
        } catch( IOException e ) {
            throw new ParseException( "Error reading File.", e );
        }

        return result;
    }
}
