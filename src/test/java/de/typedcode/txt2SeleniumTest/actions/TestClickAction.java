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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

@SuppressWarnings( "null" )
public class TestClickAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    public void before() {
        WebUtil.reset();
    }

    @Test
    public void testClickWithoutselect() {
        AAction ca = ActionFactory.createAction( this.txt2SeleniumMock, ClickAction.IDENTIFIER, "" );

        Throwable exception = assertThrows( ActionExecutionException.class, () -> ca.execute() );

        assertEquals( "Error execution ClickAction. No element was selected.", exception.getMessage() );
    }

    @Test
    public void testClickById() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/clickAction/beforeClick.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.getInstance().getTitle() );

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "id clickActionId" );
        selectAction.execute();

        ClickAction action = ( ClickAction ) ActionFactory.createAction( txt2SeleniumMock, ClickAction.IDENTIFIER, "" );
        action.execute();

        assertEquals( "After Click Id", WebUtil.getInstance().getTitle() );
    }

    @Test
    public void testClickByName() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/clickAction/beforeClick.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.getInstance().getTitle() );

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "name clickActionName" );
        selectAction.execute();

        ClickAction action = ( ClickAction ) ActionFactory.createAction( txt2SeleniumMock, ClickAction.IDENTIFIER, "" );
        action.execute();

        assertEquals( "After Click Name", WebUtil.getInstance().getTitle() );
    }

    @Test
    public void testClickByXPath() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/clickAction/beforeClick.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.getInstance().getTitle() );

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "xpath /html/body/div/p/a[3]" );
        selectAction.execute();

        ClickAction action = ( ClickAction ) ActionFactory.createAction( txt2SeleniumMock, ClickAction.IDENTIFIER, "" );
        action.execute();

        assertEquals( "After Click XPath", WebUtil.getInstance().getTitle() );
    }

    @Test
    public void testClickByXPath2() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/clickAction/beforeClick.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.getInstance().getTitle() );

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER,
                "xpath //*[contains(text(),'with spaces')]" );
        selectAction.execute();

        ClickAction action = ( ClickAction ) ActionFactory.createAction( txt2SeleniumMock, ClickAction.IDENTIFIER, "" );
        action.execute();

        assertEquals( "After Click XPath 2", WebUtil.getInstance().getTitle() );
    }

    @Test
    public void testGetCommand() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, ClickAction.IDENTIFIER, "" );

        assertEquals( ClickAction.IDENTIFIER, action.getCommand() );
    }

    @Test
    public void testLogging() {
        Path fileToOpen = Paths.get( "src/test/resources/actions/clickAction/logging.html" );

        ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( txt2SeleniumMock, SelectAction.IDENTIFIER, "id clickElement" ).execute();

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        ActionFactory.createAction( txt2SeleniumMock, ClickAction.IDENTIFIER, "" ).execute();

        List<LogRecord > logRecords = handler.getLogRecords();

        assertEquals( 1, logRecords.size() );
        assertEquals( Level.INFO, logRecords.get( 0 ).getLevel() );
        assertEquals( ClickAction.IDENTIFIER, logRecords.get( 0 ).getMessage() );
    }
}
