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

package de.typedcode.txt2selenium.actions;

import de.typedcode.txt2selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2selenium.executionContext.TestScenario;

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
     * @param scenario Scenario in which this Action will run.
     * @param action Action identifier to load action.
     * @param parameters Parameters for the Action.
     * @return Action
     */
    public static AAction createAction( TestScenario scenario, String action, String parameters ) {
        switch( action ) {
            case OpenAction.IDENTIFIER:
                return new OpenAction( scenario, parameters );
            case ScreenshotAction.IDENTIFIER:
                return new ScreenshotAction( scenario, parameters );
            case ClickAction.IDENTIFIER:
                return new ClickAction( scenario );
            case SelectAction.IDENTIFIER:
                return new SelectAction( scenario, parameters );
            case ReadAction.IDENTIFIER:
                return new ReadAction( scenario, parameters );
            case TypeAction.IDENTIFIER:
                return new TypeAction( scenario, parameters );
            case AssertEqualsAction.IDENTIFIER:
                return new AssertEqualsAction( scenario, parameters );
            case AssertCheckedAction.IDENTIFIER:
                return new AssertCheckedAction( scenario, parameters );
            case MethodAction.IDENTIFIER:
                return new MethodAction( scenario, parameters );
            case AssertReadEqualsAction.IDENTIFIER:
                return new AssertReadEqualsAction( scenario, parameters );
            case SendSpecialKeyAction.IDENTIFIER:
                return new SendSpecialKeyAction( scenario, parameters );
            default:
                throw new ActionInitiationException( "Action '" + action + "' is unknown." );
        }
    }
}
