/**
 * AuctionServer	Server to receive bids and offers from AuctionClient instances. Will calculate matching bids and offers and print them.
 * 					Priority is on earliest bids getting the cheapest offers.
 * @author          Paul Salmon
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;


public class AuctionServer{
	private static long auctionTime;
	private static long startTime;
	
	//arraylist for bids because we iterate from beginning to end, no shifting, just iterate.
	//get on linkedlist is (n), constant for arraylist.
	private static ArrayList<Option> bids = new ArrayList<Option>();
	//LinkedList over ArrayList because of constant time array shifting / insertion / removal. Quicker for insertion sort...
	private static LinkedList<Option> offers = new LinkedList<Option>();
	private static HashMap<String, Integer> stockMap = new HashMap<String, Integer>();//Our user IDS mapped to their stock quantity.
	
	/**
	 * Adds a bid to the bids ArrayList. Synchronized since multiple client threads can be awaiting or processing input.
	 * @param bid: an option type which will decide how to buy stocks. Goes into the bids data structure as long as it is > 0 price & quantity.
	 * @return a boolean to signify the success of adding a bid, or false if the auction has ended.
	 */ 
	public static synchronized boolean AddBid(Option bid){
		//bids should be taken care of based on earliest submission.
		if(!isLive()) return false;//be certain that the auction is running.
		if(bid.getQuantity() > 0 && bid.getPrice() > 0){bids.add(bid);}
		return true;
	}
	
	/**
	 * Adds an offer to the offers LinkedList. Synchronized for multiple client threads awaiting or processing input.
	 * @param newOffer: an option type which will decide how to sell stocks for the user who created it. All will see if they  can, or it stops if the owner runs out of stock.
	 * @return a boolean to signify the success of adding an offer, or false if the auction has ended.
	 */ 
	public static synchronized boolean AddOffer(Option newOffer){
		//time prioritized lowest price insertion sort (Hence, LinkedList).
		if(!isLive()) return false;//be certain that the auction is running.
		
		ListIterator<Option> offerIte = offers.listIterator(offers.size());//start at the end and go backwards since we want lowest price.
		
		if(offers.isEmpty()){ offers.add(newOffer); return true;}//first element
		
		while(offerIte.hasPrevious()){
			Option opt = offerIte.previous();
			if(newOffer.getPrice() >=opt.getPrice()){ offerIte.next(); offerIte.add(newOffer); return true; }
		}
		offers.addFirst(newOffer);//lowest thus far
		return true;
		
	}
	
	/**
	 * Registers a client with the stockMap HashMap. Guarantees a unique name and initializes it with 100 generic stock. Returns false if we cannot create the user (name in use).
	 * Synchronized to avoid duplicate name race conditions. Could make the HashMap volatile instead, but this may be easier for catching issues when expanding the project scope...for example:
	 * TODO: Room to expand registered clients. For example: we could create login information, so that the user can logout and we can serialize their client for when they log back in.
	 * @param name: a string for which we will uniquely name the client (equal to the name of it's thread and the key associated with their stock quantity).
	 * @return a boolean to signify the successful registration.
	 */ 
	public static synchronized boolean RegClient(String name){
		if(stockMap.containsKey(name)) return false;//unique names
		else{ stockMap.put(name, 100); return true;}//start registered clients with 100 value
		
	}
	
	/**
	 * Returns the quantity of the generic stock associated with the user.
	 * @param name: A string holding the clients name for which we will look up their stock quantity. Should by use be only the clients name.
	 * @return an integer representing how much stock the client owns.
	 */ 
	public static int getStock(String name){
		if(!stockMap.containsKey(name)){
			System.out.println("You do not exist!");
		}
		return stockMap.get(name);
	}
	
	/**
	 * Starts the auction, specifying a certain amount of time for which bids and offers will be accepted.
	 * @param t: a double which will be multiplied by 3600000 so that it represents hours to run the auction for. You can supply fractions of an hour since it is a double.
	 * @return none.
	 */ 
	public static void startAuction(double t){
		if(startTime == 0){
			startTime = System.currentTimeMillis();
			auctionTime = (int)(t*3600000);//hours
		}else System.out.println("Auction already started!");
		
	}
	
	/**
	 * Ends the auction. Called by AuctionClientSpawner.java after time has run out.
	 * Prints the bid and offers table (in their appropriate orders) for the sake of debugging, and then the crosses.
	 * "Crosses": sales that happen according to matching offer prices and maximum price the bidder will pay.
	 * Takes the most quantity it can until either the bid is fulfilled, the offer is fulfilled, or the offer owner runs out of stocks.
	 * @return none.
	 */ 
	public static void EndAuction(){		
		//print tables
		System.out.println("BIDS");
		System.out.println("OWNER\tQUANTITY\tPRICE");
		for(int b = 0; b < bids.size(); b++){
			Option op = bids.get(b);
			System.out.println(op.getOwner()+"\t"+op.getQuantity()+"\t"+op.getPrice());
		}
		System.out.println("OFFERS");
		System.out.println("OWNER\tQUANTITY\tPRICE");
		for(int o = 0; o < offers.size(); o++){
			Option op = offers.get(o);
			System.out.println(op.getOwner()+"\t"+op.getQuantity()+"\t"+op.getPrice());				
		}

		//find crosses
		for(int b = 0; b < bids.size(); b++){
			for(int o = 0; o < offers.size(); o++){
				if(bids.get(b).getOwner().equals(offers.get(o).getOwner())) continue;//this is the bidders offer. skip it.
				if(bids.get(b).getQuantity() == 0) break;//the bid has been fulfilled, skip the rest of the offers!
				if(offers.get(o).getQuantity() == 0){offers.remove(o); continue;}//remove the empty offer.
				if(bids.get(b).getQuantity() <= 0){break;}
				else if(bids.get(b).getPrice() >= offers.get(o).getPrice()){
					bids.get(b).setQuantity(takeOffer(bids.get(b), offers.get(o)));//take the offer and update our bids remaining quantity.
				}
			}
		}
	}
	
	
	private static int takeOffer(Option bid, Option offer){

		int bidQ = bid.getQuantity();//shares left desired in the submitted bid.
		int offerQ = offer.getQuantity();//shares remaining in the submitted offer.
		int offerOwnerQ = stockMap.get(offer.getOwner());//shares remaining in the offers OWNER account.
		int available = 0;
		
		if((offerOwnerQ - offerQ) > 0){//we can consider the entire offer in this transaction and not remove the offer.
			if(offerQ >= bidQ){available = bidQ;}//take the amount we want, if we can.
			else if(bidQ > offerQ){available = offerQ;}//take the amount they have left in the offer submission.
			
		}else if(offerOwnerQ > 0){//we'll consider only some of the offer (what owner has available in client).
			available = offerOwnerQ;
		}
		stockMap.put(bid.getOwner(), stockMap.get(bid.getOwner())+available);//the purchase update
		stockMap.put(offer.getOwner(),  stockMap.get(offer.getOwner()) - available);//the sale update
		
		System.out.println("Cross found, " + bid.getOwner() + " buys " + available + " from " + offer.getOwner() + " at " + offer.getPrice() + " per share" );
		
		//update the offer lists.
		bid.setQuantity(bidQ-available);
		offer.setQuantity(offerQ-available);
		
		return bidQ-available;
		
	}
	
	public static boolean isLive(){
		if (System.currentTimeMillis() >= auctionTime+startTime){return false;}
		return true;
	}
	

}
