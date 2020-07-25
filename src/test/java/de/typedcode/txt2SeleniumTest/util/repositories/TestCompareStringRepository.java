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

package de.typedcode.txt2SeleniumTest.util.repositories;

import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.repositories.CompareStringRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCompareStringRepository {

    private TestScenario testScenario;

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException {
        this.testScenario = Mockito.mock( TestScenario.class );
        Configuration configuration = Mockito.mock(Configuration.class);
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, configuration);
    }

    @AfterEach
    void after() {
        CompareStringRepository.getInstance().cleanRepository();
    }

    @AfterAll
    static void cleanup() throws Exception {
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }


    @Test
    void testNoCompareStrings() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "util", "repositories", "compareStringRepository", "emptyScenario" );

        Mockito.when( this.testScenario.getPath() ).thenReturn( scenarioPath );
        CompareStringRepository.getInstance().registerScenario( this.testScenario );

        assertTrue( CompareStringRepository.getInstance().getCompareStringsForScenario( this.testScenario ).isEmpty() );
    }

    @Test
    void testCompareStringsScenarioOnly() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "util", "repositories", "compareStringRepository", "scenarioOnly" );

        Mockito.when( this.testScenario.getPath() ).thenReturn( scenarioPath );
        CompareStringRepository.getInstance().registerScenario( this.testScenario );

        assertEquals( "one",  CompareStringRepository.getInstance().getCompareString( this.testScenario, "first" ).get() );
        assertEquals( "two", CompareStringRepository.getInstance().getCompareString( this.testScenario, "second" ).get() );
        assertEquals( "three", CompareStringRepository.getInstance().getCompareString( this.testScenario, "third" ).get() );
        assertEquals( 3, CompareStringRepository.getInstance().getCompareStringsForScenario( this.testScenario ).size() );
    }

    @Test
    void testAdditionalCompareStrings() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "util", "repositories", "compareStringRepository", "additionalCompareStrings" );

        TestScenario tsc = new TestScenario( this.testScenario, scenarioPath );

        assertEquals( "one",  CompareStringRepository.getInstance().getCompareString( tsc, "first" ).get() );
        assertEquals( "two", CompareStringRepository.getInstance().getCompareString( tsc, "second" ).get() );
        assertEquals( "three", CompareStringRepository.getInstance().getCompareString( tsc, "third" ).get() );
        assertEquals( 3, CompareStringRepository.getInstance().getCompareStringsForScenario( tsc ).size() );

        //Checking the SubScenario
        tsc = tsc.getSubScenarios().get( 0 );
        assertEquals( "one",  CompareStringRepository.getInstance().getCompareString( tsc, "first" ).get() );
        assertEquals( "overwrite", CompareStringRepository.getInstance().getCompareString( tsc, "second" ).get() );
        assertEquals( "three", CompareStringRepository.getInstance().getCompareString( tsc, "third" ).get() );
        assertEquals( "additional", CompareStringRepository.getInstance().getCompareString( tsc, "fourth" ).get() );
        assertEquals( 2, CompareStringRepository.getInstance().getCompareStringsForScenario( tsc ).size() );
    }
}
