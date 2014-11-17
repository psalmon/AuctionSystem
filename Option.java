/**
 * Option	Bids and Offers for the auction system. Interchangeably use this for bids and offers, as long as they go into the appropriate data structure.
 * @author          Paul Salmon
 */

public class Option {
	private String owner;
	private int quantity;
	private int price;
	
	/**Constructor
	 * Can be a bid or an offer
	 * @param name: A String holding the name of the owner of this option. Used for lookups in the bids and offers lists.
	 * @param quantity: An int for how much of this stock to consider (try and buy or try and sell).
	 * @param price: An int for the price of which to try and sell or buy this option at.
	 */ 
	
	Option(String owner, int quantity, int price){
		this.owner = owner;
		this.quantity = quantity;
		this.price = price;
	}
	
	/** get the owner of this option.
	 * @return A string for the owner of this option. Used in looking up or adjusting stock price/quantity for an individual, a bid, or an offer.
	 */ 
	public String getOwner(){
		return owner;
	}
	
	/** get the quantity of this option.
	 * @return An int for quantity of this option. How much it is trying to buy or sell of the stock.  Used in looking up or adjusting stock quantity for an individual, a bid, or an offer.
	 */ 
	public int getQuantity(){
		return quantity;
	}
	
	/** get the price of this option.
	 * @return An int for price of this option. How much it is trying to buy or sell the stock for. Used when deciding whether or not to make a transaction happen, and in the sorting of the offer list
	 * Also used for ordering the offer list. Offers will be considered in order of price, using time as a tiebreaker.
	 */ 
	public int getPrice(){
		return price;
	}
	
	/** sets the quantity of this offer. Needed because it is adjusted as partial sales / purchases happen.
	 * @param quantity: An int for what the new amount of this offer or bid is.
	 * @return none.
	 */ 
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	

}
