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

package de.typedcode.txt2SeleniumTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import de.typedcode.txt2Selenium.Txt2Selenium;

public class TstTxt2Selenium {

    @Test
    public void initiationNonExistingDirectory() {
        @SuppressWarnings( "null" )
        Throwable exception = assertThrows( RuntimeException.class,
                () -> new Txt2Selenium( Paths.get( "test/notExisting/" ) ) );
        assertEquals( "Given directory does not exist: test/notExisting", exception.getMessage() );

    }

    @Test
    public void initiationDirectoryIsFile() {
        @SuppressWarnings( "null" )
        Throwable exception = assertThrows( RuntimeException.class, () -> new Txt2Selenium(
                Paths.get( "test/testFiles/Txt2Selenium/initiation/files/globalOneTestfile/global.t2s" ) ) );
        assertEquals(
                "Given Path is not a directory: test/testFiles/Txt2Selenium/initiation/files/globalOneTestfile/global.t2s",
                exception.getMessage() );
    }

    @Test
    public void initiationEmptyFolder() {
        @SuppressWarnings( "null" )
        Throwable exception = assertThrows( RuntimeException.class,
                () -> new Txt2Selenium( Paths.get( "test/testFiles/Txt2Selenium/initiation/empty" ) ) );
        assertEquals( "Given directory does not contain any testfiles: test/testFiles/Txt2Selenium/initiation/empty",
                exception.getMessage() );
    }
}
