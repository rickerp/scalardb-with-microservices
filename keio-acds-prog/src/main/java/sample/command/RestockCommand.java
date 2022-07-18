package sample.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import sample.Sample;

@Command(name = "ReStock", description = "Restock the table product")
public class RestockCommand implements Callable<Integer>  {
	 @Parameters(index = "0", paramLabel = "CUSTOMER_ID", description = "customer ID")
	  private int customerId;

	  @Parameters(
	      index = "1",
	      paramLabel = "Item infos",
	      description = "Item. The format is \"<Item ID>:<Count>:<Price>\"")
	  private String Item;

	  @Override
	  public Integer call() throws Exception {

	    int reItemid;
	    int reCount;
	    float rePrice;
	    
	    String[] s = Item.split(":", -1);
	    reItemid= Integer.parseInt(s[0]);
	    reCount = Integer.parseInt(s[1]);
	    rePrice = Float.parseFloat(s[2]);	
		
	    try (Sample sample = new Sample()) {
	      //System.out.println(sample.placeOrder(customerId, itemIds, itemCounts));
	     sample.reStock(customerId, reItemid, reCount, rePrice);
	    }

	    return 0;
	  }
}
