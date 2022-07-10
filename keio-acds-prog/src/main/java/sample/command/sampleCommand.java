package sample.command;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "bin/sample",
    description = "ADB project version 0.1",
    subcommands = {
      LoadInitialDataCommand.class,
      GetOrdersInfoCommand.class
    })
     
public class sampleCommand {
	
	@Option(
		names = {"-h", "--help"},
		usageHelp = true,
		description = "Displays this help message and quits.",
		defaultValue = "true")
	private Boolean showHelp;

	//@Override
	public void run() {
		if (showHelp) {
			System.out.println("Show help");
			CommandLine.usage(this, System.out);
		}
	}

	public static void main(String[] args) {
		System.out.println("Enter in main");
		new CommandLine(new sampleCommand()).execute(args);
	}
}
