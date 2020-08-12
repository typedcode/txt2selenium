/*
 * MIT License
 * 
 * Copyright (c) 2020 Markus Hoffmann (www.typedcode.de)
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

import de.typedcode.txt2selenium.util.ByType;
import de.typedcode.txt2selenium.util.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestPathFactory {

    @Test
    void testIncrementFilename() throws Exception {
        Path tempFile = Files.createTempFile( "fileName", ".html" );

        String fileName = tempFile.getFileName().toString();
        String extractedFileName = fileName.substring( 0, fileName.length() - 5 );

        Path result = PathFactory.get( tempFile.getParent(), extractedFileName, ".html" );

        Path expectedResult = Paths.get( tempFile.getParent().toString(), extractedFileName + "_1.html" );

        assertEquals( expectedResult, result );
    }

    @Test
    void testMultipleIncrementFilename() throws Exception {
        Path tempFile = Files.createTempFile( "fileName", ".html" );

        String fileName = tempFile.getFileName().toString();
        String extractedFileName = fileName.substring( 0, fileName.length() - 5 );

        Path result = PathFactory.get( tempFile.getParent(), extractedFileName, ".html" );
        Files.createFile( result );

        Path expectedResult = Paths.get( tempFile.getParent().toString(), extractedFileName + "_1.html" );

        assertEquals( expectedResult, result );

        result = PathFactory.get( tempFile.getParent(), extractedFileName, ".html" );
        Files.createFile( result );
        expectedResult = Paths.get( tempFile.getParent().toString(), extractedFileName + "_2.html" );

        assertEquals( expectedResult, result );

        result = PathFactory.get( tempFile.getParent(), extractedFileName, ".html" );
        expectedResult = Paths.get( tempFile.getParent().toString(), extractedFileName + "_3.html" );

        assertEquals( expectedResult, result );
    }

    @Test
    void testGet() {
        Path result = PathFactory.get( Paths.get( "." ), "fileName", ".html" );

        Path expectedResult = Paths.get( ".", "fileName.html" );

        assertEquals( expectedResult, result );
    }

}
