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
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings( "null" )
public class TestAssertCheckedAction {

    private Txt2Selenium txt2SeleniumMock;

    @BeforeEach
    public void before() {
        this.txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );
        WebUtil.reset();
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "assertCheckedAction", "assert.html" );

        ActionFactory.createAction( txt2SeleniumMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
    }

    @Test
    public void testNoSelection() {
        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "" );

        Throwable exception = assertThrows( ActionExecutionException.class,
                () -> action.execute() );

        assertEquals( "Execution Error. No Element selected for assertCheck.",
                exception.getMessage() );
    }

    @Test
    public void testNotCheckableElementSelected() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id notCheckable").execute();

        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true" );

        Throwable exception = assertThrows( ActionExecutionException.class,
                () -> action.execute() );

        assertEquals( "Execution Error. Selected Element 'By.id: notCheckable' can not evaluated for checked status.",
                exception.getMessage() );
    }

    @Test
    public void testElementTrueButShouldBeFalse() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id checkedElement").execute();

        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "false" );

        Throwable exception = assertThrows( ActionExecutionException.class,
                () -> action.execute() );

        assertEquals( "Evaluation Error. Element 'By.id: checkedElement' is true but should be false",
                exception.getMessage() );
    }

    @Test
    public void testElementFalseButShouldBeTrue() {
        before();

        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id uncheckedElement").execute();

        AAction action = ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true" );

        Throwable exception = assertThrows( ActionExecutionException.class,
                () -> action.execute() );

        assertEquals( "Evaluation Error. Element 'By.id: uncheckedElement' is false but should be true",
                exception.getMessage() );
    }

    @Test
    public void testEvaluationTrueIgnoreCase() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id checkedElement").execute();

        ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "tRuE" ).execute();
    }

    @Test
    public void testEvaluationTrueSuccess() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id checkedElement").execute();

        ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true" ).execute();
    }

    @Test
    public void testEvaluationFalseSuccess() {
        ActionFactory.createAction(this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id uncheckedElement").execute();

        ActionFactory.createAction( txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "false" ).execute();
    }

    @Test
    public void testGetCommandTrue() {
        AAction action = ActionFactory.createAction(this.txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "true");

        assertEquals( String.format( "%s true", AssertCheckedAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    public void testGetCommandFalse() {
        AAction action = ActionFactory.createAction(this.txt2SeleniumMock, AssertCheckedAction.IDENTIFIER, "false");

        assertEquals( String.format( "%s false", AssertCheckedAction.IDENTIFIER ), action.getCommand() );
    }
}