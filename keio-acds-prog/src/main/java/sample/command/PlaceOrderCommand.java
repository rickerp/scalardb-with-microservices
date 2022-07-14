package sample.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import sample.Sample;

@Command(name = "PlaceOrder", description = "Place an order")
public class PlaceOrderCommand implements Callable<Integer> {

  @Parameters(index = "0", paramLabel = "CUSTOMER_ID", description = "customer ID")
  private int customerId;

  @Parameters(
      index = "1",
      paramLabel = "ORDERS",
      description = "create an order. The format is \"<Item ID>:<Count>:<supplier ID>\"")
  private String orders;

  @Override
  public Integer call() throws Exception {
	/*
    String[] split = orders.split(",", -1);
    int[] itemIds = new int[split.length];
    int[] itemCounts = new int[split.length];
	
    for (int i = 0; i < split.length; i++) {
      String[] s = split[i].split(":", -1);
      itemIds[i] = Integer.parseInt(s[0]);
      itemCounts[i] = Integer.parseInt(s[1]);
    }
	*/

    int itemId;
    int itemCount;
    int fromId;
    String[] s = orders.split(":", -1);
    itemId = Integer.parseInt(s[0]);
    itemCount= Integer.parseInt(s[1]);
    fromId= Integer.parseInt(s[2]);	
	
    try (Sample sample = new Sample()) {
      //System.out.println(sample.placeOrder(customerId, itemIds, itemCounts));
      sample.placeOrder(customerId, itemId, itemCount, fromId);
    }

    return 0;
  }
}