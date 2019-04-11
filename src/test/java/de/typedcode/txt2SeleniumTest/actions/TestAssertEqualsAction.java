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
import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.AssertEqualsAction;
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestAssertEqualsAction {

    private Txt2Selenium txt2SeleniumMock;
    private WebUtil webUtil;

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException {
        this.txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );
        this.webUtil = Mockito.mock( WebUtil.class );

        Field instance = WebUtil.class.getDeclaredField("WEB_UTIL" );
        instance.setAccessible( true );
        instance.set( instance, this.webUtil );
    }

    @Test
    void testActionInitiationErrorNoPrameter() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "" ) );

        assertEquals( "Could not create 'AssertEqualsAction'. Wrong number of parameters. Use 'assertEquals expectedIdentifier actualIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testActionInitiationErrorWrongParameterCount() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "oneParameter" ) );

        assertEquals( "Could not create 'AssertEqualsAction'. Wrong number of parameters. Use 'assertEquals expectedIdentifier actualIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testActionInitiationErrorToManyParameters() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "par1 par2 par3 par4" ) );

        assertEquals( "Could not create 'AssertEqualsAction'. Wrong number of parameters. Use 'assertEquals expectedIdentifier actualIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testActionInitiationErrorExpectedNotExisting() throws ActionInitiationException {
        AssertEqualsAction action = ( AssertEqualsAction ) ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "first second" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Could not find expectedIdentifier 'first'.", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testActionExecutionParamNotRead() throws ActionExecutionException {
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "actual" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( null );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Could not find actualIdentifier 'second'.", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testActionExecutionNoMatch() throws ActionExecutionException {
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "value" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( "actual" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Parameters did not match. Expected (first): value / Actual (second): actual", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testActionExecutionSuccessfull() {
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "actual" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( "actual" );

        assertAction.execute();
    }

    @Test
    void testThreeParamsTrueEvaluationSuccessfull() {
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "true first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "actual" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( "actual" );

        assertAction.execute();
    }

    @Test
    void testThreeParamsTrueEvaluationNoMatchLog() throws ActionExecutionException {
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "true first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "value" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( "actual" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format("%s %s first second", AssertEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Parameters did not match. Expected (first): value / Actual (second): actual", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testThreeParamsFalseEvaluationSuccessfull() {
        //Check that evaluation is successfull if the evaluationIndicator Parameter is false and the values do not match
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "false first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "value" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( "actual" );

        assertAction.execute();
    }

    @Test
    void testThreeParamsFalseEvaluationNoMatch() throws ActionExecutionException {
        //Check that evaluation is not successfull if the evaluationIndicator Parameter is false and the values do match
        AssertEqualsAction assertAction = ( AssertEqualsAction )ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "false first second" );

        Mockito.when( this.txt2SeleniumMock.getCompareString( "first" ) ).thenReturn( "actual" );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( "actual" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertEqualsAction.IDENTIFIER, Boolean.FALSE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Parameters did not match. Expected (first): actual / Actual (second): actual", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testGetCommandTwoParameters() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "first second" );

        assertEquals( String.format( "%s true first second", AssertEqualsAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testGetCommandThreeParametersTrue() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "true first second" );

        assertEquals( String.format( "%s true first second", AssertEqualsAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testGetCommandThreeParametersFalse() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertEqualsAction.IDENTIFIER, "false first second" );

        assertEquals( String.format( "%s false first second", AssertEqualsAction.IDENTIFIER ), action.getCommand() );
    }
}
