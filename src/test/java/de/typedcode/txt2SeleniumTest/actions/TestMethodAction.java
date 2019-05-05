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

import de.typedcode.txt2selenium.actions.AAction;
import de.typedcode.txt2selenium.actions.ActionFactory;
import de.typedcode.txt2selenium.actions.MethodAction;
import de.typedcode.txt2selenium.actions.OpenAction;
import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.executionContext.Method;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import de.typedcode.txt2selenium.util.repositories.MethodRepository;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

class TestMethodAction {

    private TestScenario testScenario = Mockito.mock( TestScenario.class );

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException  {
        WebUtil webUtil = Mockito.mock( WebUtil.class );
        Field instance = WebUtil.class.getDeclaredField("WEB_UTIL" );
        instance.setAccessible( true );
        instance.set( instance, webUtil );
    }

    @AfterEach
    void afterEach() throws Exception {
        WebUtil.reset();

        Field instance = MethodRepository.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }

    private void prepareMock( TestScenario scenario, String method, Method dummyMethod ) throws Exception {
        MethodRepository mRep = Mockito.mock( MethodRepository.class );
        Mockito.when( mRep.getMethod( scenario, method ) ).thenReturn( Optional.ofNullable( dummyMethod ) );

        Field instance = MethodRepository.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, mRep );
    }

    @Test
    void testGetCommand() throws Exception {
        Method dummyMethod = new Method( this.testScenario, Paths.get( "src", "test", "resources", "actions", "methodAction", "testLoggingEmptyMethod.t2s" ) );

        prepareMock( this.testScenario, "testLoggingEmptyMethod", dummyMethod );

        AAction action = ActionFactory.createAction( this.testScenario, MethodAction.IDENTIFIER, "testLoggingEmptyMethod" );

        assertEquals( "method testLoggingEmptyMethod", action.getCommand() );

        WebUtil.reset();
    }

    @Test
    void testEmptyParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class, () -> ActionFactory.createAction( this.testScenario, MethodAction.IDENTIFIER, "" ) );

        assertEquals( "Could not initiate Method Action. The name for the Method to call was empty. Usage: method methodName", exception.getMessage() );
    }

    @Test
    void testMethodNotFound() {
        //Mockito.when( this.testScenario.getMethod( "unknownMethod" ) ).thenReturn( Optional.empty() );

        Throwable exception = assertThrows( ActionInitiationException.class, () -> ActionFactory.createAction( this.testScenario, MethodAction.IDENTIFIER, "unknownMethod" ) );

        assertEquals( "Could not initiate Method Action. Method 'unknownMethod' was not found.", exception.getMessage() );
    }

    @Test
    void testRunMethod() throws Exception {
        //Use a non mocked instance
        WebUtil.reset();

        //Check that what should be there after running the Method is not there before
        assertTrue( WebUtil.getInstance().getReadVar( "myRead" ).isEmpty() );
        assertEquals( "", WebUtil.getInstance().getTitle() );

        //Preparing the Actions to run in the Method
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "methodAction", "page.html" );

        //Open before Using the method. Because of the Path this test would not run on Windows and Linux systems
        ActionFactory.createAction( this.testScenario, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();

        //Preparing the Method to run
        Method method = new Method( this.testScenario, Paths.get( "src", "test", "resources", "actions", "methodAction", "testRunMethod.t2s" ) );

        prepareMock( this.testScenario, "myMethod", method );

        AAction methodAction = ActionFactory.createAction( this.testScenario, MethodAction.IDENTIFIER, "myMethod" );

        methodAction.execute();

        assertEquals( "content", WebUtil.getInstance().getReadVar( "myRead" ).get() );
        assertEquals( "Run Method Title", WebUtil.getInstance().getTitle() );
    }

    @Test
    void testLoggingEmptyMethod() throws Exception {
        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        Method method = new Method( this.testScenario, Paths.get( "src", "test", "resources", "actions", "methodAction", "testLoggingEmptyMethod.t2s" ) );
        prepareMock( this.testScenario, "testLoggingEmptyMethod", method );
        //Mockito.when( this.testScenario.getMethod( "testLoggingEmptyMethod" ) ).thenReturn( Optional.of( method ) );

        ActionFactory.createAction( this.testScenario, MethodAction.IDENTIFIER, "testLoggingEmptyMethod" ).execute();

        List<LogRecord> records = handler.getLogRecords();

        assertEquals( 4, records.size() );

        records.forEach( o -> assertEquals( Level.INFO, o.getLevel()) );

        assertEquals( String.format( "%s testLoggingEmptyMethod", MethodAction.IDENTIFIER ), records.get( 0 ).getMessage() );
        assertEquals( "Starting Method: testLoggingEmptyMethod", records.get( 1 ).getMessage() );
        assertEquals( "Method empty", records.get( 2 ).getMessage() );
        assertEquals( "Ending Method: testLoggingEmptyMethod", records.get( 3 ).getMessage() );
    }
}
