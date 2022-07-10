package sample;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.config.DatabaseConfig;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Sample implements AutoCloseable {
	
	private final DistributedTransactionManager manager;
	
	public Sample() throws IOException {
	    // Create a transaction manager object
		System.out.println("Create a transaction manager object");
		TransactionFactory factory =
	        new TransactionFactory(new DatabaseConfig(new File("database.properties")));
	    manager = factory.getTransactionManager();  //errors
	    System.out.println("End to create a transaction manager object");
	}
	
	public void loadInitialData() throws TransactionException {
		System.out.println("Enter in loadInitialData()");
		DistributedTransaction transaction = null;
	    try {
	      transaction = manager.start();
	      System.out.println("Start transaction:");
	      loadCustomerIfNotExists(transaction, 1, "Life", (float) 10000.0, 1); //supermarket
	      loadCustomerIfNotExists(transaction, 2, "VegetableExpress",(float) 5000.0, 2); //supplier
	      loadCustomerIfNotExists(transaction, 3, "DinkService",(float) 5000.0, 2); //supplier
	      loadItemIfNotExists(transaction, 1, "tomato");
	      loadItemIfNotExists(transaction, 2, "apple");
	      loadItemIfNotExists(transaction, 3, "carrot");
	      loadItemIfNotExists(transaction, 4, "water");
	      loadItemIfNotExists(transaction, 5, "tea");
	      loadItemIfNotExists(transaction, 6, "caffe");
	      loadProductIfNotExists(transaction, 1, 2, 15, 200);
	      loadProductIfNotExists(transaction, 2, 2, 20, 300);
	      loadProductIfNotExists(transaction, 3, 2, 20, 150);
	      loadProductIfNotExists(transaction, 4, 3, 20, 90);
	      loadProductIfNotExists(transaction, 5, 3, 20, 100);
	      loadProductIfNotExists(transaction, 6, 3, 20, 300);
	      loadProductIfNotExists(transaction, 1, 1, 5, 250);
	      loadOrderIfNotExists(transaction, 1, 1, 2, 1, 5);
	      
	      System.out.println("Finish transaction");
	      transaction.commit();
	      System.out.println("Commit transaction");
	    } catch (TransactionException e) {
	      if (transaction != null) {
		    System.out.println("Abort transaction");
	        // If an error occurs, abort the transaction
	        transaction.abort();
	      }
	      throw e;
	    }
	}
	
	private void loadCustomerIfNotExists(DistributedTransaction transaction, int customerId, String name, float treasury, int type)
		throws TransactionException {
		System.out.println("loadCustomerIfNotExists");
		   Optional<Result> customer =
		      transaction.get(
		         new Get(new Key("customer_id", customerId))
		              .forNamespace("customer")
		              .forTable("customers"));
		   if (!customer.isPresent()) {
		      transaction.put(
		          new Put(new Key("customer_id", customerId))
		              .withValue("name", name)
		              .withValue("treasury", treasury)
		              .withValue("type", type)
		              .forNamespace("customer")
		              .forTable("customers"));
		    }
	}

	private void loadProductIfNotExists(DistributedTransaction transaction, int itemId, int customerId, int count, float price)
		throws TransactionException {
		System.out.println("loadProductIfNotExists");
		   Optional<Result> product =
		      transaction.get(
		         new Get(new Key("item_id", itemId), new Key("customer_id", customerId))
		         	  .forNamespace("customer")
		         	  .forTable("product"));
		   if (!product.isPresent()) {
		      transaction.put(
		         new Put(new Key("item_id", itemId),
		        		 new Key ("customer_id", customerId))
		              .withValue("count", count)
		              .withValue("price", price)
		              .forNamespace("customer")
		              .forTable("product"));
		    }
	}
	
	private void loadOrderIfNotExists(DistributedTransaction transaction, int orderId, int itemId, int from, int to, int count)
		throws TransactionException {
		System.out.println("loadOrderIfNotExists");
			Optional<Result> order =
		      transaction.get(
		         new Get(new Key("order_id", orderId))
		         	  .forNamespace("orders")
		         	  .forTable("orders"));
		   if (!order.isPresent()) {
		      transaction.put(
		         new Put(new Key("order_id", orderId))
		         	  .withValue("item_id", itemId)
		         	  .withValue("from_id", from)
			          .withValue("to_id", to)
			          .withValue("count", count)
			          .withValue("timestamp", System.currentTimeMillis())
			          .forNamespace("orders")
			          .forTable("orders"));
		   }
	}
	
	private void loadItemIfNotExists(DistributedTransaction transaction, int itemId, String name)
		throws TransactionException {
		System.out.println("loadItemIfNotExists");
			Optional<Result> item =
			  transaction.get(
				new Get(new Key("item_id", itemId))
			      	  .forNamespace("orders")
			          .forTable("item"));
			if (!item.isPresent()) {
		      transaction.put(
			    new Put(new Key("item_id", itemId))
			          .withValue("name", name)
		              .forNamespace("orders")
		              .forTable("item"));
			}
	}
	
	public void getCustomersInfo() throws TransactionException {
		DistributedTransaction transaction = null;
		System.out.println("Enter in getCustomerInfo()");
		try {
			System.out.println("Strat transaction manager");
			transaction = manager.start();
			
			
		} catch (Exception e) {
			if (transaction != null) {
				// If an error occurs, abort the transaction
			    transaction.abort();
			}
			throw e;
		}
	}
	
	public void getOrdersInfo() throws TransactionException {
		
		DistributedTransaction transaction = null;
		System.out.println("Enter in getOrderInfo()");
		
		try {
			System.out.println("Strat transaction manager");
			transaction = manager.start();
			//get all info in table orders
			
			for(int orderId=1; orderId<=100; orderId++) {
				Optional<Result> order =
						transaction.get( 
					         new Get(new Key("order_id", orderId))
					                  .forNamespace("orders")
					                  .forTable("orders"));
				
				//int orderId = order.getValue("order_id").get().getAsInt();
				if (order.isPresent()) {
					String orderInfo = getOrderById(transaction, orderId);
					System.out.println("Order Info:"+orderInfo);
				} else {
					break;
				}
			}
			
			//get all info in table item
			
			for(int itemId=1; itemId<=100; itemId++) {
				Optional<Result> item =
						transaction.get( 
					         new Get(new Key("item_id", itemId))
					                  .forNamespace("orders")
					                  .forTable("item"));
					
					
				//int itemId = item.getValue("item_id").get().getAsInt();
				if(item.isPresent()) {
					String itemInfo = getItemById(transaction, itemId);
					System.out.println("Item Info:"+itemInfo);
				} else {
					break;
				}
			}
			
		} catch (Exception e) {
			if (transaction != null) {
				// If an error occurs, abort the transaction
			    transaction.abort();
			}
			throw e;
		}
	}
	
	private String getItemById(DistributedTransaction transaction, int itemId) throws TransactionException {
		System.out.println("getItemById");
	    try {
	      // Retrieve the customer info for the specified customer ID from the customers table
	      Optional<Result> item =
	          transaction.get(
	              new Get(new Key("item_id", itemId))
	                  .forNamespace("orders")
	                  .forTable("item"));

	      if (!item.isPresent()) {
	        // If the customer info the specified customer ID doesn't exist, throw an exception
	        throw new RuntimeException("Item not found");
	      }

	      // Commit the transaction (even when the transaction is read-only, we need to commit)
	      transaction.commit();

	      // Return the customer info as a JSON format
	      return String.format(
	          "{\"id\": %d, \"name\": \"%s\"}",
	          itemId,
	          item.get().getValue("name").get().getAsString().get());
	    } catch (Exception e) {
	      if (transaction != null) {
	        // If an error occurs, abort the transaction
	        transaction.abort();
	      }
	      throw e;
	    }
	}
	
	private String getOrderById(DistributedTransaction transaction, int orderId) throws TransactionException {
		System.out.println("getOrderById");
	    try {
	      // Retrieve the customer info for the specified customer ID from the customers table
	      Optional<Result> order =
	          transaction.get(
	              new Get(new Key("order_id", orderId))
	                  .forNamespace("orders")
	                  .forTable("orders"));

	      if (!order.isPresent()) {
	        // If the customer info the specified customer ID doesn't exist, throw an exception
	        throw new RuntimeException("Order not found");
	      }

	      // Commit the transaction (even when the transaction is read-only, we need to commit)
	      transaction.commit();

	      // Return the customer info as a JSON format
	      return String.format(
	          "{\"id\": %d, \"item_id\": \"%d, \"from\": \"%d, \"to\": \"%d\", \"count\": \"%d, \"timestamp\": \"%d}",
	          orderId,
	          order.get().getValue("item_id").get().getAsInt(),
	          order.get().getValue("from_id").get().getAsInt(),
	          order.get().getValue("to_id").get().getAsInt(),
	          order.get().getValue("count").get().getAsInt(),
	          order.get().getValue("timestamp").get().getAsLong());
	    } catch (Exception e) {
	      if (transaction != null) {
	        // If an error occurs, abort the transaction
	        transaction.abort();
	      }
	      throw e;
	    }
	}
	
	 @Override
	 public void close() {
		 System.out.println("Close !");
		 manager.close();
	 }
}
