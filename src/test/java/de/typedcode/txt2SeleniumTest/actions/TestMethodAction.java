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
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.methods.Method;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

class TestMethodAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException  {
        WebUtil webUtil = Mockito.mock( WebUtil.class );
        Field instance = WebUtil.class.getDeclaredField("WEB_UTIL" );
        instance.setAccessible( true );
        instance.set( instance, webUtil );
    }

    @AfterEach
    void afterEach() { WebUtil.reset(); }

    @Test
    void testGetCommand() {
        Method dummyMethod = new Method( "myMethod", null );
        Mockito.when( this.txt2SeleniumMock.getMethod( "myMethod" ) ).thenReturn( dummyMethod );

        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, MethodAction.IDENTIFIER, "myMethod" );

        assertEquals( "method myMethod", action.getCommand() );

        WebUtil.reset();
    }

    @Test
    void testEmptyParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class, () -> ActionFactory.createAction( this.txt2SeleniumMock, MethodAction.IDENTIFIER, "" ) );

        assertEquals( "Could not initiate Method Action. The name for the Method to call was empty. Usage: method methodName", exception.getMessage() );
    }

    @Test
    void testMethodNotFound() {
        Mockito.when( this.txt2SeleniumMock.getMethod( "unknownMethod" ) ).thenReturn( null );

        Throwable exception = assertThrows( ActionInitiationException.class, () -> ActionFactory.createAction( this.txt2SeleniumMock, MethodAction.IDENTIFIER, "unknownMethod" ) );

        assertEquals( "Could not initiate Method Action. Method 'unknownMethod' was not found.", exception.getMessage() );
    }

    @Test
    void testRunMethod() {
        //Use a non mocked instance
        WebUtil.reset();

        //Check that what should be there after running the Method is not there before
        assertNull(WebUtil.getInstance().getReadVar( "myRead" ) );
        assertEquals( "", WebUtil.getInstance().getTitle() );

        //Preparing the Actions to run in the Method
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "methodAction", "page.html" );
        AAction openAction = ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() );
        AAction selectAction = ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id toSelect" );
        AAction readAction = ActionFactory.createAction( this.txt2SeleniumMock, ReadAction.IDENTIFIER, "myRead" );

        //Preparing the Method to run
        Method method = new Method( "myMethod", openAction );
        method.addAction( selectAction );
        method.addAction( readAction );

        Mockito.when( this.txt2SeleniumMock.getMethod( "myMethod" ) ).thenReturn( method );

        AAction methodAction = ActionFactory.createAction( this.txt2SeleniumMock, MethodAction.IDENTIFIER, "myMethod" );

        methodAction.execute();

        assertEquals( "content", WebUtil.getInstance().getReadVar( "myRead" ) );
        assertEquals( "Run Method Title", WebUtil.getInstance().getTitle() );
    }

    @Test
    void testLogging() {
        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        Method method = new Method( "myMethod", null );
        Mockito.when( this.txt2SeleniumMock.getMethod( "myMethod" ) ).thenReturn( method );

        ActionFactory.createAction( this.txt2SeleniumMock, MethodAction.IDENTIFIER, "myMethod" ).execute();

        List< LogRecord > records = handler.getLogRecords();

        assertEquals( 3, records.size() );
        assertEquals( Level.INFO, records.get( 0 ).getLevel() );
        assertEquals( Level.INFO, records.get( 1 ).getLevel() );
        assertEquals( Level.INFO, records.get( 2 ).getLevel() );
        assertEquals( String.format( "%s myMethod", MethodAction.IDENTIFIER ), records.get( 0 ).getMessage() );
        assertEquals( "Entering Method: myMethod", records.get( 1 ).getMessage() );
        assertEquals( "Exiting Method: myMethod", records.get( 2 ).getMessage() );
    }
}
