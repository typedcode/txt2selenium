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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ClickAction;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.WebUtil;

public class TstClickAction {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    public void before() {
        WebUtil.WEB_UTIL.reset();
    }

    @Test
    void clickById() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/clickAction/beforeClick.html" );
        @SuppressWarnings( "null" )
        OpenAction openAction = new OpenAction( txt2SeleniumMock, fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.WEB_UTIL.getTitle() );

        ClickAction action = new ClickAction( txt2SeleniumMock, "id clickActionId" );
        action.execute();

        assertEquals( "After Click Id", WebUtil.WEB_UTIL.getTitle() );
    }

    @Test
    void clickByName() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/clickAction/beforeClick.html" );
        @SuppressWarnings( "null" )
        OpenAction openAction = new OpenAction( txt2SeleniumMock, fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.WEB_UTIL.getTitle() );

        ClickAction action = new ClickAction( txt2SeleniumMock, "name clickActionName" );
        action.execute();

        assertEquals( "After Click Name", WebUtil.WEB_UTIL.getTitle() );
    }

    @Test
    void clickByXPath() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/clickAction/beforeClick.html" );
        @SuppressWarnings( "null" )
        OpenAction openAction = new OpenAction( txt2SeleniumMock, fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.WEB_UTIL.getTitle() );

        ClickAction action = new ClickAction( txt2SeleniumMock, "xpath /html/body/div/p/a[3]" );
        action.execute();

        assertEquals( "After Click XPath", WebUtil.WEB_UTIL.getTitle() );
    }

    @Test
    void clickByXPath2() throws ActionInitiationException {
        Path fileToOpen = Paths.get( "test/testFiles/actions/clickAction/beforeClick.html" );
        @SuppressWarnings( "null" )
        OpenAction openAction = new OpenAction( txt2SeleniumMock, fileToOpen.toUri().toString() );
        openAction.execute();

        assertEquals( "Before Click", WebUtil.WEB_UTIL.getTitle() );

        ClickAction action = new ClickAction( txt2SeleniumMock, "xpath //*[contains(text(),'with spaces')]" );
        action.execute();

        assertEquals( "After Click XPath 2", WebUtil.WEB_UTIL.getTitle() );
    }
}
