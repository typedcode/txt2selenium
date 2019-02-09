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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.actions.ScreenshotAction;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

public class TstScreenshotAction {

    Txt2Selenium txt2SeleniumMock;

    /**
     * Resetting the WebUtil and creating a new Txt2Selenium Mock
     */
    @BeforeEach
    void prepare() {
        WebUtil.WEB_UTIL.reset();
        this.txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );
    }

    /**
     * When the Action was not yet performed, the screenshot file has to be null.
     */
    @Test
    void nullResultWhenNotExecuted() throws ActionInitiationException {
        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction( txt2SeleniumMock,
                "screenshot", "" );
        assertNull( screenshotAction.getScreenshotFile() );
    }

    @Test
    void screenshotWithNoOpenActionBefore() throws IOException, ActionInitiationException {
        Mockito.when( txt2SeleniumMock.getMainDirectory() )
                .thenReturn( Paths.get( "test/testFiles/actions/screenshotAction/" ).toAbsolutePath() );

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction( txt2SeleniumMock,
                "screenshot", "" );
        screenshotAction.execute();

        Path screenshot = screenshotAction.getScreenshotFile();

        String actualContent = new String( Files.readAllBytes( screenshot ) );

        String expectedContent = new String( Files
                .readAllBytes( Paths.get( "test/testFiles/actions/screenshotAction/resultEmptyScreenshot.html" ) ) );

        Files.delete( screenshot );

        assertThat( actualContent, isIdenticalTo( expectedContent ).ignoreWhitespace() );

    }

    @Test
    void screenshotAfterOpen() throws IOException, ActionInitiationException {
        Mockito.when( txt2SeleniumMock.getMainDirectory() )
                .thenReturn( Paths.get( "test/testFiles/actions/screenshotAction/" ).toAbsolutePath() );

        Path fileToOpen = Paths.get( "test/testFiles/actions/screenshotAction/siteWithContent.html" );

        OpenAction openAction = ( OpenAction ) ActionFactory.createAction( txt2SeleniumMock, "open",
                fileToOpen.toUri().toString() );
        openAction.execute();

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction( txt2SeleniumMock,
                "screenshot", "" );
        screenshotAction.execute();

        Path screenshot = screenshotAction.getScreenshotFile();

        String actualContent = new String( Files.readAllBytes( screenshot ) );

        String expectedContent = new String(
                Files.readAllBytes( Paths.get( "test/testFiles/actions/screenshotAction/siteWithContent.html" ) ) );

        Files.delete( screenshot );

        assertThat( actualContent, isIdenticalTo( expectedContent ).ignoreWhitespace() );
    }

    @Test
    void screenshotWithIdentifier() throws IOException, ActionInitiationException {
        Mockito.when( txt2SeleniumMock.getMainDirectory() )
                .thenReturn( Paths.get( "test/testFiles/actions/screenshotAction/" ).toAbsolutePath() );

        ScreenshotAction screenshotAction = ( ScreenshotAction ) ActionFactory.createAction( txt2SeleniumMock,
                "screenshot", "testIdentifier" );
        screenshotAction.execute();

        Path screenshotFile = screenshotAction.getScreenshotFile();

        if( screenshotFile != null ) {
            String screenshotFileName = screenshotFile.getFileName().toString();
            Files.delete( screenshotFile );
            // [0-9]{14} => 14 because there is a timestamp at the end of the Filename
            // (yyyyMMddHHmmss)
            assertTrue( screenshotFileName.matches( "screenshot_testIdentifier_[0-9]{14}\\.html" ) );
        }
    }
}
