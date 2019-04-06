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

package de.typedcode.txt2Selenium.methods;

import de.typedcode.txt2Selenium.actions.AAction;
import de.typedcode.txt2Selenium.util.UnitLogger;

public class Method {

    private AAction firstAction;
    private AAction lastAction;
    public final String methodName;

    public Method( String methodName, AAction firstAction ) {
        this.methodName = methodName;

        this.firstAction = firstAction;
        this.lastAction = firstAction;
    }

    public void execute() {
        UnitLogger.logInfo( String.format( "Entering Method: %s", this.methodName ) );

        AAction currentAction = this.firstAction;

        while( currentAction != null ) {
            currentAction.execute();
            currentAction = currentAction.nextAction;
        }

        UnitLogger.logInfo( String.format( "Exiting Method: %s", this.methodName ) );
    }

    public void addAction( AAction nextAction ) {
        this.lastAction.nextAction = nextAction;
        nextAction.previousAction = this.lastAction;
        this.lastAction = nextAction;
    }

    /**
     * Returns the Name of the Method
     *
     * @return Name of the Method
     */
    public String getName() {
        return this.methodName;
    }
}
