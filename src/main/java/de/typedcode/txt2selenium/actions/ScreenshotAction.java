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

package de.typedcode.txt2selenium.actions;

import de.typedcode.txt2selenium.exceptions.ActionExecutionException;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2selenium.util.WebUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

/**
 * Saves the Pages Source to a file.
 * 
 * Usage: screenshot
 */
public class ScreenshotAction extends AAction {

    public static final String IDENTIFIER = "screenshot";

    private String pathIdentifier;

    private Path screenshotFile;

    ScreenshotAction(TestScenario scenario, String identifier ) {
        super( scenario );
        this.pathIdentifier = identifier;
    }

    @Override
    public void execute() {
        UnitLogger.logInfo( getCommand() );

        String source = WebUtil.getInstance().getPageSource();

        if( source == null ) {
            source = "";
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat( "yyyyMMddHHmmss" );
        String date = format.format( cal.getTime() );

        String fileName;

        if( this.pathIdentifier.isEmpty() ) {
            fileName = String.format( "screenshot_%s.html", date );
        }
        else {
            fileName = String.format( "screenshot_%s_%s.html", this.pathIdentifier, date );
        }

        try {
            Optional<Path> mainDirectory = Configuration.getInstance().getMainDirectory();

            if( mainDirectory.isEmpty() ) {
                UnitLogger.logSevere( "ERROR: Configuration has no Main-Directory set. Saving Screenshot aborted." );
                return;
            }

            this.screenshotFile = Paths.get( mainDirectory.get().toString(), fileName );

            Files.createFile( this.screenshotFile );

            Files.write( this.screenshotFile, source.getBytes() );

            UnitLogger.logInfo( String.format( "Saved screenshot to %s", fileName ) );
        } catch( IOException e ) {
            throw new ActionExecutionException( "Could not write File.", e );
        }
    }

    public Path getScreenshotFile() {
        return this.screenshotFile;
    }

    @Override
    public String getCommand() {
        if( !this.pathIdentifier.isEmpty() ) {
            return String.format( "%s %s", IDENTIFIER, this.pathIdentifier );
        }

        return IDENTIFIER;
    }
}
