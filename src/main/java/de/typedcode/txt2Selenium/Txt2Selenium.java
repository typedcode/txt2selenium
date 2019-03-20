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

package de.typedcode.txt2Selenium;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.exceptions.InstanceInitiationException;
import de.typedcode.txt2Selenium.exceptions.ParseException;
import de.typedcode.txt2Selenium.methods.Method;
import de.typedcode.txt2Selenium.parsers.CompareStringParser;
import de.typedcode.txt2Selenium.parsers.TestFileParser;

public class Txt2Selenium {
    public final String FILE_EXTENSION = ".t2s";
    public final String COMPARE_STRINGS_FILE_NAME = "compareStrings" + FILE_EXTENSION;
    public final String TEST_FILE_REGEX = "^(?!(global\\.t2s))(?!(compareStrings\\.t2s)).*\\.t2s";

    private List< Path > testFiles = new ArrayList< Path >();
    private Path mainDirectory;
    private Map< String, String > compareStrings;
    private Map< Path, AAction > tests;
    private Path COMPARE_STRINGS_FILE;

    public Txt2Selenium( Path directory ) {
        this.mainDirectory = directory;
        this.compareStrings = new HashMap<>();
        this.tests = new HashMap<>();

        if( !Files.exists( this.mainDirectory ) ) {
            throw new InstanceInitiationException( "Given directory does not exist: " + this.mainDirectory );
        }

        if( !Files.isDirectory( this.mainDirectory ) ) {
            throw new InstanceInitiationException( "Given Path is not a directory: " + this.mainDirectory );
        }

        // Getting the compareStrings file
        Path compareStringFile = Paths.get( this.mainDirectory.toString(), COMPARE_STRINGS_FILE_NAME );

        if( compareStringFile != null && Files.exists( compareStringFile ) ) {
            COMPARE_STRINGS_FILE = compareStringFile;
            parseCompareStrings();
        }

        // Getting the Testfiles
        try( DirectoryStream< ? > ds = Files.newDirectoryStream( this.mainDirectory, TEST_FILE_REGEX ) ) {
            Iterator< ? > iterator = ds.iterator();

            Path next;
            while( iterator.hasNext() ) {
                next = ( Path ) iterator.next();

                this.testFiles.add( next );
            }
        } catch( IOException e ) {
            // TODO add exception Handling
        }
        if( testFiles.size() == 0 ) {
            throw new InstanceInitiationException(
                    "Given directory does not contain any testfiles: " + this.mainDirectory );
        }

        try {
            parseCompareStrings();
            parseTestFiles();
        } catch( ParseException pe ) {

        }
    }

    private void parseCompareStrings() {
        if( COMPARE_STRINGS_FILE != null ) {
            this.compareStrings = CompareStringParser.parse( COMPARE_STRINGS_FILE );
        }
    }

    /**
     * Parses the given Testfiles.
     */
    private void parseTestFiles() throws ParseException {
        for( Path testFile : this.testFiles ) {
            AAction firstAction = TestFileParser.parse( this, testFile );

            if( firstAction != null ) {
                this.tests.put( testFile, firstAction );
            }
        }
    }

    /**
     * Runs the Tests contained in <code>this.testFiles</code>
     */
    public void runTests() {

    }

    public List< Path > getTestFiles() {
        return this.testFiles;
    }

    public Map< Path, AAction > getParsedTests() {
        return this.tests;
    }

    public Path getCompareStringsFile() {
        return COMPARE_STRINGS_FILE;
    }

    public Path getMainDirectory() {
        return this.mainDirectory;
    }

    public String getCompareString( String identifier ) {
        return this.compareStrings.get( identifier );
    }

    /**
     * Returns the Method represented by the methodName. Will return null if the Method was not found.
     *
     * @param methodName Name of the Method to return
     * @return Method represented by methodName or null if the Method was not found
     */
    public Method getMethod(String methodName ) {
        return null;
    }
}
