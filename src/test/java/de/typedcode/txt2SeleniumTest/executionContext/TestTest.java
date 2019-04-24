package de.typedcode.txt2SeleniumTest.executionContext;

import de.typedcode.txt2Selenium.Txt2Selenium;
import de.typedcode.txt2Selenium.actions.ActionFactory;
import de.typedcode.txt2Selenium.actions.OpenAction;
import de.typedcode.txt2Selenium.util.UnitLogger;
import de.typedcode.txt2Selenium.util.WebUtil;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestTest {

    private Txt2Selenium txt2SeleniumMock = Mockito.mock( Txt2Selenium.class );

    @AfterEach
    void afterEach() {
        WebUtil.reset();
    }

    @Test
    void testExecution() {
        Path openPath = Paths.get("src", "test", "resources", "executionContext", "test", "testExecution.html");
        Path testPath = Paths.get("src", "test", "resources", "executionContext", "test", "testExecution.t2s");
        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, openPath.toUri().toString() ).execute();

        de.typedcode.txt2Selenium.executionContext.Test tst = new de.typedcode.txt2Selenium.executionContext.Test( this.txt2SeleniumMock, testPath );

        assertTrue( WebUtil.getInstance().getReadVar( "myRead" ).isEmpty() );
        assertEquals( "Title Test Execution", WebUtil.getInstance().getTitle() );

        tst.execute();

        assertEquals( "Reading Content", WebUtil.getInstance().getReadVar( "myRead" ).get());
        assertEquals( "Title Test Execution", WebUtil.getInstance().getTitle() );
    }

    @Test
    void testLogging(){
        Path openPath = Paths.get("src", "test", "resources", "executionContext", "test", "testExecution.html");
        Path testPath = Paths.get("src", "test", "resources", "executionContext", "test", "testExecution.t2s");
        ActionFactory.createAction( this.txt2SeleniumMock, OpenAction.IDENTIFIER, openPath.toUri().toString() ).execute();

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        de.typedcode.txt2Selenium.executionContext.Test tst = new de.typedcode.txt2Selenium.executionContext.Test( this.txt2SeleniumMock, testPath );

        tst.execute();

        List<LogRecord > logRecords = handler.getLogRecords();


        logRecords.forEach( o -> assertEquals( Level.INFO, o.getLevel() ) );

        assertEquals( "Starting Test: testExecution", logRecords.get( 0 ).getMessage() );
        assertEquals( "select id clickableId", logRecords.get( 1 ).getMessage() );
        assertEquals( "click", logRecords.get( 2 ).getMessage() );
        assertEquals( "select id readId", logRecords.get( 3 ).getMessage() );
        assertEquals( "read myRead", logRecords.get( 4 ).getMessage() );
        assertEquals( "Ending Test: testExecution", logRecords.get( 5 ).getMessage() );

        assertEquals( 6, logRecords.size() );
    }

    @Test
    void testEmptyTestLogging() {
        de.typedcode.txt2Selenium.executionContext.Test tst = new de.typedcode.txt2Selenium.executionContext.Test(
                this.txt2SeleniumMock,
                Paths.get( "src", "test", "resources", "executionContext", "test", "emptyTest.t2s" ) );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        tst.execute();

        List<LogRecord > logRecords = handler.getLogRecords();

        assertEquals( 3, logRecords.size() );

        logRecords.forEach( o -> assertEquals( Level.INFO, o.getLevel() ) );

        assertEquals( "Starting Test: emptyTest", logRecords.get( 0 ).getMessage() );
        assertEquals( "Test empty", logRecords.get( 1 ).getMessage() );
        assertEquals( "Ending Test: emptyTest", logRecords.get( 2 ).getMessage() );
    }
}