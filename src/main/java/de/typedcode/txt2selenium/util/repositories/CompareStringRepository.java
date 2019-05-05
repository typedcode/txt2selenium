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

package de.typedcode.txt2selenium.util.repositories;

import de.typedcode.txt2selenium.Txt2Selenium;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.parsers.CompareStringParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompareStringRepository {

    private static CompareStringRepository instance;

    private Map< TestScenario, Map<String, String>> compareStrings = new HashMap<>();

    private CompareStringRepository() {
        //Do not instantiate
    }

    public static CompareStringRepository getInstance() {
        if( instance == null ) {
            instance =  new CompareStringRepository();
        }
        return instance;
    }

    /**
     * Computes the compare Strings for this Scenario
     */
    public void registerScenario( TestScenario scenario ) {
        Path path = Paths.get( scenario.getPath().toString(), Txt2Selenium.COMPARE_STRINGS_FILE_NAME );

        Map<String, String> scenarioStrings = new HashMap<>();

        if( Files.exists( path) ) {
            scenarioStrings = CompareStringParser.parse( path );
        }

        compareStrings.put( scenario, scenarioStrings );
    }

    public Optional< String > getCompareString( TestScenario scenario, String key ) {
        String compareString = compareStrings.get( scenario ).get( key );

        if(compareString != null) {
            return Optional.of( compareString );
        }
        else if ( scenario.parentScenario != null ) {
            return getCompareString( scenario.parentScenario, key );
        }
        else {
            return Optional.empty();
        }
    }

    public Map< String, String > getCompareStringsForScenario( TestScenario tsc ) {
        return compareStrings.get( tsc );
    }

    /**
     * Clears the Repository
     */
    public void cleanRepository() {
        this.compareStrings.clear();
    }
}
