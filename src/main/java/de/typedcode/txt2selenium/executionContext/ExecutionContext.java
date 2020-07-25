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

package de.typedcode.txt2selenium.executionContext;

import java.nio.file.Path;

public abstract class ExecutionContext {

    /**
     * Name of the Context
     */
    protected final Path path;

    final String name;
    final TestScenario testScenario;

    /**
     *
     * @param path path can be a folder or a test-file.
     * @param testScenario Scenario where the Execution-Context is located.
     */
    ExecutionContext( TestScenario testScenario, Path path ) {
        this.path = path;
        this.testScenario = testScenario;
        this.name = processName();

        processPath();
    }

    /**
     * Process the Path and set the Name
     * @return Name for the ExecutionContext
     */
    abstract String processName();

    /**
     * Returns the Name for this ExecutionContext
     * @return Name for the ExecutionContext
     */
    public String getName() {
        return this.name;
    }

    /**
     * Runs the Execution Context
     */
    public abstract void execute();

    /**
     * Does everything there is to do with the given path. Will be triggered by the Constructor.
     */
    abstract void processPath();


    /**
     * Describes the message to log when the Execution Context it executed.
     * @return Message to log.
     */
    abstract String getExecuteStartMessage();

    /**
     * Describes the message to log when the Execution Context was executed.
     * @return Message to log.
     */
    abstract String getExecuteFinishMessage();

    /**
     * Message to log if the ExecutionContext is empty on Execution.
     * @return Message to log if the ExecutionContext is empty on Execution
     */
    abstract String getEmptyMessage();

    /**
     * Returns the Path for this Scenario.
     * @return Path for the Scenario
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Returns the TestScenario where the ExecutionContext is in
     * @return TestScenario where the ExecutionContext is in
     */
    public TestScenario getTestScenario() {
        return this.testScenario;
    }
}
