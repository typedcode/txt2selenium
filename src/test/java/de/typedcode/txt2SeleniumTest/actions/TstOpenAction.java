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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

@SuppressWarnings( "null" )
public class TstOpenAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @Test
    void emptyUrl() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER, "" ) );
        assertEquals( "Coulnd not Initiate OpenAction. The given URL was empty.", exception.getMessage() );
    }

    @Test
    void spaceParameter() {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER, "  " ) );
        assertEquals( "Coulnd not Initiate OpenAction. The given URL was empty.", exception.getMessage() );
    }

    @Test
    void successfullInitiation() throws ActionInitiationException {
        String url = "http://www.typedcode.de";
        OpenAction action = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER, url );
        assertEquals( url, action.URL );
    }

    @Test
    void executeOpen() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "openAction", "open.html" );
        OpenAction action = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        action.execute();

        assertEquals( "One", WebUtil.getInstance().getTitle() );
    }

}
