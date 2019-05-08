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

package de.typedcode.txt2selenium.actions;

import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class AssertCheckedAction extends AAction {

    public static final String IDENTIFIER = "assertChecked";

    private final Boolean expectedState;

    /**
     * Creating the AssertCheckedAction. To run the AssertCheckedAction the {@link SelectAction} has
     * to be performed before.
     *
     * @param scenario Scenario in which this Action will run.
     * @param parameters Parameter to determain if the check should be true or false. Every other value then "true" will be considered false.
     */
     AssertCheckedAction( TestScenario scenario, String parameters ) {
        super( scenario );

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
        Optional<WebElement> optionalSelectedElement = WebUtil.getInstance().getSelectedElement();

        if( optionalSelectedElement.isEmpty() ) {
            UnitLogger.logSevere( "Error: No Element selected to check for selection status." );
            return;
        }

        WebElement selectedElement = optionalSelectedElement.get();

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
