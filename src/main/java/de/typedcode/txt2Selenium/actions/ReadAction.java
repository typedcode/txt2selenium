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
import de.typedcode.txt2Selenium.exceptions.ActionInitiationException;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;

/**
 * Action to read Text from previously selected Elements.
 * 
 * Usage: read varName
 * 
 * with varName representing the identifier to read the varName to for later
 * reference.
 */

public class ReadAction extends AAction {

    public static final String IDENTIFIER = "read";

    private String readToName;

    ReadAction( Txt2Selenium correspondingInstance, String readToVar ) {
        super( correspondingInstance );

        String varName = readToVar.trim();

        if( varName.isEmpty() ) {
            throw new ActionInitiationException(
                    "Could not create 'ReadAction'. varIdentifier was empty. Use 'read varIdentifier'." );
        }

        this.readToName = varName;
    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );

        String text = WebUtil.getInstance().readText( this.readToName );

        UnitLogger.logInfo( String.format( "%s = %s", this.readToName, WebUtil.getInstance().getReadVar( this.readToName ) ) );
    }

    @Override
    public String getCommand() {
        return String.format( "%s %s", IDENTIFIER, this.readToName );
    }
}
