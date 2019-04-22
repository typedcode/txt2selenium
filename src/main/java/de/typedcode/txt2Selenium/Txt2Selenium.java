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

import de.typedcode.txt2Selenium.exceptions.InstanceInitiationException;
import de.typedcode.txt2Selenium.executionContext.Method;
import de.typedcode.txt2Selenium.executionContext.TestScenario;
import de.typedcode.txt2Selenium.parsers.CompareStringParser;
import de.typedcode.txt2Selenium.util.FileUtil;
import de.typedcode.txt2Selenium.util.UnitLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Txt2Selenium {
    public static final String FILE_EXTENSION = ".t2s";
    private final String COMPARE_STRINGS_FILE_NAME = "compareStrings" + FILE_EXTENSION;
    private final String TEST_FILE_FOLDER = "tests";
    private final String METHOD_FILE_FOLDER = "methods";

    private Path mainDirectory;
    private Map< String, String > compareStrings;
    private TestScenario defaultTestScenario;
    private Map<String, Method> methods;
    private Path COMPARE_STRINGS_FILE;

    public Txt2Selenium( Path directory ) {
        this.mainDirectory = directory;
        this.compareStrings = new HashMap<>();
        this.methods = new HashMap<>();

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

        parseCompareStrings();
        parseMethodFiles();
        prepareScenarios();
    }

    private void parseCompareStrings() {
        if( COMPARE_STRINGS_FILE != null ) {
            this.compareStrings = CompareStringParser.parse( COMPARE_STRINGS_FILE );
        }
    }

    /**
     * Searches and parses the method-files
     */
    private void parseMethodFiles() {
        //Getting the methodfiles
        Path testFileDirectory = Paths.get( this.mainDirectory.toString(), this.METHOD_FILE_FOLDER );

        List<Path> methodFiles = FileUtil.getTestFiles( testFileDirectory );

        methodFiles.forEach( methodFile -> {
            Method method = new Method(this, methodFile);
            this.methods.put( method.getName(), method );
        } );
    }

    /**
     * Searches for and parses the testfiles.
     */
    private void prepareScenarios() {
        //Getting the testfiles
        Path testFileDirectory = Paths.get( this.mainDirectory.toString(), this.TEST_FILE_FOLDER );
        this.defaultTestScenario = new TestScenario( this, testFileDirectory );

        if( !TestScenario.TESTS_EXIST ) {
            throw new InstanceInitiationException(
                    "Given test-direcotry does not contain any testfiles: " + testFileDirectory );
        }
    }

    /**
     * Runs the Tests contained in <code>this.testFiles</code>
     */
    public void execute() {
        UnitLogger.logInfo( "Starting Test Execution" );

        this.defaultTestScenario.execute();

        UnitLogger.logInfo( "Test Execution Finished" );
    }

    /**
     * Returns the Main-Directory where the application was launched.
     * @return Path where the Application was launched.
     */
    public Path getMainDirectory() {
        return this.mainDirectory;
    }

    /**
     * Returns the the string which was described by the identifier. Null if the Identifier does not exist
     * @param identifier Identifier for the String to return.
     * @return Returns the Compare String identified by identifier
     */
    public String getCompareString( String identifier ) {
        return this.compareStrings.get( identifier );
    }

    /**
     * Returns the Method represented by the methodName. Will return null if the Method was not found.
     *
     * @param methodName Name of the Method to return
     * @return Method represented by methodName or null if the Method was not found
     */
    public Method getMethod( String methodName ) {
        return this.methods.get( methodName );
    }

    public TestScenario getDefaultTestScenario() {
        return this.defaultTestScenario;
    }
}
