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

package de.typedcode.txt2SeleniumTest.actions;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.*;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;
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

class TestAssertCheckedAction {

    private Txt2Selenium txt2SeleniumMock;

    @BeforeEach
    void before() {
        this.txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );
        WebUtil.reset();
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "assertCheckedAction", "assert.html" );

        ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
    }

    @Test
    void testNoSelectionLog() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );
        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( String.format( "%s %s", AssertCheckedAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals( Level.SEVERE, logRecords.get( 1 ).getLevel() );
        assertEquals( "Error: No Element selected to check for selection status.", logRecords.get( 1 ).getMessage() );
    }

    @Test
    void testNotCheckableElementSelectedLog() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id notCheckable").execute();

        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( String.format( "%s %s", AssertCheckedAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals( Level.SEVERE, logRecords.get( 1 ).getLevel() );
        assertEquals( "Execution Error. Selected Element '<p id=\"notCheckable\">' can not be evaluated for checked status.", logRecords.get( 1 ).getMessage() );
    }

    @Test
    void testElementTrueButShouldBeFalseLog() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id checkedElement").execute();

        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "false" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( String.format( "%s %s", AssertCheckedAction.IDENTIFIER, Boolean.FALSE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals( Level.SEVERE, logRecords.get( 1 ).getLevel() );
        assertEquals( String.format( "Evaluation Error. Element '<input type=\"checkbox\" id=\"checkedElement\" checked=\"\" value=\"on\" />' is %s but should be %s", Boolean.TRUE.toString(), Boolean.FALSE.toString() ), logRecords.get( 1 ).getMessage() );
    }

    @Test
    void testElementFalseButShouldBeTrueLog() {
        before();

        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id uncheckedElement").execute();

        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( String.format( "%s %s", AssertCheckedAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals( Level.SEVERE, logRecords.get( 1 ).getLevel() );
        assertEquals( String.format( "Evaluation Error. Element '<input type=\"checkbox\" id=\"uncheckedElement\" value=\"on\" />' is %s but should be %s", Boolean.FALSE.toString(), Boolean.TRUE.toString() ), logRecords.get( 1 ).getMessage() );
    }

    @Test
    void testEvaluationTrueIgnoreCase() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id checkedElement").execute();

        ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "tRuE" ).execute();
    }

    @Test
    void testEvaluationTrueSuccess() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id checkedElement").execute();

        ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true" ).execute();
    }

    @Test
    void testEvaluationFalseSuccess() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id uncheckedElement").execute();

        ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "false" ).execute();
    }

    @Test
    void testGetCommandTrue() {
        AAction action = ActionFactory.createAction(this.txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true");

        assertEquals( String.format( "%s true", AssertCheckedAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testGetCommandFalse() {
        AAction action = ActionFactory.createAction(this.txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "false");

        assertEquals( String.format( "%s false", AssertCheckedAction.IDENTIFIER ), action.getCommand() );
    }
}
