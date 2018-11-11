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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.actions.ReadAction;
import de.typedcode.txt2Selenium.actions.SelectAction;
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

@SuppressWarnings( "null" )
public class TstReadAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    public void before() {
        WebUtil.WEB_UTIL.reset();
    }

    @Test
    void actionInitiationError() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( txt2SeleniumMock, "read", "" ) );

        assertEquals( "Could not create 'ReadAction'. varIdentifier was empty. Use 'read varIdentifier'.",
                exception.getMessage() );
    }

    @Test
    void readWIthoudSelect() throws ActionInitiationException {
        ReadAction readAction = ( ReadAction ) ActionFactory.createAction( txt2SeleniumMock, "read", "myReadVar" );

        Throwable exception = assertThrows( ActionExecutionException.class, () -> readAction.execute() );
        assertEquals( "Could not execute 'read'. No Element was selected.", exception.getMessage() );
    }

    @Test
    void readWithoudNestedElements() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/readAction/readText.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select",
                "id divId" );
        selectAction.execute();

        ReadAction readAction = ( ReadAction ) ActionFactory.createAction( txt2SeleniumMock, "read", "myRead" );
        readAction.execute();

        assertEquals( "No nested", WebUtil.WEB_UTIL.getText( "myRead" ) );
    }

    @Test
    void readWithNestedElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/readAction/readText.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select",
                "id bodyId" );
        selectAction.execute();

        ReadAction readAction = ( ReadAction ) ActionFactory.createAction( txt2SeleniumMock, "read", "myRead" );
        readAction.execute();

        assertEquals( "Before nested\nNo nested\nAfter nested", WebUtil.WEB_UTIL.getText( "myRead" ) );
    }

    @Test
    void readEmptyElement() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/readAction/readText.html" );
        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        SelectAction selectAction = ( SelectAction ) ActionFactory.createAction( txt2SeleniumMock, "select", "id pId" );
        selectAction.execute();

        ReadAction readAction = ( ReadAction ) ActionFactory.createAction( txt2SeleniumMock, "read", "myRead" );
        readAction.execute();

        assertEquals( "", WebUtil.WEB_UTIL.getText( "myRead" ) );
    }
}