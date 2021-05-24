package ch.so.agi.cli;

import picocli.CommandLine.Command;

@Command(name = "oereb", description = "Search questions matching criteria.", 
    mixinStandardHelpOptions = true)
public class OerebCommand implements Runnable {
    
    @Override
    public void run() {
        System.out.println("Run subcommand...");
        System.out.println("bar");
    }
}
