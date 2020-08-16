/*
 * MIT License
 * 
 * Copyright (c) 2020 Markus Hoffmann (www.typedcode.de)
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

package de.typedcode.txt2SeleniumTest.actions;

import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import de.typedcode.txt2selenium.actions.*;
import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.UnitLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

class TestCommentAction {

    private final TestScenario testScenario = Mockito.mock( TestScenario.class );
    private TestLoggingHandler handler;


    @BeforeEach
    void before() {
        this.handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );
    }

    @AfterEach
    void afterEach() {
        UnitLogger.removeHandler( this.handler );
    }

    @Test
    void testLogEmptyComment() {
        ActionFactory.createAction( this.testScenario, CommentAction.IDENTIFIER, "" ).execute();

        List<LogRecord> records = handler.getLogRecords();

        assertEquals( 1, records.size() );

        records.forEach( o -> assertEquals( Level.INFO, o.getLevel()) );

        assertEquals( "comment: ", records.get( 0 ).getMessage() );
    }

    @Test
    void testLogComment() {
        ActionFactory.createAction( this.testScenario, CommentAction.IDENTIFIER, "test comment" ).execute();

        List<LogRecord> records = handler.getLogRecords();

        assertEquals( 1, records.size() );

        records.forEach( o -> assertEquals( Level.INFO, o.getLevel()) );

        assertEquals( "comment: test comment", records.get( 0 ).getMessage() );
    }
}
