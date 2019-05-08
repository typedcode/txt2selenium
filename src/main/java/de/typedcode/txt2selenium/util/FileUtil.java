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

package de.typedcode.txt2selenium.util;

import de.typedcode.txt2selenium.Txt2Selenium;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    /**
     * No insatnce allowed
     */
    private FileUtil() {

    }

    /**
     * Searches the given Path for test-files and returns them.
     * @param path Path to search for test-files
     * @return List of Paths containing tests.
     */
    public static List<Path > getTestFiles( Path path ) {
        List<Path > files = new ArrayList<>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*" + Txt2Selenium.FILE_EXTENSION );

        try ( Stream< Path > filesStream = Files.walk( path, 1 ) ) {
            files = filesStream
                    .filter( Files::isRegularFile )
                    .filter( o -> matcher.matches( o.getFileName() ) )
                    .collect(Collectors.toList());

        } catch( IOException e ) {
            UnitLogger.logSevere( String.format( "Error getting Test Files for %s", path ), e );
        }

        return files;
    }

    /**
     * Searches the given Path for directories and returns them.
     * @param path Path to search for directories
     * @return List of directory Paths.
     */
    public static List<Path> getTestDirectories( Path path ) {
        List<Path> result = new ArrayList<>();

        try ( Stream< Path > filesStream = Files.walk( path, 1 ) ) {
            result = filesStream
                    .filter( Files::isDirectory )
                    .filter( o -> !o.equals( path ) )  //Ignore the given path
                    .collect( Collectors.toList() );
        } catch( IOException e ) {
            UnitLogger.logSevere( String.format( "Error getting Test Directories for %s", path ), e );
        }

        return result;
    }
}
