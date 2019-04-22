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

package de.typedcode.txt2Selenium.parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.exceptions.ParseException;

public class TestFileParser {

    /**
     * Do not instantiate
     */
    private TestFileParser() {

    }

    /**
     * Will parse a Testfile.
     * 
     * @param instance
     *            Current instance of Txt2Selenium
     * @param testFile
     *            File to parse
     * @return The first action to execute after parsing the file
     * @throws ParseException
     */
    public static AAction parse( Txt2Selenium instance, Path testFile ) throws ParseException {
        AAction firstAction = null;
        AAction currentAction;
        AAction lastAction = null;

        try( Stream< ? > stream = Files.lines( testFile ) ) {
            String line;
            String[] split;

            int lineNumber = 0;

            Iterator< ? > lineIterator = stream.iterator();

            // get the next line
            while( lineIterator.hasNext() ) {
                line = ( String ) lineIterator.next();
                lineNumber++;

                if( line == null ) {
                    continue;
                }

                // first part will be the name of the action, everything after that will be
                // arguments
                split = line.split( " ", 2 );

                if( split.length > 0 ) {
                    try {
                        String aName = split[ 0 ];
                        String aParam = "";

                        if( split.length == 2 && split[ 1 ] != null ) {
                            aParam = split[ 1 ];
                        }

                        if( aParam == null ) {
                            aParam = "";
                        }

                        if( aName != null ) {
                            currentAction = ActionFactory.createAction( instance, aName, aParam );
                        } else {
                            continue;
                        }
                    } catch( ActionInitiationException ai ) {
                        throw new ParseException( "Error Parsing file '" + testFile.toAbsolutePath().toString()
                                + "' at line " + lineNumber, ai );
                    }

                    // If no Action was created yet. Do so.
                    if( currentAction != null ) {
                        if( firstAction == null || lastAction == null ) {
                            // First Action is also the last action at this point. After this the first
                            // Action will NOT be changed again.
                            firstAction = currentAction;
                            lastAction = currentAction;
                        } else {
                            // Last action gets a new successor and will be reset to the just now created
                            // action
                            lastAction.nextAction = currentAction;
                            lastAction = currentAction;
                        }
                    }
                } else {
                    // skip without comment
                }

            }
        } catch( IOException e ) {
            throw new ParseException( "Error reading File.", e );
        }

        return firstAction;
    }
}
