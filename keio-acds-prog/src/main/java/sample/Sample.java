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
		System.out.println(factory); 
		System.out.println("^ factory");
	    manager = factory.getTransactionManager();  //errors
	    System.out.println("End to create a transaction manager object");
	}
	
	public void loadInitialData() throws TransactionException {
		System.out.println("Enter in loadInitialData");
		DistributedTransaction transaction = null;
	    try {
	      transaction = manager.start();
	      System.out.println("start transaction");
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
	      
	      System.out.println("finish transaction");
	      transaction.commit();
	      System.out.println("commit transaction");
	    } catch (TransactionException e) {
	      if (transaction != null) {
		    System.out.println("abort transaction");
		    System.out.println(transaction);
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
			   System.out.println("test if order exist");
			   if (!order.isPresent()) {
				  System.out.println("start trasaction orders");
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
	
	 @Override
	 public void close() {
	    manager.close();
	 }
}
