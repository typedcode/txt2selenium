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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.actions.SelectAction;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings( "null" )
class TestSelectAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    void before() {
        WebUtil.reset();
    }

    @Test
    void testSelectNoParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "" ) );

        assertEquals( "Could not initiate the Select Action. Action needs two parameters. Use: select byIdentifier id",
                exception.getMessage() );
    }

    @Test
    void testSelectOneParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "by" ) );

        assertEquals( "Could not initiate the Select Action. Action needs two parameters. Use: select byIdentifier id",
                exception.getMessage() );
    }

    @Test
    void testSelectById() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "selectAction", "selectActionTestfile.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id selectId" ).execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        assertNotNull( selectedElement );

        assertEquals( "ID", selectedElement.getText() );
        assertEquals( "p", selectedElement.getTagName() );
    }

    @Test
    void testSelectByName() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "selectAction", "selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "name selectName" );
        action.execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        assertNotNull( selectedElement );

        assertEquals( "Name", selectedElement.getText() );
        assertEquals( "div", selectedElement.getTagName() );
    }

    @Test
    void testSelectByXPath() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "selectAction", "selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "xpath /html/body/div/p/a[3]" );
        action.execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        assertNotNull( selectedElement );

        assertEquals( "XPath", selectedElement.getText() );
        assertEquals( "a", selectedElement.getTagName() );
    }

    @Test
    void testSelectByXPath2() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "selectAction", "selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "xpath //*[contains(text(),'2')]" );
        action.execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        assertNotNull( selectedElement );

        assertEquals( "XPath2", selectedElement.getText() );
        assertEquals( "a", selectedElement.getTagName() );
    }

    @Test
    void testElementNotFoundLog() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "selectAction", "selectActionTestfile.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id unknownId" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 2, logRecords.size() );

        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( String.format( "%s id unknownId", SelectAction.IDENTIFIER ), logRecords.get( 0 ).getMessage() );

        assertEquals( Level.SEVERE, logRecords.get( 1 ).getLevel() );
        assertEquals( "No Element found that could be identified by: By.id: unknownId", logRecords.get( 1 ).getMessage() );
    }

    @Test void testLog() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "selectAction", "selectActionTestfile.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id selectId" );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        action.execute();

        List<LogRecord> logRecords = handler.getLogRecords();

        assertEquals( 1, logRecords.size() );

        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( String.format( "%s id selectId", SelectAction.IDENTIFIER ), logRecords.get( 0 ).getMessage() );
    }

    @Test
    void testGetCommand() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "xpath /html/body/div/p/a[3]" );

        assertEquals(String.format( "%s xpath /html/body/div/p/a[3]", SelectAction.IDENTIFIER ), action.getCommand() );
    }
}
