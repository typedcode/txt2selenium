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
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.methods.Method;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;

/**
 * Runs a specified method
 * 
 * Usage: method methodName
 */
public class MethodAction extends AAction {

    public static final String IDENTIFIER = "method";

    public final Method METHOD;

    MethodAction(Txt2Selenium instance, String methodName ) throws ActionInitiationException {
        super( instance );

        String trimMethodName = methodName.trim();

        if( trimMethodName.isEmpty() ) {
            throw new ActionInitiationException( "Could not initiate Method Action. The name for the Method to call was empty. Usage: method methodName" );
        }

        this.METHOD = this.correspondingInstance.getMethod( trimMethodName );

        if( this.METHOD == null ) {
            throw new ActionInitiationException( String.format( "Could not initiate Method Action. Method '%s' was not found.", trimMethodName ) );
        }
    }

    /**
     * Opens the given URL in the current state of the
     * {@see de.typedcode.txt2Selenium.util.WebUtil}.
     */
    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );
        this.METHOD.execute();
    }

    @Override
    public String getCommand() {
        return String.format( "%s %s", IDENTIFIER, this.METHOD.getName() );
    }
}
