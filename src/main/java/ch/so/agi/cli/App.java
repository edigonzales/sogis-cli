package ch.so.agi.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "sogis-cli", description = "...", 
    mixinStandardHelpOptions = true, version = "sogis-cli 0.0.1", 
    subcommands = { OerebCommand.class })
public class App /*implements Runnable*/ {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);        
    }

//    @Override
//    public void run() {
//        System.out.println("run...");
//    }
}
