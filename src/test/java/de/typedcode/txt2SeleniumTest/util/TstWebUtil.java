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

package de.typedcode.txt2SeleniumTest.util;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.actions.ReadAction;
import de.typedcode.txt2Selenium.actions.SelectAction;
import de.typedcode.txt2Selenium.util.ByInitializer;
import de.typedcode.txt2Selenium.util.ByType;
import de.typedcode.txt2Selenium.util.WebUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TstWebUtil {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @BeforeEach
    public void before() {
        WebUtil.reset();
        Path path = Paths.get( "src", "test", "resources", "util", "webUtil", "webUtil.html" );
        WebUtil.getInstance().openUrl( path.toUri().toString() );
    }

    @Test
    public void testSelect() {
        assertNull( WebUtil.getInstance().getSelectedElement() );
        assertNull( WebUtil.getInstance().getSelectedBy() );

        By selectBy = By.id( "utilContent" );

        WebUtil.getInstance().select( selectBy );

        assertEquals( selectBy, WebUtil.getInstance().getSelectedBy() );
        assertEquals( "<p id=\"utilContent\">", WebUtil.getInstance().getSelectedElement().toString() );
    }

    @Test
    public void testRead() {
        assertNull( WebUtil.getInstance().getReadVar( "myVar" ) );

        WebUtil.getInstance().select( By.id( "utilContent" ) );
        WebUtil.getInstance().readText( "myVar" );

        assertEquals( "Content", WebUtil.getInstance().getReadVar( "myVar" ) );
    }

    @Test
    public void resetTest() {
        assertNull( WebUtil.getInstance().getReadVar( "myRead" ) );
        assertNull( WebUtil.getInstance().getSelectedElement() );
        assertNull( WebUtil.getInstance().getSelectedBy() );

        ActionFactory.createAction( this.txt2SeleniumMock, SelectAction.IDENTIFIER, "id utilContent" ).execute();
        ActionFactory.createAction( this.txt2SeleniumMock, ReadAction.IDENTIFIER, "myRead" ).execute();

        assertNotNull( WebUtil.getInstance().getReadVar( "myRead" ) );
        assertNotNull( WebUtil.getInstance().getSelectedElement() );
        assertNotNull( WebUtil.getInstance().getSelectedBy() );

        WebUtil.reset();

        assertNull( WebUtil.getInstance().getReadVar( "myRead" ) );
        assertNull( WebUtil.getInstance().getSelectedElement() );
        assertNull( WebUtil.getInstance().getSelectedBy() );
    }
}
