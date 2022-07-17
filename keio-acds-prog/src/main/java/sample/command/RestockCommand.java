package sample.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import sample.Sample;

@Command(name = "ReStock", description = "Place an order")
public class RestockCommand implements Callable<Integer>  {
	 @Parameters(index = "0", paramLabel = "CUSTOMER_ID", description = "customer ID")
	  private int customerId;

	  @Parameters(
	      index = "1",
	      paramLabel = "ORDERS",
	      description = "orders. The format is \"<Item ID>:<Count\"")
	  private String orders;

	  @Override
	  public Integer call() throws Exception {

	    int reItemid;
	    int reCount;
	    float rePrice;
	    
	    String[] s = orders.split(":", -1);
	    reItemid= Integer.parseInt(s[0]);
	    reCount = Integer.parseInt(s[1]);
	    rePrice = Float.parseFloat(s[2]);	
		
	    try (Sample sample = new Sample()) {
	      //System.out.println(sample.placeOrder(customerId, itemIds, itemCounts));
	      System.out.println(sample.reStock(customerId, reItemid, reCount, rePrice));
	    }

	    return 0;
	  }
}
