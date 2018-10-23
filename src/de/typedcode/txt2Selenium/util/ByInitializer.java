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

package de.typedcode.txt2Selenium.util;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openqa.selenium.By;

@NonNullByDefault
public class ByInitializer {

    /**
     * Do not Initialize.
     */
    private ByInitializer() {

    }

    /**
     * Initializes the By Element to search for Elements within a Web-Page
     * 
     * @param byType
     *            Type to create the Element for
     * @param param
     *            Parameters to pass to the By-Element
     * @return A corresponding By-Object to use in Selenium for searching WebElents
     *         within a Web-Page
     */
    public static By initialize( ByType byType, String param ) {
        By result = null;

        switch( byType ) {
            case ID: {
                result = By.id( param );
                break;
            }
            case NAME:
                result = By.name( param );
                break;
            case XPATH:
                result = By.xpath( param );
                break;
            default:
        }

        if( result != null ) {
            return result;
        }

        throw new RuntimeException( "asdf" );
    }
}
