package ch.so.agi.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "sogis-cli", description = "...", 
    mixinStandardHelpOptions = true,
    subcommands = { OerebCommand.class })
public class App implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);        
    }

    @Override
    public void run() {
        System.out.println("run...");
    }
}
