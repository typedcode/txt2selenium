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

package de.typedcode.txt2SeleniumTest.parsers;

import de.typedcode.txt2selenium.actions.AAction;
import de.typedcode.txt2selenium.actions.OpenAction;
import de.typedcode.txt2selenium.actions.ScreenshotAction;
import de.typedcode.txt2selenium.exceptions.ParseException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.parsers.TestFileParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestTestFileParser {

    private TestScenario testScenario = Mockito.mock( TestScenario.class );

    @Test
    void testEmptyTestFile() throws ParseException {
        Optional< AAction > action = TestFileParser.parse(testScenario,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "emptyTestFile.t2s" ) );
        assertTrue( action.isEmpty() );
    }

    @Test
    void testMultipleActions() throws ParseException {
        AAction action = TestFileParser.parse(testScenario,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "multipleActions.t2s" ) ).get();

        assertEquals( OpenAction.class, action.getClass() );
        assertEquals( "http://www.markus-fischer.net", ( ( OpenAction ) action ).getUrl() );
        action = action.getNextAction();
        assertEquals( ScreenshotAction.class, action.getClass() );
        action = action.getNextAction();
        assertEquals( OpenAction.class, action.getClass() );
        assertEquals( "http://www.typedcode.de", ( ( OpenAction ) action ).getUrl() );
        action = action.getNextAction();
        assertEquals( ScreenshotAction.class, action.getClass() );
        assertNull( action.getNextAction() );
    }

    @Test
    void testOneAction() throws ParseException {
        AAction action = TestFileParser.parse(testScenario,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "oneAction.t2s" ) ).get();

        assertEquals( OpenAction.class, action.getClass() );
        assertNull( action.getNextAction() );
        assertEquals( "http://www.typedcode.de", ( ( OpenAction ) action ).getUrl() );
    }

    @Test
    void testTwoActions() throws ParseException {
        AAction action = TestFileParser.parse(testScenario,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "twoActions.t2s" ) ).get();

        assertEquals( OpenAction.class, action.getClass() );
        action = action.getNextAction();
        assertEquals( ScreenshotAction.class, action.getClass() );
        assertNull( action.getNextAction() );
    }

    @Test
    void testTwoActionsWihEmptyNewline() throws ParseException {
        AAction action = TestFileParser.parse(testScenario,
                Paths.get( "src", "test", "resources", "parsers", "testFileParser", "twoActionsWithEmptyNewline.t2s" ) ).get();

        assertEquals( OpenAction.class, action.getClass() );
        action = action.getNextAction();
        assertEquals( ScreenshotAction.class, action.getClass() );
        assertNull( action.getNextAction() );
    }

    @Test
    void testInitiationError() throws ParseException {
        Path path = Paths.get( "src", "test", "resources", "parsers", "testFileParser", "initiationError.t2s");
        Throwable exception = assertThrows( ParseException.class,
                () -> TestFileParser.parse(testScenario, path ) );
        assertEquals( "Error Parsing file '" + path.toAbsolutePath().toString() + "' at line 3",
                exception.getMessage() );
    }

}
