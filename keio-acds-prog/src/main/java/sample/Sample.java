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
	
	public void getOrdersInfo() throws TransactionException {
		
		DistributedTransaction transaction = null;
		System.out.println("Enter in getOrderInfo()");
		
		try {
			System.out.println("Strat transaction manager");
			transaction = manager.start();
			
			//get all info in table orders
			System.out.println("Display the table orders.orders: ");
			for(int orderId=1; orderId<=100; orderId++) {
				Optional<Result> order =
						transaction.get( 
					         new Get(new Key("order_id", orderId))
					                  .forNamespace("orders")
					                  .forTable("orders"));
				
				//int orderId = order.getValue("order_id").get().getAsInt();
				if (order.isPresent()) {
					String orderInfo = getOrderById(transaction, orderId);
					System.out.println(orderInfo);
				} else {
					break;
				}
			}
			
			//get all info in table item
			System.out.println("Display the table orders.item: ");
			for(int itemId=1; itemId<=100; itemId++) {
				Optional<Result> item =
						transaction.get( 
					         new Get(new Key("item_id", itemId))
					                  .forNamespace("orders")
					                  .forTable("item"));
					
					
				//int itemId = item.getValue("item_id").get().getAsInt();
				if(item.isPresent()) {
					String itemInfo = getItemById(transaction, itemId);
					System.out.println(itemInfo);
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
		//System.out.println("getItemById");
	    try {
	      // Retrieve the customer info for the specified customer ID from the customers table
	      Optional<Result> item =
	          transaction.get(
	              new Get(new Key("item_id", itemId))
	                  .forNamespace("orders")
	                  .forTable("item"));

	      if (!item.isPresent()) {
	        // If the customer info the specified customer ID doesn't exist, throw an exception
	        throw new RuntimeException("Item not found: "+itemId);
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
		//System.out.println("getOrderById");
	    try {
	      // Retrieve the customer info for the specified customer ID from the customers table
	      Optional<Result> order =
	          transaction.get(
	              new Get(new Key("order_id", orderId))
	                  .forNamespace("orders")
	                  .forTable("orders"));

	      if (!order.isPresent()) {
	        // If the customer info the specified customer ID doesn't exist, throw an exception
	        throw new RuntimeException("Order not found: "+orderId);
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
	
	public void getCustomersInfo() throws TransactionException {
		DistributedTransaction transaction = null;
		System.out.println("Enter in getCustomerInfo()");
		//Display the database customers
		System.out.println("Display all customers (customer.customers) with their product (customer.product)");
		try {
			System.out.println("Strat transaction manager");
			transaction = manager.start();
			
			for(int customerId=1; customerId<=100; customerId++) {
				Optional<Result> customer =
						transaction.get( 
					         new Get(new Key("customer_id", customerId))
					                  .forNamespace("customer")
					                  .forTable("customers"));
				
				//int orderId = order.getValue("order_id").get().getAsInt();
				if (customer.isPresent()) {
					String customerInfo = getCustomersById(transaction, customerId);
					System.out.println("Customer: "+customerInfo);
					System.out.println("...link to: ");
					getProductByCustomerId(transaction, customerId);
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
	
	private String getCustomersById(DistributedTransaction transaction, int customerId) throws TransactionException {
		//System.out.println("getCustomerById");
	    try {
	      // Retrieve the customer info for the specified customer ID from the customers table
	      Optional<Result> customer =
	          transaction.get(
	              new Get(new Key("customer_id", customerId))
	                  .forNamespace("customer")
	                  .forTable("customers"));

	      if (!customer.isPresent()) {
	        // If the customer info the specified customer ID doesn't exist, throw an exception
	        throw new RuntimeException("Customer not found");
	      }

	      // Commit the transaction (even when the transaction is read-only, we need to commit)
	      transaction.commit();

	      // Return the customer info as a JSON format
	      return String.format(
	          "{\"id\": %d, \"name\": \"%s, \"treasury\": \"%f, \"type\": \"%d\"}",
	          customerId,
	          customer.get().getValue("name").get().getAsString().get(),
	          customer.get().getValue("treasury").get().getAsFloat(),
	          customer.get().getValue("type").get().getAsInt());
	    } catch (Exception e) {
	      if (transaction != null) {
	        // If an error occurs, abort the transaction
	        transaction.abort();
	      }
	      throw e;
	    }
	}
	
	private int getNumberItems(DistributedTransaction transaction) throws TransactionException {
		int id = 1;
		boolean end =  false;
		while(!end) {
			Optional<Result> item =
					transaction.get( 
				         new Get(new Key("item_id", id))
				                  .forNamespace("orders")
				                  .forTable("item"));
			
			// Commit the transaction (even when the transaction is read-only, we need to commit)
		    transaction.commit();
		      
			if(item.isPresent())
				id++;
			else
				break;
		}
		
		return id-1;
	}
	
	private void getProductByCustomerId(DistributedTransaction transaction, int customerId) throws TransactionException {
		//System.out.println("getProductByCustomerId");
	    try {
	    	
	      int numberItems = getNumberItems(transaction);
	      //System.out.println("get numberItems: "+numberItems);
	      
	      for (int itemId=1; itemId<=numberItems; itemId++) {
	    	// Retrieve the customer info for the specified customer ID from the customers table
		      Optional<Result> product =
		          transaction.get(
		              new Get(new Key("item_id", itemId), new Key("customer_id", customerId))
		                  .forNamespace("customer")
		                  .forTable("product"));
		     
		      // Commit the transaction (even when the transaction is read-only, we need to commit)
		      transaction.commit();
		      
		      if(product.isPresent()) {
		      // Return the customer info as a JSON format
				  String productInfo = String.format(
				      "{\"item_id\": %d, \"customer_id\": \"%d, \"count\": \"%d, \"price\": \"%f\"}",
				       product.get().getValue("item_id").get().getAsInt(),
				       customerId,
				       product.get().getValue("count").get().getAsInt(),
				       product.get().getValue("price").get().getAsFloat());
				  System.out.println(productInfo);
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
	
	private void getProductByItemId(DistributedTransaction transaction, int itemId) throws TransactionException {
		System.out.println("getProductByItemId");
	    try {
	      // Retrieve the customer info for the specified customer ID from the customers table
	      List<Result> products =
	          transaction.scan(
	              new Scan(new Key("item_id", itemId))
	                  .forNamespace("customer")
	                  .forTable("product"));
	     
	      // Commit the transaction (even when the transaction is read-only, we need to commit)
	      transaction.commit();
	      
	      for(Result product: products) {

	    	  // Return the customer info as a JSON format
		      String productInfo = String.format(
		          "{\"item_id\": %d, \"customer_id\": \"%d, \"count\": \"%d, \"price\": \"%f\"}",
		          itemId,
		          product.getValue("customer_id").get().getAsInt(),
		          product.getValue("count").get().getAsInt(),
		          product.getValue("price").get().getAsFloat());
		      System.out.println(productInfo);
	      }
	     
	      
	    } catch (Exception e) {
	      if (transaction != null) {
	        // If an error occurs, abort the transaction
	        transaction.abort();
	      }
	      throw e;
	    }
	}
	
	
	/*	Place order
	  */

	public void placeOrder(int customerId, int itemId, int itemCount, int fromId)throws TransactionException {
		System.out.println("placeOrder");
	    DistributedTransaction transaction = null;
	    try {     
	      // Start a transaction
	      int count;
	      float price;
	      transaction = manager.start();
	      
	       // get last order id + 1
	      int orderId=1;
	      int vrai = 0;
	      do{
	      Optional<Result> order =
				transaction.get( 
				new Get(new Key("order_id", orderId))
					.forNamespace("orders")
					.forTable("orders"));
				if (order.isPresent()){
					vrai = 0;
					orderId = orderId + 1;
				}else{
					vrai = 1;
				}
		  }while ( vrai == 0);
		  System.out.println("set up the order id");

	      // Obtain product
	      //fair un get avec l'Itemid & fromid
	      Optional<Result> product_supplier =
				transaction.get( 
					new Get(new Key("item_id", itemId), new Key("customer_id", fromId))
						.forNamespace("customer")
						.forTable("product"));
				
		  count = product_supplier.get().getValue("count").get().getAsInt();
		  price = product_supplier.get().getValue("price").get().getAsFloat();

		  // verif count
		  if (itemCount <= count){
			  System.out.println("check the asked count");
		  
			  // Obtain customer
			  Optional<Result> customer =
				transaction.get(
	              	new Get(new Key("customer_id", customerId))
	                  	.forNamespace("customer")
	                  	.forTable("customers"));
	         float treasury = customer.get().getValue("treasury").get().getAsFloat();
	         //System.out.println(treasury);	
	                  				
	         // verif enough money
	         if (itemCount * price <= treasury){
	        	 System.out.println("check the customer treasury");
	             System.out.println("the final amount of the transaction is: "+itemCount * price);
	             //orderId = orderId+1;
	                  		
	             // maj bdd ******************
	                  					
	             // NEW order
	             //COM
	             transaction.put(
	            	new Put(new Key("order_id", orderId))
						.withValue("item_id", itemId)
						.withValue("from_id", fromId)
						.withValue("to_id", customerId)
						.withValue("count", itemCount)
						.withValue("timestamp", System.currentTimeMillis())
						.forNamespace("orders")
						.forTable("orders"));
							
				System.out.println("upadte the orders.orders table");
				// REMOVE from supplier Product : 2 cases
				// if (itemCount < count) and if (itemCount == count)
				//COM
				transaction.put(
					new Put(new Key("item_id", itemId), 
							new Key("customer_id", fromId))
								.withValue("count", count - itemCount)
								.withValue("price", price)
								.forNamespace("customer")
								.forTable("product"));
				System.out.println("upadte the customer.product table with the supplier informations");

				// ADD to supermarket Product : 2 cases
				//verif if buyer already have some of these item to add in table product in the same line, else add a new line in product
				//COM
				//get avec curtomerid & itemid et faire un isPresent()
				Optional<Result> product_customer = transaction.get( 
					new Get(new Key("item_id", itemId), 
							new Key("customer_id", customerId))
							.forNamespace("customer")
							.forTable("product"));
									
				// exist=0 --> not in product
				//if not already present
				if (!product_customer.isPresent()){
					transaction.put(
						new Put(new Key("item_id", itemId), 
								new Key("customer_id", customerId))
								.withValue("count", itemCount)
								.withValue("price", price+50)
								.forNamespace("customer")
								.forTable("product"));
									//si deja présent
				} else{ // already in product
				// get count then MAJ
				// count in tocount
				// MAJ
				float price_customer= product_customer.get().getValue("price").get().getAsFloat();
				int tocount = product_customer.get().getValue("count").get().getAsInt();
				transaction.put(								
					new Put(new Key("item_id", itemId), 
							new Key("customer_id", customerId))
							.withValue("count", itemCount + tocount )
							.withValue("price", price_customer)
							.forNamespace("customer")
							.forTable("product"));		
				}
				System.out.println("upadte the customer.product table with the supermarket informations");
												
				// MAJ of treasury
				// for buyer
				//COM
				transaction.put(
					new Put(new Key("customer_id", customerId))
							.withValue("treasury", treasury - itemCount * price)
							.forNamespace("customer")
							.forTable("customers"));
										
				// for seller 
				//COM
				Optional<Result> customer2 =
	          		transaction.get(
	              		new Get(new Key("customer_id", fromId))
	              				.forNamespace("customer")
	              				.forTable("customers"));
				float treasury2 = customer2.get().getValue("treasury").get().getAsFloat();
	            //System.out.println(treasury2);
				transaction.put(
					new Put(new Key("customer_id", fromId))
							.withValue("treasury", treasury2 + itemCount * price)
							.forNamespace("customer")
							.forTable("customers"));
								
				System.out.println("upadte the treasury information");	
	         } else {
	    	    System.out.println("not enough money in the supermarket treasury !");
	         }    				
		 } else {
			 System.out.println("not enough product in the supplier stock !");
		 }
					
	    transaction.commit();
	    } catch (Exception e) {
	    	if (transaction != null) {
	        	// If an error occurs, abort the transaction
	        	transaction.abort();
	      	}
	    }
	}
	
		/* 
				RESTOCK
		 */
	public String reStock(int customerId, int reItemid, int reCount, float rePrice)
	  throws TransactionException {
		System.out.println("reStock");
		System.out.println("customerId = " + customerId);
		System.out.println("itemId = " + reItemid);
		System.out.println("count = " + reCount);
		System.out.println("price = " + rePrice);
		DistributedTransaction transaction = null;
		try { 
			transaction = manager.start();
		 
		 	Optional<Result> item = 
		 		transaction.get( 
		 			new Get(new Key("item_id", reItemid))
		 					.forNamespace("orders")
		 					.forTable("item"));
		 	if (item.isPresent()){
		 
		 		Optional<Result> product_customer = 
		 				transaction.get( 
		 					new Get(new Key("item_id", reItemid), 
		 							new Key("customer_id", customerId))
		 							.forNamespace("customer")
		 							.forTable("product"));
		 		// check if a relation between the item and the customer already exist in table product	
		 		if (!product_customer.isPresent()){
		 			System.out.println("Haven't ever exist in product table!");
		 			System.out.println("So we will create it");
			
		 			transaction.put(
		 				new Put(new Key("item_id", reItemid),
		 						new Key("customer_id", customerId))
		 						.withValue("count", reCount)
		 						.withValue("price", rePrice)
		 						.forNamespace("customer")
		 						.forTable("product"));
						
		 			System.out.println("Created :)");
			
		 		} else{
		 			int count = product_customer.get().getValue("count").get().getAsInt();
		 			transaction.put(
		 				new Put(new Key("item_id", reItemid),
		 						new Key("customer_id", customerId))
		 						.withValue("count", count + reCount)
		 						.withValue("price", rePrice)
		 						.forNamespace("customer")
		 						.forTable("product"));	
		 		}
		 } else{
		 	System.out.println("Item doesn't exist in table item");
		 }
		 	
		 transaction.commit();
		 return String.format("Transaction");
		 
		 } catch (Exception e) {
			 	if (transaction != null) {
			    	// If an error occurs, abort the transaction
			    	transaction.abort();
		  		}
			 	throw e;
		}
	}


	/*
		NEW ITEM
	*/
	public String newItem(String itemName) throws TransactionException {
		System.out.println("newItem");
		//System.out.println("itemName =" + itemName);
		//System.out.println("	taille =" + itemName.length());
		DistributedTransaction transaction = null;
		try { 
			transaction = manager.start();
		   	// verif if there is another item with the same name
		   	
		   	// find item last id + check if name already used
		   	System.out.println("last id + check name");
		   	int i=1;
		    int vrai = 0;
		    int vraiItem = 0;       
		    do{
				Optional<Result> item =
		      			transaction.get( 
		      				new Get(new Key("item_id", i))
		      						.forNamespace("orders")
		      						.forTable("item"));
					
			
				if (item.isPresent()){
					vrai = 0;
					i = i + 1;
					String name = item.get().getValue("name").get().getAsString().get();
					//System.out.println("name =" + name);
					//System.out.println("	taille =" + name.length());
					if (name.equals(itemName) == true){
						vraiItem = 1;
					}
				
				//System.out.println("		vraiItem" + vraiItem);
				}else{
					vrai = 1;
				}
		    } while ( vrai == 0);
		    
			System.out.println(" new rk i = " + i);
			// future rank is in i
			// if vraiItem = 1 --> means the name was already used
			
			if (vraiItem == 1){
				System.out.println("An item with this name already exist");
			} else {
				System.out.println("Let's create it!");
				transaction.put(
				new Put(new Key("item_id", i))
						.withValue("name", itemName)
						.forNamespace("orders")
						.forTable("item"));
				System.out.println("Finish :)");
			}
		
			transaction.commit();
				return String.format("Transaction");
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
