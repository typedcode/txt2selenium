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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

@SuppressWarnings( "null" )
public class TstTypeAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @Test
    void elementNotSelected() {
        Path fileToOpen = Paths.get( "src/test/resources/actions/typeAction/typeAction.html" );
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, "open", fileToOpen.toUri().toString() );
        action.execute();

        Throwable exception = assertThrows( ActionExecutionException.class,
                () -> ActionFactory.createAction( this.txt2SeleniumMock, "type", "Text to type" ).execute() );
        assertEquals( "Coulnd not Type text. No Element selected yet.", exception.getMessage() );
    }

    @Test
    void elementNoTextElement() {
        Path fileToOpen = Paths.get( "src/test/resources/actions/typeAction/typeAction.html" );
        AAction action = ActionFactory.createAction( this.txt2SeleniumMock, "open", fileToOpen.toUri().toString() );
        action.execute();

        action = ActionFactory.createAction( this.txt2SeleniumMock, "select", "id notTypeable" );
        action.execute();

        Throwable exception = assertThrows( ActionExecutionException.class,
                () -> ActionFactory.createAction( this.txt2SeleniumMock, "type", "Text to type" ).execute() );
        assertEquals( "Selected element is no Text element to type text to.", exception.getMessage() );
    }

    @Test
    void typeTextIntoEmptyElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/typeAction/typeAction.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, "open", fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, "select", "id idEmpty" ).execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        if( selectedElement == null ) {
            fail( "Selected Element was null." );
            return;
        }

        assertEquals( "", selectedElement.getText() );

        ActionFactory.createAction( this.txt2SeleniumMock, "type", "Text to type" ).execute();

        assertEquals( "Text to type", selectedElement.getAttribute( "value" ) );
    }

    @Test
    void typeTextIntoNonEmptyElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/typeAction/typeAction.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, "open", fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, "select", "id idExisting" ).execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        if( selectedElement == null ) {
            fail( "Selected Element was null." );
            return;
        }

        assertEquals( "Already Existing!", selectedElement.getAttribute( "value" ) );

        ActionFactory.createAction( this.txt2SeleniumMock, "type", "Text to type" ).execute();

        assertEquals( "Already Existing!Text to type", selectedElement.getAttribute( "value" ) );
    }

    @Test
    void typeToInputField() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src/test/resources/actions/typeAction/typeAction.html" );

        ActionFactory.createAction( this.txt2SeleniumMock, "open", fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, "select", "id idInput" ).execute();

        WebElement selectedElement = WebUtil.WEB_UTIL.getSelectedElement();

        if( selectedElement == null ) {
            fail( "Selected Element was null." );
            return;
        }

        assertEquals( "", selectedElement.getText() );

        ActionFactory.createAction( this.txt2SeleniumMock, "type", "Text to type" ).execute();

        assertEquals( "Text to type", selectedElement.getAttribute( "value" ) );
    }
}
