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

package de.typedcode.txt2SeleniumTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.UnitLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.typedcode.txt2selenium.Txt2Selenium;

import static org.junit.jupiter.api.Assertions.*;

class TestTxt2Selenium {

    private TestLoggingHandler handler;

    @BeforeEach
    void before() {
        this.handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );
    }

    @AfterEach
    void afterEach() {
        UnitLogger.removeHandler( this.handler );
    }

    @Test
    void testInitiationNonExistingDirectory() {
        Path path = Paths.get( "test", "notExisting" );

        Configuration.getInstance().setMainDirectory( path );

        Throwable exception = assertThrows( RuntimeException.class, Txt2Selenium::new);
        assertEquals( String.format( "Given directory does not exist: %s", path.toString() ), exception.getMessage() );
    }

    @Test
    void testInitiationDirectoryIsFile() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "files", "globalOneTestfile", "global.t2s" );

        Configuration.getInstance().setMainDirectory( path );

        Throwable exception = assertThrows( RuntimeException.class, Txt2Selenium::new );
        assertEquals(
                String.format( "Given Path is not a directory: %s", path.toString() ), exception.getMessage() );
    }

    @Test
    void testResolvingTestFiles() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving" );
        Path first = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving", "tests", "first.t2s" );
        Path second = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving", "tests", "second.t2s" );

        List<Path> wanted = new ArrayList<>();
        wanted.add( first );
        wanted.add( second );

        Configuration.getInstance().setMainDirectory( path );

        Txt2Selenium instance = new Txt2Selenium();

        TestScenario defaultTestScenario = instance.getDefaultTestScenario();
        List<de.typedcode.txt2selenium.executionContext.Test> tests = defaultTestScenario.getTests();

        assertEquals(2, tests.size() );
        tests.forEach( o -> assertTrue( wanted.contains( o.getPath()) ) );
    }

    @Test
    void testMainMethodToManyArguments() {
        String[] arguments = new String[] { "first", "second" };
        Throwable exception = assertThrows( RuntimeException.class, () -> { Txt2Selenium.main( arguments ); } );
        assertEquals(
                "Run txt2Selenium with path as argument only. e.g. java -jar txt2Selenium.jar path/to/tests",
                exception.getMessage() );
    }

    @Test
    void testMainSettingMainDirectory() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving" );
        String[] arguments = new String[] { path.toString() };

        Txt2Selenium.main( arguments );

        assertEquals( path, Configuration.getInstance().getMainDirectory().get() );
    }

    @Test
    void testExecutionLogging() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving" );
        String[] arguments = new String[] { path.toString() };

        Txt2Selenium.main( arguments );

        List<LogRecord> logRecords = this.handler.getLogRecords();

        assertEquals( "Starting test execution", logRecords.get( 0 ).getMessage() );
        assertEquals( "Test execution finished", logRecords.get( logRecords.size() - 1 ).getMessage() );
    }
}
