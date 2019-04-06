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
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.swing.*;

public class AssertCheckedAction extends AAction {

    public static final String IDENTIFIER = "assertChecked";

    private final Boolean expectedState;

    /**
     * Creating the AssertCheckedAction. To run the AssertCheckedAction the {@link SelectAction} has
     * to be performed before.
     *
     * @param instance Instance of the Txt2Selenium where this Action is registered.
     * @param parameters Parameter to determain if the check should be true or false. Every other value then "true" will be considered false.
     */
     AssertCheckedAction(Txt2Selenium instance, String parameters ) {
        super( instance );

        //If there is no parameter given, the check should evaluate to true
        if( parameters != null && !parameters.isEmpty() ) {
            this.expectedState = Boolean.parseBoolean(parameters);
        }
        else {
            this.expectedState = true;
        }
    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );
        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        if( selectedElement == null ) {
            UnitLogger.logSevere( "Error: No Element selected to check for selection status." );
            return;
        }

        try {
            Boolean actualState = selectedElement.isSelected();

            if( actualState != this.expectedState ) {
                UnitLogger.logSevere( String.format( "Evaluation Error. Element '%s' is %s but should be %s", selectedElement.toString(), actualState, this.expectedState ) ) ;
            }
        }
        catch( UnsupportedOperationException e ) {
            UnitLogger.logSevere( String.format( "Execution Error. Selected Element '%s' can not be evaluated for checked status.", selectedElement.toString() ) );
        }
    }

    @Override
    public String getCommand() {
        return String.format("%s %s", IDENTIFIER, this.expectedState);
    }
}
