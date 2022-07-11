package sample.command;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import sample.Sample;

@Command(name = "GetCustomersInfo", description = "Get info of database customer")

public class GetCustomersInfoCommand implements Callable<Integer> {
	@Override
	  public Integer call() throws Exception {
		System.out.println("Enter in LoadInitialDataCommand and execute...");
	    try (Sample sample = new Sample()) {
	    	System.out.println("sample.getCustomersInfo()");
	    	sample.getCustomersInfo();
	    }
	    return 0;
	  }
}
