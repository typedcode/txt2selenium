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

package de.typedcode.txt2SeleniumTest.util.repositories;

import de.typedcode.txt2selenium.executionContext.Method;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.repositories.MethodRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TestMethodRepository {

    private TestScenario testScenario;
    private Configuration configuration;

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException {
        this.testScenario = Mockito.mock( TestScenario.class );
        configuration = Mockito.mock( Configuration.class );
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, configuration );
    }

    @AfterEach
    void afterEach() throws Exception {
        Field instance = MethodRepository.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }

    @AfterAll
    static void cleanup() throws Exception {
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }


    @Test
    void testNoMethods() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "util", "repositories", "methodRepository", "emptyScenario" );

        Mockito.when( this.testScenario.getPath() ).thenReturn( scenarioPath );
        MethodRepository.getInstance().registerScenario( this.testScenario );

        assertTrue( MethodRepository.getInstance().getMethodsForScenario( this.testScenario ).get().isEmpty() );
    }

    @Test
    void testFindingMethods() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "util", "repositories", "methodRepository", "findingMethods" );

        Mockito.when( this.testScenario.getPath() ).thenReturn( scenarioPath );
        MethodRepository.getInstance().registerScenario( this.testScenario );

        assertTrue( MethodRepository.getInstance().getMethod( this.testScenario, "method1" ).isPresent() );
        assertTrue( MethodRepository.getInstance().getMethod( this.testScenario, "method2" ).isPresent() );
        assertTrue( MethodRepository.getInstance().getMethod( this.testScenario, "method3" ).isPresent() );
        assertEquals( 3, MethodRepository.getInstance().getMethodsForScenario( this.testScenario ).get().size() );
    }

    @Test
    void testMethodOverriding() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "util", "repositories", "methodRepository", "methodOverriding" );

        TestScenario tsc = new TestScenario( null, scenarioPath );
        TestScenario subScenario = tsc.getSubScenarios().get( 0 );

        Method method1 = MethodRepository.getInstance().getMethod( tsc, "method1").get();
        Method method1Sub = MethodRepository.getInstance().getMethod( subScenario, "method1" ).get();

        assertEquals( tsc, method1.getTestScenario() );
        assertEquals( subScenario, method1Sub.getTestScenario() );
        assertTrue( MethodRepository.getInstance().getMethod( subScenario, "method3").isPresent() );

        assertNotEquals( method1, method1Sub );
    }
}
