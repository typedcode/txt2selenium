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

package de.typedcode.txt2Selenium.executionContext;

import com.google.common.io.Files;
import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.parsers.TestFileParser;
import de.typedcode.txt2Selenium.util.UnitLogger;

import java.nio.file.Path;
import java.util.Optional;

public class Test extends ExecutionContext {


    /**
     * First Action to execute in this Context
     */
    private AAction firstAction;

    /**
     * @param instance Instance where this ExecutionContext will run in.
     * @param path     path can be a folder or a test-file.
     */
    public Test(Txt2Selenium instance, Path path) {
        super(instance, path);
    }

    @Override
    String processName() {
        return Files.getNameWithoutExtension( this.PATH.getFileName().toString() );
    }

    @Override
    String getExecuteStartMessage() {
        return String.format( "Starting Test: %s", this.name );
    }

    @Override
    String getExecuteFinishMessage() {
        return String.format( "Ending Test: %s", this.name );
    }

    @Override
    String getEmptyMessage() {
        return "Test empty";
    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getExecuteStartMessage() );

        AAction currentAction = this.firstAction;

        if( currentAction == null ) {
            UnitLogger.logInfo( getEmptyMessage() );
        }

        while( currentAction != null ) {
            currentAction.execute();
            currentAction = currentAction.nextAction;
        }

        UnitLogger.logInfo( getExecuteFinishMessage() );
    }

    @Override
    void processPath() {

        Optional<AAction> optionalAction = TestFileParser.parse(this.txt2Selenium, this.PATH);

        if( optionalAction.isEmpty() ) {
            this.firstAction = null;
        }
        else {
            this.firstAction = optionalAction.get();
        }
    }
}
