package ch.so.agi.cli;

import org.junit.jupiter.api.Test;

import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;

class AppTest {
    @Test 
    void oereb_Ok() {        
        App app = new App();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute("oereb", "-q", "messen 168");

        assertEquals(0, exitCode);
        assertEquals("\n"
                + "GB-Nr: 168 - Balm bei Messen [Messen] (Liegenschaft)\n"
                + "      https://geo.so.ch/map/?oereb_egrid=CH181832067404\n"
                + "\n"
                + "GB-Nr: 168 - Messen (Liegenschaft)\n"
                + "      https://geo.so.ch/map/?oereb_egrid=CH807306583219\n"
                + "\n"
                + "GB-Nr: 90168 - Messen (Liegenschaft)\n"
                + "      https://geo.so.ch/map/?oereb_egrid=CH869832061756\n"
                + "", sw.toString());
    }
}
