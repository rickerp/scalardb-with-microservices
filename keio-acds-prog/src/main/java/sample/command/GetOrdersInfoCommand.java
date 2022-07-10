package sample.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import sample.Sample;

@Command(name = "GetOrdersInfo", description = "Get info of database orders")

public class GetOrdersInfoCommand implements Callable<Integer> {
	@Override
	  public Integer call() throws Exception {
		System.out.println("Enter in LoadInitialDataCommand and execute...");
	    try (Sample sample = new Sample()) {
	    	System.out.println("sample.loadInitialData()");
	    	sample.getOrdersInfo();
	    }
	    return 0;
	  }

}
