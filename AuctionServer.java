import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;


public class AuctionServer{
	private long auctionTime;
	private long startTime;
	
	//arraylist for bids because we iterate from beginning to end, no shifting, just iterate.
	//get on linkedlist is (n), constant for arraylist.
	private ArrayList<Option> bids = new ArrayList<Option>();
	//LinkedList over ArrayList because of constant time array shifting / insertion / removal. Necessary.
	private LinkedList<Option> offers = new LinkedList<Option>();
	private HashMap<String, Integer> stockMap = new HashMap<String, Integer>();
	
	public synchronized void AddBid(Option bid){
		//bids should be taken care of based on earliest
		//for i = last ele in list, i >= o i--, if bids.get(i) are null or if tthe prices of it are > bids price, put i before the bid. break.
		bids.add(bid);
		
	}
	
	public synchronized void AddOffer(Option newOffer){
		//same as above but start at beginning bc time prioritized lowest sourt. check if < price and sort by lowest offer price
		ListIterator<Option> offerIte = offers.listIterator(offers.size());
		
		if(!(offerIte.hasPrevious())) offerIte.add(newOffer);//first element
		
		while(true){//iterate the list, append in appropriate spot to keep time prioritized descending option order.
			Option offer = offerIte.previous();
			if(newOffer.getPrice() >= offer.getPrice()){ offerIte.add(newOffer); break; }//<=put it right after it!. Consider equals to keep time priority.		
			if(!(offerIte.hasPrevious())){ offerIte.add(newOffer); break; }//lowest offer thus far.
			else continue;
		}
		
	}
	
	public synchronized boolean RegClient(String name) throws InvalidParameterException{
		//get unique id and add to list. possibly for writing client objs to disk (serialize em) so users can logout.
		if(stockMap.containsKey(name)) throw new InvalidParameterException();
		else{ stockMap.put(name, 100); return true;}//start registered clients with 100 value
		
	}
	
	public void startAuction(int t){
		if(startTime == 0){
			startTime = System.currentTimeMillis();
			auctionTime = t*3600000;
		}else System.out.println("Auction already started!");
		//sleep for remaining time, then end.
		//interrupt sleep when someone wants to do something, then sleep again for remaining time.
	}
	
	//@TODO:
	//find crosses, print them, and update clients.
	private void EndAuction(){
		
	}
	

}
