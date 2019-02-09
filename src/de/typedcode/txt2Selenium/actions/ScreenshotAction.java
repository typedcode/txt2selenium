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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2Selenium.util.WebUtil;

/**
 * Saves the Pages Source to a file.
 * 
 * Usage: screenshot
 */
public class ScreenshotAction extends AAction {

    public final static String IDENTIFIER = "screenshot";

    private String pathIdentifier;

    private Path screenshotFile;

    public ScreenshotAction( Txt2Selenium instance ) {
        super( instance );
        this.pathIdentifier = "";
    }

    public ScreenshotAction( Txt2Selenium instance, String identifier ) {
        super( instance );
        this.pathIdentifier = identifier + "_";
    }

    @Override
    public void execute() {
        String source = WebUtil.WEB_UTIL.getPageSource();

        if( source == null ) {
            source = "";
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat( "yyyyMMddHHmmss" );
        String date = format.format( cal.getTime() );

        String fileName = "screenshot_" + this.pathIdentifier + date + ".html";

        try {
            this.screenshotFile = Paths.get( this.correspondingInstance.getMainDirectory().toString(), fileName );

            Files.createFile( this.screenshotFile );

            Files.write( this.screenshotFile, source.getBytes() );
        } catch( IOException e ) {
            throw new ActionExecutionException( "Could not write File.", e );
        }
    }

    public Path getScreenshotFile() {
        return this.screenshotFile;
    }

}
