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

import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import de.typedcode.txt2selenium.actions.AAction;
import de.typedcode.txt2selenium.actions.ActionFactory;
import de.typedcode.txt2selenium.actions.AssertReadEqualsAction;
import de.typedcode.txt2selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import de.typedcode.txt2selenium.util.repositories.CompareStringRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestAssertReadEqualsAction {

    private TestScenario testScenarioMock;
    private WebUtil webUtil;

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException {
        this.testScenarioMock = Mockito.mock( TestScenario.class );
        this.webUtil = Mockito.mock( WebUtil.class );
        CompareStringRepository compareStringRepository = Mockito.mock(CompareStringRepository.class);

        Field instance = WebUtil.class.getDeclaredField("webUtil" );
        instance.setAccessible( true );
        instance.set( instance, this.webUtil );

        instance = CompareStringRepository.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, compareStringRepository);
    }

    @AfterAll
    static void cleanUp() throws Exception {
        Field instance = WebUtil.class.getDeclaredField("webUtil" );
        instance.setAccessible( true );
        instance.set( instance, null );

        instance = CompareStringRepository.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }

    @Test
    void testActionInitiationErrorNoPrameter() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "" ) );

        assertEquals( "Could not create 'AssertReadEqualsAction'. Wrong number of parameters. Use 'assertReadEquals expectedIdentifier actualIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testActionInitiationErrorWrongParameterCount() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "oneParameter" ) );

        assertEquals( "Could not create 'AssertReadEqualsAction'. Wrong number of parameters. Use 'assertReadEquals expectedIdentifier actualIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testActionInitiationErrorToManyParameters() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "par1 par2 par3 par4" ) );

        assertEquals( "Could not create 'AssertReadEqualsAction'. Wrong number of parameters. Use 'assertReadEquals expectedIdentifier actualIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void testActionInitiationErrorExpectedNotExisting() throws ActionInitiationException {
        AssertReadEqualsAction action = ( AssertReadEqualsAction ) ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "first second" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertReadEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Could not find expectedIdentifier 'first'.", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testActionExecutionParamNotRead() throws ActionExecutionException {
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "first second" );

        Mockito.when( this.webUtil.getReadVar( "first" ) ).thenReturn( Optional.of( "actual" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.empty() );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertReadEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Could not find actualIdentifier 'second'.", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testActionExecutionNoMatch() throws ActionExecutionException {
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "first second" );

        Mockito.when( this.webUtil.getReadVar( "first" ) ).thenReturn( Optional.of( "value" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.of( "actual" ) );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertReadEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Parameters did not match. Expected (first): value / Actual (second): actual", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testActionExecutionSuccessfull() {
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "first second" );

        Mockito.when( CompareStringRepository.getInstance().getCompareString( this.testScenarioMock, "first" ) ).thenReturn( Optional.of( "actual" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.of( "actual" ) );

        assertAction.execute();
    }

    @Test
    void testThreeParamsTrueEvaluationSuccessfull() {
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "true first second" );

        Mockito.when( CompareStringRepository.getInstance().getCompareString( this.testScenarioMock, "first" ) ).thenReturn( Optional.of( "actual" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.of( "actual" ) );

        assertAction.execute();
    }

    @Test
    void testThreeParamsTrueEvaluationNoMatchLog() throws ActionExecutionException {
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "true first second" );

        Mockito.when( this.webUtil.getReadVar( "first" ) ).thenReturn( Optional.of( "value" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.of( "actual" ) );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format("%s %s first second", AssertReadEqualsAction.IDENTIFIER, Boolean.TRUE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Parameters did not match. Expected (first): value / Actual (second): actual", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testThreeParamsFalseEvaluationSuccessfull() {
        //Check that evaluation is successfull if the evaluationIndicator Parameter is false and the values do not match
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "false first second" );

        Mockito.when( CompareStringRepository.getInstance().getCompareString( this.testScenarioMock,  "first" ) ).thenReturn( Optional.of( "value" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.of( "actual" ) );

        assertAction.execute();
    }

    @Test
    void testThreeParamsFalseEvaluationNoMatch() throws ActionExecutionException {
        //Check that evaluation is not successfull if the evaluationIndicator Parameter is false and the values do match
        AssertReadEqualsAction assertAction = ( AssertReadEqualsAction )ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "false first second" );

        Mockito.when( this.webUtil.getReadVar( "first" ) ).thenReturn( Optional.of( "actual" ) );
        Mockito.when( this.webUtil.getReadVar( "second" ) ).thenReturn( Optional.of( "actual" ) );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        assertAction.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s %s first second", AssertReadEqualsAction.IDENTIFIER, Boolean.FALSE.toString() ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Execution Error. Parameters did not match. Expected (first): actual / Actual (second): actual", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testGetCommandTwoParameters() {
        AAction action = ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "first second" );

        assertEquals( String.format( "%s true first second", AssertReadEqualsAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testGetCommandThreeParametersTrue() {
        AAction action = ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "true first second" );

        assertEquals( String.format( "%s true first second", AssertReadEqualsAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testGetCommandThreeParametersFalse() {
        AAction action = ActionFactory.createAction(testScenarioMock, AssertReadEqualsAction.IDENTIFIER, "false first second" );

        assertEquals( String.format( "%s false first second", AssertReadEqualsAction.IDENTIFIER ), action.getCommand() );
    }
}
