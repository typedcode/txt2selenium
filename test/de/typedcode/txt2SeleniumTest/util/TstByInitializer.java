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

package de.typedcode.txt2SeleniumTest.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;

import de.typedcode.txt2Selenium.util.ByInitializer;
import de.typedcode.txt2Selenium.util.ByType;

public class TstByInitializer {

    @Test
    public void initializeWithId() {
        String id = "MyId";

        By initialize = ByInitializer.initialize( ByType.ID, id );

        assertEquals( ById.class, initialize.getClass() );

        ById byIdItem = ( ById ) initialize;

        assertThat( byIdItem.toString(), containsString( id ) );
    }

    @Test
    public void initializeWithName() {
        String name = "MyName";

        By initialize = ByInitializer.initialize( ByType.NAME, name );

        assertEquals( ByName.class, initialize.getClass() );

        ByName byNameItem = ( ByName ) initialize;

        assertThat( byNameItem.toString(), containsString( name ) );
    }

    @Test
    public void initializeWithXpath() {
        String xpath = "MyName";

        By initialize = ByInitializer.initialize( ByType.XPATH, xpath );

        assertEquals( ByXPath.class, initialize.getClass() );

        ByXPath byXpathItem = ( ByXPath ) initialize;

        assertThat( byXpathItem.toString(), containsString( xpath ) );
    }
}
