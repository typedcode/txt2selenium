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

package de.typedcode.txt2Selenium.util;

public enum ByType {
    NAME( "name" ), ID( "id" ), XPATH( "xpath" );

    private final String type;

    ByType( String type ) {
        this.type = type;
    }

    /**
     * Returns the Enum ByType for the corresponding String.
     * 
     * @param type
     *            Type to create the Enum for.
     * @return Returns the corresponding Enum ByType to <code>type</code>.
     * @throws NullPointerException
     *             If the given <code>type</code> is unknown.
     */
    public static ByType getType( String type ) throws NullPointerException {
        String lowerCaseType = type.toLowerCase();

        if( NAME.type.equals( lowerCaseType ) ) {
            return NAME;
        }

        if( ID.type.equals( lowerCaseType ) ) {
            return ID;
        }

        if( XPATH.type.equals( lowerCaseType ) ) {
            return XPATH;
        }

        throw new NullPointerException( String.format( "Given type '%s' is unknown.", type ) );
    }
}
