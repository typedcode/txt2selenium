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

package de.typedcode.txt2Selenium.executionContext;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.parsers.CompareStringParser;
import de.typedcode.txt2Selenium.util.FileUtil;
import de.typedcode.txt2Selenium.util.UnitLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TestScenario extends ExecutionContext {

    /**
     * Will be set to true if in any given Scenario is a test.
     */
    public static boolean TESTS_EXIST = false;
    private List<Test> tests;
    private List<TestScenario> subScenarios;
    private TestScenario parentScenario;
    private Map< String, String > compareStrings;

    public TestScenario( Txt2Selenium instance, Path path ) {
        this( instance, null, path ) ;
    }

    public TestScenario( Txt2Selenium instance, TestScenario parentScenario, Path path ) {
        super( instance, path );
        this.parentScenario = parentScenario;
        this.compareStrings = new HashMap<>();

        computeCompareStrings();
    }

    @Override
    String processName() {
        return this.txt2Selenium.getMainDirectory().relativize( this.PATH ).toString();
    }

    @Override
    public void execute() {

        if( !this.tests.isEmpty() ) {
            UnitLogger.logInfo( getExecuteStartMessage() );
            this.tests.forEach( Test::execute );
            UnitLogger.logInfo( getExecuteFinishMessage() );
        }
        else if ( !this.subScenarios.isEmpty() ) {
            this.subScenarios.forEach( TestScenario::execute );
        }
        else {
            UnitLogger.logInfo( getEmptyMessage() );
        }
    }

    @Override
    String getExecuteStartMessage() {
        return String.format( "Starting Scenario: " + this.name );
    }

    @Override
    String getExecuteFinishMessage() {
        return String.format( "Ending Scenario: " + this.name );
    }

    @Override
    String getEmptyMessage() {
        return "Scenario 'emptyScenario' does not contain any tests or sub-scenarios.";
    }

    /**
     * Searches for and parses the testfiles.
     */
     @Override
    void processPath() {
         this.tests = new ArrayList<>();
         this.subScenarios = new ArrayList<>();
         processTests();
         processSubScenarios();
    }

    private void processSubScenarios() {
        //Getting the testfiles
        List<Path > directories = FileUtil.getTestDirectories( this.PATH );

        if( directories.size() != 0 ) {
            TESTS_EXIST = true;
        }

        directories.forEach( o -> this.subScenarios.add( new TestScenario( this.txt2Selenium, this, o ) ) );
    }

    public List<Test> getTests() {
         return this.tests;
    }

    public List<TestScenario> getSubScenarios() {
         return this.subScenarios;
    }

    private void processTests() {
        //Getting the testfiles
        List<Path > testFiles = FileUtil.getTestFiles( this.PATH );

        if( testFiles.size() != 0 ) {
            TESTS_EXIST = true;
        }

        testFiles.stream()
                .filter( o -> !Txt2Selenium.COMPARE_STRINGS_FILE_NAME.equals( o.getFileName().toString() ) )
                .forEach( o -> this.tests.add( new Test( this.txt2Selenium, o ) ) );
    }

    /**
     * Computes the compare Strings for this Scenario
     */
    private void computeCompareStrings() {
        Path path = Paths.get(this.PATH.toString(), Txt2Selenium.COMPARE_STRINGS_FILE_NAME);

        if( Files.exists( path) ) {
            this.compareStrings = CompareStringParser.parse( path );
        }

    }

    public Optional< String > getCompareString( String key ) {
        String compareString = this.compareStrings.get( key );

        if(compareString != null) {
            return Optional.of( compareString );
        }
        else if( this.parentScenario != null ) {
            return (this.parentScenario.getCompareString( key ) );
        }

        return Optional.empty();
    }

    public Map< String, String > getCompareStrings() {
         return this.compareStrings;
    }
}
