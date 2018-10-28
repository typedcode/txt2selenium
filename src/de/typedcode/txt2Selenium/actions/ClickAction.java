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
import org.openqa.selenium.By;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.util.ByInitializer;
import de.typedcode.txt2Selenium.util.ByType;
import de.typedcode.txt2Selenium.util.WebUtil;

@NonNullByDefault
public class ClickAction extends AAction {

    public static final String IDENTIFIER = "clickAction";

    /**
     * By Object to perform the Action on.
     */
    private By by;

    public ClickAction( Txt2Selenium instance, String byParameters ) {
        super( instance );

        String[] parameters = byParameters.split( " " );

        String param1 = parameters[ 0 ];
        String param2 = parameters[ 1 ];

        if( param1 == null || param2 == null ) {
            throw new RuntimeException( "" );
        }

        this.by = ByInitializer.initialize( ByType.getType( param1 ), param2 );
    }

    @Override
    public void execute() {
        WebUtil.WEB_UTIL.click( by );
    }

}
