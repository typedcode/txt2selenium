/*
 * MIT License
 * 
 * Copyright (c) 2018 Markus Hoffmann (www.typedcode.de)
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

import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import de.typedcode.txt2selenium.actions.*;
import de.typedcode.txt2selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import de.typedcode.txt2selenium.util.repositories.CompareStringRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

class TestSendSpecialKeyAction {

    private TestScenario testScenarioMock;
    private WebUtil webUtil;
    private HtmlUnitDriver driver;

    @Test
    void testUnknownKey() throws ActionInitiationException {
        Throwable exception = assertThrows( ActionInitiationException.class,
                () -> ActionFactory.createAction( testScenarioMock, SendSpecialKeyAction.IDENTIFIER, "unknown" ) );

        assertEquals( "Could not initiate the Send Special Key Action. Unknown key: unknown",
                exception.getMessage() );
    }

    @Test
    void testNoElementSelected() throws ActionInitiationException {
        Throwable exception = assertThrows(ActionExecutionException.class,
                () -> ActionFactory.createAction( testScenarioMock, SendSpecialKeyAction.IDENTIFIER, "enter").execute() );

        assertEquals( "Error executing SendSpecialKeyAction. No element was selected.",
                exception.getMessage() );
    }

    @Test
    void testSendKey() {
        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "sendSpecialKeyAction", "sendSpecialKey.html" );

        ActionFactory.createAction( this.testScenarioMock, OpenAction.IDENTIFIER, fileToOpen.toUri().toString() ).execute();
        ActionFactory.createAction( this.testScenarioMock, SelectAction.IDENTIFIER, "id sendSpecialKeyTo").execute();
        ActionFactory.createAction( this.testScenarioMock, SendSpecialKeyAction.IDENTIFIER, "ARROW_RIGHT" ).execute();
        ActionFactory.createAction( this.testScenarioMock, SendSpecialKeyAction.IDENTIFIER, "BACK_SPACE" ).execute();

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement().get();

        assertNotNull( selectedElement );

        assertEquals( "", selectedElement.getAttribute( "value" ) );
    }
}
