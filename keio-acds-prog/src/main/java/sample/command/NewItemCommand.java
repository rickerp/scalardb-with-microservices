package sample.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import sample.Sample;

@Command(name = "NewItem", description = "Create a new item")
public class NewItemCommand implements Callable<Integer> {

	  @Parameters(index = "0", paramLabel = "NAME", description = "Item Name")
	  private String itemName;


	  @Override
	  public Integer call() throws Exception {

		
	    try (Sample sample = new Sample()) {
	      //System.out.println(sample.placeOrder(customerId, itemIds, itemCounts));
	      sample.newItem(itemName);
	    }

	    return 0;
	  }
}
