/**
 * AuctionClient	Client for making auction bids and offers on AuctionServer. As is, assigns 1 random offer and bid.
 * @author          Paul Salmon
 */


public class AuctionClient implements Runnable{
	private String name;
	private Thread t;
	
	/**
	 * Constructor
	 * AuctionServer is local for testing purposes. Starts thread immediately.
	 * @param name: a string to name your thread and the identity of the user (used in AuctionServer bid lookups)
	 */ 
	AuctionClient(String name){
		if(AuctionServer.RegClient(name)){
			this.name = name;
			t = new Thread(this, name);
			t.start();			
		}else{System.out.println("Name in use!");}
	}

	/**
	 * Thread starts call run, and this will (for testing purposes) add an random offer and a bid in the cliets name.
	 * @override
	 * @return No return value.
	 */ 
	public void run() {
		//initialize with randoms for testing
		if(!AuctionServer.AddOffer(new Option(name, (int)(Math.random()*100), (int)(Math.random()*50)))){ System.out.println("Auction over");}
		if(!AuctionServer.AddBid(new Option(name, (int)(Math.random()*100), (int)(Math.random()*75)))){ System.out.println("Auction over");}
	}
	
	/**
	 * Start the thread as long as we had no errors with names (or if the thread is somehow already running).
	 * @return No return value.
	 */ 
	public void start(){
		if(name.equals("")) return;
		else{
			if (t == null){
				t = new Thread(this, name);
				t.start();
			}
		}
	}
	
	/**
	 * 
	 * @return The instantiated objects thread instance. Useful for synchronization (such as a join...)
	 */ 
	public Thread getThread(){
		return t;
	}
	
}
