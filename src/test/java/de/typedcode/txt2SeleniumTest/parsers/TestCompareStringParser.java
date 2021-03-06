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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.typedcode.txt2selenium.exceptions.ParseException;
import de.typedcode.txt2selenium.parsers.CompareStringParser;

class TestCompareStringParser {

    @Test
    void testEmptyFile() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "empty.t2s" ) );

        assertEquals( 0, parse.size() );
    }

    @Test
    void testSingleEntry() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "singleEntry.t2s" ) );

        assertEquals( 1, parse.size() );
        assertTrue( parse.containsKey( "key" ) );
        assertEquals( "value", parse.get( "key" ) );
    }

    @Test
    void testWithEmptyLines() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "withEmptyLines.t2s" ) );

        assertEquals( 2, parse.size() );

        assertTrue( parse.containsKey( "key" ) );
        assertEquals( "value", parse.get( "key" ) );

        assertTrue( parse.containsKey( "key2" ) );
        assertEquals( "value2", parse.get( "key2" ) );
    }

    @Test
    void testContentWithSpaces() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "contentWithSpaces.t2s" ) );

        assertEquals( 2, parse.size() );

        assertTrue( parse.containsKey( "key" ) );
        assertEquals( "hier ein Wert", parse.get( "key" ) );

        assertTrue( parse.containsKey( "secondKey" ) );
        assertEquals( "another Value", parse.get( "secondKey" ) );
    }

    @Test
    void testDoubleKey() {
        Throwable exception = assertThrows( ParseException.class, () -> CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "duplicatedKey.t2s" ) ) );

        assertEquals( "Comparestrings contain a doubled key entry at line 3", exception.getMessage() );
    }

}
