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
import de.typedcode.txt2selenium.util.ByInitializer;
import de.typedcode.txt2selenium.util.ByType;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class SelectAction extends AAction {

    public static final String IDENTIFIER = "select";

    /**
     * By Object to perform the SelectAction on.
     */
    private By by;

    private String paramBy;

    private String paramValue;

    SelectAction(TestScenario scenario, String byParameters ) {
        super( scenario );

        String[] parameters = byParameters.split( " ", 2 );

        if( parameters.length != 2 ) {
            throw new ActionInitiationException(
                    "Could not initiate the Select Action. Action needs two parameters. Use: select byIdentifier id" );
        }

        this.paramBy = parameters[ 0 ];
        this.paramValue = parameters[ 1 ];

        this.by = ByInitializer.initialize( ByType.getType( this.paramBy ), this.paramValue );
    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );

        try {
            WebUtil.getInstance().select( this.by );
        }
        catch( NoSuchElementException e ) {
            UnitLogger.logSevere( String.format( "No Element found that could be identified by: %s", this.by.toString() ) );
        }
    }

    @Override
    public String getCommand() {
        return String.format( "%s %s %s", IDENTIFIER, this.paramBy, this.paramValue );
    }
}
