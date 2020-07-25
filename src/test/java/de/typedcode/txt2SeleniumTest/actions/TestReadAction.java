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

package de.typedcode.txt2SeleniumTest.actions;

import de.typedcode.txt2selenium.actions.*;
import de.typedcode.txt2selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestReadAction {

    private TestScenario testScenario = Mockito.mock( TestScenario.class );

    @BeforeEach
    void before() {
        WebUtil.reset();
    }

    @Test
    void testActionInitiationError() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "" ) );

        assertEquals( "Could not create 'ReadAction'. varIdentifier was empty. Use 'read varIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testReadWIthoudSelect() throws ActionInitiationException {
        ReadAction readAction = ( ReadAction ) ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "myReadVar" );

        Throwable exception = assertThrows( ActionExecutionException.class, readAction::execute );
        assertEquals( "Could not execute 'read'. No Element was selected.", exception.getMessage() );
    }

    @Test
    void testReadWithoudNestedElements() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "readAction", "readText.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction(testScenario, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction(testScenario, SelectAction.IDENTIFIER,
                "id divId" );
        selectAction.execute();

        ReadAction readAction = ( ReadAction ) ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "myRead" );
        readAction.execute();

        assertEquals( "No nested", WebUtil.getInstance().getReadVar( "myRead" ).get() );
    }

    @Test
    void testReadWithNestedElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "readAction", "readText.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction(testScenario, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction(testScenario, SelectAction.IDENTIFIER,
                "id bodyId" );
        selectAction.execute();

        ReadAction readAction = ( ReadAction ) ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "myRead" );
        readAction.execute();

        assertEquals( "Before nested\nNo nested\nAfter nested", WebUtil.getInstance().getReadVar( "myRead" ).get() );
    }

    @Test
    void testReadEmptyElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "readAction", "readText.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction(testScenario, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction(testScenario, SelectAction.IDENTIFIER, "id pId" );
        selectAction.execute();

        ReadAction readAction = ( ReadAction ) ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "myRead" );
        readAction.execute();

        assertEquals( "", WebUtil.getInstance().getReadVar( "myRead" ).get() );
    }

    @Test
    void testGetCommand() {
        AAction action = ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "myRead" );

        assertEquals( String.format( "%s myRead", ReadAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testLogging() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "readAction", "logging.html" );

        ActionFactory.createAction(testScenario, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction(testScenario, SelectAction.IDENTIFIER, "id divId" ).execute();

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.setLogLevel( Level.FINE );
        UnitLogger.addHandler( handler );

        ActionFactory.createAction(testScenario, ReadAction.IDENTIFIER, "myRead" ).execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );
        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( Level.FINE, logRecords.get( 1 ).getLevel() );
        assertEquals( String.format( "%s myRead", ReadAction.IDENTIFIER ), logRecords.get( 0 ).getMessage() );
        assertEquals( "myRead = Read Logging", logRecords.get( 1 ).getMessage() );

        //Reset LogLevel for further tests
        UnitLogger.setLogLevel( Level.INFO );
    }
}
