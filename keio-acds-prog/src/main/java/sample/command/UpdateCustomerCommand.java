package sample.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import sample.Sample;

@Command(name = "UpdateCustomer", description = "Create new customer, or update existing customer")
public class UpdateCustomerCommand implements Callable<Integer> {
	
	@Parameters(index = "0", paramLabel = "NAME", description = "Customer Name")
	  private String customerName;
	
	@Parameters(
		      index = "1",
		      paramLabel = "Customer infos",
		      description = "Customer. The format is \"<treasury>:<type>\". The default value for type is 1")
		  private String customerInfo;


	  @Override
	  public Integer call() throws Exception {
		  
		  int treasury;
		  int type=1;
		    
		  String[] s = customerInfo.split(":", -1);
		  treasury= Integer.parseInt(s[0]);
		  if (s.length>1) {
			  if ((Integer.parseInt(s[1])==2) || (Integer.parseInt(s[2])==1)) 
				  type = Integer.parseInt(s[1]);
		  }
		
	    try (Sample sample = new Sample()) {
	      //System.out.println(sample.placeOrder(customerId, itemIds, itemCounts));
	      sample.updateCustomer(customerName, treasury, type);
	    }

	    return 0;
	  }
}
