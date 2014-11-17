import javax.naming.InvalidNameException;


public class AuctionClient implements Runnable{
	private String name;
	private int uid;
	
	//your name, and the auction server you're connecting to
	AuctionClient(String name, AuctionServer server){
		if(server.RegClient(name)) this.name = name;
		else{System.out.println("Name in use!");
	}
	
	

}

	@Override
	public void run() {
		if(name.equals("")) return;
		//else continue.
		
	}
