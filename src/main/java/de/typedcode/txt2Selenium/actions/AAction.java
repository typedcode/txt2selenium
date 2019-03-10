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

package de.typedcode.txt2Selenium.actions;

import de.typedcode.txt2Selenium.Txt2Selenium;

public abstract class AAction {

    /**
     * The Action that was performed before this action is to be run. Can be null. Will only be null if this action is the first one to perform
     */
    public AAction previousAction;

    /**
     * The action to perform after this action was run. Can be null. Will onlny be null if this action is the last to perform.
     */
    public AAction nextAction;

    /**
     * Instance of Txt2Selenium where this AAction will run in.
     */
    Txt2Selenium correspondingInstance;

    public AAction( Txt2Selenium correspondingInstance ) {
        this.correspondingInstance = correspondingInstance;
    }

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Returns the String of the Command that is represented.
     * @return Command String
     */
    public abstract String getCommand();
}