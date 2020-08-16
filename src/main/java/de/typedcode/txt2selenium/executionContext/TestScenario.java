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

package de.typedcode.txt2selenium.executionContext;

import de.typedcode.txt2selenium.Txt2Selenium;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.FileUtil;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.repositories.CompareStringRepository;
import de.typedcode.txt2selenium.util.repositories.MethodRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestScenario extends ExecutionContext {

    /**
     * Will be set to true if in any given Scenario is a test.
     */
    private static boolean testsExist = false;

    private List<Test> tests;
    private List<TestScenario> subScenarios;
    public final TestScenario parentScenario;

    public TestScenario( TestScenario testScenario, Path path ) {
        this( testScenario, null, path ) ;
    }

    public TestScenario( TestScenario testScenario, TestScenario parentScenario, Path path ) {
        super( testScenario, path );
        this.parentScenario = parentScenario;

        CompareStringRepository.getInstance().registerScenario( this );
        MethodRepository.getInstance().registerScenario( this );
    }

    public static boolean testsExist() {
        return testsExist;
    }

    @Override
    String processName() {
        Optional<Path> mainDirectory = Configuration.getInstance().getMainDirectory();

        if( mainDirectory.isPresent() ) {
            return mainDirectory.get().relativize(this.path).toString();
        }

        UnitLogger.logConfig( "WARNING: No Main Directory configured!" );

        return this.path.toString();
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
        return "Starting Scenario: " + this.name;
    }

    @Override
    String getExecuteFinishMessage() {
        return "Ending Scenario: " + this.name;
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
        List<Path > directories = FileUtil.getTestDirectories( this.path);

        directories.forEach( o -> {

            if( !Txt2Selenium.METHOD_FILE_FOLDER.equals( o.getFileName().toString() ) ) {
                this.subScenarios.add(new TestScenario(this.testScenario, this, o));
            }
        } );
    }

    public List<Test> getTests() {
         return this.tests;
    }

    public List<TestScenario> getSubScenarios() {
         return this.subScenarios;
    }

    private void processTests() {
        //Getting the testfiles
        List<Path > testFiles = FileUtil.getTestFiles( this.path);

        testFiles.stream()
                .filter( o -> !Txt2Selenium.COMPARE_STRINGS_FILE_NAME.equals( o.getFileName().toString() ) )
                .forEach( o -> this.tests.add( new Test( this, o ) ) );

        if( !tests.isEmpty() ) {
            TestScenario.setTestsExist();
        }
    }

    private static void setTestsExist() {
         TestScenario.testsExist = true;
    }
}
