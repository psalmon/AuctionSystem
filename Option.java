
public class Option {
	private String type;
	private String owner;
	//int id;
	private int quantity;
	private int price;
	
	Option(String owner, int quantity, int price){
		this.owner = owner;
		this.quantity = quantity;
		this.price = price;
	}
	
	public String getOwner(){
		return owner;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public int getPrice(){
		return price;\
	}
	

}
