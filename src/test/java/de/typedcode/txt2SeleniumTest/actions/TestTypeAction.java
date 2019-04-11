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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import de.typedcode.txt2Selenium.actions.*;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

@SuppressWarnings( "null" )
class TestTypeAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    void before() { WebUtil.reset(); }

    @Test
    void testElementNotSelectedLog() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "typeAction", "typeAction.html" );
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() );
        action.execute();

        action = ActionFactory.createAction( this.txt2SeleniumMock, TypeAction.IDENTIFIER, "Text to type" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s Text to type", TypeAction.IDENTIFIER ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Coulnd not Type text. No Element selected yet.", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testElementNoTextElementLog() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "typeAction", "typeAction.html" );
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() );
        action.execute();

        action = ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id notTypeable" );
        action.execute();

        action = ActionFactory.createAction( this.txt2SeleniumMock, TypeAction.IDENTIFIER, "Text to type" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals(Level.INFO, logRecords.get( 0 ).getLevel());
        assertEquals( String.format( "%s Text to type", TypeAction.IDENTIFIER ), logRecords.get( 0 ).getMessage() );

        assertEquals(Level.SEVERE, logRecords.get( 1 ).getLevel());
        assertEquals( "Selected element is no Text element to type text to.", logRecords.get( 1 ).getMessage());
    }

    @Test
    void testTypeTextIntoEmptyElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "typeAction", "typeAction.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id idEmpty" ).execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        if( selectedElement == null ) {
            fail( "Selected Element was null." );
            return;
        }

        assertEquals( "", selectedElement.getText() );

        ActionFactory.createAction( this.txt2SeleniumMock, TypeAction.IDENTIFIER, "Text to type" ).execute();

        assertEquals( "Text to type", selectedElement.getAttribute( "value" ) );
    }

    @Test
    void testTypeTextIntoNonEmptyElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "typeAction", "typeAction.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id idExisting" ).execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        if( selectedElement == null ) {
            fail( "Selected Element was null." );
            return;
        }

        assertEquals( "Already Existing!", selectedElement.getAttribute( "value" ) );

        ActionFactory.createAction( this.txt2SeleniumMock, TypeAction.IDENTIFIER, "Text to type" ).execute();

        assertEquals( "Already Existing!Text to type", selectedElement.getAttribute( "value" ) );
    }

    @Test
    void testTypeToInputField() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "typeAction", "typeAction.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id idInput" ).execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        if( selectedElement == null ) {
            fail( "Selected Element was null." );
            return;
        }

        assertEquals( "", selectedElement.getText() );

        ActionFactory.createAction( this.txt2SeleniumMock, TypeAction.IDENTIFIER, "Text to type" ).execute();

        assertEquals( "Text to type", selectedElement.getAttribute( "value" ) );
    }

    @Test
    void testGetCommand() {
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, TypeAction.IDENTIFIER, "Text to type" );

        assertEquals( String.format( "%s Text to type", TypeAction.IDENTIFIER ), action.getCommand() );
    }
}
