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

import de.typedcode.txt2Selenium.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TestFileUtil {

    @Test
    void getTestFilesOnEmptyDirectory(){
        Path path = Paths.get("src", "test", "resources", "util", "fileUtil", "empty");

        List<Path> testFiles = FileUtil.getTestFiles(path);

        assertEquals( 0, testFiles.size() );
    }

    @Test
    void getTestFilesOnly() {

        Path path = Paths.get("src", "test", "resources", "util", "fileUtil", "filesOnly" );

        Path first = Paths.get("src", "test", "resources", "util", "fileUtil", "filesOnly", "first.t2s" );
        Path second = Paths.get("src", "test", "resources", "util", "fileUtil", "filesOnly", "second.t2s" );
        Path third = Paths.get("src", "test", "resources", "util", "fileUtil", "filesOnly", "third.t2s" );

        List<Path> testFiles = FileUtil.getTestFiles(path);

        assertEquals( 3, testFiles.size() );

        assertTrue( testFiles.contains( first ) );
        assertTrue( testFiles.contains( second ) );
        assertTrue( testFiles.contains( third ) );

        testFiles.forEach( o -> assertTrue( Files.exists( o ) ) );
        testFiles.forEach( o -> assertTrue( Files.isRegularFile( o ) ) );
    }

    @Test
    void getTestFilesNoFiles() {
        Path path = Paths.get("src", "test", "resources", "util", "fileUtil", "directoriesOnly" );

        List<Path> testFiles = FileUtil.getTestFiles(path);

        assertEquals( 0, testFiles.size() );
    }

    @Test
    void getTestsCheckEverything() {
        Path path = Paths.get("src", "test", "resources", "util", "fileUtil", "everything" );

        //Files that are in everything
        Path firstFile = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "first.t2s" );
        Path secondFile = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "second.t2s" );
        Path thirdFile = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "third.t2s" );

        //Directories that are in everything
        Path firstDirectory = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "first" );
        Path secondDirectory = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "second" );
        Path thirdDirectory = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "third" );
        Path confuseDirectory = Paths.get("src", "test", "resources", "util", "fileUtil", "everything", "confuse.t2s" );

        List<Path> testFiles = FileUtil.getTestFiles(path);

        //Checking all the Files
        assertEquals( 3, testFiles.size() );

        assertTrue( testFiles.contains( firstFile ) );
        assertTrue( testFiles.contains( secondFile ) );
        assertTrue( testFiles.contains( thirdFile ) );

        testFiles.forEach( o -> assertTrue( Files.exists( o ) ) );
        testFiles.forEach( o -> assertTrue( Files.isRegularFile( o ) ) );

        List<Path> testDirectories = FileUtil.getTestDirectories(path);

        assertEquals( 4, testDirectories.size() );

        assertTrue( testDirectories.contains( firstDirectory ) );
        assertTrue( testDirectories.contains( secondDirectory ) );
        assertTrue( testDirectories.contains( thirdDirectory ) );
        assertTrue( testDirectories.contains( confuseDirectory ) );

        testDirectories.forEach( o -> assertTrue( Files.exists( o ) ) );
        testDirectories.forEach( o -> assertTrue( Files.isDirectory( o ) ) );
    }

    @Test
    void getDirectoriesNoDirectories() {
        Path path = Paths.get("src", "test", "resources", "util", "fileUtil", "empty" );

        List<Path> testDirectories = FileUtil.getTestDirectories( path );

        assertEquals( 0, testDirectories.size() );
    }

    @Test
    void getDirectories() {
        Path path = Paths.get("src", "test", "resources", "util", "fileUtil", "directoriesOnly" );

        Path first = Paths.get("src", "test", "resources", "util", "fileUtil", "directoriesOnly", "first" );
        Path second = Paths.get("src", "test", "resources", "util", "fileUtil", "directoriesOnly", "second" );
        Path third = Paths.get("src", "test", "resources", "util", "fileUtil", "directoriesOnly", "third" );

        List<Path> testFiles = FileUtil.getTestDirectories( path );

        assertEquals( 3, testFiles.size() );

        assertTrue( testFiles.contains( first ) );
        assertTrue( testFiles.contains( second ) );
        assertTrue( testFiles.contains( third ) );

        testFiles.forEach( o -> assertTrue( Files.exists( o ) ) );
        testFiles.forEach( o -> assertTrue( Files.isDirectory( o ) ) );
    }
}
