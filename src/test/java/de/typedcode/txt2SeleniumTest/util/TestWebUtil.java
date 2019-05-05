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

import de.typedcode.txt2selenium.actions.ActionFactory;
import de.typedcode.txt2selenium.actions.ReadAction;
import de.typedcode.txt2selenium.actions.SelectAction;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.WebUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TestWebUtil {

    private TestScenario testScenario = Mockito.mock( TestScenario.class );

    @BeforeEach
    void before() {
        WebUtil.reset();
        Path path = Paths.get( "src", "test", "resources", "util", "webUtil", "webUtil.html" );
        WebUtil.getInstance().openUrl( path.toUri().toString() );
    }

    @Test
    void testSelect() {
        assertTrue( WebUtil.getInstance().getSelectedElement().isEmpty() );
        assertTrue( WebUtil.getInstance().getSelectedBy().isEmpty() );

        By selectBy = By.id( "utilContent" );

        WebUtil.getInstance().select( selectBy );

        assertEquals( selectBy, WebUtil.getInstance().getSelectedBy().get() );
        assertEquals( "<p id=\"utilContent\">", WebUtil.getInstance().getSelectedElement().get().toString() );
    }

    @Test
    void testRead() {
        assertTrue( WebUtil.getInstance().getReadVar( "myVar" ).isEmpty() );

        WebUtil.getInstance().select( By.id( "utilContent" ) );
        WebUtil.getInstance().readText( "myVar" );

        assertEquals( "Content", WebUtil.getInstance().getReadVar( "myVar" ).get() );
    }

    @Test
    void testReset() {
        assertTrue( WebUtil.getInstance().getReadVar( "myRead" ).isEmpty() );
        assertTrue( WebUtil.getInstance().getSelectedElement() .isEmpty() );
        assertTrue( WebUtil.getInstance().getSelectedBy().isEmpty() );

        ActionFactory.createAction( this.testScenario, SelectAction.IDENTIFIER, "id utilContent" ).execute();
        ActionFactory.createAction( this.testScenario, ReadAction.IDENTIFIER, "myRead" ).execute();

        assertTrue( WebUtil.getInstance().getReadVar( "myRead" ).isPresent() );
        assertTrue( WebUtil.getInstance().getSelectedElement().isPresent() );
        assertTrue( WebUtil.getInstance().getSelectedBy().isPresent() );

        WebUtil.reset();

        assertTrue( WebUtil.getInstance().getReadVar( "myRead" ).isEmpty() );
        assertTrue( WebUtil.getInstance().getSelectedElement().isEmpty() );
        assertTrue( WebUtil.getInstance().getSelectedBy().isEmpty() );
    }
}
