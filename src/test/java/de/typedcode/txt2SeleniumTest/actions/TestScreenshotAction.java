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

import de.typedcode.txt2selenium.actions.AAction;
import de.typedcode.txt2selenium.actions.ActionFactory;
import de.typedcode.txt2selenium.actions.OpenAction;
import de.typedcode.txt2selenium.actions.ScreenshotAction;
import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

class TestScreenshotAction {

    private TestScenario testScenario;
    private Configuration configuration;

    /**
     * Resetting the WebUtil and creating a new Txt2Selenium Mock
     */
    @BeforeEach
    void prepare() throws Exception {
        WebUtil.reset();
        this.testScenario = Mockito.mock( TestScenario.class );
        this.configuration = Mockito.mock( Configuration.class );

        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, this.configuration );
    }

    @AfterAll
    static void cleanUp() throws Exception {
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }

    /**
     * When the Action was not yet performed, the screenshot file has to be null.
     */
    @Test
    void testNullResultWhenNotExecuted() throws ActionInitiationException {
        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction(testScenario,
                ScreenshotAction.IDENTIFIER, "" );
        assertNull( screenshotAction.getScreenshotFile() );
    }

    @Test
    void testScreenshotWithNoOpenActionBefore() throws IOException, ActionInitiationException {
        prepareMock( Paths.get( "src", "test", "resources", "actions", "screenshotAction" ).toAbsolutePath() );

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction(testScenario,
                ScreenshotAction.IDENTIFIER, "" );
        screenshotAction.execute();

        Path screenshot = screenshotAction.getScreenshotFile();

        String actualContent = new String( Files.readAllBytes( screenshot ) );

        String expectedContent = new String( Files
                .readAllBytes( Paths.get( "src", "test", "resources", "actions", "screenshotAction", "resultEmptyScreenshot.html" ) ) );

        Files.delete( screenshot );

        assertThat( actualContent, isIdenticalTo( expectedContent ).ignoreWhitespace() );

    }

    @Test
    void testScreenshotAfterOpen() throws IOException, ActionInitiationException {
        prepareMock( Paths.get( "src", "test", "resources", "actions", "screenshotAction" ).toAbsolutePath() );

        Path fileToOpen = Paths.get( "src", "test", "resources", "actions", "screenshotAction", "siteWithContent.html" );

        OpenAction openAction = ( OpenAction ) ActionFactory.createAction(testScenario, OpenAction.IDENTIFIER,
                fileToOpen.toUri().toString() );
        openAction.execute();

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction(testScenario,
                ScreenshotAction.IDENTIFIER, "" );
        screenshotAction.execute();

        Path screenshot = screenshotAction.getScreenshotFile();

        String actualContent = new String( Files.readAllBytes( screenshot ) );

        String expectedContent = new String(
                Files.readAllBytes( Paths.get( "src", "test", "resources", "actions", "screenshotAction", "siteWithContent.html" ) ) );

        Files.delete( screenshot );

        assertThat( actualContent, isIdenticalTo( expectedContent ).ignoreWhitespace() );
    }

    @Test
    void testScreenshotWithIdentifier() throws IOException, ActionInitiationException {
        prepareMock( Paths.get( "src", "test", "resources", "actions", "screenshotAction" ).toAbsolutePath() );

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction(testScenario,
                ScreenshotAction.IDENTIFIER, "testIdentifier" );
        screenshotAction.execute();

        Path screenshotFile = screenshotAction.getScreenshotFile();

        assertNotNull( screenshotFile );

        String screenshotFileName = screenshotFile.getFileName().toString();
        Files.delete( screenshotFile );
        // [0-9]{14} => 14 because there is a timestamp at the end of the Filename
        // (yyyyMMddHHmmss)
        assertTrue( screenshotFileName.matches( "screenshot_testIdentifier_[0-9]{14}\\.html" ) );
    }

    @Test
    void testScreenshotWithoutIdentifier() throws IOException, ActionInitiationException {
        prepareMock( Paths.get( "src", "test", "resources", "actions", "screenshotAction" ).toAbsolutePath() );

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction(testScenario,
                ScreenshotAction.IDENTIFIER, "" );
        screenshotAction.execute();

        Path screenshotFile = screenshotAction.getScreenshotFile();

        assertNotNull( screenshotFile );

        String screenshotFileName = screenshotFile.getFileName().toString();
        Files.delete( screenshotFile );
        // [0-9]{14} => 14 because there is a timestamp at the end of the Filename
        // (yyyyMMddHHmmss)
        assertTrue( screenshotFileName.matches( "screenshot_[0-9]{14}\\.html" ) );
    }

    @Test
    void testGetCommandWithoudIdentifier() {
        AAction action = ActionFactory.createAction(testScenario, ScreenshotAction.IDENTIFIER, "" );

        assertEquals( ScreenshotAction.IDENTIFIER, action.getCommand() );
    }

    @Test
    void testGetCommandWithIdentifier() {
        AAction action = ActionFactory.createAction(testScenario, ScreenshotAction.IDENTIFIER, "testIdentifier" );

        assertEquals( String.format( "%s testIdentifier", ScreenshotAction.IDENTIFIER ), action.getCommand() );
    }

    @Test
    void testScreenshotLogging() throws IOException {
        prepareMock( Paths.get( "src", "test", "resources", "actions", "screenshotAction" ).toAbsolutePath() );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        ScreenshotAction action = ( ScreenshotAction )ActionFactory.createAction(testScenario, ScreenshotAction.IDENTIFIER, "" );
        action.execute();

        Path file = action.getScreenshotFile();
        Files.delete( file );

        List<LogRecord > records = handler.getLogRecords();

        assertEquals( 2, records.size() );
        assertEquals( Level.INFO, records.get( 0 ).getLevel() );
        assertEquals( Level.INFO, records.get( 1 ).getLevel() );
        assertEquals( ScreenshotAction.IDENTIFIER, records.get( 0 ).getMessage() );
        assertTrue( records.get( 1 ).getMessage().matches( "Saved screenshot to screenshot_[0-9]{14}\\.html" ) );
    }

    private void prepareMock( Path pathToMock ) {
        Mockito.when( this.configuration.getMainDirectory() )
                .thenReturn( Optional.of( pathToMock ) );
    }
}
