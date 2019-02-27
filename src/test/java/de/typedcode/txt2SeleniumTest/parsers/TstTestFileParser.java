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

package de.typedcode.txt2SeleniumTest.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.actions.ScreenshotAction;
import de.typedcode.txt2Selenium.exceptions.ParseException;
import de.typedcode.txt2Selenium.parsers.TestFileParser;

@SuppressWarnings( "null" )
public class TstTestFileParser {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @Test
    public void emptyTestFile() throws ParseException {
        AAction action = TestFileParser.parse( txt2SeleniumMock,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "emptyTestFile.t2s" ) );
        assertNull( action );
    }

    @Test
    public void multipleActions() throws ParseException {
        AAction action = TestFileParser.parse( txt2SeleniumMock,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "multipleActions.t2s" ) );
        if( action == null ) {
            fail( "action is null" );
        } else {
            assertEquals( OpenAction.class, action.getClass() );
            assertEquals( "http://www.markus-fischer.net", ( ( OpenAction ) action ).URL );
            action = action.nextAction;
            assertEquals( ScreenshotAction.class, action.getClass() );
            action = action.nextAction;
            assertEquals( OpenAction.class, action.getClass() );
            assertEquals( "http://www.typedcode.de", ( ( OpenAction ) action ).URL );
            action = action.nextAction;
            assertEquals( ScreenshotAction.class, action.getClass() );
            assertNull( action.nextAction );
        }
    }

    @Test
    public void oneAction() throws ParseException {
        AAction action = TestFileParser.parse( txt2SeleniumMock,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "oneAction.t2s" ) );

        if( action == null ) {
            fail( "action is null" );
        } else {
            assertEquals( OpenAction.class, action.getClass() );
            assertNull( action.nextAction );
            assertEquals( "http://www.typedcode.de", ( ( OpenAction ) action ).URL );
        }
    }

    @Test
    public void twoActions() throws ParseException {
        AAction action = TestFileParser.parse( txt2SeleniumMock,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "twoActions.t2s" ) );

        if( action == null ) {
            fail( "action is null" );
        } else {
            assertEquals( OpenAction.class, action.getClass() );
            action = action.nextAction;
            assertEquals( ScreenshotAction.class, action.getClass() );
            assertNull( action.nextAction );
        }
    }

    @Test
    public void initiationError() throws ParseException {
        Path path = Paths.get( "src", "test", "resources", "parsers", "testFileParser", "initiationError.t2s");
        Throwable exception = assertThrows( ParseException.class,
                () -> TestFileParser.parse( txt2SeleniumMock, path ) );
        assertEquals( "Error Parsing file '" + path.toAbsolutePath().toString() + "' at line 3",
                exception.getMessage() );
    }

}
