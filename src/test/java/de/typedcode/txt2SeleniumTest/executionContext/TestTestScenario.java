package de.typedcode.txt2SeleniumTest.executionContext;

import de.typedcode.txt2selenium.executionContext.TestScenario;
import de.typedcode.txt2selenium.util.Configuration;
import de.typedcode.txt2selenium.util.UnitLogger;
import de.typedcode.txt2SeleniumTest.testUtils.TestLoggingHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestTestScenario {

    private TestScenario testScenario;
    private Configuration configuration;

    @BeforeEach
    void before() throws NoSuchFieldException, IllegalAccessException {
        this.testScenario = Mockito.mock( TestScenario.class );

        configuration = Mockito.mock( Configuration.class );
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, configuration );
    }

    @AfterAll
    static void cleanup() throws Exception {
        Field instance = Configuration.class.getDeclaredField("instance" );
        instance.setAccessible( true );
        instance.set( instance, null );
    }

    @Test
    void testTestsOnly() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "executionContext", "testScenario", "testsOnly" );
        Mockito.when( this.configuration.getMainDirectory() ).thenReturn( Optional.of (scenarioPath.getParent() ) );

        TestScenario tsc = new TestScenario( this.testScenario,scenarioPath );

        assertTrue( tsc.getSubScenarios().isEmpty() );
        assertEquals( 3, tsc.getTests().size() );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        tsc.execute();

        List<LogRecord> records = handler.getLogRecords();

        records.forEach( o -> assertEquals(Level.INFO, o.getLevel() ) );

        assertEquals( 11, records.size() );
        assertEquals( "Starting Scenario: testsOnly", records.get( 0 ).getMessage() );

        assertEquals( "Starting Test: test2", records.get( 1 ).getMessage() );
        assertEquals( "Test empty", records.get( 2 ).getMessage() );
        assertEquals( "Ending Test: test2", records.get( 3 ).getMessage() );

        assertEquals( "Starting Test: test3", records.get( 4 ).getMessage() );
        assertEquals( "Test empty", records.get( 5 ).getMessage() );
        assertEquals( "Ending Test: test3", records.get( 6 ).getMessage() );

        assertEquals( "Starting Test: test1", records.get( 7 ).getMessage() );
        assertEquals( "Test empty", records.get( 8 ).getMessage() );
        assertEquals( "Ending Test: test1", records.get( 9 ).getMessage() );

        assertEquals( "Ending Scenario: testsOnly", records.get( 10 ).getMessage() );
    }

    @Test
    void testSubScenarioOnly() {
        //Scenario does not contain any tests but a Sub-Scenario which does
        Path scenarioPath = Paths.get( "src", "test", "resources", "executionContext", "testScenario", "subScenarioOnly" );
        Mockito.when( this.configuration.getMainDirectory() ).thenReturn( Optional.of (scenarioPath.getParent() ) );

        TestScenario tsc = new TestScenario( this.testScenario, scenarioPath );


        assertTrue( tsc.getTests().isEmpty() );
        assertEquals( 1, tsc.getSubScenarios().size() );
        assertEquals( 1, tsc.getSubScenarios().get( 0 ).getTests().size() );
        assertTrue( tsc.getSubScenarios().get( 0 ).getSubScenarios().isEmpty() );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        tsc.execute();

        List<LogRecord> records = handler.getLogRecords();

        records.forEach( o -> assertEquals(Level.INFO, o.getLevel() ) );

        assertEquals( "Starting Scenario: subScenarioOnly/sub", records.get( 0 ).getMessage() );
        assertEquals( "Starting Test: test", records.get( 1 ).getMessage() );
        assertEquals( "Test empty", records.get( 2 ).getMessage() );
        assertEquals( "Ending Test: test", records.get( 3 ).getMessage() );
        assertEquals( "Ending Scenario: subScenarioOnly/sub", records.get( 4 ).getMessage() );

        assertEquals( 5, records.size() );
    }

    @Test
    void testEmptyAndSubScenarioEmpty() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "executionContext", "testScenario", "emptyScenario" );
        Mockito.when( this.configuration.getMainDirectory() ).thenReturn( Optional.of (scenarioPath.getParent() ) );
        TestScenario tsc = new TestScenario( this.testScenario, scenarioPath );

        assertTrue( tsc.getTests().isEmpty() );
        assertTrue( tsc.getSubScenarios().isEmpty() );

        TestLoggingHandler handler = new TestLoggingHandler();
        UnitLogger.addHandler( handler );

        tsc.execute();

        List<LogRecord> records = handler.getLogRecords();


        assertEquals(Level.INFO, records.get( 0 ).getLevel() );
        assertEquals( "Scenario 'emptyScenario' does not contain any tests or sub-scenarios.", records.get( 0 ).getMessage() );
        assertEquals( 1, records.size() );
    }

    @Test
    void testMethodDirectoryIsNoSubScenario() {
        Path scenarioPath = Paths.get( "src", "test", "resources", "executionContext", "testScenario", "methodDirectoryIsNoSubScenario" );
        Mockito.when( this.configuration.getMainDirectory() ).thenReturn( Optional.of (scenarioPath.getParent() ) );

        TestScenario tsc = new TestScenario( this.testScenario, scenarioPath );

        assertTrue( tsc.getTests().isEmpty() );
        assertTrue( tsc.getSubScenarios().isEmpty() );
    }
}