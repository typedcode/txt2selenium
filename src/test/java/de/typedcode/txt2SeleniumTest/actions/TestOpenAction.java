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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import de.typedcode.txt2selenium.actions.AAction;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.typedcode.txt2selenium.actions.ActionFactory;
import de.typedcode.txt2selenium.actions.OpenAction;
import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.util.WebUtil;

class TestOpenAction {

    private TestScenario testScenario = Mockito.mock( TestScenario.class );

    @Test
    void testEmptyUrl() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( testScenario, OpenAction.IDENTIFIER, "" ) );
        assertEquals( "Coulnd not Initiate OpenAction. The given URL was empty.", exception.getMessage() );
    }

    @Test
    void testSpaceParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( testScenario, OpenAction.IDENTIFIER, "  " ) );
        assertEquals( "Coulnd not Initiate OpenAction. The given URL was empty.", exception.getMessage() );
    }

    @Test
    void testSuccessfullInitiation() throws ActionInitiationException {
        String url = "http://www.typedcode.de";
        OpenAction action = ( OpenAction ) ActionFactory.createAction( testScenario, OpenAction.IDENTIFIER, url );
        assertEquals( url, action.getUrl() );
    }

    @Test
    void testExecuteOpen() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "openAction", "open.html" );
        OpenAction action = ( OpenAction ) ActionFactory.createAction( testScenario, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        action.execute();

        assertEquals( "One", WebUtil.getInstance().getTitle() );
    }

    @Test
    void testGetCommand() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "openAction", "open.html" );
        AAction action = ActionFactory.createAction( testScenario, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );

        assertEquals( String.format( "%s %s", OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ), action.getCommand() );
    }

    @Test
    void testLogging() {
        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "openAction", "open.html" );
        ActionFactory.createAction( testScenario, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();

        List<LogRecord> records = handler.getLogRecords();

        assertEquals( 1, records.size() );
        assertEquals( Level.INFO, records.get( 0 ).getLevel() );
        assertEquals( String.format( "%s %s", OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ), records.get( 0 ).getMessage() );
    }
}
