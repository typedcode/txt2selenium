/*
 * MIT License
 *
 * Copyright (c) 2018 Markus Hoffmann (www.typedcode.de)
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

package de.typedcode.txt2selenium;

import de.typedcode.txt2selenium.exceptions.InstanceInitiationException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.UnitLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Txt2Selenium {
    public static final String FILE_EXTENSION = ".t2s";
    public static final String COMPARE_STRINGS_FILE_NAME = "compareStrings" + FILE_EXTENSION;
    private static final String TEST_FILE_FOLDER = "tests";
    public static final String METHOD_FILE_FOLDER = "methods";

    private TestScenario defaultTestScenario;

    public Txt2Selenium() {
        Optional<Path> mainDirectoryOptional = Configuration.getInstance().getMainDirectory();

        if( mainDirectoryOptional.isEmpty() ) {
            throw new InstanceInitiationException( "No directory was set." );
        }

        Path mainDirectory = mainDirectoryOptional.get();

        if( !Files.exists( mainDirectory ) ) {
            throw new InstanceInitiationException( "Given directory does not exist: " + mainDirectory );
        }

        if( !Files.isDirectory( mainDirectory ) ) {
            throw new InstanceInitiationException( "Given Path is not a directory: " + mainDirectory );
        }

        prepareScenarios( Paths.get( mainDirectory.toString(), TEST_FILE_FOLDER) );
    }

    /**
     * Searches for and parses the testfiles.
     */
    private void prepareScenarios( Path scenarioPath ) {
        //Getting the testfiles
        this.defaultTestScenario = new TestScenario( null, scenarioPath );

        if( !TestScenario.testsExist() ) {
            throw new InstanceInitiationException(
                    "Given test-direcotry does not contain any testfiles: " + scenarioPath );
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

    public TestScenario getDefaultTestScenario() {
        return this.defaultTestScenario;
    }

    public static void main(String[] args) {
        if( args.length == 0 ) {
            Configuration.getInstance().setMainDirectory( Path.of( "." ) );
        }
        else if ( args.length == 1 ){
            Configuration.getInstance().setMainDirectory( Path.of( args[0] ) );
        }
        else {
            throw new IllegalArgumentException( "Run txt2Selenium with path as argument onlny. e.g. java -jar txt2Selenium.jar path/to/tests" );
        }

        Txt2Selenium txt2Selenium = new Txt2Selenium();
        txt2Selenium.execute();
    }
}
