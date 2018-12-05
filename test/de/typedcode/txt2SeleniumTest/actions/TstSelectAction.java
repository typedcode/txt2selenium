/**
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

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

@SuppressWarnings( "null" )
public class TstSelectAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    public void before() {
        WebUtil.WEB_UTIL.reset();
    }

    @Test
    void selectNoParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> new SelectAction( this.txt2SeleniumMock, "" ) );

        assertEquals( "Could not initiate the Select Action. Action needs two parameters. Use: select byIdentifier id",
                exception.getMessage() );
    }

    @Test
    void selectOneParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> new SelectAction( this.txt2SeleniumMock, "by" ) );

        assertEquals( "Could not initiate the Select Action. Action needs two parameters. Use: select byIdentifier id",
                exception.getMessage() );
    }

    @Test
    void selectById() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/selectAction/selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select", "id selectId" );
        action.execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        assertNotNull( selectedElement );

        if( selectedElement != null ) {
            assertEquals( "ID", selectedElement.getText() );
            assertEquals( "p", selectedElement.getTagName() );
        }
    }

    @Test
    void selectByName() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/selectAction/selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select",
                "name selectName" );
        action.execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        assertNotNull( selectedElement );

        if( selectedElement != null ) {
            assertEquals( "Name", selectedElement.getText() );
            assertEquals( "div", selectedElement.getTagName() );
        }
    }

    @Test
    void selectByXPath() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/selectAction/selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select",
                "xpath /html/body/div/p/a[3]" );
        action.execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        assertNotNull( selectedElement );

        if( selectedElement != null ) {
            assertEquals( "XPath", selectedElement.getText() );
            assertEquals( "a", selectedElement.getTagName() );
        }
    }

    @Test
    void selectByXPath2() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/selectAction/selectActionTestfile.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction action = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select",
                "xpath //*[contains(text(),'2')]" );
        action.execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        assertNotNull( selectedElement );

        if( selectedElement != null ) {
            assertEquals( "XPath2", selectedElement.getText() );
            assertEquals( "a", selectedElement.getTagName() );
        }
    }
}
