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
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;
import org.openqa.selenium.WebElement;

/**
 * Action will type Text into an previously selected input type="text" element
 * or textarea.
 * 
 * Usage: type textToType
 * 
 * where textToType is the text to fill into the element. This Action will NOT
 * overwrite. If there was text within the element, the given text will be
 * appended to the already existing text in the element.
 */
public class TypeAction extends AAction {

    public static final String IDENTIFIER = "type";
    private String textToType;

    TypeAction( Txt2Selenium correspondingInstance, String textToType ) {
        super( correspondingInstance );
        this.textToType = textToType;
    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );

        WebElement selectedElement = WebUtil.getInstance().getSelectedElement();

        if( selectedElement == null ) {
            UnitLogger.logSevere("Coulnd not Type text. No Element selected yet." );
            return;
        }

        // Check that the element is textarea or input with type text
        String tagName = selectedElement.getTagName();

        if( "textarea".equals( tagName )
                || ( "input".equals( tagName ) && "text".equals( selectedElement.getAttribute( "type" ) ) ) ) {
            selectedElement.sendKeys( this.textToType );
        } else {
            // No suitable Element to input text
            UnitLogger.logSevere("Selected element is no Text element to type text to." );
        }

    }

    @Override
    public String getCommand() {
        return String.format( "%s %s", IDENTIFIER, this.textToType );
    }
}
