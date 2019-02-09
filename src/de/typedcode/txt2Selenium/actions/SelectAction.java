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

import org.openqa.selenium.By;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.ByInitializer;
import de.typedcode.txt2Selenium.util.ByType;
import de.typedcode.txt2Selenium.util.WebUtil;

public class SelectAction extends AAction {

    public static final String IDENTIFIER = "select";

    /**
     * By Object to perform the SelectAction on.
     */
    private By by;

    public SelectAction( Txt2Selenium correspondingInstance, String byParameters ) {
        super( correspondingInstance );

        String[] parameters = byParameters.split( " ", 2 );

        if( parameters.length != 2 ) {
            throw new ActionInitiationException(
                    "Could not initiate the Select Action. Action needs two parameters. Use: select byIdentifier id" );
        }

        String param1 = parameters[ 0 ];
        String param2 = parameters[ 1 ];

        this.by = ByInitializer.initialize( ByType.getType( param1 ), param2 );
    }

    @Override
    public void execute() {
        WebUtil.WEB_UTIL.select( by );
    }

}
