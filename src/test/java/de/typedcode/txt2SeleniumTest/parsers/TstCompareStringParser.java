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

package de.typedcode.txt2SeleniumTest.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.typedcode.txt2Selenium.exceptions.ParseException;
import de.typedcode.txt2Selenium.parsers.CompareStringParser;

@SuppressWarnings( "null" )
public class TstCompareStringParser {

    @Test
    public void emptyFile() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "empty.t2s" ) );

        assertEquals( 0, parse.size() );
    }

    @Test
    public void singleEntry() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "singleEntry.t2s" ) );

        assertEquals( 1, parse.size() );
        assertTrue( parse.containsKey( "key" ) );
        assertEquals( "value", parse.get( "key" ) );
    }

    @Test
    public void withEmptyLines() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "withEmptyLines.t2s" ) );

        assertEquals( 2, parse.size() );

        assertTrue( parse.containsKey( "key" ) );
        assertEquals( "value", parse.get( "key" ) );

        assertTrue( parse.containsKey( "key2" ) );
        assertEquals( "value2", parse.get( "key2" ) );
    }

    @Test
    public void contentWithSpaces() throws ParseException {
        Map< String, String > parse = CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "contentWithSpaces.t2s" ) );

        assertEquals( 2, parse.size() );

        assertTrue( parse.containsKey( "key" ) );
        assertEquals( "hier ein Wert", parse.get( "key" ) );

        assertTrue( parse.containsKey( "secondKey" ) );
        assertEquals( "another Value", parse.get( "secondKey" ) );
    }

    @Test
    public void doubleKey() {
        @SuppressWarnings( "null" )
        Throwable exception = assertThrows( ParseException.class, () -> CompareStringParser
                .parse( Paths.get( "src", "test", "resources", "parsers", "compareStrings", "duplicatedKey.t2s" ) ) );

        assertEquals( "Comparestrings contain a doubled key entry at line 3", exception.getMessage() );
    }

}
