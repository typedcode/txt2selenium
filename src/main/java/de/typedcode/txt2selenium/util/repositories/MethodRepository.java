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

package de.typedcode.txt2selenium.util.repositories;

import de.typedcode.txt2selenium.Txt2Selenium;
import de.typedcode.txt2selenium.executionContext.Method;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MethodRepository {

    private Map< TestScenario, Map<String, Method>> methodMap = new HashMap<>();

    private static MethodRepository instance;

    private MethodRepository() {
        //Do not instantiate
    }

    public static MethodRepository getInstance() {
        if( instance == null ) {
            instance =  new MethodRepository();
        }

        return instance;
    }

    /**
     * Computes the compare Strings for this Scenario
     */
    public void registerScenario( TestScenario scenario ) {
        Path path = Paths.get( scenario.getPath().toString(), Txt2Selenium.METHOD_FILE_FOLDER );

        Map<String, Method> scenarioMethods = new HashMap<>();
        if( Files.exists( path ) ) {


            List<Path> testFiles = FileUtil.getTestFiles(path);

            testFiles.forEach(o -> {
                Method method = new Method(scenario, o);

                scenarioMethods.put(method.getName(), method);
            });

        }

        methodMap.put( scenario, scenarioMethods );
    }

    public Optional< Map< String, Method > >getMethodsForScenario(TestScenario testScenario) {
        return Optional.ofNullable( this.methodMap.get( testScenario ) );
    }

    public Optional< Method > getMethod( TestScenario testScenario, String methodName ) {
        Map<String, Method> scenarioMethods = this.methodMap.get(testScenario);

        if( scenarioMethods == null ) {
             return Optional.empty();
        }

        return Optional.ofNullable( scenarioMethods.get( methodName ) );
    }
}
