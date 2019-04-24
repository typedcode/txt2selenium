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

import java.util.Optional;

public class AssertEqualsAction extends AAction {

    public static final String IDENTIFIER = "assertEquals";

    private final String expectedIdentifier;
    private final String actualIdentifier;
    private final boolean evaluationIndicator;

    /**
     * Creating the AssertEqualsAction. To run the AssertEqualsAction the {@link ReadAction} has
     * to be performed before.
     *
     * @param instance Instance of the Txt2Selenium where this Action is registered.
     * @param parameters List of parameters for the action. Has to contain two space-seperated parameters.
     */
     AssertEqualsAction( Txt2Selenium instance, String parameters ) {
        super( instance );

        String[] params = parameters.split( " " );



        if( params.length == 2 ) {
            this.evaluationIndicator = true;
            this.expectedIdentifier = params[ 0 ];
            this.actualIdentifier = params[ 1 ];
        }
        else if( params.length == 3 ) {
            this.evaluationIndicator = Boolean.parseBoolean( params[ 0 ] );
            this.expectedIdentifier = params[ 1 ];
            this.actualIdentifier = params[ 2 ];
        }
        else {
            throw new ActionInitiationException( "Could not create 'AssertEqualsAction'. Wrong number of parameters. Use 'assertEquals expectedIdentifier actualIdentifier'." );
        }


    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );

        String expected = this.correspondingInstance.getCompareString( this.expectedIdentifier );
        Optional<String> optionalActual = WebUtil.getInstance().getReadVar( this.actualIdentifier );

        if( expected == null ) {
            UnitLogger.logSevere(String.format( "Execution Error. Could not find expectedIdentifier '%s'.", this.expectedIdentifier ) );
            return;
        }

        if( optionalActual.isEmpty() ) {
            UnitLogger.logSevere( String.format( "Execution Error. Could not find actualIdentifier '%s'.", this.actualIdentifier ) );
            return;
        }

        String actual = optionalActual.get();

        if( ( !actual.equals( expected ) && this.evaluationIndicator ) || ( actual.equals( expected ) && !this.evaluationIndicator ) ) {
            UnitLogger.logSevere( String.format( "Execution Error. Parameters did not match. Expected (%s): %s / Actual (%s): %s", this.expectedIdentifier, expected, this.actualIdentifier, actual ) );
        }
    }

    @Override
    public String getCommand() {
        return String.format( "%s %s %s %s", IDENTIFIER, this.evaluationIndicator, this.expectedIdentifier, this.actualIdentifier );
    }
}
