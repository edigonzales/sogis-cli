package ch.so.agi.cli;

import org.junit.jupiter.api.Test;

import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;

class AppTest {
    @Test 
    void appHasAGreeting() {
//        App classUnderTest = new App();
//        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
        
        App app = new App();
        CommandLine cmd = new CommandLine(app);

//        String[] args = "my command args".split(" ");
//        new CommandLine(new MyApp()).execute(args);
//        assertEquals("MY COMMAND OUTPUT", out.toString
        
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));
        
        int exitCode = cmd.execute("oereb");
        assertEquals(0, exitCode);
        assertEquals("Your output is abc...", sw.toString());


    }
}
