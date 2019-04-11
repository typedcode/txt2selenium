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

package de.typedcode.txt2SeleniumTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import de.typedcode.txt2Selenium.Txt2Selenium;

import static org.junit.jupiter.api.Assertions.*;

class TestTxt2Selenium {

    @Test
    void testInitiationNonExistingDirectory() {
        Path path = Paths.get( "test", "notExisting" );

        Throwable exception = assertThrows( RuntimeException.class,
                () -> new Txt2Selenium( path ) );
        assertEquals( String.format( "Given directory does not exist: %s", path.toString() ), exception.getMessage() );

    }

    @Test
    void testInitiationDirectoryIsFile() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "files", "globalOneTestfile", "global.t2s" );

        Throwable exception = assertThrows( RuntimeException.class, () -> new Txt2Selenium( path ) );
        assertEquals(
                String.format( "Given Path is not a directory: %s", path.toString() ), exception.getMessage() );
    }

    @Test
    void testInitiationEmptyFolder() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "empty" );

        Throwable exception = assertThrows( RuntimeException.class,
                () -> new Txt2Selenium( path ) );
        assertEquals( String.format( "Given directory does not contain any testfiles: %s", path.toString() ),
                exception.getMessage() );
    }

    @Test
    void testResolvingTestFiles() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving" );
        Path first = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving", "tests", "first.t2s" );
        Path second = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving", "tests", "second.t2s" );

        Txt2Selenium instance = new Txt2Selenium( path );

        assertEquals(2, instance.getParsedTests().size() );
        assertTrue( instance.getParsedTests().containsKey( first ) );
        assertTrue( instance.getParsedTests().containsKey( second ) );
    }

    @Test
    void testResolvingMethodsFiles() {
        Path path = Paths.get( "src", "test", "resources", "Txt2Selenium", "initiation", "testResolving" );

        Txt2Selenium instance = new Txt2Selenium( path );

        assertNotNull( instance.getMethod( "firstMethod" ) );
        assertNotNull( instance.getMethod( "secondMethod" ) );
        assertNull( instance.getMethod( "someUnknownMethod" ) );
    }
}
