/**
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

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.UnitLogger;

@NonNullByDefault
public class ActionFactory {

    /**
     * Instantiating this Class is not allowed.
     */
    private ActionFactory() {

    }

    /**
     * Tries to create the action identified by <code>action</code> with the given
     * <code>parameters</code>
     * 
     * @param action
     * @param parameters
     * @return
     */
    public static AAction createAction( Txt2Selenium instance, String action, String parameters )
            throws ActionInitiationException {
        switch( action ) {
            case OpenAction.IDENTIFIER: {
                return new OpenAction( instance, parameters );
            }

            case ScreenshotAction.IDENTIFIER: {
                // Ignore parameters because the ScreenshotAction does not need them.
                return new ScreenshotAction( instance );
            }
            default:
                RuntimeException exception = new RuntimeException( "Action '" + action + "' is unknown." );
                UnitLogger.logSevere( "Action '" + action + "' is unknown", exception );
                throw exception;
        }
    }
}
