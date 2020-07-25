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

package de.typedcode.txt2selenium.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import de.typedcode.txt2selenium.exceptions.ActionExecutionException;

public class WebUtil {

    /**
     * HTML driver to use in the Tests.
     */
    private HtmlUnitDriver driver = new HtmlUnitDriver();

    /**
     * Map to store Text read from WebElements.
     */
    private Map< String, String > readElements = new HashMap<>();

    /**
     * WebElement that has be lastly selected.
     */
    private WebElement selectedElement;

    /**
     * Identifier by which the selectedElement was selected. If no element was found, it will contain the last By element used.
     */
    private By selectedBy;

    /**
     * Instance of the WebUtil.
     */
    private static WebUtil webUtil;

    /**
     * Resets the WebUtil. Resets the driver, the selected element and previously
     * read text.
     */
    public static void reset() {
        webUtil = new WebUtil();
    }

    public static WebUtil getInstance() {
        if( webUtil == null ) {
            reset();
        }

        return webUtil;
    }

    /**
     * Opens the given URL in the WebDriver.
     * 
     * @param url
     *            URL to open
     */
    public void openUrl( String url ) {
        this.driver.get( url );
    }

    /**
     * Returns the Title from the current loaded page.
     * 
     * @return Title from the currently loaded page.
     */
    public String getTitle() {
        return this.driver.getTitle();
    }

    /**
     * Returns the Page Source from the current loaded page.
     * 
     * @return Page Source from the currently loaded page.
     */
    public String getPageSource() {
        return this.driver.getPageSource();
    }

    /**
     * Returns the currently selected Element. May be null if no Element has been
     * selected yet.
     * 
     * @return Selected Element
     */
    public Optional<WebElement> getSelectedElement() {
        return Optional.ofNullable( this.selectedElement );
    }

    /**
     * Performs the Click Action on the given <code>By</code> Element.
     *
     * @throws NullPointerException
     *             if there was no Element selected.
     */
    public void click() {
        if( this.selectedElement != null ) {
            this.selectedElement.click();
        }
        else {
            throw new NullPointerException( "No Element Selected." );
        }

    }

    /**
     * Returns the previously read content for the corresponding identifier or null if it was not written before
     * @param identifier
     *             Identifier to get the content from
     * @return Content identified by the identifier or null if the content does not exist
     */
    public Optional<String> getReadVar( String identifier ) {
        return Optional.ofNullable( this.readElements.get( identifier ) );
    }

    /**
     * Selects the Element identified by <code>By</code>
     * 
     * @param by
     *            Identifying Object
     */
    public void select( By by ) {
        this.selectedElement = driver.findElement( by );
        this.selectedBy = by;
    }

    public Optional<By> getSelectedBy() {
        return Optional.ofNullable( this.selectedBy );
    }

    /**
     * Reads the text from the currently selected Element.
     * 
     * @param readToName
     *            Internal variable to assign the current text to.
     */
    public String readText( String readToName ) {

        if( this.selectedElement == null ) {
            throw new ActionExecutionException( "Could not execute 'read'. No Element was selected." );
        }

        String text = this.selectedElement.getText();

        if( text == null ) {
            throw new ActionExecutionException( "Reading the Text from selected Element returned null" );
        }

        this.readElements.put( readToName, text );

        return text;
    }
}